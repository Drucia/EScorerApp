package com.druciak.escorerapp.model.entities;

import java.util.Optional;

public class Undo extends Action {

    public Undo(Action action) {

    }

    @Override
    public Optional<Integer> returnTeamIdIfIsPoint() {
        return Optional.empty();
    }
}
