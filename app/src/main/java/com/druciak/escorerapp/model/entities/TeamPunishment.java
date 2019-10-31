package com.druciak.escorerapp.model.entities;

import static com.druciak.escorerapp.model.entities.MatchInfo.RED_CARD_ID;
import static com.druciak.escorerapp.model.entities.MatchInfo.TEAM_A_ID;
import static com.druciak.escorerapp.model.entities.MatchInfo.TEAM_B_ID;

public class TeamPunishment extends Action {
    private int cardId;
    private int teamId;
    private int set;

    public TeamPunishment(int cardId, int teamId, int set, int teamPoints, int sndTeamPoints){
        this.cardId = cardId;
        this.teamId = teamId;
        this.set = set;
        setScore(teamPoints + " : " + sndTeamPoints);
    }

    @Override
    public Integer returnTeamIdIfIsPoint() {
        if (cardId == RED_CARD_ID)
            return teamId == TEAM_A_ID ? TEAM_B_ID : TEAM_A_ID;
        else
            return null;
    }
}
