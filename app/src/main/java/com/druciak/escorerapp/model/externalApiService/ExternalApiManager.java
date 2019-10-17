package com.druciak.escorerapp.model.externalApiService;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.druciak.escorerapp.interfaces.IMainPanelMVP;
import com.druciak.escorerapp.interfaces.IMatchSettingsMVP;
import com.druciak.escorerapp.model.entities.Match;
import com.druciak.escorerapp.model.entities.Player;
import com.druciak.escorerapp.model.entities.Team;
import com.druciak.escorerapp.model.firebaseService.Result;
import com.druciak.escorerapp.presenter.MainPanelPresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import retrofit2.Retrofit;

public class ExternalApiManager implements IMatchSettingsMVP.IModel, IMainPanelMVP.IMatchModel {
    private static final String BASE_DZPS_SERVER_URL = "";
    private static ArrayList<Team> teams = new ArrayList<>();
    private static Map<Integer, List<Player>> playersOfTeams = new HashMap<>();
    private static ArrayList<Match> matches = new ArrayList<>();

    private Retrofit retrofit;
    private IMatchSettingsMVP.IPresenter presenter;
    private IMainPanelMVP.IPresenter mainPanelPresenter;

    public ExternalApiManager(IMatchSettingsMVP.IPresenter presenter) {
        this.presenter = presenter;

        // prepare players
        ArrayList<Player> poloniaPlayers = new ArrayList<>();
        poloniaPlayers.add(new Player(1, "Aleksandra", "Druciak", 'K', 2));
        poloniaPlayers.add(new Player(2, "Aleksandra", "Nowak", 'K', 2));
        poloniaPlayers.add(new Player(3, "Aleksandra", "Kowalska", 'K', 2));
        poloniaPlayers.add(new Player(4, "Aleksandra", "Drucia", 'K', 2));
        poloniaPlayers.add(new Player(5, "Paulina", "Druciak", 'K', 2));
        poloniaPlayers.add(new Player(6, "Karolina", "Druciak", 'K', 2));

        // prepare players
        ArrayList<Player> chelmiecPlayers = new ArrayList<>();
        poloniaPlayers.add(new Player(7, "Aleksandra", "Druciak", 'K', 1));
        poloniaPlayers.add(new Player(8, "Aleksandra", "Nowak", 'K', 1));
        poloniaPlayers.add(new Player(9, "Aleksandra", "Kowalska", 'K', 1));
        poloniaPlayers.add(new Player(10, "Aleksandra", "Drucia", 'K', 1));
        poloniaPlayers.add(new Player(11, "Paulina", "Druciak", 'K', 1));
        poloniaPlayers.add(new Player(12, "Karolina", "Druciak", 'K', 1));
        poloniaPlayers.add(new Player(13, "Kar", "Druci", 'K', 1));
        poloniaPlayers.add(new Player(14, "Karo", "Druciak", 'K', 1));

        playersOfTeams.put(1, poloniaPlayers);
        playersOfTeams.put(2, chelmiecPlayers);
    }

    public ExternalApiManager(MainPanelPresenter mainPanelPresenter) {
        this.mainPanelPresenter = mainPanelPresenter;

        // prepare teams
        Team polonia = new Team(1, "Polonia", "MKS Polonia Świdnica");
        Team chelmiec = new Team(2, "Chełmiec", "MKS Chełmiec Wodociągi Wałbrzych");
        Team tygrysy = new Team(3, "Tygrysy", "MKS Tygrysy Strzelin");
        Team gwardia = new Team(4, "Gwardia", "UKS Gwardia Wrocław");

        teams.add(polonia);
        teams.add(chelmiec);
        teams.add(tygrysy);
        teams.add(gwardia);

        // prepare matches
        // a6vhS2jGZddait8YEiDaeqf8euh2 - Tomasz
        // URdZLXrwoyRxdrgVx1CaciAUpBh1 - Maciej

        matches.add(new Match(teams.get(1), teams.get(2), "a6vhS2jGZddait8YEiDaeqf8euh2"));
        matches.add(new Match(teams.get(2), teams.get(3), "a6vhS2jGZddait8YEiDaeqf8euh2"));
        matches.add(new Match(teams.get(1), teams.get(3), "a6vhS2jGZddait8YEiDaeqf8euh2"));
        matches.add(new Match(teams.get(2), teams.get(1), "URdZLXrwoyRxdrgVx1CaciAUpBh1"));
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
        presenter.onPrepareTeamListEventCompleted(new Result.Success<>(teams));
    }

    @Override
    public void getAllPlayersOfTeam(int teamId) {
//        this.getPlayerService().getPlayersOfTeam(teamId).enqueue(new Callback<List<Player>>() {
//            @Override
//            public void onResponse(Call<List<Player>> call, Response<List<Player>> response) {
//                List<Player> players = response.body();
//                if (players != null)
//                    presenter.onPrepareTeamListEventCompleted(new Result.Success(players));
//                else
//                    presenter.onPreparePlayersListEventCompleted(new Result.Error(new Exception("Player Body is empty")));
//            }
//
//            @Override
//            public void onFailure(Call<List<Player>> call, Throwable t) {
//                presenter.onPrepareTeamListEventCompleted(new Result.Error(new Exception("Player Connection error")));
//            }
//        });
        // TODO mock server!!!
        presenter.onPreparePlayersListEventCompleted(new Result.Success<>(playersOfTeams.get(teamId)));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void getMatchedForReferee(final String refereeId) {
        // TODO mock server!!!
        List<Match> filteredMatches = matches.stream().filter(match -> match.getRefereeId().equals(refereeId)).collect(Collectors.toList());
        mainPanelPresenter.onPrepareMatchesListCompleted(new Result.Success<>(filteredMatches));
    }
}
