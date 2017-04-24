package com.mad.k00191419.prorun.activities;

import java.util.Calendar;
import java.util.Date;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mad.k00191419.prorun.R;
import com.mad.k00191419.prorun.db.Goal;
import com.mad.k00191419.prorun.db.ProRunDB;
import com.mad.k00191419.prorun.utils.Utils;

public class SetGoalsActivity extends AppCompatActivity
        implements View.OnClickListener{

    // Constants
    public static final String INTENT_KEY = "INTENT_KEY";
    public static final String INTENT_VALUE_DAILY = "daily";
    public static final String INTENT_VALUE_WEEKLY = "weekly";
    public static final String INTENT_VALUE_MONTHLY = "monthly";

    // Views
    Button      btnSave;
    Button      btnCancel;
    TextView    tvCaloriesValue;
    TextView    tvDistanceValue;
    TextView    tvLabelSetGoal;
    SeekBar     sbCalories;
    SeekBar     sbDistance;
    // Fields
    ProRunDB mDb;
    Goal     mGoal;
    Calendar mToday;
    int      mDailyMaxDistance = 10000;
    int      mDailyMaxCalories = 3000;
    int      mStepDivisor = 20;
    int      mStepDist;
    int      mStepCal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_goals);

        getGoal();
        setupReferences();
        setSeekbars();
        setLabels();
        setupListeners();
    }

    private void setSeekbars() {
        // set current progress
        int maxDist, maxCal;
        switch(mGoal.getType()){
            case DAILY:
                maxDist = mDailyMaxDistance;
                mStepDist = mDailyMaxDistance / mStepDivisor;
                maxCal = mDailyMaxCalories;
                mStepCal = mDailyMaxCalories / mStepDivisor;
                break;
            case WEEKLY:
                maxDist = mDailyMaxDistance * 7;
                mStepDist = maxDist / mStepDivisor;
                maxCal = mDailyMaxCalories * 7;
                mStepCal = maxCal / mStepDivisor;
                break;
            case MONTHLY:
                maxDist = mDailyMaxDistance * 30;
                mStepDist = maxDist / mStepDivisor;
                maxCal = mDailyMaxCalories * 30;
                mStepDist = maxCal / mStepDivisor;
                break;
            default:
                maxDist = 0;
                mStepDist = 0;
                maxCal = 0;
                mStepCal = 0;
                break;
        }
        sbDistance.setMax(maxDist);
        sbCalories.setMax(maxCal);

        sbDistance.setProgress((int)mGoal.getTargetDistance());
        sbCalories.setProgress((int)mGoal.getTargetCalories());
    }

    private void setLabels() {
        String label = "";
        // error - stop activity
        if(mGoal == null) finish();
        switch (mGoal.getType()){
            case DAILY:
                label = getString(R.string.string_label_daily_goal);
                break;
            case MONTHLY:
                label = getString(R.string.string_label_monthly_goal);
                break;
            case WEEKLY:
                label = getString(R.string.string_label_weekly_goal);
                break;
        }

        tvLabelSetGoal.setText(label);
        tvDistanceValue.setText(Utils.formatDistance(mGoal.getTargetDistance()));
        tvCaloriesValue.setText(Utils.formatCalories(mGoal.getTargetCalories()));

    }

    private void setupListeners() {
        btnCancel.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        sbCalories.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mStepCal != 0)
                    progress = (Math.round(progress/mStepCal ))*mStepCal;
                tvCaloriesValue.setText(Utils.formatCalories((float)progress));
                sbCalories.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        sbDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mStepDist != 0)
                    progress = (Math.round(progress/mStepDist ))*mStepDist;
                tvDistanceValue.setText(Utils.formatDistance((double)progress));
                sbDistance.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void setupReferences() {
        btnSave = (Button)findViewById(R.id.btnSave);
        btnCancel= (Button)findViewById(R.id.btnCancel);
        tvCaloriesValue = (TextView) findViewById(R.id.tvCaloriesValue);
        tvDistanceValue = (TextView) findViewById(R.id.tvDistanceValue);
        tvLabelSetGoal = (TextView)findViewById(R.id.tvLabelSetGoal);
        sbCalories = (SeekBar) findViewById(R.id.sbCalories);
        sbDistance = (SeekBar) findViewById(R.id.sbDistance);
    }

    private void getGoal() {
        mDb = new ProRunDB(this);
        // get intent key
        switch (getIntent().getStringExtra(INTENT_KEY)){
            case INTENT_VALUE_DAILY:
                mGoal = mDb.getDailyGoal();
                break;
            case INTENT_VALUE_WEEKLY:
                mGoal = mDb.getWeeklyGoal();
                break;
            case INTENT_VALUE_MONTHLY:
                mGoal = mDb.getMonthlyGoal();
                break;
            default:
                finish();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch(id){
            case R.id.btnSave:
                saveGoal();
                break;
            case R.id.btnCancel:
                finish();
                break;
        }
    }

    private void saveGoal() {
        mGoal.setTargetCalories(sbCalories.getProgress());
        mGoal.setTargetDistance(sbDistance.getProgress());
        Calendar c = Calendar.getInstance();

        switch(mGoal.getType()){
            case DAILY:
                c.add(Calendar.DATE, 1);
                break;
            case WEEKLY:
                c.add(Calendar.DATE, 7);
                break;
            case MONTHLY:
                c.add(Calendar.MONTH, 1);
                break;
        }

        long due = c.getTimeInMillis();
        mGoal.setDate(due);
        mDb.updateGoal(mGoal);
        Toast.makeText(this,
                getString(R.string.string_alert_toast_goal_saved),
                Toast.LENGTH_SHORT).show();

    }

}
