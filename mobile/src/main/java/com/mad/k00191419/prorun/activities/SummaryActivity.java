package com.mad.k00191419.prorun.activities;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mad.k00191419.prorun.R;
import com.mad.k00191419.prorun.db.Goal;
import com.mad.k00191419.prorun.db.ProRunDB;
import com.mad.k00191419.prorun.db.Run;
import com.mad.k00191419.prorun.fragments.SummaryFragment;
import com.mad.k00191419.prorun.utils.Utils;

public class SummaryActivity extends FragmentActivity implements View.OnClickListener{


    // View References
    Button btnDetails;
    Button btnOk;

    // Fields
    ProRunDB mDb;
    Run mRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        setupReferences();
        setupListeners();
        updateFromIntent();
        getGoals();
    }

    private void updateFromIntent() {
        // Get run obj from intent and format data
        Intent intent = getIntent();
        mRun = intent.getParcelableExtra(CurrentActivity.INTENT_KEY_RUN);
        double avg = intent.getDoubleExtra(CurrentActivity.INTENT_KEY_AVG,0);
        if(mRun == null) return;    // null ref check
        String distance = Utils.formatDistance(mRun.getTotalDistance());
        String time = Utils.formatInterval(mRun.getTotalTime());
        String avgSpeed = Utils.formatSpeed(avg);
        String calories = Utils.formatCalories(mRun.getTotalCalories());
        String startDate = Utils.formatDate(mRun.getStartDate());

        SummaryFragment f = (SummaryFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_summary);
        // set textviews using fragment's public method
        if(f != null){
            f.setTextViewText(R.id.tvTotalDistance, distance);
            f.setTextViewText(R.id.tvSummaryTime, time);
            f.setTextViewText(R.id.tvAvgSpeed, avgSpeed);
            f.setTextViewText(R.id.tvTotalCalories, calories);
            f.setTextViewText(R.id.tvStartDate, startDate);
        }
    }

    private void setupReferences() {
        btnDetails = (Button)findViewById(R.id.btnDetails);
        btnOk = (Button)findViewById(R.id.btnOk);
    }

    private void setupListeners() {
        btnOk.setOnClickListener(this);
        btnDetails.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.btnDetails:
                Intent intent = new Intent(this, DetailsActivity.class);
                intent.putExtra(CurrentActivity.INTENT_KEY_RUN, mRun);
                startActivity(intent);
                break;
            case R.id.btnOk:

                finish();
        }
    }

    public void getGoals() {
        if(mDb == null)
            mDb = new ProRunDB(this);
        if( mRun == null) return;   // null ref check

        Goal daily = mDb.getDailyGoal().updateFromRun(mRun);
        Goal weekly = mDb.getWeeklyGoal().updateFromRun(mRun);
        Goal monthly = mDb.getMonthlyGoal().updateFromRun(mRun);

        // get updated progress
        int dailyProg = daily.getTotalProgress();
        int weeklyProg = weekly.getTotalProgress();
        int monthlyProg = monthly.getTotalProgress();

        // set progressbars values
        SummaryFragment f = (SummaryFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_summary);
        if(f != null){
            f.setProgressBarProgress(R.id.pbDaily, dailyProg);
            f.setProgressBarProgress(R.id.pbWeekly, weeklyProg);
            f.setProgressBarProgress(R.id.pbMonthly, monthlyProg);
        }

        // update goal values
        mDb.updateGoal(daily);
        mDb.updateGoal(weekly);
        mDb.updateGoal(monthly);
    }

}
