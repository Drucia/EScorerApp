package com.druciak.escorerapp.model.entities;

public class Shift extends Action{
    private int outPlayerNb;
    private int enterPlayerNb;
    private int teamId;

    public Shift(MatchPlayer out, MatchPlayer enter, int teamId) {
        outPlayerNb = out.getPlayer().getNumber();
        enterPlayerNb = enter.getPlayer().getNumber();
        this.teamId = teamId;
    }
}
