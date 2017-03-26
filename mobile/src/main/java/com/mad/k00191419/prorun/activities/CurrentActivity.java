package com.mad.k00191419.prorun.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.mad.k00191419.prorun.R;

public class CurrentActivity extends AppCompatActivity implements View.OnClickListener{

    // View References
    ImageButton ibStop;
    ImageButton ibStartPause;
    Button      btnOpenMap;

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
        btnOpenMap.setOnClickListener(this);
    }

    private void setupReferences() {
        ibStop = (ImageButton)findViewById(R.id.ibStop);
        ibStartPause = (ImageButton)findViewById(R.id.ibStartPause);
        btnOpenMap = (Button)findViewById(R.id.btnOpenMap);
    }

    // Interface Implementations
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.ibStop:
                stopCurrentRun();
                break;
            case R.id.ibStartPause:
                startPauseRun();
                break;
            case R.id.btnOpenMap:
                Intent intent = new Intent(this, MapActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void startPauseRun() {
    }

    private void stopCurrentRun() {
        Intent intent = new Intent(this, SummaryActivity.class);
        startActivity(intent);
    }
}
