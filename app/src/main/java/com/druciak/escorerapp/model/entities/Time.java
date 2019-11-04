package com.druciak.escorerapp.model.entities;

import java.util.Optional;

public class Time extends Action {
    private int teamId;
    private int timeCount;

    public Time(MatchTeam team, int sndTeamPoints) {
        this.teamId = team.getTeamId();
        setScore(team.getPoints(), sndTeamPoints);
        team.addTime();
        timeCount = team.getTimesCounter();
    }

    public int getTeamId() {
        return teamId;
    }

    @Override
    public Optional<Integer> returnTeamIdIfIsPoint() {
        return Optional.empty();
    }
}
