package com.druciak.escorerapp.model.entities;

public final class PlayerPunishment extends TeamPunishment {
    public static final String COACH_ID = "C";

    private int memberId;

    public PlayerPunishment(int cardId, MatchTeam team, TeamAdditionalMember member,
                            MatchTeam sndTeam, int set) {
        super(cardId, team, set, sndTeam);
        this.memberId = member.getMemberTypeId();
    }

    public PlayerPunishment(int cardId, MatchTeam team, MatchPlayer player,
                            MatchTeam sndTeam, int set) {
        super(cardId, team, set, sndTeam);
        this.memberId = player.getNumber();
        player.setCardId(cardId);
    }

    @Override
    public String toString() {
        return "PlayerPunishment{" +
                "memberId=" + memberId +
                ", teamPun=" + super.toString() +
                '}';
    }
}
