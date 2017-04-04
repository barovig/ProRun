package com.mad.k00191419.prorun.activities;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mad.k00191419.prorun.R;
import com.mad.k00191419.prorun.fragments.SummaryFragment;

public class SummaryActivity extends FragmentActivity implements View.OnClickListener{


    // View References
    Button btnDetails;
    Button btnOk;

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
        Intent intent = getIntent();
        String distance = intent.getStringExtra(CurrentActivity.INTENT_KEY_DISTANCE);
        String time = intent.getStringExtra(CurrentActivity.INTENT_KEY_TIME);
        String avgSpeed = intent.getStringExtra(CurrentActivity.INTENT_KEY_AVG_SPEED);
        String calories = intent.getStringExtra(CurrentActivity.INTENT_KEY_CALORIES);
        String startDate = intent.getStringExtra(CurrentActivity.INTENT_KEY_START_DATE);

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
                startActivity(intent);
                break;
            case R.id.btnOk:
                finish();
        }
    }

    public void getGoals() {
        //TODO: Get goals from DB, calculate progress and display it
    }

}
