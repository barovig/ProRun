package com.mad.k00191419.prorun.utils;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mad.k00191419.prorun.R;
import com.mad.k00191419.prorun.activities.CurrentActivity;
import com.mad.k00191419.prorun.db.Run;
import com.mad.k00191419.prorun.fragments.DetailsFragment;
import com.mad.k00191419.prorun.fragments.GraphFragment;
import com.mad.k00191419.prorun.fragments.MapFragment;
import com.mad.k00191419.prorun.fragments.SummaryFragment;


public class DetailsPagerAdapter extends FragmentPagerAdapter {

    private final int NUM_FRAGMENTS = 4;
    Run mRun;
    public DetailsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch(position){
            case 1:
                fragment = new GraphFragment();
                break;
            case 2:
                fragment = new GraphFragment();
                break;
            case 3:
                fragment = new MapFragment();
                break;
            default:
                fragment = new DetailsFragment();
                ((DetailsFragment)fragment).setRun(mRun);
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return NUM_FRAGMENTS;
    }


    public void setRun(Run run){
        mRun = run;
    }
}
