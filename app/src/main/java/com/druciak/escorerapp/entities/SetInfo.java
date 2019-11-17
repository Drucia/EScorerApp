package com.druciak.escorerapp.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SetInfo {
    @SerializedName("shiftsHome")
    @Expose
    private int shiftsA;
    @SerializedName("shiftsGuest")
    @Expose
    private int shiftsB;
    @SerializedName("timesHome")
    @Expose
    private int timesA;
    @SerializedName("timesGuest")
    @Expose
    private int timesB;
    @SerializedName("pointsHome")
    @Expose
    private int pointsA;
    @SerializedName("pointsGuest")
    @Expose
    private int pointsB;
    @SerializedName("set")
    @Expose
    private int set;
    @SerializedName("time")
    @Expose
    private int time;

    public SetInfo(int shiftsA, int shiftsB, int timesA, int timesB, int pointsA, int pointsB,
                   int set, int time) {
        this.shiftsA = shiftsA;
        this.shiftsB = shiftsB;
        this.timesA = timesA;
        this.timesB = timesB;
        this.pointsA = pointsA;
        this.pointsB = pointsB;
        this.set = set;
        this.time = time;
    }

    public int getShiftsA() {
        return shiftsA;
    }

    public int getShiftsB() {
        return shiftsB;
    }

    public int getTimesA() {
        return timesA;
    }

    public int getTimesB() {
        return timesB;
    }

    public int getPointsA() {
        return pointsA;
    }

    public int getPointsB() {
        return pointsB;
    }

    public int getSet() {
        return set;
    }

    public int getTime() {
        return time;
    }
}
