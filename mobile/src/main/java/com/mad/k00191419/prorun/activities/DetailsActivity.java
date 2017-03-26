package com.mad.k00191419.prorun.activities;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mad.k00191419.prorun.R;
import com.mad.k00191419.prorun.utils.DetailsPagerAdapter;

public class DetailsActivity extends FragmentActivity implements View.OnClickListener {

    private ViewPager mViewPager;

    // View References
    Button btnSummaryClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        setupReferences();
        setupListeners();

        DetailsPagerAdapter adapter = new DetailsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
    }

    private void setupListeners() {
        btnSummaryClose.setOnClickListener(this);
    }

    private void setupReferences() {
        mViewPager = (ViewPager)findViewById(R.id.vpDetailsActivity);
        btnSummaryClose = (Button)findViewById(R.id.btnSummaryClose);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.btnSummaryClose:
                finish();
                break;
        }
    }
}
