package com.druciak.escorerapp.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Match implements Parcelable {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("hostTeam")
    @Expose
    private Team hostTeam;
    @SerializedName("guestTeam")
    @Expose
    private Team guestTeam;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("userId")
    @Expose
    private String refereeId;
    @SerializedName("date")
    @Expose
    private String date;

    public Match(Team hostTeam, Team guestTeam, String refereeId) {
        this.id = -1;
        this.hostTeam = hostTeam;
        this.guestTeam = guestTeam;
        this.refereeId = refereeId;
        this.name = hostTeam.getShortName() + " vs " + guestTeam.getShortName();
        this.date = getCurrDate();
    }

    public Match(int id, Team hostTeam, Team guestTeam, String refereeId) {
        this.id = id;
        this.hostTeam = hostTeam;
        this.guestTeam = guestTeam;
        this.refereeId = refereeId;
        this.name = hostTeam.getShortName() + " vs " + guestTeam.getShortName();
        this.date = getCurrDate();
    }

    private String getCurrDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    protected Match(Parcel in) {
        id = in.readInt();
        hostTeam = in.readParcelable(Team.class.getClassLoader());
        guestTeam = in.readParcelable(Team.class.getClassLoader());
        name = in.readString();
        refereeId = in.readString();
        date = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeParcelable(hostTeam, flags);
        dest.writeParcelable(guestTeam, flags);
        dest.writeString(name);
        dest.writeString(refereeId);
        dest.writeString(date);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Match> CREATOR = new Creator<Match>() {
        @Override
        public Match createFromParcel(Parcel in) {
            return new Match(in);
        }

        @Override
        public Match[] newArray(int size) {
            return new Match[size];
        }
    };

    public void setHostTeam(Team hostTeam) {
        this.hostTeam = hostTeam;
    }

    public void setGuestTeam(Team guestTeam) {
        this.guestTeam = guestTeam;
    }

    public String getName() {
        return hostTeam.getFullName() + " vs " + guestTeam.getFullName();
    }

    public Team getHostTeam() {
        return hostTeam;
    }

    public Team getGuestTeam() {
        return guestTeam;
    }

    public String getRefereeId() {
        return refereeId;
    }

    public int getId() {
        return id;
    }
}
