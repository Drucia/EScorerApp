package com.druciak.escorerapp.model.entities;

import java.util.Optional;

public abstract class Action {
    private int teamMadeActionPoints = 0;
    private int sndTeamPoints = 0;

    public void setScore(int madeActionPoints, int sndTeamPoints) {
        this.teamMadeActionPoints = madeActionPoints;
        this.sndTeamPoints = sndTeamPoints;
    }

    public abstract Optional<Integer> returnTeamIdIfIsPoint();

    @Override
    public String toString() { return teamMadeActionPoints + " : " + sndTeamPoints; }
}
