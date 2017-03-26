package com.mad.k00191419.prorun.activities;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mad.k00191419.prorun.R;

public class DetailsActivity extends FragmentActivity {

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        // get references to private fields
        setupReferences();
    }

    private void setupReferences() {
        mViewPager = (ViewPager)findViewById(R.id.vpDetailsActivity);

    }
}
