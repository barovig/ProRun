package com.mad.k00191419.prorun.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mad.k00191419.prorun.R;

public class ViewGoalsActivity extends AppCompatActivity implements View.OnClickListener{

    // Views
    Button  btnSetGoalDaily;
    Button  btnSetGoalWeekly;
    Button  btnSetGoalMonthly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_goals);
        setupReferences();
        setupListeners();
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

    }

    // Overrides
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnSetGoalDaily:
            case R.id.btnSetGoalWeekly:
            case R.id.btnSetGoalMonthly:
                Intent intent = new Intent(this, SetGoalsActivity.class);
                startActivity(intent);
                break;
        }
    }
}
