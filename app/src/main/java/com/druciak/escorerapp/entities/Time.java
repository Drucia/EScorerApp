package com.druciak.escorerapp.entities;

import android.os.Parcel;

import java.util.Optional;

public class Time extends Action {
    private int teamId;
    private int timeCount;

    public Time(MatchTeam team, int sndTeamPoints) {
        this.teamId = team.getTeamId();
        this.teamMadeActionPoints = team.getPoints();
        this.sndTeamPoints = sndTeamPoints;
        team.addTime();
        timeCount = team.getTimesCounter();
    }

    protected Time(Parcel in) {
        teamId = in.readInt();
        timeCount = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(teamId);
        dest.writeInt(timeCount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Time> CREATOR = new Creator<Time>() {
        @Override
        public Time createFromParcel(Parcel in) {
            return new Time(in);
        }

        @Override
        public Time[] newArray(int size) {
            return new Time[size];
        }
    };

    public int getTeamId() {
    return teamId;
}

    @Override
    public Optional<Integer> returnTeamIdIfIsPoint() {
        return Optional.empty();
    }

    @Override
    public String toString() {
        return "Time{" +
                "teamId=" + teamId +
                ", timeCount=" + timeCount +
                ", score=" + super.toString() +
                '}';
    }
}
