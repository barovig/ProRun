package com.mad.k00191419.prorun.activities;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mad.k00191419.prorun.R;
import com.mad.k00191419.prorun.services.ProRunService;

import java.util.ArrayList;

public class MapActivity extends FragmentActivity
        implements ServiceConnection, OnMapReadyCallback {

    // Constants
    private static final String APP_NAME = "__PRO_RUN";
    public static final String KEY_LOCATIONS = "com.mad.k00191419.prorun.KEY_LOCATIONS";

    // Fields
    private ProRunService   mService;
    private boolean         mServiceConnected;
    private GoogleMap       map;
    private ArrayList<Location> mLocations;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // if GoogleMap object is not already available, get it
        if (map == null) {
            FragmentManager manager = getSupportFragmentManager();
            SupportMapFragment fragment =
                    (SupportMapFragment) manager.findFragmentById(R.id.mapRouteTaken);
            fragment.getMapAsync(this);
        }

        // if GoogleMap object is available, configure it
        if (map != null) {
            map.getUiSettings().setZoomControlsEnabled(true);
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

    private void displayRun(){
        if (map != null) {
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        displayRun();
    }
}
