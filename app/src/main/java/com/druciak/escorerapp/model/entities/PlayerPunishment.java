package com.druciak.escorerapp.model.entities;

public final class PlayerPunishment extends TeamPunishment {
    public static final String COACH_ID = "C";

    private int memberId;

    public PlayerPunishment(int cardId, int teamId, int memberId) {
        super(cardId, teamId);
        this.memberId = memberId;
    }
}
