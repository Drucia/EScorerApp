package com.druciak.escorerapp.model.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.druciak.escorerapp.model.entities.MatchInfo.MAX_SHIFTS_AMOUNT;
import static com.druciak.escorerapp.model.entities.MatchInfo.MAX_TIMES_AMOUNT;
import static com.druciak.escorerapp.model.entities.MatchInfo.TEAM_A_ID;
import static com.druciak.escorerapp.view.RunningMatchActivity.LEFT_TEAM_ID;
import static com.druciak.escorerapp.view.RunningMatchActivity.RIGHT_TEAM_ID;

public class MatchTeam extends Team {
    public static final int PLAYERS_ON_COURT = 6;

    private List<MatchPlayer> players;
    private Map<Integer, MatchPlayer> lineUp;
    private int points;
    private int sets;
    private int teamId;
    private int teamSideId;
    private int timesCounter;
    private int shiftCounter;
    private boolean isLineUpSet;

    public MatchTeam(Team team, List<Player> players, int teamId) {
        super(team);
        this.players = players.stream().map(MatchPlayer::new).collect(Collectors.toList());
        lineUp = new HashMap<>();
        points = 0;
        sets = 0;
        this.teamId = teamId;
        this.teamSideId = teamId == TEAM_A_ID ? LEFT_TEAM_ID : RIGHT_TEAM_ID;
        this.isLineUpSet = false;
    }

    public boolean getIsLineUpSet() {
        return isLineUpSet;
    }

    public int getTeamSideId() {
        return teamSideId;
    }

    public void setTeamSideId(int teamSideId) {
        this.teamSideId = teamSideId;
    }

    public void addTime(){timesCounter += 1;}

    public void addShift(){shiftCounter += 1;}

    public boolean canGetTime() {return timesCounter < MAX_TIMES_AMOUNT;}
    public boolean canGetShift() {return shiftCounter < MAX_SHIFTS_AMOUNT;}

    public int getTimesCounter() {
        return timesCounter;
    }

    public void addPoint(){points += 1;}

    public int getPoints() {
        return points;
    }

    public int getSets() {
        return sets;
    }

    public void addSet() {
        sets += 1;
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

    public void nextSet() {
        this.points = 0;
        this.timesCounter = 0;
        this.shiftCounter = 0;
    }

    public void setLineUp(int areaNb, MatchPlayer player) {
        if (player == null){
            lineUp.remove(areaNb);
        } else {
            lineUp.put(areaNb, player);
        }
        isLineUpSet = lineUp.size() == PLAYERS_ON_COURT;
    }

    public void setIsLineUpSet(boolean isSetLineUp) {
        this.isLineUpSet = isSetLineUp;
    }
}
