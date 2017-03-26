package com.mad.k00191419.prorun.db;


public class Goal {

    public enum Type{
        DAILY, WEEKLY, MONTHLY
    }

    private Type mType;
    private int mId;
    private long  mDate;
    private double mTargetDistance;
    private double mCurrentDistance;
    private long mTargetCalories;
    private long mCurrentCalories;

    public Goal(Type mType, int mId, long mDate, double mTargetDistance, double mCurrentDistance, long mTargetCalories, long mCurrentCalories) {
        this.mType = mType;
        this.mId = mId;
        this.mDate = mDate;
        this.mTargetDistance = mTargetDistance;
        this.mCurrentDistance = mCurrentDistance;
        this.mTargetCalories = mTargetCalories;
        this.mCurrentCalories = mCurrentCalories;
    }

    public Type getType() {
        return mType;
    }

    public void setType(Type mType) {
        this.mType = mType;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public long getDate() {
        return mDate;
    }

    public void setDate(long mDate) {
        this.mDate = mDate;
    }

    public double getTargetDistance() {
        return mTargetDistance;
    }

    public void setTargetDistance(double mTargetDistance) {
        this.mTargetDistance = mTargetDistance;
    }

    public double getCurrentDistance() {
        return mCurrentDistance;
    }

    public void setCurrentDistance(double mCurrentDistance) {
        this.mCurrentDistance = mCurrentDistance;
    }

    public long getTargetCalories() {
        return mTargetCalories;
    }

    public void setTargetCalories(long mTargetCalories) {
        this.mTargetCalories = mTargetCalories;
    }

    public long getCurrentCalories() {
        return mCurrentCalories;
    }

    public void setCurrentCalories(long mCurrentCalories) {
        this.mCurrentCalories = mCurrentCalories;
    }
}
