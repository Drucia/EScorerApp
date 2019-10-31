package com.druciak.escorerapp.model.entities;

public abstract class Action {
    public String score;

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public abstract Integer returnTeamIdIfIsPoint();
}
