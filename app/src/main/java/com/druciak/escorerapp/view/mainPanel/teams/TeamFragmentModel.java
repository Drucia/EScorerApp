package com.druciak.escorerapp.view.mainPanel.teams;

import com.druciak.escorerapp.entities.Team;
import com.druciak.escorerapp.interfaces.ITeamFragmentMVP;
import com.druciak.escorerapp.model.firebaseService.Result;
import com.druciak.escorerapp.model.internalApiService.InternalApiManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeamFragmentModel implements ITeamFragmentMVP.IModel {
    private ITeamFragmentMVP.IPresenter presenter;
    private InternalApiManager manager;
    private String userId;

    public TeamFragmentModel(ITeamFragmentMVP.IPresenter presenter, String userId) {
        this.presenter = presenter;
        this.userId = userId;
        this.manager = new InternalApiManager();
    }

    @Override
    public void prepareAllTeamsOfUser() {
        manager.getTeamService().getTeamsOfUser(userId).enqueue(new Callback<List<Team>>() {
            @Override
            public void onResponse(Call<List<Team>> call, Response<List<Team>> response) {
                List<Team> teamsResponse = response.body();
                if (teamsResponse != null)
                    presenter.onPrepareTeamsListEventCompleted(new Result.Success<>(teamsResponse));
            }

            @Override
            public void onFailure(Call<List<Team>> call, Throwable t) {
                // todo
            }
        });
    }
}
