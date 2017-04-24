package com.mad.k00191419.prorun.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mad.k00191419.prorun.db.ProRunDB;
import com.mad.k00191419.prorun.db.Run;

import java.util.ArrayList;


public class RunListAdapter extends BaseAdapter {

    private Context         mContext;
    private ArrayList<Run>  mRuns;

    public RunListAdapter(Context context, ArrayList<Run> mRuns) {
        this.mContext = context;
        this.mRuns = mRuns;
    }

    @Override
    public int getCount() {
        return mRuns.size();
    }

    @Override
    public Object getItem(int position) {
        return mRuns.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RunLayout rLayout = null;
        Run r = mRuns.get(position);
        if(convertView == null){
            rLayout = new RunLayout(mContext, r);
        }
        else{
            rLayout = (RunLayout) convertView;
            rLayout.setRunText(r);
        }
        return rLayout;
    }
}
