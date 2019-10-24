package com.druciak.escorerapp.interfaces;

import com.druciak.escorerapp.model.entities.MatchSettings;
import com.druciak.escorerapp.model.entities.Player;
import com.druciak.escorerapp.model.firebaseService.Result;

import java.util.List;

public interface IMatchSettingsMVP {
    interface IModel{
        void getAllPlayersOfTeams(int hostId, int guestId);
    }

    interface IView{
        void onPreparePlayerListsEventSucceeded(List<Player> players);
        void onPreparePlayerListEventFailed(String error);
        void addPlayer(Player player);
        void removePlayer(Player player);
        void removeAdditionalMember(String name, int teamId, int memberId);
        void addAdditionalMember(String name, int teamId, int memberId);
        void onMatchStartClicked();
        void startMatch(MatchSettings matchSettings);
        void showPopUpWithErrorMatchSettings(String error);
        void setMatchSettingsParams(String sTournamentName, String sType, boolean isZas, String
                sTown, String sStreet, String sHall, String sRefereeFirst, String sRefereeSnd,
                                    String sLine1, String sLine2, String sLine3, String sLine4,
                                    boolean isMan);
    }

    interface IFragmentView {
        void onPlayerClicked(Player player, int adapterPosition);
    }

    interface IPresenter{
        void onPreparePlayersListEventCompleted(Result<List<Player>> result);
        void preparePlayersOfTeams(int hostId, int guestId);
        void addPlayer(Player player);
        void removePlayer(Player player);
        void removeAdditionalMember(String name, int teamId, int memberId);
        void addAdditionalMember(String name, int teamId, int memberId);
        void onMatchStartClicked();
        void setMatchSettingsParams(String sTournamentName, String sType, boolean isZas,
                                    String sTown, String sStreet, String sHall,
                                    String sRefereeFirst, String sRefereeSnd, String sLine1,
                                    String sLine2, String sLine3, String sLine4, boolean isMan);
    }
}
