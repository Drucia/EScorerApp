package com.druciak.escorerapp.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Match implements Parcelable {
    private static int counter;

    private int id;
    private Team hostTeam;
    private Team guestTeam;
    private String name;
    private String refereeId;

    public Match(Team hostTeam, Team guestTeam, String refereeId) {
        counter++;
        this.id = counter;
        this.hostTeam = hostTeam;
        this.guestTeam = guestTeam;
        this.refereeId = refereeId;
        this.name = hostTeam.getShortName() + " vs " + guestTeam.getShortName();
    }

    public Match(int id, Team hostTeam, Team guestTeam, String refereeId) {
        counter++;
        this.id = counter;
        this.hostTeam = hostTeam;
        this.guestTeam = guestTeam;
        this.refereeId = refereeId;
        this.name = hostTeam.getShortName() + " vs " + guestTeam.getShortName();
    }

    protected Match(Parcel in) {
        id = in.readInt();
        hostTeam = in.readParcelable(Team.class.getClassLoader());
        guestTeam = in.readParcelable(Team.class.getClassLoader());
        name = in.readString();
        refereeId = in.readString();
    }

    public Match(Team hostTeam, Team guestTeam) {
        this.id = -1;
        this.hostTeam = hostTeam;
        this.guestTeam = guestTeam;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeParcelable(hostTeam, flags);
        dest.writeParcelable(guestTeam, flags);
        dest.writeString(name);
        dest.writeString(refereeId);
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
