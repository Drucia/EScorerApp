package com.druciak.escorerapp.model.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MatchTeam {
    private Team team;
    private List<MatchPlayer> players;
    private Map<Integer, MatchPlayer> lineUp;
    private int points;

    public MatchTeam(Team team, List<Player> players) {
        this.team = team;
        this.players = players.stream().map(MatchPlayer::new).collect(Collectors.toList());
        lineUp = new HashMap<>();
        points = 0;
    }

    public void addPoint(){points += 1;}
}
