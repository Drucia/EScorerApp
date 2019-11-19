package com.druciak.escorerapp.interfaces;

import com.druciak.escorerapp.entities.LoggedInUser;
import com.druciak.escorerapp.entities.MatchSettings;
import com.druciak.escorerapp.entities.Player;
import com.druciak.escorerapp.entities.Team;
import com.druciak.escorerapp.model.firebaseService.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface IMatchSettingsMVP{
    interface IExternalModel{
        void getAllPlayersOfTeams(int hostId, int guestId);
    }

    interface IInternalModel{
        void getAllTeamsOfUser(String userId);
        void getAllPlayersOfTeam(int teamId);
        void saveTeam(Team team, List<Player> players, String userId);
    }

    interface IView{
        void onPreparePlayerListsEventSucceeded(List<Player> players);
        void onPreparePlayerListEventFailed(String error);
        void addPlayer(Player player);
        void removePlayer(Player player);
        void removeAdditionalMember(String name, int teamId, int memberId);
        void addAdditionalMember(String name, int teamId, int memberId);
        void onMatchStartClicked();
        void startMatch(MatchSettings matchSettings, LoggedInUser user, boolean isSimplyMatch);
        void showPopUpWithErrorMatchSettings(String error);
        void setMatchSettingsParams(String sTournamentName, String sType, boolean isZas, String
                sTown, String sStreet, String sHall, String sRefereeFirst, String sRefereeSnd,
                                    String sLine1, String sLine2, String sLine3, String sLine4,
                                    boolean isMan);
        void goToMainPanel(LoggedInUser user);
        void updateTeamName(String name, int teamId);
        void updateTeamsInPopUp(ArrayList<Team> teams);
        void startMatchWithSaveDataOnServer(MatchSettings matchSettings, LoggedInUser loggedInUser,
                                            boolean isSimplyMatch);
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
        void onDiscardClicked();
        MatchSettings getMatchSettings();
        void updateTeamName(String name, int teamId);
        void onGetUserTeamsCompleted(Result<List<Team>> result);
        void prepareTeams();
        Optional<ArrayList<Team>> getTeamsList();
        void preparePlayersOfTeams(Team team, boolean isHostTeam);
        void onGetPlayersOfTeamCompleted(Result<List<Player>> result);
        void saveDataOnServer(boolean home, boolean guest, boolean conf);
    }

    interface IFragmentView extends IOnPlayerTouchCallback{
    }
}
