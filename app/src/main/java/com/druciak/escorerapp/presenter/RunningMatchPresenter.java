package com.druciak.escorerapp.presenter;

import com.druciak.escorerapp.interfaces.IRunningMatchMVP;
import com.druciak.escorerapp.model.entities.Action;
import com.druciak.escorerapp.model.entities.MatchInfo;
import com.druciak.escorerapp.model.entities.MatchTeam;
import com.druciak.escorerapp.model.entities.Point;
import com.druciak.escorerapp.model.entities.Time;

import static com.druciak.escorerapp.model.entities.MatchInfo.MATCH_END_POINTS;
import static com.druciak.escorerapp.model.entities.MatchInfo.MATCH_END_POINTS_IN_TIEBREAK;
import static com.druciak.escorerapp.model.entities.MatchInfo.MATCH_MIN_DIFFERENT_POINTS;
import static com.druciak.escorerapp.view.RunningMatchActivity.LEFT_TEAM_ID;
import static com.druciak.escorerapp.view.RunningMatchActivity.RIGHT_TEAM_ID;

public class RunningMatchPresenter implements IRunningMatchMVP.IPresenter {
    private IRunningMatchMVP.IView view;
    private MatchInfo matchInfo;
    MatchTeam serveTeam;
    MatchTeam leftTeam;
    MatchTeam rightTeam;
    int actualSet;

    public RunningMatchPresenter(IRunningMatchMVP.IView view, MatchInfo matchInfo) {
        this.view = view;
        this.matchInfo = matchInfo;
        this.serveTeam = matchInfo.getServeTeam();
        this.leftTeam = matchInfo.getTeamA();
        this.rightTeam = matchInfo.getTeamB();
        this.actualSet = 1;
    }

    @Override
    public void onActivityCreated() {
        view.showPopUpForLineUp(matchInfo.getTeamAName(MatchInfo.TEAM_A_ID),
                matchInfo.getPlayers(MatchInfo.TEAM_A_ID), true);
    }

    @Override
    public void onFirstLineUpSet() {
        view.showPopUpForLineUp(matchInfo.getTeamAName(MatchInfo.TEAM_B_ID),
                matchInfo.getPlayers(MatchInfo.TEAM_B_ID), false);
    }

    @Override
    public void onSecondLineUpSet() {
        view.setAdapterWithPlayersLineUp(matchInfo.getTeamA().getLineUp(), matchInfo.getTeamB().getLineUp());
        view.setFields(leftTeam.getFullName(), rightTeam.getFullName(), serveTeam.getTeamSideId());
    }

    @Override
    public void onAttentionsClicked() {

    }

    @Override
    public void onReturnActionClicked() {

    }

    @Override
    public void onTeamsInfoClicked() {

    }

    @Override
    public void onFinishMatchClicked() {

    }

    @Override
    public void onAddPointClicked(int teamSideId) {
        Action action = new Point(teamSideId == RIGHT_TEAM_ID ? rightTeam : leftTeam,
                teamSideId == RIGHT_TEAM_ID ? leftTeam.getPoints() : rightTeam.getPoints());
        updateMatchState(action);
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
            Integer teamId = action.returnTeamIdIfIsPoint();
            if (teamId != null) {
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
            view.showTimeCountDown(teamId == leftTeam.getTeamId() ?
                    leftTeam.getShortName() : rightTeam.getShortName());
            view.addTimeFor(teamId == leftTeam.getTeamId() ?
                    LEFT_TEAM_ID : RIGHT_TEAM_ID, teamId == leftTeam.getTeamId() ?
                    leftTeam.getTimesCounter() : rightTeam.getTimesCounter());
        }
    }

    private void isEndOfSet() {
        view.showPopUpWithEndOf(getWinner().getShortName(), "Koniec seta", false);
    }

    private boolean checkIfIsEndOfSet() {
        int different = Math.abs(leftTeam.getPoints() - rightTeam.getPoints());
        return (leftTeam.getPoints() >= (matchInfo.isTiebreak(actualSet) ? MATCH_END_POINTS_IN_TIEBREAK : MATCH_END_POINTS)
                || rightTeam.getPoints() >= (matchInfo.isTiebreak(actualSet) ? MATCH_END_POINTS_IN_TIEBREAK : MATCH_END_POINTS))
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
        view.setScore("0 : 0");
        view.setSets(leftTeam.getSets(), rightTeam.getSets());
        view.setFields(leftTeam.getFullName(), rightTeam.getFullName(),
                serveTeam == leftTeam ? LEFT_TEAM_ID : RIGHT_TEAM_ID);
        view.resetTimes();
    }

    @Override
    public void onTimeClicked(int teamId) {
        MatchTeam team = teamId == LEFT_TEAM_ID ? leftTeam : rightTeam;
        if (team.canGetTime()) {
            view.showPopUpWithConfirm(teamId);
        } else {
            view.showPopUpWithInfo("Uwaga", "Drużyna już nie ma czasów", "OK");
        }
    }

    @Override
    public void onTimeConfirmClicked(int teamId) {
        MatchTeam team = teamId == LEFT_TEAM_ID ? leftTeam : rightTeam;
        Action action = new Time(team,
                teamId == LEFT_TEAM_ID ? leftTeam.getPoints() : rightTeam.getPoints());
        updateMatchState(action);
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
