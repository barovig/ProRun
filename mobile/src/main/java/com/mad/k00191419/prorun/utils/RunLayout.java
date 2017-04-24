package com.mad.k00191419.prorun.utils;

import android.content.Context;
import java.text.SimpleDateFormat;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mad.k00191419.prorun.R;
import com.mad.k00191419.prorun.activities.CurrentActivity;
import com.mad.k00191419.prorun.activities.SummaryActivity;
import com.mad.k00191419.prorun.db.ProRunDB;
import com.mad.k00191419.prorun.db.Run;

import java.text.Format;
import java.util.Date;


public class RunLayout extends LinearLayout
implements View.OnClickListener{

    // Views
    private TextView tvRunDate;
    private TextView tvDistance;
    private TextView tvTime;
    private Run      mRun;

    // Fields
    private Context mContext;   // to activity where view is rendered

    public RunLayout(Context context, Run r) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.listview_run, this, true);

        setupReferences();
        setOnClickListener(this);
        mContext = context;
        mRun = r;
        setRunText(r);

    }

    private void setupReferences() {
        tvRunDate = (TextView) findViewById(R.id.tvRunDate);
        tvDistance = (TextView) findViewById(R.id.tvDistance);
        tvTime = (TextView) findViewById(R.id.tvTime);
    }

    public void setRunText(Run run) {
        Date date = new Date(run.getStartDate());
        Format format = new SimpleDateFormat("EEE dd/mm/yyyy");
        mRun = run;
        tvRunDate.setText(format.format(date));
        tvTime.setText(Utils.formatInterval(run.getTotalTime()));
        tvDistance.setText(Utils.formatDistance(run.getTotalDistance()));
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(mContext, SummaryActivity.class);
        intent.putExtra(CurrentActivity.INTENT_KEY_RUN, mRun);
        // Progress Details in Shared Prefs
        mContext.startActivity(intent);
    }
}
