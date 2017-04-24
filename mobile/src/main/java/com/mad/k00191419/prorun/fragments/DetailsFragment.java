package com.mad.k00191419.prorun.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mad.k00191419.prorun.R;
import com.mad.k00191419.prorun.db.Run;
import com.mad.k00191419.prorun.utils.Utils;


public class DetailsFragment extends Fragment {

    //Views
    TextView tvTotalDistance;
    TextView tvSummaryTime;
    TextView tvAvgSpeed;
    TextView tvTotalCalories;
    TextView tvStartDate;
    TextView tvHighestSpeed;

    //Fields
    Run mRun;
    View mView;

    public DetailsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_details, container, false);
        setupReferences(mView);
        updateFromRun();
        return mView;
    }

    private void updateFromRun() {
        if(mRun == null) return;    // null ref check
        String distance = Utils.formatDistance(mRun.getTotalDistance());
        String time = Utils.formatInterval(mRun.getTotalTime());
        String avgSpeed = Utils.formatSpeed(mRun.getAvgSpeed());
        String calories = Utils.formatCalories(mRun.getTotalCalories());
        String startDate = Utils.formatDate(mRun.getStartDate());
        String highestSpeed = Utils.formatSpeed(mRun.getHighestSpeed());
        // set textviews
        tvTotalDistance.setText(distance);
        tvSummaryTime.setText(time);
        tvAvgSpeed.setText(avgSpeed);
        tvTotalCalories.setText(calories);
        tvStartDate.setText(startDate);
        tvHighestSpeed.setText(highestSpeed);
    }

    private void setupReferences(View view) {
        tvTotalDistance = (TextView) view.findViewById(R.id.tvTotalDistance);
        tvSummaryTime = (TextView) view.findViewById(R.id.tvSummaryTime);
        tvAvgSpeed = (TextView) view.findViewById(R.id.tvAvgSpeed);
        tvTotalCalories = (TextView) view.findViewById(R.id.tvTotalCalories);
        tvStartDate = (TextView) view.findViewById(R.id.tvStartDate);
        tvHighestSpeed = (TextView) view.findViewById(R.id.tvHighestSpeed);
    }


    public void setRun(Run run) {
        mRun = run;
    }
}
