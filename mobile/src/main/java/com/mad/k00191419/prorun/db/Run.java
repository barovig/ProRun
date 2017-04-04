package com.mad.k00191419.prorun.db;


import android.location.Location;

import java.util.ArrayList;
import java.util.Locale;

public class Run {

    private long mNo;
    private long mStartDate;
    private long mTotalTime;
    private float mTotalDistance;
    private long mTotalCalories;
    private double mAvgSpeed;
    private Location mLastLocation;

    @Override
    public String toString() {
        return String.format("No: %d\nStartDate: %d\nTotalTime: %d\nTotalDistance: %f\nTotalCalories: %d\n, AvgSpeed: %f\n",
                mNo, mStartDate, mTotalTime, mTotalDistance, mTotalCalories, mAvgSpeed);
    }

    private ArrayList<Location> mLocations;

    public Run(long mNo, long mStartDate, long mTotalTime, float mTotalDistance, long mTotalCalories, double mAvgSpeed) {
        this.mNo = mNo;
        this.mStartDate = mStartDate;
        this.mTotalTime = mTotalTime;
        this.mTotalDistance = mTotalDistance;
        this.mTotalCalories = mTotalCalories;
        this.mAvgSpeed = mAvgSpeed;
        mLocations = new ArrayList<>();
    }

    public long getNo() {
        return mNo;
    }

    public void setNo(long mNo) {
        this.mNo = mNo;
    }

    public long getStartDate() {
        return mStartDate;
    }

    public void setStartDate(long mStartDate) {
        this.mStartDate = mStartDate;
    }

    public long getTotalTime() {
        return mTotalTime;
    }

    public void setTotalTime(long mTotalTime) {
        this.mTotalTime = mTotalTime;
    }

    public float getTotalDistance() {
        return mTotalDistance;
    }

    public void setTotalDistance(float mTotalDistance) {
        this.mTotalDistance = mTotalDistance;
    }

    public long getTotalCalories() {
        return mTotalCalories;
    }

    public void setTotalCalories(long mTotalCalories) {
        this.mTotalCalories = mTotalCalories;
    }

    public double getAvgSpeed() {

        double avgSpeed = 0;
        for(Location l : mLocations){
            avgSpeed += l.getSpeed();
        }
        return avgSpeed / mLocations.size();
    }

    public void setAvgSpeed(double mAvgSpeed) {
        this.mAvgSpeed = mAvgSpeed;
    }

    public ArrayList<Location> getLocations() {
        return mLocations;
    }

    public void addLocation(Location location){
        mLocations.add(location);
    }








}
