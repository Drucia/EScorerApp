package com.druciak.escorerapp.model.entities;

import java.util.Optional;

public class LineUp extends Action {
    private int enterNb;
    private int areaNb;
    private int teamId;

    public LineUp(MatchTeam team, MatchPlayer enter, int areaNb) {
        this.teamId = team.getTeamId();
        this.enterNb = enter.getNumber();
        this.areaNb = areaNb;
        enter.setStatusId(MatchPlayer.STATUS_PLAYER_ON_COURT);
        team.setLineUp(areaNb, enter);
    }

    @Override
    public Optional<Integer> returnTeamIdIfIsPoint() {
        return Optional.empty();
    }

    public int getTeamId() {
        return teamId;
    }

    public int getEnterNb() {
        return enterNb;
    }

    public int getAreaNb() {
        return areaNb;
    }
}
