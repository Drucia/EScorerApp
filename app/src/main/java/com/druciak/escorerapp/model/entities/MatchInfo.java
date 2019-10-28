package com.druciak.escorerapp.model.entities;

import java.util.Map;

public class MatchInfo {
    public static final int NO_CARD_ID = 0;
    public static final int YELLOW_CARD_ID = 1;
    public static final int RED_CARD_ID = 2;
    public static final int YELLOW_AND_RED_CARD_TOGETHER_ID = 3;
    public static final int YELLOW_AND_RED_CARD_SEPARATELY_ID = 4;

    private MatchTeam teamA;
    private MatchTeam teamB;
    private MatchTeam serveTeam;
    private MatchSettings settings;
    private Map<Integer, Action> actionsOfSets;
}
