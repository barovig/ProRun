package com.mad.k00191419.prorun.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mad.k00191419.prorun.R;
import com.mad.k00191419.prorun.services.ProRunService;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class CurrentActivity extends AppCompatActivity
        implements View.OnClickListener, ServiceConnection{

    // Constants
    private static final String APP_NAME = "__PRO_RUN";
    private final long TIMER_INTERVAL = 500;

    // Fields
    ProRunService   mService = null;
    boolean         mServiceConnected = false;
    boolean         isOnPause = false;
    boolean         isStopped = true;
    Timer           mTimer;
    Handler         mUiHandler;     // used to update UI thread from timer

    // View References
    ImageButton     ibStop;
    ImageButton     ibStartPause;
    Button          btnOpenMap;
    TextView        tvCurrentDistance;
    TextView        tvCurrentTime;
    TextView        tvCurrentSpeed;
    TextView        tvCurrentCalories;

    // Activity overrides
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current);

        setupReferences();
        setupListeners();

        // create UI Handler
        mUiHandler = new Handler();

    }

    @Override
    protected void onStart() {
        super.onStart();
        // bind and start ProRunService
        connectToService();
    }

    @Override
    protected void onDestroy() {
        disconnectFromService();
        super.onDestroy();
    }

    // Interface Implementations
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.ibStop:
                stopRun();
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

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mService = ((ProRunService.LocalBinder)service).getService();
        mServiceConnected = true;
        Log.d(APP_NAME, "Connected to ProRunService.");
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mService = null;
        mServiceConnected = true;
        Log.d(APP_NAME, "Disconnected from ProRunService.");

    }

    // Private helpers
    private void setupListeners() {
        ibStop.setOnClickListener(this);
        ibStartPause.setOnClickListener(this);
        btnOpenMap.setOnClickListener(this);
    }

    private void setupReferences() {
        ibStop = (ImageButton)findViewById(R.id.ibStop);
        ibStartPause = (ImageButton)findViewById(R.id.ibStartPause);
        btnOpenMap = (Button)findViewById(R.id.btnOpenMap);
        tvCurrentTime = (TextView)findViewById(R.id.tvCurrentTime);
        tvCurrentDistance = (TextView)findViewById(R.id.tvCurrentDistance);
        tvCurrentCalories = (TextView)findViewById(R.id.tvCurrentCalories);
        tvCurrentSpeed = (TextView)findViewById(R.id.tvCurrentSpeed);
    }

    private void startPauseRun() {
        if(!mServiceConnected) connectToService();

        if(isStopped){              // run if is stopped
            isStopped = false;
            mService.startRun();
            startUiUpdater();
        }
        else if(isOnPause){         // resume if on pause
            isOnPause = false;
            mService.resumeRun();
        }
        else{                       // its running - pause service
            isOnPause = true;
            mService.pauseRun();
            stopUiUpdater();
        }
    }

    private void startUiUpdater() {
        TimerTask task = new TimerTask(){

            @Override
            public void run() {
                updateUI();
            }
        };
        mTimer = new Timer(true);
        mTimer.scheduleAtFixedRate(task, 0, TIMER_INTERVAL);
    }

    private void stopUiUpdater(){
        mTimer.cancel();
    }

    private void stopRun() {
        // stop service
        mService.stopRun();
        isStopped = true;
        // finish this activity and don't return to it if navigating back.
        finish();
        // start summary activity
        Intent intent = new Intent(this, SummaryActivity.class);
        startActivity(intent);
    }

    private void connectToService(){
        bindService(new Intent(this, ProRunService.class), this, Context.BIND_AUTO_CREATE);
        startService(new Intent(this, ProRunService.class));
    }

    private void disconnectFromService(){
        if (mServiceConnected) {
            unbindService(this);
            stopService(new Intent(this, ProRunService.class));
            mServiceConnected = false;
        }
    }

    private void updateUI(){
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                String[] stats = mService.getStats();
                String  time = stats[1],
                        distance = stats[0],
                        cal = "",
                        speed = stats[2];

                tvCurrentDistance.setText(distance);
                tvCurrentTime.setText(time);
                tvCurrentCalories.setText(cal);
                tvCurrentSpeed.setText(speed);
                Log.d(APP_NAME,"Updated UI\n\tTime: "+time+"\n\tDistance: "+distance);
            }
        });
    }


}
