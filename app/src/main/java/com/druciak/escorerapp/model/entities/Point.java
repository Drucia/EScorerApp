package com.druciak.escorerapp.model.entities;

public class Point extends Action {
    private int teamId;

    public Point(MatchTeam team, int pointsOtherTeam) {
        team.addPoint();
        this.teamId = team.getTeamId();
        setScore(team.getPoints() + " : " + pointsOtherTeam);
    }

    @Override
    public Integer returnTeamIdIfIsPoint() {
        return teamId;
    }
}
