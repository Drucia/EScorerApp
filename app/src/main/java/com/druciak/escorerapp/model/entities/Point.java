package com.druciak.escorerapp.model.entities;

import static com.druciak.escorerapp.model.entities.MatchInfo.TEAM_A_ID;

public class Point extends Action {
    private int teamId;

    public Point(MatchTeam team, int pointsOtherTeam) {
        team.addPoint();
        this.teamId = team.getTeamId();
        setScore(teamId == TEAM_A_ID ? team.getPoints() + " : " + pointsOtherTeam :
                pointsOtherTeam + " : " + team.getPoints());
    }

    @Override
    public Integer returnTeamIdIfIsPoint() {
        return teamId;
    }
}
