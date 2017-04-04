package com.mad.k00191419.prorun.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mad.k00191419.prorun.R;
import com.mad.k00191419.prorun.services.ProRunService;
import com.mad.k00191419.prorun.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MapActivity extends FragmentActivity
        implements ServiceConnection, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    // Constants
    private static final String APP_NAME = "__PRO_RUN";
    public static final String  KEY_LOCATIONS = "com.mad.k00191419.prorun.KEY_LOCATIONS";
    private static final long INTERVAL_REFRESH = 1000;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private static final int MAP_PADDING = 3;

    // Fields
    private ProRunService       mService;
    private boolean             isServiceConnected = false;
    private GoogleMap           map;
    private ArrayList<Location> mLocations;
    private GoogleApiClient     mGApiClient;
    private Timer               mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        connectToService();
        setupGAPIClient();
    }

    @Override
    protected void onDestroy() {
        disconnectFromService();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // if GoogleMap object is not already available, get it
        if (map == null) {
            FragmentManager manager = getSupportFragmentManager();
            SupportMapFragment fragment =
                    (SupportMapFragment) manager.findFragmentById(R.id.mapFragment);
            fragment.getMapAsync(this);
        }

        // if GoogleMap object is available, configure it
        if (map != null) {
            map.getUiSettings().setZoomControlsEnabled(true);
        }
        // connect to google api client
        mGApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGApiClient.disconnect();
        super.onStop();
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



    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        // when map is ready we want to zoom-in to accomodate entire run
        LatLngBounds.Builder builder = new LatLngBounds.Builder();  // get builder
        List<Location> locs = mService.getRun().getLocations();     // get locations from run in service
        if(locs.size() != 0) {
            builder.include(Utils.getLatLngFromLocation(locs.get(0)));  // add LatLong builded from Location's
            builder.include(Utils.getLatLngFromLocation(locs.get(locs.size() - 1)));
            // build 'bounds' object and move maps camera
            LatLngBounds bounds = builder.build();
            map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, MAP_PADDING));
            displayRun();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayRun();
        setMapRefresh();
    }

    private void setMapRefresh(){
        mTimer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                MapActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        displayRun();
                    }
                });
            }
        };
        mTimer.schedule(task, INTERVAL_REFRESH, INTERVAL_REFRESH);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // if Google Play services can resolve the error, display activity
        if (connectionResult.hasResolution()) {
            try {
                // start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
            }
            catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        }
        else {
            new AlertDialog.Builder(this)
                    .setMessage("Connection failed. Error code: "
                            + connectionResult.getErrorCode())
                    .show();
        }
    }

    // helpers
    private void setupGAPIClient() {
        mGApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
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

    private void displayRun(){
        if (map != null && isServiceConnected) {
            mLocations = mService.getRun().getLocations();
            PolylineOptions polyline = new PolylineOptions();
            if (mLocations.size() > 0) {
                for (Location l : mLocations) {
                    LatLng point = new LatLng(
                            l.getLatitude(), l.getLongitude());
                    polyline.add(point);
                }
            }
            map.addPolyline(polyline);
        }
    }
}
