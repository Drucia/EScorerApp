package com.druciak.escorerapp.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Team implements Parcelable {
    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("fullName")
    @Expose
    public String fullName;

    @SerializedName("shortName")
    @Expose
    public String shortName;

    public Team(int id, String fullName, String shortName) {
        this.id = id;
        this.fullName = fullName;
        this.shortName = shortName;
    }

    protected Team(Parcel in) {
        id = in.readInt();
        fullName = in.readString();
        shortName = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(fullName);
        parcel.writeString(shortName);
    }
}
