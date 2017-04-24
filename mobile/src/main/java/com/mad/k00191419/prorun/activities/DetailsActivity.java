package com.mad.k00191419.prorun.activities;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mad.k00191419.prorun.R;
import com.mad.k00191419.prorun.db.Run;
import com.mad.k00191419.prorun.fragments.SummaryFragment;
import com.mad.k00191419.prorun.utils.DetailsPagerAdapter;
import com.mad.k00191419.prorun.utils.Utils;

public class DetailsActivity extends FragmentActivity implements View.OnClickListener {

    private ViewPager mViewPager;

    // View References
    Button btnSummaryClose;

    // Fields
    Run mRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        setupReferences();
        setupListeners();
        DetailsPagerAdapter adapter = new DetailsPagerAdapter(getSupportFragmentManager());
        // following is rather hacky way to communicate with fragment created with ViewPager.
        // pass Run to adapter which will create fragment
        // and will update relevant views in it right after creation
        Intent intent = getIntent();
        mRun = intent.getParcelableExtra(CurrentActivity.INTENT_KEY_RUN);
        if(mRun == null) return;    // null ref check
        adapter.setRun(mRun);
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
