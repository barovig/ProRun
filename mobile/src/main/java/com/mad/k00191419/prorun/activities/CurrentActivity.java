package com.mad.k00191419.prorun.activities;

import android.content.Intent;
import android.media.Image;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.mad.k00191419.prorun.R;

public class CurrentActivity extends AppCompatActivity implements View.OnClickListener{

    private ViewPager mViewPager;

    // View References
    ImageButton ibStop;
    ImageButton ibStartPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current);

        setupReferences();
        setupListeners();

    }

    private void setupListeners() {
        ibStop.setOnClickListener(this);
        ibStartPause.setOnClickListener(this);
    }

    private void setupReferences() {
        mViewPager = (ViewPager)findViewById(R.id.vpCurrentActivity);
        ibStop = (ImageButton)findViewById(R.id.ibStop);
        ibStartPause = (ImageButton)findViewById(R.id.ibStartPause);
    }

    // Interface Implementations
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.ibStop:
                stopCurrentRun();
                break;
        }
    }

    private void stopCurrentRun() {
        Intent intent = new Intent(this, SummaryActivity.class);
        startActivity(intent);
    }
}
