package com.druciak.escorerapp.model.entities;

import java.util.Optional;

public class Point extends Action {
    private int teamId;

    public Point(MatchTeam team, int pointsOtherTeam) {
        team.addPoint();
        this.teamId = team.getTeamId();
        setScore(team.getPoints(), pointsOtherTeam);
    }

    @Override
    public Optional<Integer> returnTeamIdIfIsPoint() {
        return Optional.of(teamId);
    }
}
