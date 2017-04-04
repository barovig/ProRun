package com.mad.k00191419.prorun.services;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import com.mad.k00191419.prorun.db.ProRunDB;
import com.mad.k00191419.prorun.db.Run;
import com.mad.k00191419.prorun.utils.Utils;
//import com.android.server.*

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class ProRunService extends Service implements LocationListener {

    // Consts
    private final String    APP_NAME = "__PRO_RUN";
    private final long      MIN_TIME_INTERVAL_MS = 0;

    // Service fields
    private LocationManager mLocationManager;
    private long            mRunNo;
    private final IBinder   mBinder = new LocalBinder();
    private ProRunDB        mDb;
    private Run             mRun;
    private boolean         isRunning = false;
    private float           mDistance;
    private long            mTime;
    private float           mCalories;
    private float           mSpeed;
    private long            mStartDate;
    private Location        mLastLocation;
    private float           mAccuracy;

    // inner Binder subclass
    public class LocalBinder extends Binder {
        public ProRunService getService() {
            return ProRunService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // get db
        mDb = new ProRunDB(getApplicationContext());
        // setup LocationManager
        mLocationManager = (LocationManager) this.getSystemService(getApplicationContext().LOCATION_SERVICE);
        setupLocationListener();
    }

    // Overrides
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        Log.d(APP_NAME, "Binding ProRunService...");
        return mBinder;
    }

    @Override
    public void onLocationChanged(Location location) {
        if(isRunning) {
            long locTime = location.getTime();
            Log.d(APP_NAME, String.format("S:%d\tL:%d\n", mStartDate, locTime));
            if(locTime > mStartDate) {    // skip locations before startDate
                // update stats
                long timeInterval = (locTime - mStartDate);
                float distInterval =
                        (mLastLocation == null) ? 0 : mLastLocation.distanceTo(location);
                mTime += timeInterval;
                mDistance += distInterval;
                mSpeed = location.getSpeed();
                mRun.addLocation(location);
                mLastLocation = location;
                // calculate calories spend:
                // http://fitness.stackexchange.com/questions/15608/energy-expenditure-calories-burned-equation-for-running
                if(mSpeed > 1.0)        // don't count low speed movement contribution
                    mCalories += 0.5*90*(0.2 * mSpeed*60 + 3.5);
                Log.d(APP_NAME, String.format("D%f\tT%d\tS%f\t", mDistance, mTime, mSpeed));
            }
        }
        // get location accuracy
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

    public void startRun(){
        mStartDate = System.currentTimeMillis();
        // get next run's ID
        mRunNo = mDb.getNextRunNo();
        // create Run object
        mRun = new Run(mRunNo,  mStartDate, 0, 0, 0, 0);
        isRunning = true;
        // set fields
        mDistance = 0;
        mTime = 0;
        mSpeed = 0;
        mCalories = 0;

    }

    public void pauseRun(){
        isRunning = false;
    }

    public void resumeRun(){
        setupLocationListener();
    }

    private void setupLocationListener() {
        // check permission and set listener
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            /// HANDLE PERMISSION DENIAL
            return;
        }

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_INTERVAL_MS, 1, this);

    }

    public void stopRun(){
        // create and add run ONLY after run is stopped
        //mDb.insertRun(mRun);
        // clear Run obj
        mRun = null;
        isRunning = false;
        mLocationManager.removeUpdates(this);
    }

    //TODO: refactor - move calls to Utils to activity
    public String[] getStats(){
        return new String[]{
                Utils.formatDistance(mDistance),
                Utils.formatInterval(mTime),
                Utils.formatSpeed(mSpeed),
                Utils.formatCalories(mCalories)};
    }

    public float getAccuracy(){
        return mAccuracy;
    }

    public Run getRun(){
        return mRun;
    }

}
