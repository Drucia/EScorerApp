package com.druciak.escorerapp.presenter;

import com.druciak.escorerapp.interfaces.IRunningMatchMVP;
import com.druciak.escorerapp.model.entities.Action;
import com.druciak.escorerapp.model.entities.LineUp;
import com.druciak.escorerapp.model.entities.MatchInfo;
import com.druciak.escorerapp.model.entities.MatchPlayer;
import com.druciak.escorerapp.model.entities.MatchTeam;
import com.druciak.escorerapp.model.entities.Point;
import com.druciak.escorerapp.model.entities.Shift;
import com.druciak.escorerapp.model.entities.Time;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.druciak.escorerapp.model.entities.MatchInfo.MATCH_END_POINTS;
import static com.druciak.escorerapp.model.entities.MatchInfo.MATCH_END_POINTS_IN_TIEBREAK;
import static com.druciak.escorerapp.model.entities.MatchInfo.MATCH_MIN_DIFFERENT_POINTS;
import static com.druciak.escorerapp.model.entities.MatchPlayer.STATUS_PLAYER_NOT_TO_SHIFT;
import static com.druciak.escorerapp.model.entities.MatchPlayer.STATUS_PLAYER_ON_DESK;
import static com.druciak.escorerapp.model.entities.MatchPlayer.STATUS_PLAYER_SHIFTED;
import static com.druciak.escorerapp.view.RunningMatchActivity.LEFT_TEAM_ID;
import static com.druciak.escorerapp.view.RunningMatchActivity.RIGHT_TEAM_ID;

public class RunningMatchPresenter implements IRunningMatchMVP.IPresenter {
    private static final String LINE_UP_NOT_SET = "Nie wprowadzono ustawień początkowych zespołów.";

    private IRunningMatchMVP.IView view;
    private MatchInfo matchInfo;
    MatchTeam serveTeam;
    MatchTeam leftTeam;
    MatchTeam rightTeam;
    int actualSet;
    boolean canPlay;

    public RunningMatchPresenter(IRunningMatchMVP.IView view, MatchInfo matchInfo) {
        this.view = view;
        this.matchInfo = matchInfo;
        this.serveTeam = matchInfo.getServeTeam();
        this.leftTeam = matchInfo.getTeamA();
        this.rightTeam = matchInfo.getTeamB();
        this.actualSet = 1;
        this.canPlay = false;
    }

    @Override
    public void onAttentionsClicked() {

    }

    @Override
    public void onReturnActionClicked() {
        if (canPlay){

        } else
        {
            view.showToast(LINE_UP_NOT_SET);
        }
    }

    @Override
    public void onTeamsInfoClicked() {

    }

    @Override
    public void onFinishMatchClicked() {

    }

    @Override
    public void onAddPointClicked(int teamSideId) {
        if (canPlay) {
            Action action = new Point(teamSideId == RIGHT_TEAM_ID ? rightTeam : leftTeam,
                    teamSideId == RIGHT_TEAM_ID ? leftTeam.getPoints() : rightTeam.getPoints());
            updateMatchState(action);
        } else
        {
            view.showToast(LINE_UP_NOT_SET);
        }
    }

    private void updateMatchState(Action action) {
        makeAction(action);
        if (checkIfIsEndOfSet()) {
            if (checkIfIsEndOfMatch()) {
                isEndOfMatch();
            } else {
                isEndOfSet();
            }
        }
        else {
            Optional<Integer> teamIdOpt = action.returnTeamIdIfIsPoint();
            if (teamIdOpt.isPresent()) {
                Integer teamId = teamIdOpt.get();
                if (serveTeam.getTeamId() != teamId) {
                    changeServeTeam();
                    makeShiftInLineUp(teamId);
                    view.makeShiftInLineUp(serveTeam.getTeamSideId());
                }
            }
        }
        view.setScore(leftTeam.getPoints() + " : " + rightTeam.getPoints());
        matchInfo.addAction(actualSet, action);
    }

    private void makeAction(Action action) {
        if (action instanceof Time){
            int teamId = ((Time) action).getTeamId();
            MatchTeam team = teamId == leftTeam.getTeamId() ? leftTeam : rightTeam;
            view.showTimeCountDown(team.getShortName());
            view.addTimeFor(teamId == leftTeam.getTeamId() ?
                    LEFT_TEAM_ID : RIGHT_TEAM_ID, team.getTimesCounter());
        } else if (action instanceof Shift) {
            int teamId = ((Shift) action).getTeamId();
            MatchTeam team = teamId == leftTeam.getTeamId() ? leftTeam : rightTeam;
            view.makeChangeInAdapter(team.getPlayerByNumber(((Shift) action).getOutPlayerNb()),
                    team.getPlayerByNumber(((Shift) action).getEnterPlayerNb()),
                    teamId == leftTeam.getTeamId() ? LEFT_TEAM_ID : RIGHT_TEAM_ID);
        } else if (action instanceof LineUp) {
            LineUp lineUp = (LineUp) action;
            int teamId = lineUp.getTeamId();
            MatchTeam team = teamId == leftTeam.getTeamId() ? leftTeam : rightTeam;
            view.updateAdapterWithPlayersLineUp(team.getPlayerByNumber(lineUp.getEnterNb()),
                    lineUp.getAreaNb(), team.getTeamSideId());
            if (team.getIsLineUpSet())
                view.showPopUpWithConfirmLineUp(team);
        }
    }

    private void isEndOfSet() {
        view.showPopUpWithEndOf(getWinner().getShortName(), "Koniec seta", false);
    }

    private boolean checkIfIsEndOfSet() {
        int different = Math.abs(leftTeam.getPoints() - rightTeam.getPoints());
        return (leftTeam.getPoints() >= (matchInfo.isTiebreak(actualSet) ?
                MATCH_END_POINTS_IN_TIEBREAK : MATCH_END_POINTS)
                || rightTeam.getPoints() >= (matchInfo.isTiebreak(actualSet) ?
                MATCH_END_POINTS_IN_TIEBREAK : MATCH_END_POINTS))
                && different >= MATCH_MIN_DIFFERENT_POINTS;
    }

    private void makeShiftInLineUp(Integer teamId) {
        // todo
    }

    private void isEndOfMatch() {
        view.showPopUpWithEndOf(getWinner().getFullName(), "Koniec meczu", true);
    }

    private void changeServeTeam() {
        serveTeam = serveTeam == leftTeam ? rightTeam : leftTeam;
    }

    private boolean checkIfIsEndOfMatch(){
        return getWinner().getSets() + 1 == matchInfo.getMinSetsToWin();
    }

    @Override
    public void onNextSetClicked() {
        setTeamsParams();
        MatchTeam tmp = leftTeam;
        leftTeam = rightTeam;
        rightTeam = tmp;
        serveTeam = getServeTeam(++actualSet);
        canPlay = false;
        view.setScore("0 : 0");
        view.setSets(leftTeam.getSets(), rightTeam.getSets());
        view.setFields(leftTeam.getFullName(), rightTeam.getFullName(),
                serveTeam == leftTeam ? LEFT_TEAM_ID : RIGHT_TEAM_ID);
        view.resetTimes();
    }

    @Override
    public void onTimeClicked(int teamId) {
        if (canPlay) {
            MatchTeam team = teamId == LEFT_TEAM_ID ? leftTeam : rightTeam;
            if (team.canGetTime()) {
                view.showPopUpWithConfirmTime(teamId);
            } else {
                view.showPopUpWithInfo("Uwaga", "Drużyna już nie ma czasów", "OK");
            }
        } else
        {
            view.showToast(LINE_UP_NOT_SET);
        }
    }

    @Override
    public void onTimeConfirmClicked(int teamId) {
        MatchTeam team = teamId == LEFT_TEAM_ID ? leftTeam : rightTeam;
        Action action = new Time(team,
                teamId == LEFT_TEAM_ID ? leftTeam.getPoints() : rightTeam.getPoints());
        updateMatchState(action);
    }

    @Override
    public void onPlayerClicked(MatchPlayer mPlayer, int adapterPosition, int teamSideId) {
        if (!canPlay){
            onPlayerClickedBeforeStartMatch(adapterPosition, teamSideId);
        } else {
            onPlayerClickedOnMatchDuring(mPlayer, adapterPosition, teamSideId);
        }
    }

    private void onPlayerClickedBeforeStartMatch(int adapterPosition,
                                                 int teamSideId) {
        MatchTeam team = teamSideId == LEFT_TEAM_ID ? leftTeam : rightTeam;
        view.showPopUpWithShift(team.getPlayers().stream()
                .filter(player -> player.getStatusId() == STATUS_PLAYER_ON_DESK &&
                        !player.isLibero())
                .collect(Collectors.toList()), adapterPosition, teamSideId, canPlay);
    }

    private void onPlayerClickedOnMatchDuring(MatchPlayer mPlayer, int adapterPosition,
                                              int teamSideId) {
        MatchTeam team = teamSideId == LEFT_TEAM_ID ? leftTeam : rightTeam;
        if (team.canGetShift()){
            int shiftNumber = mPlayer.getShiftNumber();
            if (mPlayer.getStatusId() == STATUS_PLAYER_NOT_TO_SHIFT){
                view.showPopUpWithInfo("Zmiana", "Zawodnik nie może być zmieniony.",
                        "OK");
            } else {
                List<MatchPlayer> players = team.getPlayers().stream()
                        .filter(player -> player.getStatusId() == STATUS_PLAYER_ON_DESK &&
                                !player.isLibero() && player.isCanPlay())
                        .collect(Collectors.toList());
                if (mPlayer.getStatusId() == STATUS_PLAYER_SHIFTED)
                {
                    players = team.getPlayers().stream()
                            .filter(player -> player.getNumber() == shiftNumber && player.isCanPlay())
                            .collect(Collectors.toList());
                }
                if (players.isEmpty())
                    view.showPopUpWithInfo("Zmiana", "Nie ma zawodników do zmiany.",
                            "OK");
                else
                    view.showPopUpWithShift(players, adapterPosition, teamSideId, canPlay);
            }
        } else
        {
            view.showPopUpWithInfo("Zmiana", "Zespół nie ma już zmian.", "OK");
        }
    }

    @Override
    public void chosenPlayerToShift(MatchPlayer playerToShift, MatchPlayer player, int teamSideId) {
        Action action = new Shift(player, playerToShift, teamSideId == LEFT_TEAM_ID ? leftTeam : rightTeam,
                teamSideId == LEFT_TEAM_ID ? rightTeam.getPoints() : leftTeam.getPoints());
        updateMatchState(action);
    }

    @Override
    public void chosenPlayerToLineUp(Optional<MatchPlayer> player, MatchPlayer out,
                                     int areaNb, int teamSideId) {
        MatchTeam team = teamSideId == LEFT_TEAM_ID ? leftTeam : rightTeam;
        if (player.isPresent()) {
            Action action = new LineUp(team, player.get(), areaNb);
            updateMatchState(action);
        } else {
            if (out != null)
                out.setStatusId(STATUS_PLAYER_ON_DESK);
            team.setLineUp(areaNb, null);
            view.updateAdapterWithPlayersLineUp(null, areaNb, teamSideId);
        }
    }

    @Override
    public void onConfirmLineUp(boolean isSetLineUp, MatchTeam team) {
        team.setIsLineUpSet(isSetLineUp);
        updateCanPlay();
    }

    @Override
    public void onActivityCreated() {
        view.setFields(leftTeam.getFullName(), rightTeam.getFullName(), serveTeam.getTeamSideId());
    }

    @Override
    public void cancelChoosePlayerToLineUp(int teamSideId) {
        MatchTeam team = teamSideId == leftTeam.getTeamSideId() ? leftTeam : rightTeam;
        if (team.getIsLineUpSet())
            view.showPopUpWithConfirmLineUp(team);
    }

    private void updateCanPlay() {
        canPlay = leftTeam.getIsLineUpSet() && rightTeam.getIsLineUpSet();
    }

    private MatchTeam getServeTeam(int set) {
        MatchTeam first = matchInfo.getServeTeam();
        if (set % 2 == 0)
            return getSecondTeam(first);
        else
            return first;
    }

    private MatchTeam getSecondTeam(MatchTeam firstTeam) {
        if (firstTeam.getTeamId() == leftTeam.getTeamId())
            return rightTeam;
        else
            return leftTeam;
    }

    private void setTeamsParams() {
        getWinner().addSet();
        leftTeam.nextSet();
        rightTeam.nextSet();
    }

    private MatchTeam getWinner()
    {
        return leftTeam.getPoints() > rightTeam.getPoints() ? leftTeam : rightTeam;
    }
}
