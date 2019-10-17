package com.druciak.escorerapp.interfaces;

import com.druciak.escorerapp.model.entities.Player;
import com.druciak.escorerapp.model.entities.Team;
import com.druciak.escorerapp.model.firebaseService.Result;

import java.util.List;

public interface IMatchSettingsMVP {
    interface IModel{
        void getAllTeams();
        void getAllPlayersOfTeam(int teamId);
    }

    interface IView{
        void onPlayerClicked(Player player, int adapterPosition);
        void onPrepareTeamListEventSucceeded(List<Team> teams);
        void onPrepareTeamListEventFailed(String error);
        void onPreparePlayerListEventSucceeded(List<Player> teams);
        void onPreparePlayerListEventFailed(String error);
    }

    interface IPresenter{
        void setPlayersList(int teamId);
        void prepareTeamList();
        void onPrepareTeamListEventCompleted(Result<List<Team>> result);
        void onPreparePlayersListEventCompleted(Result<List<Player>> result);
    }
}
