package com.druciak.escorerapp.model.entities;

import java.util.Optional;

public class Undo extends Action {
    public Undo(int leftPoints, int rightPoints) {
        setScore(leftPoints, rightPoints);
    }

    @Override
    public Optional<Integer> returnTeamIdIfIsPoint() {
        return null;
    }
}
