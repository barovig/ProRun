package com.mad.k00191419.prorun.services;

import android.Manifest;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.mad.k00191419.prorun.R;
import com.mad.k00191419.prorun.activities.CurrentActivity;
import com.mad.k00191419.prorun.db.ProRunDB;
import com.mad.k00191419.prorun.db.Run;
import com.google.android.gms.wearable.Node;
import com.mad.k00191419.prorun.utils.Utils;

import android.widget.Toast;


//import com.android.server.*

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;


public class ProRunService extends Service implements LocationListener {

    // Consts
    private final String    APP_NAME = "__PRO_RUN";
    private final long      MIN_TIME_INTERVAL_MS = 0;
    private final String    PREF_KEY_ISRUNNING = "isRunning";
    public static final int INTENT_REQCODE_SEND = 154;
    private final int       NOTIF_ID = 123234;
    private final String    MSGAPI_MSG_KEY = "/message_prorun";
    private final String    MSGAPI_MSG_STOP = "/prorun_stop";

    // Service fields
    private LocationManager mLocationManager;
    private long            mRunNo;
    private final IBinder   mBinder = new LocalBinder();
    private ProRunDB        mDb;
    private Run             mRun;
    private float           mDistance;
    private long            mTime;
    private long            mCalories;
    private float           mSpeed;
    private long            mStartDate;
    private Location        mLastLocation;
    private float           mAccuracy;
    private SharedPreferences mPrefs;
    NotificationManagerCompat mNotificationManager;
    GoogleApiClient           mApi = null;
    Node                      mNode = null;
    TimerTask                 mWearTimer;

    // Flags
    private boolean         isRunning = false;
    private boolean         isListeningLocUpdates = false;

    // Inner classes
    public class LocalBinder extends Binder {
        public ProRunService getService() {
            return ProRunService.this;
        }
    }

    class ApiConnector extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            setupNodeAndApi();
            // void return type
            return null;
        }
    }


    // Overrides

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(APP_NAME, "Created ProRun service");
        // get db
        mDb = new ProRunDB(getApplicationContext());
        // setup LocationManager and prefs
        mLocationManager = (LocationManager) this.getSystemService(getApplicationContext().LOCATION_SERVICE);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        // Check that location is enabled
        checkLocationProvider();
        // create compat notification manager
        mNotificationManager = NotificationManagerCompat.from(this);

        // execute API connection thread
        new ApiConnector().execute();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(APP_NAME, "Binding ProRunService...");
        return mBinder;
    }

    @Override
    public void onLocationChanged(Location location) {
        // If running, update run data
        if(isRunning) {
            updateRunFromLoc(location);
        }
        // else just get location accuracy
        mAccuracy = location.getAccuracy();
    }



    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    // SETUP CODE
    ////////////////////////////////////

    private void checkLocationProvider() {
        if(!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(this, getString(R.string.enable_gps), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private void setupNodeAndApi() {
            // create google client and connect
            mApi = new GoogleApiClient.Builder(this)
                    .addApi(Wearable.API)
                    .build();

            mApi.blockingConnect(); // sync call. use connect() for async

            // Retrieve all nodes
            NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes( mApi ).await();

            // Assume only one is connected
            for(Node n : nodes.getNodes()){
                mNode = n;
                return;
            }
    }

    private void buildNotification() {
        Intent intent = new Intent(this, CurrentActivity.class);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_launcher)
                .setLargeIcon(
                        BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)
                )
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.string_notification_text))
                .setContentIntent(
                        PendingIntent.getActivity(this, INTENT_REQCODE_SEND,
                                intent, PendingIntent.FLAG_UPDATE_CURRENT)
                )
                .setOngoing(true);

        mNotificationManager.notify(NOTIF_ID, builder.build());
    }

    // EXECUTION CODE
    ////////////////////////////////

    private void updateRunFromLoc(Location location) {


        long locTime = location.getTime();

        if (locTime > mStartDate) {    // skip locations before startDate (glitch?)
            // update stats
            float distInterval =
                    (mLastLocation == null) ? 0 : mLastLocation.distanceTo(location);

            // ignore if moved within accuracy range
            if(mAccuracy/2 < distInterval) {
                mDistance += distInterval;
                mRun.addLocation(location);
            }

            mSpeed = location.getSpeed();
            mLastLocation = location;

            // calculate calories spend:
            // http://fitness.stackexchange.com/questions/15608/energy-expenditure-calories-burned-equation-for-running
            if (mSpeed > 0.7)        // don't count low speed movement contribution
                mCalories += 0.5 * 90 * (0.2 * mSpeed * 60 + 3.5);

            Log.d(APP_NAME, String.format("D%f\tT%d\tS%f\t", mDistance, mTime, mSpeed));

        }
    }


    private void notifyWearable() {
        String msg =
                Utils.formatDistance(mDistance) + '|' +
                Utils.formatSpeed(mSpeed);

        Log.d(APP_NAME, "Sent string to wearable: "+msg);
        // send message to wearable
        if(mNode != null){      // if wearable available
            Wearable.MessageApi.sendMessage(mApi,
                    mNode.getId(),
                    MSGAPI_MSG_KEY,
                    msg.getBytes()
            ); // discard MessageResult, don't need
        }
    }


    // PUBLIC INTERFACE
    /////////////////////////////////////////////////////////////////

    public void startLocationListener(){
        // request location updates from locManager
        // check permission and set listener
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            Log.d(APP_NAME, "No Fine Location Perms granted");
            return;
        }
        isListeningLocUpdates = true;
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_INTERVAL_MS, 1, this);
    }

    public void stopLocationListener(){
        // make sure we are not running
        if(!isRunning) {
            isListeningLocUpdates = false;
            mLocationManager.removeUpdates(this);
        }
    }

    public void startRun(){
        // make sure we registered for location changes
        if(!isListeningLocUpdates)
            startLocationListener();

        Calendar c = Calendar.getInstance();
        mStartDate = c.getTimeInMillis();
        mDistance = 0;
        mTime = 0;
        mSpeed = 0;
        mCalories = 0;

        // get next run's ID
        mRunNo = mDb.getNextRunNo();
        // create Run object
        mRun = new Run(mRunNo, mStartDate, mTime, mDistance, mCalories, 0);
        isRunning = true;

        // Set running flag in SharefPrefs
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(PREF_KEY_ISRUNNING, true);
        editor.commit();

        // Setup ongoing notification for CurrentActivity
        buildNotification();

        //Start wear update thread
        if(isRunning){
            mWearTimer = new TimerTask() {
                @Override
                public void run() {
                    notifyWearable();
                }
            };
            Timer timer = new Timer(true);
            timer.schedule(mWearTimer, 0, 1000);
        }
    }

    public void stopRun(){
//       create and add run to db ONLY after run is stopped
        if(mRun != null) {
            mRun.setStartDate(mStartDate);
            mRun.setTotalDistance(mDistance);
            mRun.setTotalCalories(mCalories);
            // get total from start date and current time
            Calendar c = Calendar.getInstance();
            mRun.setTotalTime(c.getTimeInMillis() - mStartDate);
            mRun.getAvgSpeed();
            mDb.insertRun(mRun);
        }
//       clear Run obj
        mRun = null;
        isRunning = false;

        // stop listening for updates
        stopLocationListener();

        // Save in prefs that no longer running
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(PREF_KEY_ISRUNNING, false);
        editor.commit();

        // remove ongoing notification
        if(mNotificationManager != null)
            mNotificationManager.cancel(NOTIF_ID);
        // stop wearable messages
        // construct thread with new runnable and start it
        new Thread(new Runnable() {
            @Override
            public void run() {
                Wearable.MessageApi.sendMessage(mApi,
                        mNode.getId(),
                        MSGAPI_MSG_STOP,
                        "stop_notification".getBytes()
                ).await();  // discard MessageResult, don't need
            }
        }).start();
        if(mWearTimer != null)
            mWearTimer.cancel();

    }

    public void pauseRun(){
        isRunning = false;
    }

    public void resumeRun(){
        isRunning = true;
    }

    public Run getRun(){
        return mRun;
    }

    public float getAccuracy(){
        return mAccuracy;
    }

    public String[] getStats(){
        return new String[]{
                Utils.formatDistance(mDistance),
                Utils.formatSpeed(mSpeed),
                Utils.formatCalories(mCalories)};
    }
}