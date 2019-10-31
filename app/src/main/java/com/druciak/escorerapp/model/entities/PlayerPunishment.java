package com.druciak.escorerapp.model.entities;

public final class PlayerPunishment extends TeamPunishment {
    public static final String COACH_ID = "C";

    private int memberId;

    public PlayerPunishment(int cardId, int teamId, TeamAdditionalMember member, int teamPoints,
                            int sndTeamPoints, int set) {
        super(cardId, teamId, set, teamPoints, sndTeamPoints);
        this.memberId = member.getMemberId();
    }

    public PlayerPunishment(int cardId, int teamId, MatchPlayer player, int teamPoints,
                            int sndTeamPoints, int set) {
        super(cardId, teamId, set, teamPoints, sndTeamPoints);
        this.memberId = player.getNumber();
        player.setCardId(cardId);
    }
}
