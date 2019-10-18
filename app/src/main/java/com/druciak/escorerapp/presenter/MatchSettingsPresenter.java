package com.druciak.escorerapp.presenter;

import com.druciak.escorerapp.interfaces.IMatchSettingsMVP;
import com.druciak.escorerapp.model.entities.Player;
import com.druciak.escorerapp.model.externalApiService.ExternalApiManager;
import com.druciak.escorerapp.model.firebaseService.Result;

import java.util.List;

public class MatchSettingsPresenter implements IMatchSettingsMVP.IPresenter {
    private IMatchSettingsMVP.IModel externalManager;
    private IMatchSettingsMVP.IView view;

    public MatchSettingsPresenter(IMatchSettingsMVP.IView view) {
        this.externalManager = new ExternalApiManager(this);
        this.view = view;
    }

    @Override
    public void onPreparePlayersListEventCompleted(Result<List<Player>> result) {
        if (result instanceof Result.Success) {
            view.onPreparePlayerListsEventSucceeded(((Result.Success<List<Player>>) result).getData());
        }
        else
            view.onPreparePlayerListEventFailed(((Result.Error) result).getError().getMessage());
    }

    @Override
    public void preparePlayersOfTeams(int hostId, int guestId) {
        externalManager.getAllPlayersOfTeams(hostId, guestId);
    }
}
