package com.mad.k00191419.prorun.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mad.k00191419.prorun.R;
import com.mad.k00191419.prorun.db.Goal;
import com.mad.k00191419.prorun.db.ProRunDB;

public class ViewGoalsActivity extends AppCompatActivity implements View.OnClickListener{

    // Views
    Button  btnSetGoalDaily;
    Button  btnSetGoalWeekly;
    Button  btnSetGoalMonthly;

    TextView tvDailyDistPercent;
    TextView tvWeeklyDistPercent;
    TextView tvMonthlyDistPercent;
    TextView tvDailyCalPercent;
    TextView tvWeeklyCalPercent;
    TextView tvMonthlyCalPercent;

    ProgressBar pbDailyDistance;
    ProgressBar pbWeeklyDistance;
    ProgressBar pbMonthlyDistance;
    ProgressBar pbDailyCalories;
    ProgressBar pbWeeklyCalories;
    ProgressBar pbMonthlyCalories;

    // Fields
    ProRunDB mDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_goals);
        setupReferences();
        setupListeners();

        setProgressValues();
    }

    private void setProgressValues() {
        // get data from tables
        if(mDb == null)
            mDb = new ProRunDB(this);

        // get goals
        Goal daily = mDb.getDailyGoal();
        Goal weekly = mDb.getWeeklyGoal();
        Goal monthly = mDb.getMonthlyGoal();

        int dailyDist = daily.getDistanceProgress();
        int dailyCal = daily.getCaloriesProgress();
        int weeklyDist = weekly.getDistanceProgress();
        int weeklyCal = weekly.getCaloriesProgress();
        int monthlyDist = monthly.getDistanceProgress();
        int monthlyCal = monthly.getCaloriesProgress();

        // set percent textviews and progress bars
        tvDailyDistPercent.setText(String.format("%d %%", dailyDist));
        tvDailyCalPercent.setText(String.format("%d %%", dailyCal));

        pbDailyCalories.setProgress(dailyCal);
        pbDailyDistance.setProgress(dailyDist);

        tvWeeklyDistPercent.setText(String.format("%d %%", weeklyDist));
        tvWeeklyCalPercent.setText(String.format("%d %%", weeklyCal));

        pbWeeklyCalories.setProgress(weeklyCal);
        pbWeeklyDistance.setProgress(weeklyDist);

        tvMonthlyDistPercent.setText(String.format("%d %%", monthlyDist));
        tvMonthlyCalPercent.setText(String.format("%d %%", monthlyCal));

        pbMonthlyCalories.setProgress(monthlyCal);
        pbMonthlyDistance.setProgress(monthlyDist);

    }

    private void setupListeners() {
        btnSetGoalDaily.setOnClickListener(this);
        btnSetGoalMonthly.setOnClickListener(this);
        btnSetGoalWeekly.setOnClickListener(this);

    }

    private void setupReferences() {
        btnSetGoalDaily = (Button)findViewById(R.id.btnSetGoalDaily);
        btnSetGoalWeekly = (Button)findViewById(R.id.btnSetGoalWeekly);
        btnSetGoalMonthly = (Button)findViewById(R.id.btnSetGoalMonthly);

        tvDailyDistPercent = (TextView)findViewById(R.id.tvDailyDistPercent);
        tvWeeklyDistPercent = (TextView)findViewById(R.id.tvWeeklyDistPercent);
        tvMonthlyDistPercent = (TextView)findViewById(R.id.tvMonthlyDistPercent);
        tvDailyCalPercent = (TextView)findViewById(R.id.tvDailyCalPercent);
        tvWeeklyCalPercent = (TextView)findViewById(R.id.tvWeeklyCalPercent);
        tvMonthlyCalPercent = (TextView)findViewById(R.id.tvMonthlyCalPercent);

        pbDailyDistance = (ProgressBar) findViewById(R.id.pbDailyDistance);
        pbWeeklyDistance = (ProgressBar) findViewById(R.id.pbWeeklyDistance);
        pbMonthlyDistance = (ProgressBar) findViewById(R.id.pbMonthlyDistance);
        pbDailyCalories = (ProgressBar) findViewById(R.id.pbDailyCalories);
        pbWeeklyCalories = (ProgressBar) findViewById(R.id.pbWeeklyCalories);
        pbMonthlyCalories = (ProgressBar) findViewById(R.id.pbMonthlyCalories);

    }

    // Overrides
    @Override
    public void onClick(View v) {

        Intent intent = new Intent(this, SetGoalsActivity.class);

        switch(v.getId()){
            case R.id.btnSetGoalDaily:
                intent.putExtra(SetGoalsActivity.INTENT_KEY,
                        SetGoalsActivity.INTENT_VALUE_DAILY);
                break;
            case R.id.btnSetGoalWeekly:
                intent.putExtra(SetGoalsActivity.INTENT_KEY,
                        SetGoalsActivity.INTENT_VALUE_WEEKLY);
                break;
            case R.id.btnSetGoalMonthly:
                intent.putExtra(SetGoalsActivity.INTENT_KEY,
                        SetGoalsActivity.INTENT_VALUE_MONTHLY);
                break;
        }
        startActivity(intent);
    }
}
