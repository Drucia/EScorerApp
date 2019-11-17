package com.druciak.escorerapp.model;

import com.druciak.escorerapp.entities.Pair;
import com.druciak.escorerapp.entities.Player;
import com.druciak.escorerapp.entities.Team;
import com.druciak.escorerapp.interfaces.IMatchSettingsMVP;
import com.druciak.escorerapp.model.firebaseService.Result;
import com.druciak.escorerapp.model.internalApiService.InternalApiManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchSettingsModel implements IMatchSettingsMVP.IInternalModel {
    private IMatchSettingsMVP.IPresenter presenter;
    private InternalApiManager manager;

    public MatchSettingsModel(IMatchSettingsMVP.IPresenter presenter) {
        this.presenter = presenter;
        manager = new InternalApiManager();
    }

    @Override
    public void getAllTeamsOfUser(String userId) {
        manager.getUserDataService().getUserTeams(userId).enqueue(new Callback<List<Team>>() {
            @Override
            public void onResponse(Call<List<Team>> call, Response<List<Team>> response) {
                List<Team> responseTeams = response.body();
                if (responseTeams != null)
                {
                    presenter.onGetUserTeamsCompleted(new Result.Success<List<Team>>(responseTeams));
                }
            }

            @Override
            public void onFailure(Call<List<Team>> call, Throwable t) {

            }
        });
    }

    @Override
    public void getAllPlayersOfTeam(String userId, int teamId) {
        manager.getUserDataService().getUserSpecificTeamPlayers(userId, teamId).enqueue(new Callback<List<Player>>() {
            @Override
            public void onResponse(Call<List<Player>> call, Response<List<Player>> response) {
                List<Player> responsePlayers = response.body();
                if (responsePlayers != null)
                {
                    presenter.onGetPlayersOfTeamCompleted(new Result.Success<List<Player>>(responsePlayers));
                }
            }

            @Override
            public void onFailure(Call<List<Player>> call, Throwable t) {

            }
        });
    }

    @Override
    public void saveTeam(Team team, List<Player> players, String userId) {
        manager.getUserDataService().saveTeam(userId, new Pair<>(team, players)).enqueue(new Callback<Pair<Team, Player>>() {
            @Override
            public void onResponse(Call<Pair<Team, Player>> call, Response<Pair<Team, Player>> response) {
                // do sth todo
            }

            @Override
            public void onFailure(Call<Pair<Team, Player>> call, Throwable t) {
                // do sth todo
            }
        });
    }
}
