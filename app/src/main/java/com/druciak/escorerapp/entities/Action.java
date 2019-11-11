
package com.druciak.escorerapp.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class Action implements Parcelable {
    public static final Map<Integer, Creator<? extends Action>> categoryMap = new HashMap<>();

    public static final int LINE_UP_ID = 0;
    public static final int POINT_ID = 1;
    public static final int SHIFT_ID = 2;
    public static final int PLAYER_PUNISHMENT_ID = 3;
    public static final int TEAM_PUNISHMENT_ID = 4;
    public static final int TIME_ID = 5;

    int teamMadeActionId;
    int teamMadeActionPoints;
    int sndTeamPoints;

    static {
        categoryMap.put(LINE_UP_ID, LineUp.CREATOR);
        categoryMap.put(POINT_ID, Point.CREATOR);
        categoryMap.put(SHIFT_ID, Shift.CREATOR);
        categoryMap.put(TEAM_PUNISHMENT_ID, TeamPunishment.CREATOR);
        categoryMap.put(TIME_ID, Time.CREATOR);
    }

    public void readFromParcel(Parcel in){
        teamMadeActionId = in.readInt();
        teamMadeActionPoints = in.readInt();
        sndTeamPoints = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(teamMadeActionId);
        dest.writeInt(teamMadeActionPoints);
        dest.writeInt(sndTeamPoints);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public abstract Optional<Integer> returnTeamIdIfIsPoint();

    public int getTeamMadeActionId() {
        return teamMadeActionId;
    }

    public int getTeamMadeActionPoints() {
        return teamMadeActionPoints;
    }

    public int getSndTeamPoints() {
        return sndTeamPoints;
    }

    @Override
    public String toString() {
        return "Action{" +
                "teamMadeActionId=" + teamMadeActionId +
                ", score=" + teamMadeActionPoints +
                " : " + sndTeamPoints +
                '}';
    }
}
