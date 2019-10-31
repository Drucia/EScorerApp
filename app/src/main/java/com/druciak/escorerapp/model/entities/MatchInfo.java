package com.druciak.escorerapp.model.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchInfo {
    public static final int NO_CARD_ID = 0;
    public static final int YELLOW_CARD_ID = 1;
    public static final int RED_CARD_ID = 2;
    public static final int YELLOW_AND_RED_CARD_TOGETHER_ID = 3;
    public static final int YELLOW_AND_RED_CARD_SEPARATELY_ID = 4;
    public static final int TEAM_A_ID = 6;
    public static final int TEAM_B_ID = 7;
    public static final int MATCH_END_POINTS = 25;
    public static final int MATCH_END_POINTS_IN_TIEBREAK = 15;
    public static final int MATCH_MIN_DIFFERENT_POINTS = 2;
    public static final int LAST_SET_FOR_YOUNG = 3;
    public static final int LAST_SET = 5;
    public static final int MIN_SETS_TO_WIN = 3;
    public static final int MIN_SETS_TO_WIN_FOR_YOUNG = 2;
    public static final String YOUNG_TYPE_OF_MATCH = "Młodzicy";

    private MatchTeam teamA;
    private MatchTeam teamB;
    private MatchTeam serveTeam;
    private MatchSettings settings;
    private Map<Integer, ArrayList<Action>> actionsOfSets;

    public MatchInfo(Team teamA, List<Player> playersA, Team teamB, List<Player> playersB,
                     boolean isAServe, MatchSettings settings) {
        this.teamA = new MatchTeam(teamA, playersA, TEAM_A_ID);
        this.teamB = new MatchTeam(teamB, playersB, TEAM_B_ID);
        this.serveTeam = isAServe ? this.teamA : this.teamB;
        this.settings = settings;
        actionsOfSets = new HashMap<>();
        actionsOfSets.put(1, new ArrayList<>());
        actionsOfSets.put(2, new ArrayList<>());
        actionsOfSets.put(3, new ArrayList<>());
        actionsOfSets.put(4, new ArrayList<>());
        actionsOfSets.put(5, new ArrayList<>());
    }

    public String getTeamAName(int teamId) {
        return teamId == TEAM_A_ID ? teamA.getTeam().getFullName() : teamB.getTeam().getFullName();
    }

    public MatchTeam getTeamA() {
        return teamA;
    }

    public MatchTeam getTeamB() {
        return teamB;
    }

    public MatchTeam getServeTeam() {
        return serveTeam;
    }

    public MatchSettings getSettings() {
        return settings;
    }

    public List<MatchPlayer> getPlayers(int teamId){
        return teamId == TEAM_A_ID ? teamA.getPlayers() : teamB.getPlayers();
    }

    public void setTeamLineUp(int teamId, Map<Integer, MatchPlayer> lineUp){
        if (teamId == TEAM_A_ID) {
            teamA.setLineUp(lineUp);
        } else {
            teamB.setLineUp(lineUp);
        }
    }

    public void addAction(int set, Action action) {
        ArrayList<Action> actions = actionsOfSets.get(set);
        actions.add(action);
    }

    public boolean isTiebreak(int set){
        if (settings.getType().equals(YOUNG_TYPE_OF_MATCH))
            return set == LAST_SET_FOR_YOUNG;
        else
            return set == LAST_SET;
    }

    public int getMinSetsToWin(){
        if (settings.getType().equals(YOUNG_TYPE_OF_MATCH))
            return MIN_SETS_TO_WIN_FOR_YOUNG;
        else
            return MIN_SETS_TO_WIN;
    }
}
