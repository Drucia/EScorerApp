package com.druciak.escorerapp.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Team implements Parcelable {
    @SerializedName("id")
    @Expose
    protected int id;

    @SerializedName("fullName")
    @Expose
    protected String fullName;

    @SerializedName("shortName")
    @Expose
    protected String shortName;

    public Team(int id, String shortName, String fullName) {
        this.id = id;
        this.fullName = fullName;
        this.shortName = shortName;
    }

    public Team(int id) {
        this.id = id;
    }

    Team(Team team){
        this.id = team.getId();
        this.shortName = team.getShortName();
        this.fullName = team.getFullName();
    }

    protected Team(Parcel in) {
        id = in.readInt();
        fullName = in.readString();
        shortName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(fullName);
        dest.writeString(shortName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Team> CREATOR = new Creator<Team>() {
        @Override
        public Team createFromParcel(Parcel in) {
            return new Team(in);
        }

        @Override
        public Team[] newArray(int size) {
            return new Team[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
