package com.druciak.escorerapp.entities;

import android.os.Parcel;

import java.util.Optional;

import static com.druciak.escorerapp.entities.MatchPlayer.NO_SHIFT;
import static com.druciak.escorerapp.entities.MatchPlayer.STATUS_PLAYER_NOT_TO_SHIFT;
import static com.druciak.escorerapp.entities.MatchPlayer.STATUS_PLAYER_SHIFTED;

public class Shift extends Action{
    private int outPlayerNb;
    private int enterPlayerNb;
    private int teamId;

    public Shift(MatchPlayer out, MatchPlayer enter, MatchTeam team, int sndTeamPoints) {
        enterPlayerNb = enter.getNumber();
        outPlayerNb = out.getNumber();
        this.teamId = team.getTeamId();
        teamMadeActionPoints = team.getPoints();
        this.sndTeamPoints = sndTeamPoints;
        if (out.getShiftNumber() != NO_SHIFT) {
            out.setStatusId(STATUS_PLAYER_NOT_TO_SHIFT);
            enter.setStatusId(STATUS_PLAYER_NOT_TO_SHIFT);
        } else {
            out.setStatusId(STATUS_PLAYER_SHIFTED);
            enter.setStatusId(STATUS_PLAYER_SHIFTED);
        }
        out.setShiftNumber(enterPlayerNb);
        out.setOnCourt(false);
        enter.setShiftNumber(outPlayerNb);
        out.setOnCourt(true);
        team.addShift();
    }

    protected Shift(Parcel in) {
        super.readFromParcel(in);
        outPlayerNb = in.readInt();
        enterPlayerNb = in.readInt();
        teamId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeInt(SHIFT_ID); // to know which create use
        super.writeToParcel(dest, flags);
        dest.writeInt(outPlayerNb);
        dest.writeInt(enterPlayerNb);
        dest.writeInt(teamId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Shift> CREATOR = new Creator<Shift>() {
        @Override
        public Shift createFromParcel(Parcel in) {
            return new Shift(in);
        }

        @Override
        public Shift[] newArray(int size) {
            return new Shift[size];
        }
    };

        @Override
    public Optional<Integer> returnTeamIdIfIsPoint() {
        return Optional.empty();
    }

    public int getTeamId() {
        return teamId;
    }

    public int getOutPlayerNb() {
        return outPlayerNb;
    }

    public int getEnterPlayerNb() {
        return enterPlayerNb;
    }

    @Override
    public String toString() {
        return "Shift{" +
                "outPlayerNb=" + outPlayerNb +
                ", enterPlayerNb=" + enterPlayerNb +
                ", teamId=" + teamId +
                ", score=" + super.toString() +
                '}';
    }
}
