package com.druciak.escorerapp.model;

import com.druciak.escorerapp.entities.Action;
import com.druciak.escorerapp.entities.LoggedInUser;
import com.druciak.escorerapp.entities.MatchInfo;
import com.druciak.escorerapp.entities.MatchSummary;
import com.druciak.escorerapp.entities.MatchTeam;
import com.druciak.escorerapp.entities.SetInfo;
import com.druciak.escorerapp.entities.Shift;
import com.druciak.escorerapp.entities.Time;
import com.druciak.escorerapp.interfaces.ISummaryMVP;
import com.druciak.escorerapp.model.firebaseService.Result;
import com.druciak.escorerapp.model.internalApiService.InternalApiManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SummaryModel implements ISummaryMVP.IModel {
    private ISummaryMVP.IPresenter presenter;
    private MatchInfo matchInfo;
    private LoggedInUser user;
    private InternalApiManager manager;

    public SummaryModel(ISummaryMVP.IPresenter presenter, MatchInfo matchInfo, LoggedInUser user) {
        this.presenter = presenter;
        this.matchInfo = matchInfo;
        this.user = user;
        this.manager = new InternalApiManager();
    }

    @Override
    public List<String> getTeamsNames() {
        return new ArrayList<>(Arrays.asList(matchInfo.getSettings().getMatch().getHostTeam().getFullName(),
                matchInfo.getSettings().getMatch().getGuestTeam().getFullName()));
    }

    @Override
    public Map<Integer, SetInfo> getSetsInfo() {
        Map<Integer, Integer> times = matchInfo.getTimesOfSets();
        Map<Integer, SetInfo> result = new HashMap<>();
        Map<Integer, ArrayList<Action>> actionsOfSets = matchInfo.getActionsOfSets();
        int hostTeamId = matchInfo.getSettings().getMatch().getHostTeam().getId();
        int guestTeamId = matchInfo.getSettings().getMatch().getGuestTeam().getId();

        for (Integer set : actionsOfSets.keySet())
        {
            ArrayList<Action> actions = actionsOfSets.get(set);
            if (!actions.isEmpty()) {
                int shiftsOfHomeTeam = (int) actions.stream().filter(action -> action instanceof Shift
                        && action.getTeamMadeActionId() == hostTeamId).count();
                int shiftsOfGuestTeam = (int) actions.stream().filter(action -> action instanceof Shift
                        && action.getTeamMadeActionId() == guestTeamId).count();
                int timesOfHomeTeam = (int) actions.stream().filter(action -> action instanceof Time
                        && action.getTeamMadeActionId() == hostTeamId).count();
                int timesOfGuestTeam = (int) actions.stream().filter(action -> action instanceof Time
                        && action.getTeamMadeActionId() == guestTeamId).count();
                Action lastAction = actions.get(actions.size() - 1);
                int pointsOfHomeTeam = lastAction.getTeamMadeActionId() == hostTeamId ?
                        lastAction.getTeamMadeActionPoints() : lastAction.getSndTeamPoints();
                int pointsOfGuestTeam = lastAction.getTeamMadeActionId() == guestTeamId ?
                        lastAction.getTeamMadeActionPoints() : lastAction.getSndTeamPoints();

                SetInfo setInfo = new SetInfo(shiftsOfHomeTeam, shiftsOfGuestTeam, timesOfHomeTeam,
                        timesOfGuestTeam, pointsOfHomeTeam, pointsOfGuestTeam, set, times.get(set));
                result.put(set, setInfo);
            }
        }
        return result;
    }

    @Override
    public List<Integer> getSetsScores() {
        MatchTeam teamHome = matchInfo.getTeamA().getId() == matchInfo.getSettings().getMatch()
                .getHostTeam().getId() ? matchInfo.getTeamA() : matchInfo.getTeamB();
        MatchTeam teamGuest = matchInfo.getTeamA().getId() == matchInfo.getSettings().getMatch()
                .getGuestTeam().getId() ? matchInfo.getTeamA() : matchInfo.getTeamB();
        return new ArrayList<>(Arrays.asList(teamHome.getSets(), teamGuest.getSets()));
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

    @Override
    public LoggedInUser getUser() { return user; }

    @Override
    public void saveSummaryOnServer() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        String time = dtf.format(now) + matchInfo.getSettings().getStartTime();
        List<Integer> sets = getSetsScores();
        MatchSummary matchSummary = new MatchSummary(
                time,
                matchInfo.getSettings().getMatch(),
                matchInfo.getWinner(),
                sets.get(0),
                sets.get(1),
                getSetsInfo()
        );
        manager.getUserDataService().saveSummary(user.getUserId(), matchSummary).enqueue(new Callback<MatchSummary>() {
            @Override
            public void onResponse(Call<MatchSummary> call, Response<MatchSummary> response) {
                MatchSummary summaryResponse = response.body();
                if (summaryResponse != null)
                {
                    presenter.onSaveSummaryCompleted(new Result.Success<String>("Zapisano na serwerze"));
                } else
                {
                    presenter.onSaveSummaryCompleted(new Result.Error(new Exception(response.errorBody().toString())));
                }
            }

            @Override
            public void onFailure(Call<MatchSummary> call, Throwable t) {
                presenter.onSaveSummaryCompleted(new Result.Error(new Exception(t.getMessage())));
            }
        });
    }
}
