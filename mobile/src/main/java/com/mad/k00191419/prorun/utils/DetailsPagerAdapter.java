package com.mad.k00191419.prorun.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mad.k00191419.prorun.fragments.GraphFragment;
import com.mad.k00191419.prorun.fragments.MapFragment;
import com.mad.k00191419.prorun.fragments.SummaryFragment;


public class DetailsPagerAdapter extends FragmentPagerAdapter {

    private final int NUM_FRAGMENTS = 4;
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
                fragment = new SummaryFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return NUM_FRAGMENTS;
    }
}
