package com.druciak.escorerapp.entities;

import android.os.Parcel;

import java.util.Optional;

public class LineUp extends Action{
    private int enterNb;
    private int areaNb;
    private int teamId;

    public LineUp(MatchTeam team, MatchPlayer enter, int areaNb) {
        this.teamId = team.getTeamId();
        this.enterNb = enter.getNumber();
        this.areaNb = areaNb;
        enter.setStatusId(MatchPlayer.STATUS_PLAYER_ON_COURT);
        team.setLineUp(areaNb, enter);
    }

    protected LineUp(Parcel in) {
        enterNb = in.readInt();
        areaNb = in.readInt();
        teamId = in.readInt();
        teamMadeActionPoints = in.readInt();
        sndTeamPoints = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(enterNb);
        dest.writeInt(areaNb);
        dest.writeInt(teamId);
        dest.writeInt(teamMadeActionPoints);
        dest.writeInt(sndTeamPoints);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LineUp> CREATOR = new Creator<LineUp>() {
        @Override
        public LineUp createFromParcel(Parcel in) {
            return new LineUp(in);
        }

        @Override
        public LineUp[] newArray(int size) {
            return new LineUp[size];
        }
    };

    @Override
    public Optional<Integer> returnTeamIdIfIsPoint() {
        return Optional.empty();
    }

    public int getTeamId() {
        return teamId;
    }

    public int getEnterNb() {
        return enterNb;
    }

    public int getAreaNb() {
        return areaNb;
    }

    @Override
    public String toString() {
        return "LineUp{" +
                "enterNb=" + enterNb +
                ", areaNb=" + areaNb +
                ", teamId=" + teamId +
                ", score=" + super.toString() +
                '}';
    }
}
