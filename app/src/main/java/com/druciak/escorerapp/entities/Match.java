package com.druciak.escorerapp.entities;

import java.io.Serializable;

public class Match implements Serializable {
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
}
