package com.druciak.escorerapp.presenter;

import com.druciak.escorerapp.interfaces.IRunningMatchMVP;
import com.druciak.escorerapp.model.entities.Action;
import com.druciak.escorerapp.model.entities.MatchInfo;
import com.druciak.escorerapp.model.entities.MatchTeam;
import com.druciak.escorerapp.model.entities.Point;

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
        view.setFields(leftTeam.getTeam().getFullName(), rightTeam.getTeam().getFullName(),
                serveTeam == leftTeam ? LEFT_TEAM_ID : RIGHT_TEAM_ID);
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

        makeAction(action);
    }

    private void makeAction(Action action) {
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
                    view.makeShiftInLineUp(serveTeam == leftTeam ? LEFT_TEAM_ID : RIGHT_TEAM_ID);
                }
            }
        }
        view.setScore(leftTeam.getPoints() + " : " + rightTeam.getPoints());
        matchInfo.addAction(actualSet, action);
    }

    private void isEndOfSet() {
        view.showPopUpWithEndOf(getWinner().getTeam().getShortName(), "Koniec seta", false);
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
        view.showPopUpWithEndOf(getWinner().getTeam().getFullName(), "Koniec meczu", true);
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
        view.setFields(leftTeam.getTeam().getFullName(), rightTeam.getTeam().getFullName(),
                serveTeam == leftTeam ? LEFT_TEAM_ID : RIGHT_TEAM_ID);
    }

    private MatchTeam getServeTeam(int set) {
        MatchTeam first = matchInfo.getServeTeam();
        if (set % 2 == 0)
            return getSecondTeam(first);
        else
            return first;
    }

    private MatchTeam getSecondTeam(MatchTeam firstTeam) {
        if (firstTeam.getTeam().getId() == leftTeam.getTeam().getId())
            return rightTeam;
        else
            return leftTeam;
    }

    private void setTeamsParams() {
        getWinner().addSet();
        leftTeam.resetPoints();
        rightTeam.resetPoints();
    }

    private MatchTeam getWinner()
    {
        return leftTeam.getPoints() > rightTeam.getPoints() ? leftTeam : rightTeam;
    }
}
