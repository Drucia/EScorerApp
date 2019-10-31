package com.druciak.escorerapp.model.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class MatchTeam {
    private Team team;
    private List<MatchPlayer> players;
    private Map<Integer, MatchPlayer> lineUp;
    private int points;
    private int teamId;

    public MatchTeam(Team team, List<Player> players, int teamId) {
        this.team = team;
        this.players = players.stream().map(MatchPlayer::new).collect(Collectors.toList());
        lineUp = new HashMap<>();
        points = 0;
        this.teamId = teamId;
    }

    public void addPoint(){points += 1;}

    public int getPoints() {
        return points;
    }

    public Team getTeam() {
        return team;
    }

    public int getTeamId() {
        return teamId;
    }

    public List<MatchPlayer> getPlayers() {
        return players;
    }

    public Map<Integer, MatchPlayer> getLineUp() {
        return lineUp;
    }

    public void setLineUp(Map<Integer, MatchPlayer> lineUp) {
        this.lineUp = lineUp;
    }

    public MatchPlayer getPlayerByNumber(int number){
        Optional<MatchPlayer> player = players.stream()
                .filter(matchPlayer -> matchPlayer.getNumber() == number).findAny();

        return player.orElse(null);
    }
}
