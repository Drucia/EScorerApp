package com.druciak.escorerapp.model.entities;

public class Undo extends Action {
    public Undo(int leftPoints, int rightPoints) {
        setScore(leftPoints, rightPoints);
    }

    @Override
    public Integer returnTeamIdIfIsPoint() {
        return null;
    }
}
