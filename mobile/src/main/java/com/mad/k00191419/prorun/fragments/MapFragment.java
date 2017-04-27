package com.mad.k00191419.prorun.fragments;


import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mad.k00191419.prorun.R;
import com.mad.k00191419.prorun.activities.CurrentActivity;
import com.mad.k00191419.prorun.db.ProRunDB;
import com.mad.k00191419.prorun.db.Run;
import com.mad.k00191419.prorun.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {

    // Consts
    private static final int  MAP_PADDING = 3;

    // Fields
    MapView mMapView;
    GoogleMap mMap;
    private ArrayList<Location> mLocations;
    private Timer mTimer;
    private ProRunDB mDb;
    private Run mRun;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_map, container, false);

        // setup DB connection
        mDb = new ProRunDB(getActivity().getApplicationContext());
        // Get run from intent
        mRun = getActivity().getIntent().getParcelableExtra(CurrentActivity.INTENT_KEY_RUN);
        // get full run with locations
        mRun = mDb.getRun(mRun.getNo());

        mMapView = (MapView)rootView.findViewById(R.id.mapFragment);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        try{
            MapsInitializer.initialize(getActivity().getApplicationContext());
        }catch (Exception e){
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                // get locations, display and draw route
                LatLngBounds.Builder builder = new LatLngBounds.Builder();//get builder
                // no data to display - explain to user
                if(mRun == null || mRun.getLocations().size() < 2){
                    Toast.makeText(getContext(), getString(R.string.string_toast_nomapdata),
                            Toast.LENGTH_SHORT).show();
                }

                List<Location> locs = mRun.getLocations();
                if(locs.size() >= 2) {        // need at least 2 locs for bounds
                    builder.include(Utils.getLatLngFromLocation(locs.get(0)));  // first
                    builder.include(Utils.getLatLngFromLocation(
                            locs.get(locs.size()-1)));  //last
                    // build 'bounds' obj and move camera
                    LatLngBounds bounds = builder.build();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, MAP_PADDING));
                }
                displayRun();
            }

            private void displayRun() {

                if (mMap != null && mRun != null) {
                    mLocations = mRun.getLocations();
                    PolylineOptions polyline = new PolylineOptions();
                    if (mLocations.size() > 0) {
                        for (Location l : mLocations) {
                            LatLng point = new LatLng(
                                    l.getLatitude(), l.getLongitude());
                            polyline.add(point);
                        }
                    }
                    mMap.addPolyline(polyline);
                }
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}
