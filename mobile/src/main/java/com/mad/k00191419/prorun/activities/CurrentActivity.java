package com.mad.k00191419.prorun.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
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
        implements View.OnClickListener, ServiceConnection{

    // Constants
    private static final String APP_NAME = "__PRO_RUN";
    private static final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 300;
    private final long TIMER_INTERVAL = 500;
    public static final String INTENT_KEY_RUN = "run";
    public static final String INTENT_KEY_AVG = "avg";
    public static final String INTENT_KEY_MAX = "max";
    // Fields
    ProRunService       mService = null;
    boolean             isServiceConnected = false;
    boolean             isOnPause = false;
    boolean             isStopped = true;
    long                mStartTimeMs;
    Timer               mTimer;
    Handler             mUiHandler;     // used to update UI thread from timer
    SharedPreferences   mPref;

    // View References
    ImageButton     ibStop;
    ImageButton     ibStartPause;
    Button          btnOpenMap;
    TextView        tvCurrentDistance;
    TextView        tvCurrentTime;
    TextView        tvCurrentSpeed;
    TextView        tvCurrentCalories;
    TextView        tvAccuracy;

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
        // bind and start ProRunService onStart (not onCreate)
        connectToService();
        if(isStopped){
            startAccuracyUpdater();
        }
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
                startMapActivity();
                break;
        }
    }



    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mService = ((ProRunService.LocalBinder)service).getService();
        isServiceConnected = true;
        Log.d(APP_NAME, "Connected to ProRunService.");
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mService = null;
        isServiceConnected = false;
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
        tvAccuracy = (TextView)findViewById(R.id.tvAccuracy);
    }

    private void startPauseRun() {

        if(checkSelfPermissionToAccessDeviceLocation()) {
            if (!isServiceConnected) connectToService();
            if (isStopped) {              // start run when stopped
                isStopped = false;
                mService.startRun();
                ibStartPause.setImageResource(R.mipmap.pause);
                startUiUpdater();
            } else if (isOnPause) {         // resume if on pause
                isOnPause = false;
                ibStartPause.setImageResource(R.mipmap.play);
                mService.resumeRun();
            } else {                       // pause service if is running
                isOnPause = true;
                ibStartPause.setImageResource(R.mipmap.pause);
                mService.pauseRun();
                stopUiUpdater();
            }
        }
    }

    private void startUiUpdater() {
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

    private void stopUiUpdater(){
        mStartTimeMs = 0;
        mTimer.cancel();
    }

    private void stopRun() {
        if(isStopped) return;
        // Get run obj and add to intent
        Run run = mService.getRun();
        Intent intent = new Intent(this, SummaryActivity.class);
        intent.putExtra(INTENT_KEY_RUN, run);
        intent.putExtra(INTENT_KEY_AVG, run.getAvgSpeed());
        intent.putExtra(CurrentActivity.INTENT_KEY_MAX, run.getHighestSpeed());

        // stop service
        mService.stopRun();
        isStopped = true;
        // finish this activity and don't return to it if navigating back.
        finish();

        startActivity(intent);
    }

    private void connectToService(){
        bindService(new Intent(this, ProRunService.class), this, Context.BIND_AUTO_CREATE);
        startService(new Intent(this, ProRunService.class));
    }

    private void disconnectFromService(){
        if (isServiceConnected) {
            unbindService(this);
            stopService(new Intent(this, ProRunService.class));
            isServiceConnected = false;
        }
    }

    private void updateUI(){
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                Calendar c = Calendar.getInstance();
                String[] stats = mService.getStats();
                String  distance = stats[0],
                        speed = stats[2],
                        cal = stats[3];

                tvCurrentDistance.setText(distance);
                tvCurrentTime.setText(Utils.formatInterval(c.getTimeInMillis() - mStartTimeMs));
                tvCurrentCalories.setText(cal);
                tvCurrentSpeed.setText(speed);
                Log.d(APP_NAME,"Updated UI\n\tSpeed: "+speed+"\n\tDistance: "+distance);
            }
        });
    }

    private void startAccuracyUpdater() {
        if(!isServiceConnected) connectToService();
        TimerTask task = new TimerTask(){

            @Override
            public void run() {
                if(isServiceConnected)
                    updateAccuracy();
            }
        };
        mTimer = new Timer(true);
        mTimer.scheduleAtFixedRate(task, 0, TIMER_INTERVAL*2);
    }

    private void updateAccuracy() {
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                float acc = mService.getAccuracy();
                if(acc > 0)
                    tvAccuracy.setText(String.format("GPS Accuracy: %.2fm", acc));
                else
                    tvAccuracy.setText("GPS Accuracy: Low");
                Log.d(APP_NAME,"Updated Accuracy: "+acc);
            }
        });
    }

    private void startMapActivity() {
        if(isServiceConnected && mService.isRunning()) {
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "No route recorded yet", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkSelfPermissionToAccessDeviceLocation(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_ACCESS_FINE_LOCATION);

            } else {
                //show request to grant permission
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_ACCESS_FINE_LOCATION);
            }
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case MY_PERMISSIONS_ACCESS_FINE_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        //TODO: request permission from user
                        return;
                    }
                    else{
                        Toast.makeText(this, "Location Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }
}
