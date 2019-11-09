package com.druciak.escorerapp.entities;

import android.os.Parcel;

import java.util.Optional;

public class Point extends Action {
    private int teamId;
    private int points;

    public Point(MatchTeam team, int pointsOtherTeam) {
        team.addPoint();
        this.points = team.getPoints();
        this.teamId = team.getTeamId();
        teamMadeActionPoints = team.getPoints();
        sndTeamPoints = pointsOtherTeam;
    }

        protected Point(Parcel in) {
            teamId = in.readInt();
            points = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(teamId);
            dest.writeInt(points);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Point> CREATOR = new Creator<Point>() {
            @Override
            public Point createFromParcel(Parcel in) {
                return new Point(in);
            }

            @Override
            public Point[] newArray(int size) {
                return new Point[size];
            }
        };

        @Override
    public Optional<Integer> returnTeamIdIfIsPoint() {
        return Optional.of(teamId);
    }

    @Override
    public String toString() {
        return "Point{" +
                "teamId=" + teamId +
                ", points=" + points +
                ", score=" + super.toString() +
                '}';
    }
}
