package com.druciak.escorerapp.model.entities;

public abstract class Action {
    private int teamMadeActionPoints;
    private int sndTeamPoints;

    public String getScore() {
        return teamMadeActionPoints + " : " + sndTeamPoints;
    }

    public void setScore(int madeActionPoints, int sndTeamPoints) {
        this.teamMadeActionPoints = madeActionPoints;
        this.sndTeamPoints = sndTeamPoints;
    }

    public abstract Integer returnTeamIdIfIsPoint();
}
