package com.mad.k00191419.prorun.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mad.k00191419.prorun.R;

import org.w3c.dom.Text;

public class SummaryFragment extends Fragment {

    // Views
//    TextView tvTotalDistance;
//    TextView tvSummaryTime;
//    TextView tvAvgSpeed;
//    TextView tvTotalCalories;
//    TextView tvStartDate;
//    TextView tvProgressDaily;
//    TextView tvProgressWeekly;
//    TextView tvProgressMonthly;

    private View mView = null;

    public SummaryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_summary, container, false);
        //setupReferences(view);
        return mView;
    }

    public void setTextViewText(int id, String text){
        View view = mView.findViewById(id);
        // make sure we found it and it is TextView
        if(view != null && view instanceof TextView){
            TextView tView = (TextView)mView.findViewById(id);
            tView.setText(text);
        }
    }

//    private void setupReferences(View view){
//        tvTotalDistance = (TextView)view.findViewById(R.id.tvTotalDistance);
//        tvSummaryTime = (TextView)view.findViewById(R.id.tvSummaryTime);
//        tvCurrentSpeed = (TextView)view.findViewById(R.id.tvAvgSpeed);
//        tvCurrentCalories = (TextView)view.findViewById(R.id.tvTotalCalories);
//        tvStartDate = (TextView)view.findViewById(R.id.tvStartDate);
//        tvProgressDaily = (TextView)view.findViewById(R.id.tvProgressDaily);
//        tvProgressWeekly = (TextView)view.findViewById(R.id.tvProgressWeekly);
//        tvProgressMonthly = (TextView)view.findViewById(R.id.tvProgressMonthly);
//    }


}
