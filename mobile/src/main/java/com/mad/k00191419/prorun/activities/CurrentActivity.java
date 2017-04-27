package com.mad.k00191419.prorun.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mad.k00191419.prorun.R;
import com.mad.k00191419.prorun.db.Run;
import com.mad.k00191419.prorun.services.ProRunService;
import com.mad.k00191419.prorun.utils.Utils;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class CurrentActivity extends AppCompatActivity
        implements View.OnClickListener, ServiceConnection {

    // Constants
    private static final String APP_NAME = "__PRO_RUN";
    private static final int    MY_PERMISSIONS_ACCESS_FINE_LOCATION = 300;
    private final long          TIMER_INTERVAL = 500;
    private final String        PREF_KEY_ISRUNNING = "isRunning";
    public static final String  INTENT_KEY_RUN = "prorun_run";

    // Views
    ImageButton     ibStop;
    ImageButton     ibStartPause;
    Button          btnOpenMap;
    TextView        tvCurrentDistance;
    TextView        tvCurrentTime;
    TextView        tvCurrentSpeed;
    TextView        tvCurrentCalories;
    TextView        tvAccuracy;

    // Fields
    ProRunService       mService = null;
    boolean             isServiceConnected = false;
    boolean             isOnPause = false;
    boolean             isStopped = true;
    long                mStartTimeMs = 0;
    Timer               mTimer;
    Handler             mUiHandler;     // used to update UI thread from timer
    SharedPreferences   mPref;

    Runnable mUiRunnable = new Runnable() {
        @Override
        public void run() {
            float acc = mService.getAccuracy();
            if (acc > 0)
                tvAccuracy.setText(String.format("GPS Accuracy: %.2fm", acc));
            else
                tvAccuracy.setText("GPS Accuracy: Low");
            //Log.d(APP_NAME, "Updated Accuracy: " + acc);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(APP_NAME, "CURRENT ACTIVIITY___: onCreate()");
        setContentView(R.layout.activity_current);
        setupReferences();
        setupListeners();
        // create UI Handler
        mUiHandler = new Handler();
        mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }


    @Override
    protected void onStart() {
        Log.d(APP_NAME, "CURRENT ACTIVIITY___: onStart()");
        super.onStart();
        // bind and start ProRunService onStart
        connectToService();
        // other jobs will run only when service is connected (in onConnected)

    }

    @Override
    protected void onPause() {
        Log.d(APP_NAME, "CURRENT ACTIVIITY___: onPause()");
        super.onPause();
        disconnectFromService();
    }

    @Override
    protected void onDestroy() {
        Log.d(APP_NAME, "CURRENT ACTIVIITY___: onDestroy()");
        super.onDestroy();
        mTimer.cancel();
        mUiHandler.removeCallbacks(mUiRunnable);
        // make sure activity is closed
        //finish();
    }

    @Override
    protected void onResume() {
        Log.d(APP_NAME, "CURRENT ACTIVIITY___: onResume()");
        super.onResume();
    }

    private void disconnectFromService() {
        if (isServiceConnected && mService != null) {
            mService.stopLocationListener();
            unbindService(this);
            isServiceConnected = false;
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        // Once connected, get service obj
        mService = ((ProRunService.LocalBinder)service).getService();
        Log.d(APP_NAME, "CURRENT ACTIVIITY___: Connected to ProRunService.");

        // set flag and start getting accuracy
        isServiceConnected = true;
        // tell service to start listening for location changes
        mService.startLocationListener();

        // if still running, start UI updater
        if(mPref.getBoolean(PREF_KEY_ISRUNNING, false)){
            isStopped = false;
            attachToRun();
        }
        startAccuracyUpdater();

    }

    private void attachToRun() {
        if(mService != null) {
            // get run start time
            Run run = mService.getRun();
            if(run == null) return;
            mStartTimeMs = run.getStartDate();
            startUiUpdater();
            ibStartPause.setImageResource(R.mipmap.pause);
        }
    }

    private void startUiUpdater() {
        if(mStartTimeMs == 0)
            mStartTimeMs = System.currentTimeMillis();

        TimerTask task = new TimerTask(){

            @Override
            public void run() {
                updateUI();
            }
        };
        mTimer = new Timer(true);
        mTimer.scheduleAtFixedRate(task, 0, TIMER_INTERVAL);
    }

    private void updateUI(){
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                Calendar c = Calendar.getInstance();
                String[] stats = mService.getStats();
                String  distance = stats[0],
                        speed = stats[1],
                        cal = stats[2];

                tvCurrentDistance.setText(distance);
                tvCurrentTime.setText(Utils.formatInterval(c.getTimeInMillis() - mStartTimeMs));
                tvCurrentCalories.setText(cal);
                tvCurrentSpeed.setText(speed);
            }
        });
    }


    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

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
                startMapActivity();
                break;
        }
    }


    // SETUP CODE
    ////////////////////////


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
        tvAccuracy = (TextView)findViewById(R.id.tvAccuracy);
    }


    private void connectToService() {
        bindService(new Intent(this, ProRunService.class), this, Context.BIND_AUTO_CREATE);
        startService(new Intent(this, ProRunService.class));
        isServiceConnected = true;
    }

    private void startAccuracyUpdater() {
        // check if service is connected (it should be)
        if(!isServiceConnected)
            connectToService();

        TimerTask task = new TimerTask(){

            @Override
            public void run() {
                updateAccuracy();
            }
        };
        mTimer = new Timer(true);
        mTimer.scheduleAtFixedRate(task, 0, TIMER_INTERVAL*2);
    }

    private void updateAccuracy() {
        mUiHandler.post(mUiRunnable);
    }

    private void stopUiUpdater() {
        mTimer.cancel();
    }

    // EXECUTION CODE
    //////////////////////////////////////

    private void stopRun() {

        if(isStopped) {
            Toast.makeText(this, getString(R.string.string_toast_notrunning),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Stop UI updater
        mTimer.cancel();

        // Get run obj and add to intent
        Run run = mService.getRun();
        Intent intent = new Intent(this, SummaryActivity.class);
        intent.putExtra(INTENT_KEY_RUN, run);
        // stop run
        mService.stopRun();
        // disconnect from service
        disconnectFromService();
        // STOP service
        stopService(new Intent(this, ProRunService.class));

        // cleanup and run SummaryActivity
        isStopped = true;
        startActivity(intent);
        finish();
    }

    private void startMapActivity() {
        if(isServiceConnected &&
                mService.getRun() != null &&
                mService.getRun().getLocations().size() != 0) {
            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra(INTENT_KEY_RUN, mService.getRun());
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "No route recorded yet", Toast.LENGTH_SHORT).show();
        }
    }

    private void startPauseRun() {
        if (mService == null)
            connectToService();

        if (isStopped) {              // start run when stopped
            isStopped = false;
            mService.startRun();
            mStartTimeMs = mService.getRun().getStartDate();
            ibStartPause.setImageResource(R.mipmap.pause);
            startUiUpdater();
        } else if (isOnPause) {         // resume if on pause
            isOnPause = false;
            ibStartPause.setImageResource(R.mipmap.pause);
            mService.resumeRun();
            startUiUpdater();
        } else {                       // pause service if is running
            isOnPause = true;
            ibStartPause.setImageResource(R.mipmap.play);
            mService.pauseRun();
            stopUiUpdater();
        }
    }

}
