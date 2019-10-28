package com.druciak.escorerapp.model.entities;

public class Action {
    String score = "";

    public void makeActionForTeam(MatchTeam team){
        team.addPoint();
    }
}
