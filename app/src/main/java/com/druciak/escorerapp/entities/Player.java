package com.druciak.escorerapp.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Player implements Parcelable {
    @SerializedName("id")
    @Expose
    protected int id;

    @SerializedName("name")
    @Expose
    protected String name;

    @SerializedName("surname")
    @Expose
    protected String surname;

    @SerializedName("sex")
    @Expose
    protected char sex;

    @SerializedName("team")
    @Expose
    protected Team team;

    protected boolean isLibero;
    protected boolean isCaptain;
    protected int number = 0;

    public Player() {
    }

    public Player(int id, String name, String surname, char sex, Team team) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.sex = sex;
        this.team = team;
    }

    public Player(String name, String surname, Team team, boolean isLibero, boolean isCaptain, int number) {
        this.name = name;
        this.surname = surname;
        this.team = team;
        this.isLibero = isLibero;
        this.isCaptain = isCaptain;
        this.number = number;
        this.id = -1;
    }

    public Player(Player player) {
        id = player.getId();
        name = player.getName();
        surname = player.getSurname();
        sex = player.getSex();
        team = player.getTeam();
        isLibero = player.isLibero();
        isCaptain = player.isCaptain();
        number = player.getNumber();
    }

    protected Player(Parcel in) {
        id = in.readInt();
        name = in.readString();
        surname = in.readString();
        sex = (char) in.readInt();
        team = in.readParcelable(Team.class.getClassLoader());
        isLibero = in.readByte() != 0;
        isCaptain = in.readByte() != 0;
        number = in.readInt();
    }

    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team teamId) {
        this.team = team;
    }

    public boolean isLibero() {
        return isLibero;
    }

    public void setLibero(boolean libero) {
        isLibero = libero;
    }

    public boolean isCaptain() {
        return isCaptain;
    }

    public void setCaptain(boolean captain) {
        isCaptain = captain;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return surname + " " + name + "       " + number;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(surname);
        parcel.writeInt((int) sex);
        parcel.writeParcelable(team, i);
        parcel.writeByte((byte) (isLibero ? 1 : 0));
        parcel.writeByte((byte) (isCaptain ? 1 : 0));
        parcel.writeInt(number);
    }
}
