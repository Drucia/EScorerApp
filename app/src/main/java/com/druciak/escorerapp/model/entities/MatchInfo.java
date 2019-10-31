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

    private MatchTeam teamA;
    private MatchTeam teamB;
    private MatchTeam serveTeam;
    private MatchSettings settings;
    private Map<Integer, ArrayList<Action>> actionsOfSets;

    public MatchInfo(Team teamA, List<Player> playersA, Team teamB, List<Player> playersB,
                     boolean isAServe, MatchSettings settings) {
        this.teamA = new MatchTeam(teamA, playersA);
        this.teamB = new MatchTeam(teamB, playersB);
        this.serveTeam = isAServe ? this.teamA : this.teamB;
        this.settings = settings;
        actionsOfSets = new HashMap<>();
        actionsOfSets.put(1, new ArrayList<>());
        actionsOfSets.put(2, new ArrayList<>());
        actionsOfSets.put(3, new ArrayList<>());
        actionsOfSets.put(4, new ArrayList<>());
        actionsOfSets.put(5, new ArrayList<>());
    }
}
