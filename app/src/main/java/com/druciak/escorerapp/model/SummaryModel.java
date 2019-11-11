package com.druciak.escorerapp.model;

import com.druciak.escorerapp.entities.Action;
import com.druciak.escorerapp.entities.MatchInfo;
import com.druciak.escorerapp.entities.MatchTeam;
import com.druciak.escorerapp.entities.SetInfo;
import com.druciak.escorerapp.entities.Shift;
import com.druciak.escorerapp.entities.Time;
import com.druciak.escorerapp.interfaces.ISummaryMVP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SummaryModel implements ISummaryMVP.IModel {
    private ISummaryMVP.IPresenter presenter;
    private MatchInfo matchInfo;

    public SummaryModel(ISummaryMVP.IPresenter presenter, MatchInfo matchInfo) {
        this.presenter = presenter;
        this.matchInfo = matchInfo;
    }

    @Override
    public List<String> getTeamsNames() {
        return new ArrayList<>(Arrays.asList(matchInfo.getTeamA().getFullName(),
                matchInfo.getTeamB().getFullName()));
    }

    @Override
    public Map<Integer, SetInfo> getSetsInfo() {
        Map<Integer, Integer> times = matchInfo.getTimesOfSets();
        Map<Integer, SetInfo> result = new HashMap<>();
        Map<Integer, ArrayList<Action>> actionsOfSets = matchInfo.getActionsOfSets();
        MatchTeam teamA = matchInfo.getTeamA();
        MatchTeam teamB = matchInfo.getTeamB();
        for (Integer set : actionsOfSets.keySet())
        {
            ArrayList<Action> actions = actionsOfSets.get(set);
            if (!actions.isEmpty()) {
                int shiftsOfTeamA = (int) actions.stream().filter(action -> action instanceof Shift
                        && action.getTeamMadeActionId() == teamA.getTeamId()).count();
                int shiftsOfTeamB = (int) actions.stream().filter(action -> action instanceof Shift
                        && action.getTeamMadeActionId() == teamB.getTeamId()).count();
                int timesOfTeamA = (int) actions.stream().filter(action -> action instanceof Time
                        && action.getTeamMadeActionId() == teamA.getTeamId()).count();
                int timesOfTeamB = (int) actions.stream().filter(action -> action instanceof Time
                        && action.getTeamMadeActionId() == teamB.getTeamId()).count();
                Action lastAction = actions.get(actions.size() - 1);
                int pointsOfA = lastAction.getTeamMadeActionId() == teamA.getTeamId() ?
                        lastAction.getTeamMadeActionPoints() : lastAction.getSndTeamPoints();
                int pointsOfB = lastAction.getTeamMadeActionId() == teamB.getTeamId() ?
                        lastAction.getTeamMadeActionPoints() : lastAction.getSndTeamPoints();

                SetInfo setInfo = new SetInfo(shiftsOfTeamA, shiftsOfTeamB, timesOfTeamA, timesOfTeamB,
                        pointsOfA, pointsOfB, set, times.get(set));
                result.put(set, setInfo);
            }
        }
        return result;
    }

    @Override
    public List<Integer> getSetsScores() {
        return new ArrayList<>(Arrays.asList(matchInfo.getTeamA().getSets(), matchInfo.getTeamB().getSets()));
    }

    @Override
    public String getAttentions() {
        return matchInfo.getAttentions();
    }

    @Override
    public void updateAttentions(String attentions) {
        matchInfo.setAttentions(attentions);
    }

    @Override
    public MatchInfo getMatchInfo() {
        return matchInfo;
    }
}
