package com.druciak.escorerapp.model.entities;

import java.util.Optional;

public class Point extends Action {
    private int teamId;
    private int points;

    public Point(MatchTeam team, int pointsOtherTeam) {
        team.addPoint();
        this.points = team.getPoints();
        this.teamId = team.getTeamId();
        setScore(team.getPoints(), pointsOtherTeam);
    }

    @Override
    public Optional<Integer> returnTeamIdIfIsPoint() {
        return Optional.of(teamId);
    }

    @Override
    public String toString() {
        return "Point{" +
                "teamId=" + teamId +
                ", points=" + points +
                ", score=" + super.toString() +
                '}';
    }
}
