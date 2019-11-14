package com.druciak.escorerapp.presenter;

import com.druciak.escorerapp.entities.LoggedInUser;
import com.druciak.escorerapp.interfaces.IMatchSettingsMVP;
import com.druciak.escorerapp.entities.Match;
import com.druciak.escorerapp.entities.MatchSettings;
import com.druciak.escorerapp.entities.Player;
import com.druciak.escorerapp.entities.TeamAdditionalMember;
import com.druciak.escorerapp.model.externalApiService.ExternalApiManager;
import com.druciak.escorerapp.model.firebaseService.Result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MatchSettingsPresenter implements IMatchSettingsMVP.IPresenter {
    private static final int MIN_COUNT_OF_PLAYERS = 6;

    private IMatchSettingsMVP.IModel externalManager;
    private IMatchSettingsMVP.IView view;
    private Match match;
    private LoggedInUser loggedInUser;
    private List<Player> players;
    private List<TeamAdditionalMember> members;
    private MatchSettings matchSettings;

    public MatchSettingsPresenter(IMatchSettingsMVP.IView view, Match match, LoggedInUser user) {
        this.externalManager = new ExternalApiManager(this);
        this.view = view;
        this.match = match;
        this.loggedInUser = user;
        matchSettings = new MatchSettings();
        members = new ArrayList<>();
    }

    @Override
    public void onPreparePlayersListEventCompleted(Result<List<Player>> result) {
        if (result instanceof Result.Success) {
            players = ((Result.Success<List<Player>>) result).getData();
            view.onPreparePlayerListsEventSucceeded(players);
        }
        else
            view.onPreparePlayerListEventFailed(((Result.Error) result).getError().getMessage());
    }

    @Override
    public void preparePlayersOfTeams(int hostId, int guestId) {
        externalManager.getAllPlayersOfTeams(hostId, guestId);
    }

    @Override
    public void addPlayer(Player player) {
        players.add(player);
    }

    @Override
    public void removePlayer(Player player) {
        players.remove(player);
    }

    @Override
    public void removeAdditionalMember(String name, int teamId, int memberId) {
        TeamAdditionalMember member = members.stream()
                .filter(teamAdditionalMember -> teamAdditionalMember.getName()
                .equals(name) && teamAdditionalMember.getTeamId() == teamId &&
                        teamAdditionalMember.getMemberTypeId() == memberId).findAny().get();
        members.remove(member);
    }

    @Override
    public void addAdditionalMember(String name, int teamId, int memberId) {
        members.add(new TeamAdditionalMember(name, teamId, memberId));
    }

    @Override
    public void onMatchStartClicked() {
        // check if all team have at least 6 players, 1 captain and
        // all players have number
        int teamHostCountMembers = (int) players.stream().filter(player -> player.getTeam().getId()
                == match.getHostTeam().getId()).count();

        int teamGuestCountMembers = (int) players.stream().filter(player -> player.getTeam().getId()
                == match.getGuestTeam().getId()).count();

        boolean isHostCaptain = players.stream().anyMatch(player -> player.isCaptain()
                && player.getTeam().getId() == match.getHostTeam().getId());
        boolean isGuestCaptain = players.stream().anyMatch(player -> player.isCaptain()
                && player.getTeam().getId() == match.getGuestTeam().getId());
        boolean allHostHaveNumber = players.stream()
                .filter(player -> player.getTeam().getId() == match.getHostTeam().getId())
                .allMatch(player -> player.getNumber() != 0);
        boolean allGuestHaveNumber= players.stream()
                .filter(player -> player.getTeam().getId() == match.getGuestTeam().getId())
                .allMatch(player -> player.getNumber() != 0);

        StringBuilder msgError = new StringBuilder();
        if (teamHostCountMembers < MIN_COUNT_OF_PLAYERS)
            msgError.append("Za mało zawodników w drużynie gospodarzy.\n\n");
        if (teamGuestCountMembers < MIN_COUNT_OF_PLAYERS)
            msgError.append("Za mało zawodników w drużynie gości.\n\n");
        if (!isHostCaptain)
            msgError.append("Nie wybrano kapitana w drużynie gospodarzy.\n\n");
        if (!isGuestCaptain)
            msgError.append("Nie wybrano kapitana w drużynie gości.\n\n");
        if (!allHostHaveNumber)
            msgError.append("Nie wszyscy zawodnicy w drużynie gospodarzy mają numery koszulek.\n\n");
        if (!allGuestHaveNumber)
            msgError.append("Nie wszyscy zawodnicy w drużynie gości mają numery koszulek.\n\n");

        if (msgError.length() > 0)
            msgError.delete(msgError.length()-2, msgError.length());

        String error = msgError.toString();

        if (error.equals("")) {
            matchSettings.setMatch(match);
            matchSettings.setPlayers(players);
            matchSettings.setMembers(members);
            view.startMatch(matchSettings, loggedInUser);
        }
        else
            view.showPopUpWithErrorMatchSettings(error);
    }

    @Override
    public void setMatchSettingsParams(String sTournamentName, String sType, boolean isZas, String
            sTown, String sStreet, String sHall, String sRefereeFirst, String sRefereeSnd,
                                       String sLine1, String sLine2, String sLine3,
                                       String sLine4, boolean isMan) {
        matchSettings.setTournamentName(sTournamentName);
        matchSettings.setType(sType);
        matchSettings.setZas(isZas);
        matchSettings.setTown(sTown);
        matchSettings.setStreet(sStreet);
        matchSettings.setHall(sHall);
        matchSettings.setRefereeFirst(sRefereeFirst);
        matchSettings.setRefereeSnd(sRefereeSnd);
        List<String> lineReferees = Arrays.asList(sLine1, sLine2, sLine3, sLine4);
        matchSettings.setLineReferees(lineReferees);
        matchSettings.setMan(isMan);
    }

    @Override
    public void onDiscardClicked() {
        view.goToMainPanel(loggedInUser);
    }
}
