package com.mad.k00191419.prorun.db;


import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Locale;

public class Run implements Parcelable {

    private long    mNo;
    private long    mStartDate;
    private long    mTotalTime;
    private float   mTotalDistance;
    private long    mTotalCalories;
    private double  mAvgSpeed;

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
        // sanity check
        if(mLocations == null || mLocations.size() == 0)
            return 0;

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

    // Parcelable interface so we can pass Run obj with intents.
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mNo);
        dest.writeLong(mStartDate);
        dest.writeLong(mTotalTime);
        dest.writeFloat(mTotalDistance);
        dest.writeDouble(mAvgSpeed);
    }

    public static final Parcelable.Creator<Run> CREATOR = new Parcelable.Creator<Run>(){
        @Override
        public Run createFromParcel(Parcel in){
            return new Run(in);
        }

        @Override
        public Run[] newArray(int size) {
            return new Run[size];
        }
    };

    private Run(Parcel in){
        mNo = in.readLong();
        mStartDate = in.readLong();
        mTotalTime = in.readLong();
        mTotalDistance = in.readFloat();
        mAvgSpeed = in.readDouble();
    }

    public float getHighestSpeed() {
        if (mLocations == null) return 0;
        else
        {
            float maxSpeed = 0;
            for(Location l : mLocations){
                if(l.getSpeed() > maxSpeed) maxSpeed = l.getSpeed();
            }
            return maxSpeed;
        }
    }
}
