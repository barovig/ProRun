package com.mad.k00191419.prorun.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.mad.k00191419.prorun.R;

public class ProRunActivity extends AppCompatActivity implements View.OnClickListener {

    // View references
    TextView    tvProgress;
    ProgressBar pbGoalProgress;
    Spinner     spinGoalPeriod;
    ImageButton ibStart;
    ImageButton ibStop;
    ListView    lvHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_run);

        setupReferences();
        setupListeners();
    }

    private void setupReferences() {
        tvProgress = (TextView) findViewById(R.id.tvProgress);
        pbGoalProgress = (ProgressBar)findViewById(R.id.pbGoalProgress);
        spinGoalPeriod = (Spinner) findViewById(R.id.spinGoalPeriod);
        ibStart = (ImageButton) findViewById(R.id.ibStart);
        ibStop = (ImageButton) findViewById(R.id.ibStop);
        lvHistory = (ListView) findViewById(R.id.lvHistory);

    }

    private void setupListeners() {
        ibStart.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch(id){
            case R.id.ibStart:
                Intent intent = new Intent(this, CurrentActivity.class);
                startActivity(intent);
                break;

        }

    }
}
