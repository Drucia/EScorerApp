package com.druciak.escorerapp.presenter;

import com.druciak.escorerapp.interfaces.IMatchSettingsMVP;
import com.druciak.escorerapp.model.entities.Player;
import com.druciak.escorerapp.model.entities.Team;
import com.druciak.escorerapp.model.firebaseService.Result;

import java.util.List;

public class MatchSettingsPresenter implements IMatchSettingsMVP.IPresenter {
    private IMatchSettingsMVP.IModel externalManager;
    private IMatchSettingsMVP.IView view;

    public MatchSettingsPresenter(IMatchSettingsMVP.IModel externalManager, IMatchSettingsMVP.IView view) {
        this.externalManager = externalManager;
        this.view = view;
    }

    @Override
    public void setPlayersList(int teamId) {
        externalManager.getAllTeams();
    }

    @Override
    public void prepareTeamList() {

    }

    @Override
    public void onPrepareTeamListEventCompleted(Result<List<Team>> result) {
        if (result instanceof Result.Success)
            view.onPrepareTeamListEventSucceeded(((Result.Success<List<Team>>) result).getData());
        else
            view.onPrepareTeamListEventFailed(((Result.Error) result).getError().getMessage());
    }

    @Override
    public void onPreparePlayersListEventCompleted(Result<List<Player>> result) {
        if (result instanceof Result.Success)
            view.onPreparePlayerListEventSucceeded(((Result.Success<List<Player>>) result).getData());
        else
            view.onPreparePlayerListEventFailed(((Result.Error) result).getError().getMessage());
    }
}
