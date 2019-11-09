package com.druciak.escorerapp.entities;

public class SetInfo {
    private int shiftsA;
    private int shiftsB;
    private int timesA;
    private int timesB;
    private int pointsA;
    private int pointsB;
    private int set;
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
