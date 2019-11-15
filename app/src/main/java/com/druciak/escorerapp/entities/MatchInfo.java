package com.druciak.escorerapp.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.druciak.escorerapp.entities.Action.LINE_UP_ID;
import static com.druciak.escorerapp.entities.Action.PLAYER_PUNISHMENT_ID;
import static com.druciak.escorerapp.entities.Action.POINT_ID;
import static com.druciak.escorerapp.entities.Action.SHIFT_ID;
import static com.druciak.escorerapp.entities.Action.TEAM_PUNISHMENT_ID;
import static com.druciak.escorerapp.entities.Action.TIME_ID;

public class MatchInfo implements Parcelable {
    public static final int NO_CARD_ID = 0;
    public static final int YELLOW_CARD_ID = 1;
    public static final int RED_CARD_ID = 2;
    public static final int YELLOW_AND_RED_CARD_TOGETHER_ID = 3;
    public static final int YELLOW_AND_RED_CARD_SEPARATELY_ID = 4;
    public static final int WARNING_ID = 5;
    public static final int TEAM_A_ID = 6;
    public static final int TEAM_B_ID = 7;

    public static final int MATCH_END_POINTS = 25;
    public static final int MATCH_END_POINTS_IN_TIEBREAK = 15;
    public static final int MATCH_MIN_DIFFERENT_POINTS = 2;
    public static final int LAST_SET_FOR_YOUNG = 3;
    public static final int LAST_SET = 2;
    public static final int MIN_SETS_TO_WIN = 3;
    public static final int MIN_SETS_TO_WIN_FOR_YOUNG = 2;
    public static final int MAX_TIMES_AMOUNT = 2;
    public static final int MAX_SHIFTS_AMOUNT = 6;
    public static final int TIME_LENGTH = 30;
    public static final int TIEBREAK_POINTS_TO_SHIFT = 8;
    public static final String YOUNG_TYPE_OF_MATCH = "Młodzicy";

    private MatchTeam teamA;
    private MatchTeam teamB;
    private MatchTeam serveTeam;
    private MatchSettings settings;
    private String attentions;
    private Map<Integer, ArrayList<Action>> actionsOfSets;
    private Map<Integer, Integer> timesOfSets;
    private Map<Integer, Integer> servesOfSets;

    public MatchInfo(Team teamA, List<Player> playersA, Team teamB, List<Player> playersB,
                     boolean isAServe, MatchSettings settings) {
        this.teamA = new MatchTeam(teamA, playersA, TEAM_A_ID, settings.getMembers());
        this.teamB = new MatchTeam(teamB, playersB, TEAM_B_ID, settings.getMembers());
        this.serveTeam = isAServe ? this.teamA : this.teamB;
        this.settings = settings;
        this.attentions = "";
        actionsOfSets = new HashMap<>();
        actionsOfSets.put(1, new ArrayList<>());
        actionsOfSets.put(2, new ArrayList<>());
        actionsOfSets.put(3, new ArrayList<>());
        actionsOfSets.put(4, new ArrayList<>());
        actionsOfSets.put(5, new ArrayList<>());
        timesOfSets = new HashMap<>();
        servesOfSets = new HashMap<>();
        servesOfSets.put(1, serveTeam.getTeamId());
    }

    protected MatchInfo(Parcel in) {
        teamA = in.readParcelable(MatchTeam.class.getClassLoader());
        teamB = in.readParcelable(MatchTeam.class.getClassLoader());
        serveTeam = in.readParcelable(MatchTeam.class.getClassLoader());
        settings = in.readParcelable(MatchSettings.class.getClassLoader());
        attentions = in.readString();
        int size = in.readInt();
        actionsOfSets = new HashMap<>();
        actionsOfSets.put(1, new ArrayList<>());
        actionsOfSets.put(2, new ArrayList<>());
        actionsOfSets.put(3, new ArrayList<>());
        actionsOfSets.put(4, new ArrayList<>());
        actionsOfSets.put(5, new ArrayList<>());
        for (int i = 0; i < size; i++)
        {
            int key = in.readInt();
            int sizeOfActions = in.readInt();
            for (int j = 0; j < sizeOfActions; j++)
            {
                ArrayList<Action> actions = null;
                int classId = in.readInt();
                switch (classId)
                {
                    case LINE_UP_ID:
                        actions = actionsOfSets.get(key);
                        actions.add(in.readParcelable(LineUp.class.getClassLoader()));
                        break;
                    case PLAYER_PUNISHMENT_ID:
                        actions = actionsOfSets.get(key);
                        actions.add(in.readParcelable(PlayerPunishment.class.getClassLoader()));
                        break;
                    case TEAM_PUNISHMENT_ID:
                        actions = actionsOfSets.get(key);
                        actions.add(in.readParcelable(TeamPunishment.class.getClassLoader()));
                        break;
                    case POINT_ID:
                        actions = actionsOfSets.get(key);
                        actions.add(in.readParcelable(Point.class.getClassLoader()));
                        break;
                    case SHIFT_ID:
                        actions = actionsOfSets.get(key);
                        actions.add(in.readParcelable(Shift.class.getClassLoader()));
                        break;
                    case TIME_ID:
                        actions = actionsOfSets.get(key);
                        actions.add(in.readParcelable(Time.class.getClassLoader()));
                        break;
                }
                actionsOfSets.put(key, actions);
            }
        }
        size = in.readInt();
        timesOfSets = new HashMap<>();
        for (int i = 0; i < size; i++)
        {
            int key = in.readInt();
            int value = in.readInt();
            timesOfSets.put(key, value);
        }
        size = in.readInt();
        servesOfSets = new HashMap<>();
        for (int i = 0; i < size; i++)
        {
            int key = in.readInt();
            int value = in.readInt();
            servesOfSets.put(key, value);
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(teamA, flags);
        dest.writeParcelable(teamB, flags);
        dest.writeParcelable(serveTeam, flags);
        dest.writeParcelable(settings, flags);
        dest.writeString(attentions);
        dest.writeInt(actionsOfSets.size());
        for (Integer key : actionsOfSets.keySet())
        {
            dest.writeInt(key);
            ArrayList<Action> value = actionsOfSets.get(key);
            dest.writeInt(value.size());
            for (Action a : value)
            {
                if (a instanceof LineUp)
                    dest.writeInt(LINE_UP_ID);
                else if (a instanceof PlayerPunishment)
                    dest.writeInt(PLAYER_PUNISHMENT_ID);
                else if (a instanceof TeamPunishment)
                    dest.writeInt(TEAM_PUNISHMENT_ID);
                else if (a instanceof Point)
                    dest.writeInt(POINT_ID);
                else if (a instanceof Shift)
                    dest.writeInt(SHIFT_ID);
                else if (a instanceof Time)
                    dest.writeInt(TIME_ID);
                dest.writeParcelable(a, flags);
            }
        }
        dest.writeInt(timesOfSets.size());
        for (Integer key : timesOfSets.keySet())
        {
            dest.writeInt(key);
            dest.writeInt(timesOfSets.get(key));
        }
        dest.writeInt(servesOfSets.size());
        for (Integer key : servesOfSets.keySet())
        {
            dest.writeInt(key);
            dest.writeInt(servesOfSets.get(key));
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MatchInfo> CREATOR = new Creator<MatchInfo>() {
        @Override
        public MatchInfo createFromParcel(Parcel in) {
            return new MatchInfo(in);
        }

        @Override
        public MatchInfo[] newArray(int size) {
            return new MatchInfo[size];
        }
    };

    public Map<Integer, Integer> getServesOfSets() {
        return servesOfSets;
    }

    public void addServeOfSet(int set, int teamId)
    {
        servesOfSets.put(set, teamId);
    }

    public MatchTeam getTeamA() {
        return teamA;
    }

    public MatchTeam getTeamB() {
        return teamB;
    }

    public Map<Integer, ArrayList<Action>> getActionsOfSets() {
        return actionsOfSets;
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

    public String getAttentions() {
        return attentions;
    }

    public void setAttentions(String attentions) {
        this.attentions = attentions;
    }

    public void addTimeForSet(int set, int time)
    {
        this.timesOfSets.put(set, time);
    }

    public Map<Integer, Integer> getTimesOfSets() {
        return timesOfSets;
    }
}
