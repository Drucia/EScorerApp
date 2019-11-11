package com.druciak.escorerapp.entities;

import android.os.Parcel;

import java.util.Optional;

public class LineUp extends Action{
    private int enterNb;
    private int areaNb;

    public LineUp(MatchTeam team, MatchPlayer enter, int areaNb) {
        teamMadeActionId = team.getTeamId();
        this.enterNb = enter.getNumber();
        this.areaNb = areaNb;
        enter.setStatusId(MatchPlayer.STATUS_PLAYER_ON_COURT);
        team.setLineUp(areaNb, enter);
    }

    protected LineUp(Parcel in) {
        enterNb = in.readInt();
        areaNb = in.readInt();
        teamMadeActionId = in.readInt();
        teamMadeActionPoints = in.readInt();
        sndTeamPoints = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(enterNb);
        dest.writeInt(areaNb);
        dest.writeInt(teamMadeActionId);
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
        return teamMadeActionId;
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
                ", score=" + super.toString() +
                '}';
    }
}
