package com.druciak.escorerapp.model.externalApiService;

import com.druciak.escorerapp.interfaces.IMatchSettingsMVP;
import com.druciak.escorerapp.model.entities.Player;
import com.druciak.escorerapp.model.entities.Team;
import com.druciak.escorerapp.model.firebaseService.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ExternalApiManager implements IMatchSettingsMVP.IModel {
    private static final String BASE_DZPS_SERVER_URL = "";
    private static ArrayList<Team> teams = new ArrayList<>();
    private static Map<Integer, List<Player>> playersOfTeams;

    private Retrofit retrofit;
    private IMatchSettingsMVP.IPresenter presenter;

    public ExternalApiManager(IMatchSettingsMVP.IPresenter presenter) {
        this.presenter = presenter;

        // prepare teams
        teams.add(new Team(1, "Polonia", "MKS Polonia Świdnica"));
        teams.add(new Team(2, "Chełmiec", "MKS Chełmiec Wodociągi Wałbrzych"));
        teams.add(new Team(3, "Tygrysy", "MKS Tygrysy Strzelin"));
        teams.add(new Team(4, "Gwardia", "UKS Gwardia Wrocław"));

        // prepare players
        ArrayList<Player> poloniaPlayers = new ArrayList<>();
        poloniaPlayers.add(new Player());
    }

    private void initializeRetrofit(String serverUrl)
    {
        retrofit = new Retrofit.Builder()
                .baseUrl(serverUrl)
                .build();
    }

    private TeamService getTeamService()
    {
        initializeRetrofit(BASE_DZPS_SERVER_URL);
        return retrofit.create(TeamService.class);
    }

    private PlayerService getPlayerService()
    {
        initializeRetrofit(BASE_DZPS_SERVER_URL);
        return retrofit.create(PlayerService.class);
    }

    @Override
    public void getAllTeams() {
//        this.getTeamService().getTeams().enqueue(new Callback<List<Team>>() {
//            @Override
//            public void onResponse(Call<List<Team>> call, Response<List<Team>> response) {
//                List<Team> teams = response.body();
//
//                if (teams != null)
//                    presenter.onPrepareTeamListEventCompleted(new Result.Success(teams));
//                else
//                    presenter.onPrepareTeamListEventCompleted(new Result.Error(new Exception("Team Body is empty")));
//            }
//
//            @Override
//            public void onFailure(Call<List<Team>> call, Throwable t) {
//                presenter.onPrepareTeamListEventCompleted(new Result.Error(new Exception("Team Connection error")));
//            }
//        });

        // TODO mock server!!!
    }

    @Override
    public void getAllPlayersOfTeam(int teamId) {
        this.getPlayerService().getPlayersOfTeam(teamId).enqueue(new Callback<List<Player>>() {
            @Override
            public void onResponse(Call<List<Player>> call, Response<List<Player>> response) {
                List<Player> players = response.body();
                if (players != null)
                    presenter.onPrepareTeamListEventCompleted(new Result.Success(players));
                else
                    presenter.onPreparePlayersListEventCompleted(new Result.Error(new Exception("Player Body is empty")));
            }

            @Override
            public void onFailure(Call<List<Player>> call, Throwable t) {
                presenter.onPrepareTeamListEventCompleted(new Result.Error(new Exception("Player Connection error")));
            }
        });
    }
}
