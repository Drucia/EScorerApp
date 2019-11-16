package com.druciak.escorerapp.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Pair<Team, Player> {
    @SerializedName("team")
    @Expose
    private Team team;

    @SerializedName("players")
    @Expose
    private List<Player> players;

    public Pair() {
    }

    public Pair(Team team, List<Player> players) {
        this.team = team;
        this.players = players;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
