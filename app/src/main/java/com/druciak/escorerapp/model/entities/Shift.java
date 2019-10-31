package com.druciak.escorerapp.model.entities;

public class Shift extends Action{
    private int outPlayerNb;
    private int enterPlayerNb;
    private int teamId;

    public Shift(MatchPlayer out, MatchPlayer enter, int teamId, int teamPoints, int sndTeamPoints) {
        outPlayerNb = out.getNumber();
        enterPlayerNb = enter.getNumber();
        this.teamId = teamId;
        setScore(teamPoints + " : " + sndTeamPoints);
        out.setShiftNumber(enterPlayerNb);
        enter.setShiftNumber(outPlayerNb);
        out.setStatusId(MatchPlayer.STATUS_PLAYER_ON_DESK);
        enter.setStatusId(MatchPlayer.STATUS_PLAYER_ON_COURT);
    }

    @Override
    public Integer returnTeamIdIfIsPoint() {
        return null;
    }
}
