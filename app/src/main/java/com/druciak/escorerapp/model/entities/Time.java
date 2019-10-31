package com.druciak.escorerapp.model.entities;

public class Time extends Action {
    private int teamId;

    public Time(int teamId, int teamPoints, int sndTeamPoints) {
        this.teamId = teamId;
        setScore(teamPoints + " : " + sndTeamPoints);
    }

    @Override
    public Integer returnTeamIdIfIsPoint() {
        return null;
    }
}
