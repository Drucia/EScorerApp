package com.druciak.escorerapp.model.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Player {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("surname")
    @Expose
    private String surname;

    @SerializedName("sex")
    @Expose
    private char sex;

    @SerializedName("team")
    @Expose
    private Team team;

    private boolean isLibero;
    private boolean isCaptain;
    private int number = 0;

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
}
