package com.druciak.escorerapp.model.entities;

public class TeamPunishment extends Action {
    private int cardId;
    private int teamId;

    public TeamPunishment(int cardId, int teamId) {
        this.cardId = cardId;
        this.teamId = teamId;
    }
}
