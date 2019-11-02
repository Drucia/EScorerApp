package com.druciak.escorerapp.model.entities;

import static com.druciak.escorerapp.model.entities.MatchPlayer.NO_SHIFT;
import static com.druciak.escorerapp.model.entities.MatchPlayer.STATUS_PLAYER_NOT_TO_SHIFT;
import static com.druciak.escorerapp.model.entities.MatchPlayer.STATUS_PLAYER_SHIFTED;

public class Shift extends Action{
    private int outPlayerNb;
    private int enterPlayerNb;
    private int teamId;

    public Shift(MatchPlayer out, MatchPlayer enter, MatchTeam team, int sndTeamPoints) {
        outPlayerNb = out.getNumber();
        enterPlayerNb = enter.getNumber();
        this.teamId = team.getTeamId();
        setScore(team.getPoints(), sndTeamPoints);
        if (out.getShiftNumber() != NO_SHIFT){
            out.setStatusId(STATUS_PLAYER_NOT_TO_SHIFT);
            enter.setStatusId(STATUS_PLAYER_NOT_TO_SHIFT);
        }
        else {
            out.setStatusId(STATUS_PLAYER_SHIFTED);
            enter.setStatusId(STATUS_PLAYER_SHIFTED);
        }
        out.setShiftNumber(enterPlayerNb);
        enter.setShiftNumber(outPlayerNb);
        team.addShift();
    }

    @Override
    public Integer returnTeamIdIfIsPoint() {
        return null;
    }

    public int getTeamId() {
        return teamId;
    }

    public int getOutPlayerNb() {
        return outPlayerNb;
    }

    public int getEnterPlayerNb() {
        return enterPlayerNb;
    }
}
