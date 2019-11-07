package com.druciak.escorerapp.model.entities;

import java.util.Optional;

import static com.druciak.escorerapp.model.entities.MatchInfo.RED_CARD_ID;
import static com.druciak.escorerapp.model.entities.MatchInfo.TEAM_A_ID;
import static com.druciak.escorerapp.model.entities.MatchInfo.TEAM_B_ID;

public class TeamPunishment extends Action {
    private int cardId;
    private int teamId;
    private int set;

    public TeamPunishment(int cardId, MatchTeam team, int set, MatchTeam sndTeam){
        this.cardId = cardId;
        this.teamId = team.getTeamId();
        this.set = set;
        setScore(team.getPoints(), sndTeam.getPoints());
        team.setCardId(cardId);
        if (cardId == RED_CARD_ID)
            sndTeam.addPoint();
    }

    @Override
    public Optional<Integer> returnTeamIdIfIsPoint() {
        if (cardId == RED_CARD_ID)
            return Optional.of(teamId == TEAM_A_ID ? TEAM_B_ID : TEAM_A_ID);
        else
            return Optional.empty();
    }

    @Override
    public String toString() {
        return "TeamPunishment{" +
                "cardId=" + cardId +
                ", teamId=" + teamId +
                ", set=" + set +
                ", score=" + super.toString() +
                '}';
    }
}
