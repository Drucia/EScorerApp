package com.druciak.escorerapp.interfaces;

import com.druciak.escorerapp.model.entities.Player;
import com.druciak.escorerapp.model.firebaseService.Result;

import java.util.List;

public interface IMatchSettingsMVP {
    interface IModel{
        void getAllTeams();
        void getAllPlayersOfTeams(int hostId, int guestId);
    }

    interface IView{
        void onPreparePlayerListsEventSucceeded(List<Player> players);
        void onPreparePlayerListEventFailed(String error);
        void addPlayer(Player player);
        void removePlayer(Player player);
    }

    interface IFragmentView {
        void onPlayerClicked(Player player, int adapterPosition);
    }

    interface IPresenter{
        void onPreparePlayersListEventCompleted(Result<List<Player>> result);
        void preparePlayersOfTeams(int hostId, int guestId);
        void addPlayer(Player player);
        void removePlayer(Player player);
    }
}
