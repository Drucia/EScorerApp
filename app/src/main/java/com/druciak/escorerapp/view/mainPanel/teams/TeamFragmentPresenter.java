package com.druciak.escorerapp.view.mainPanel.teams;

import com.druciak.escorerapp.entities.Team;
import com.druciak.escorerapp.interfaces.ITeamFragmentMVP;
import com.druciak.escorerapp.model.firebaseService.Result;

import java.util.List;

class TeamFragmentPresenter implements ITeamFragmentMVP.IPresenter {
    private ITeamFragmentMVP.IView view;
    private ITeamFragmentMVP.IModel model;

    public TeamFragmentPresenter(ITeamFragmentMVP.IView view, String userID) {
        this.view = view;
        this.model = new TeamFragmentModel(this, userID);
    }

    @Override
    public void onPrepareTeamsListEventCompleted(Result<List<Team>> result) {
        if (result instanceof Result.Success)
            view.onPrepareTeamsListEventSuccess(((Result.Success<List<Team>>) result).getData());
    }

    @Override
    public void onRefresh() {
        model.prepareAllTeamsOfUser();
    }
}
