package com.mad.k00191419.prorun.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mad.k00191419.prorun.R;
import com.mad.k00191419.prorun.db.Run;
import com.mad.k00191419.prorun.utils.Utils;

import org.w3c.dom.Text;

public class SummaryFragment extends Fragment {

    // Views
//    TextView tvTotalDistance;
//    TextView tvSummaryTime;
//    TextView tvAvgSpeed;
//    TextView tvTotalCalories;
//    TextView tvStartDate;
//    TextView tvLabelProgressDaily;
//    TextView tvLabelProgressWeekly;
//    TextView tvLabelProgressMonthly;
//    ProgressBar pbDaily;
//    ProgressBar pbWeekly;
//    ProgressBar pbMonthly;

    private View mView = null;

    public SummaryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_summary, container, false);
//        setupReferences(mView);
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

    public void setProgressBarProgress(int id, int progress){
        View view = mView.findViewById(id);

        if(view != null && view instanceof ProgressBar){
            ProgressBar pb = (ProgressBar) mView.findViewById(id);
            pb.setProgress(progress);
        }
    }

//    private void setupReferences(View view){
//        tvTotalDistance = (TextView)view.findViewById(R.id.tvTotalDistance);
//        tvSummaryTime = (TextView)view.findViewById(R.id.tvSummaryTime);
//        tvAvgSpeed = (TextView)view.findViewById(R.id.tvAvgSpeed);
//        tvTotalCalories = (TextView)view.findViewById(R.id.tvTotalCalories);
//        tvStartDate = (TextView)view.findViewById(R.id.tvStartDate);
//        tvLabelProgressDaily = (TextView)view.findViewById(R.id.tvLabelProgressDaily);
//        tvLabelProgressWeekly = (TextView)view.findViewById(R.id.tvLabelProgressWeekly);
//        tvLabelProgressMonthly = (TextView)view.findViewById(R.id.tvLabelProgressMonthly);
//
//        pbDaily = (ProgressBar)view.findViewById(R.id.pbDaily);
//        pbWeekly = (ProgressBar)view.findViewById(R.id.pbWeekly);
//        pbMonthly = (ProgressBar)view.findViewById(R.id.pbMonthly);
//    }



}
