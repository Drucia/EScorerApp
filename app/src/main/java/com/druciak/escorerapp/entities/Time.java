package com.druciak.escorerapp.entities;

import android.os.Parcel;

import java.util.Optional;

public class Time extends Action {
    private int timeCount;

    public Time(MatchTeam team, int sndTeamPoints) {
        this.teamMadeActionId = team.getTeamId();
        this.teamMadeActionPoints = team.getPoints();
        this.sndTeamPoints = sndTeamPoints;
        team.addTime();
        timeCount = team.getTimesCounter();
    }

    protected Time(Parcel in) {
        teamMadeActionId = in.readInt();
        teamMadeActionPoints = in.readInt();
        sndTeamPoints = in.readInt();

        timeCount = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(teamMadeActionId);
        dest.writeInt(teamMadeActionPoints);
        dest.writeInt(sndTeamPoints);

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

    @Override
    public Optional<Integer> returnTeamIdIfIsPoint() {
        return Optional.empty();
    }

    @Override
    public String toString() {
        return "Time{" +
                ", timeCount=" + timeCount +
                ", score=" + super.toString() +
                '}';
    }
}
