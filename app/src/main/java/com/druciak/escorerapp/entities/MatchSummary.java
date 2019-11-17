package com.druciak.escorerapp.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class MatchSummary {
    @SerializedName("id")
    @Expose
    protected int summaryId;

    @SerializedName("date")
    @Expose
    protected String date;

    @SerializedName("match")
    @Expose
    protected Match match;

    @SerializedName("winner")
    @Expose
    protected Team winner;

    @SerializedName("hostSets")
    @Expose
    protected int hostSets;

    @SerializedName("guestSets")
    @Expose
    protected int guestSets;

    @SerializedName("sets")
    @Expose
    protected Map<Integer, SetInfo> sets;

    public MatchSummary() {
    }

    public MatchSummary(String date, Match match, Team winner, int hostSets, int guestSets, Map<Integer, SetInfo> sets) {
        this.date = date;
        this.match = match;
        this.winner = winner;
        this.hostSets = hostSets;
        this.guestSets = guestSets;
        this.sets = sets;
    }

    public int getSummaryId() {
        return summaryId;
    }

    public void setSummaryId(int summaryId) {
        this.summaryId = summaryId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public Team getWinner() {
        return winner;
    }

    public void setWinner(Team winner) {
        this.winner = winner;
    }

    public int getHostSets() {
        return hostSets;
    }

    public void setHostSets(int hostSets) {
        this.hostSets = hostSets;
    }

    public int getGuestSets() {
        return guestSets;
    }

    public void setGuestSets(int guestSets) {
        this.guestSets = guestSets;
    }

    public Map<Integer, SetInfo> getSets() {
        return sets;
    }

    public void setSets(Map<Integer, SetInfo> sets) {
        this.sets = sets;
    }
}
