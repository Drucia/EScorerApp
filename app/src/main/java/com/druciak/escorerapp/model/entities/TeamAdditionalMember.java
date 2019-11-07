package com.druciak.escorerapp.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class TeamAdditionalMember implements Parcelable {
    public static final int COACH_MEMBER_ID = 1;
    public static final int MEDICINE_MEMBER_ID = 2;
    public static final int MASSEUR_MEMBER_ID = 3;

    private String name;
    private int teamId;
    private int memberTypeId;

    public TeamAdditionalMember(String name, int teamId, int memberId) {
        this.name = name;
        this.teamId = teamId;
        this.memberTypeId = memberId;
    }

    protected TeamAdditionalMember(Parcel in) {
        name = in.readString();
        teamId = in.readInt();
        memberTypeId = in.readInt();
    }

    public static final Creator<TeamAdditionalMember> CREATOR = new Creator<TeamAdditionalMember>() {
        @Override
        public TeamAdditionalMember createFromParcel(Parcel in) {
            return new TeamAdditionalMember(in);
        }

        @Override
        public TeamAdditionalMember[] newArray(int size) {
            return new TeamAdditionalMember[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(teamId);
        parcel.writeInt(memberTypeId);
    }

    public String getName() {
        return name;
    }

    public int getTeamId() {
        return teamId;
    }

    public int getMemberTypeId() {
        return memberTypeId;
    }
}
