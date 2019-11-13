package com.druciak.escorerapp.presenter;

import android.util.Log;

import com.druciak.escorerapp.entities.Action;
import com.druciak.escorerapp.entities.LineUp;
import com.druciak.escorerapp.entities.MatchInfo;
import com.druciak.escorerapp.entities.MatchPlayer;
import com.druciak.escorerapp.entities.MatchTeam;
import com.druciak.escorerapp.entities.PlayerPunishment;
import com.druciak.escorerapp.entities.Point;
import com.druciak.escorerapp.entities.Shift;
import com.druciak.escorerapp.entities.TeamAdditionalMember;
import com.druciak.escorerapp.entities.TeamPunishment;
import com.druciak.escorerapp.entities.Time;
import com.druciak.escorerapp.interfaces.IRunningMatchMVP;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.druciak.escorerapp.entities.MatchInfo.MATCH_END_POINTS;
import static com.druciak.escorerapp.entities.MatchInfo.MATCH_END_POINTS_IN_TIEBREAK;
import static com.druciak.escorerapp.entities.MatchInfo.MATCH_MIN_DIFFERENT_POINTS;
import static com.druciak.escorerapp.entities.MatchInfo.TIEBREAK_POINTS_TO_SHIFT;
import static com.druciak.escorerapp.entities.MatchPlayer.STATUS_PLAYER_NOT_TO_SHIFT;
import static com.druciak.escorerapp.entities.MatchPlayer.STATUS_PLAYER_ON_DESK;
import static com.druciak.escorerapp.entities.MatchPlayer.STATUS_PLAYER_SHIFTED;
import static com.druciak.escorerapp.view.runningMatch.RunningMatchActivity.LEFT_TEAM_ID;
import static com.druciak.escorerapp.view.runningMatch.RunningMatchActivity.RIGHT_TEAM_ID;

public class RunningMatchPresenter implements IRunningMatchMVP.IPresenter {
    private static final String ACTION_TAG = "ACTION";
    private static final String LINE_UP_NOT_SET = "Nie wprowadzono ustawień początkowych zespołów.";

    private IRunningMatchMVP.IView view;
    private MatchInfo matchInfo;
    private MatchTeam serveTeam;
    private MatchTeam leftTeam;
    private MatchTeam rightTeam;
    private int actualSet;
    private boolean canPlay;
    private boolean isTiebreak;
    private boolean isAfterShift;
    private double timeOfStartSet;

    public RunningMatchPresenter(IRunningMatchMVP.IView view, MatchInfo matchInfo) {
        this.view = view;
        this.matchInfo = matchInfo;
        this.serveTeam = matchInfo.getServeTeam();
        this.leftTeam = matchInfo.getTeamA();
        this.rightTeam = matchInfo.getTeamB();
        this.actualSet = 1;
        this.canPlay = false;
        this.isTiebreak = false;
        this.isAfterShift = false;
        this.timeOfStartSet = 0;
    }

    @Override
    public void onAttentionsClicked() {
        view.showPopUpWithAttentions(matchInfo.getAttentions());
    }

    @Override
    public void onReturnActionClicked() {
        if (canPlay){
            // todo
        } else
        {
            view.showToast(LINE_UP_NOT_SET);
        }
    }

    @Override
    public void onTeamsInfoClicked() {
        view.showTeamsInfo();
    }

    @Override
    public void onFinishMatchClicked() {

    }

    @Override
    public void onAddPointClicked(int teamSideId) {
        if (canPlay) {
            Action action = new Point(teamSideId == RIGHT_TEAM_ID ? rightTeam : leftTeam,
                    teamSideId == RIGHT_TEAM_ID ? leftTeam.getPoints() : rightTeam.getPoints());
            Log.d(ACTION_TAG, action.toString());
            updateMatchState(action);
            if (isTiebreak && !isAfterShift && (leftTeam.getPoints() == TIEBREAK_POINTS_TO_SHIFT ||
                    rightTeam.getPoints() == TIEBREAK_POINTS_TO_SHIFT))
            {
                changeTeamSides();
                setFieldsAfterFinishSet();
                changeLineUpAndTimesAfterChangeTeamSides();
                this.isAfterShift = true;
            }
        } else
        {
            view.showToast(LINE_UP_NOT_SET);
        }
    }

    private void changeLineUpAndTimesAfterChangeTeamSides() {
        for (int area : leftTeam.getLineUp().keySet())
        {
            MatchPlayer left = leftTeam.getLineUp().get(area);
            MatchPlayer right = rightTeam.getLineUp().get(area);
            view.updateAdapterWithPlayersLineUp(left, area, leftTeam.getTeamSideId());
            view.updateAdapterWithPlayersLineUp(right, area, rightTeam.getTeamSideId());
            view.resetTimes();
            for (int i=1; i<=leftTeam.getTimesCounter(); i++)
                view.addTimeFor(leftTeam.getTeamSideId(), i);
            for (int i=1; i<=rightTeam.getTimesCounter(); i++)
                view.addTimeFor(rightTeam.getTeamSideId(), i);
        }
    }

    private void changeTeamSides() {
        MatchTeam tmp = leftTeam;
        leftTeam = rightTeam;
        leftTeam.setTeamSideId(LEFT_TEAM_ID);
        rightTeam = tmp;
        rightTeam.setTeamSideId(RIGHT_TEAM_ID);
    }

    private void updateMatchState(Action action) {
        makeAction(action);
        matchInfo.addAction(actualSet, action);
        view.setScore(leftTeam.getPoints() + " : " + rightTeam.getPoints());

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
    }

    private void makeAction(Action action) {
        if (action instanceof Time){
            int teamId = action.getTeamMadeActionId();
            MatchTeam team = teamId == leftTeam.getTeamId() ? leftTeam : rightTeam;
            view.showTimeCountDown(team.getShortName());
            view.addTimeFor(teamId == leftTeam.getTeamId() ?
                    LEFT_TEAM_ID : RIGHT_TEAM_ID, team.getTimesCounter());
        } else if (action instanceof Shift) {
            int teamId = action.getTeamMadeActionId();
            MatchTeam team = teamId == leftTeam.getTeamId() ? leftTeam : rightTeam;
            view.makeChangeInAdapter(team.getPlayerByNumber(((Shift) action).getOutPlayerNb()),
                    team.getPlayerByNumber(((Shift) action).getEnterPlayerNb()),
                    teamId == leftTeam.getTeamId() ? LEFT_TEAM_ID : RIGHT_TEAM_ID);
        } else if (action instanceof LineUp) {
            LineUp lineUp = (LineUp) action;
            int teamId = action.getTeamMadeActionId();
            MatchTeam team = teamId == leftTeam.getTeamId() ? leftTeam : rightTeam;
            view.updateAdapterWithPlayersLineUp(team.getPlayerByNumber(lineUp.getEnterNb()),
                    lineUp.getAreaNb(), team.getTeamSideId());
            if (team.getIsLineUpSet())
                view.showPopUpWithConfirmLineUp(team);
        }
    }

    private void isEndOfSet() {
        double endOfSet = System.currentTimeMillis();
        int timeInMin = (int) Math.ceil((endOfSet - timeOfStartSet) / (1000 * 60));
        matchInfo.addTimeForSet(actualSet, timeInMin);
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
        MatchTeam team = teamId == leftTeam.getTeamId() ? leftTeam : rightTeam;
        team.makeShift();
    }

    private void isEndOfMatch() {
        double endOfSet = System.currentTimeMillis();
        int timeInMin = (int) Math.ceil((endOfSet - timeOfStartSet) / (1000 * 60));
        matchInfo.addTimeForSet(actualSet, timeInMin);
        view.showPopUpWithEndOf(getWinner().getFullName(), "Koniec meczu", true);
    }

    private void changeServeTeam() {
        serveTeam = serveTeam == leftTeam ? rightTeam : leftTeam;
    }

    private boolean checkIfIsEndOfMatch(){
        return getWinner().getSets() + 1 == matchInfo.getMinSetsToWin() ||
                matchInfo.isTiebreak(actualSet);
    }

    @Override
    public void onNextSetClicked() {
        if (checkIfIsEndOfMatch()){
            getWinner().addSet();
            view.moveToSummary(matchInfo);
        } else {
            isTiebreak = matchInfo.isTiebreak(++actualSet);
            if (isTiebreak) {
                view.showDrawActivity(matchInfo.getSettings());
            } else {
                changeTeamSides();
                serveTeam = getServeTeam(actualSet);
                if (!matchInfo.getServesOfSets().containsKey(actualSet))
                    matchInfo.addServeOfSet(actualSet, serveTeam.getTeamId());
                setTeamsParams();
                setFieldsAfterFinishSet();
            }
            canPlay = false;
            timeOfStartSet = 0;
            view.resetTimes();
            view.resetAdapters();
        }
    }

    private void setFieldsAfterFinishSet()
    {
        view.setScore(leftTeam.getPoints() + " : " + rightTeam.getPoints());
        view.setSets(leftTeam.getSets(), rightTeam.getSets());
        view.setFields(leftTeam.getFullName(), rightTeam.getFullName(),
                serveTeam == leftTeam ? LEFT_TEAM_ID : RIGHT_TEAM_ID);
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
                teamId == LEFT_TEAM_ID ? rightTeam.getPoints() : leftTeam.getPoints());
        Log.d(ACTION_TAG, action.toString());
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
        if (team.getIsLineUpSet())
        {
            view.showToast("Nie wprowadzono ustawienia drugiej drużyny");
        } else {
            view.showPopUpWithShift(team.getPlayers().stream()
                    .filter(player -> player.getStatusId() == STATUS_PLAYER_ON_DESK &&
                            !player.isLibero())
                    .collect(Collectors.toList()), adapterPosition, teamSideId, canPlay);
        }
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
        Log.d(ACTION_TAG, action.toString());
        updateMatchState(action);
    }

    @Override
    public void chosenPlayerToLineUp(Optional<MatchPlayer> player, MatchPlayer out,
                                     int areaNb, int teamSideId) {
        MatchTeam team = teamSideId == LEFT_TEAM_ID ? leftTeam : rightTeam;
        if (player.isPresent()) {
            Action action = new LineUp(team, player.get(), areaNb);
            Log.d(ACTION_TAG, action.toString());
            makeAction(action);
        } else {
            team.setLineUp(areaNb, null);
            view.updateAdapterWithPlayersLineUp(null, areaNb, teamSideId);
        }
        if (out != null)
            out.setStatusId(STATUS_PLAYER_ON_DESK);
    }

    @Override
    public void onConfirmLineUp(boolean isSetLineUp, MatchTeam team) {
        team.setIsLineUpSet(isSetLineUp);
        updateCanPlay();

        if (canPlay) {
            addLineUpActions(leftTeam);
            addLineUpActions(rightTeam);
        }
    }

    private void addLineUpActions(MatchTeam team) {
        Map<Integer, MatchPlayer> lineUp = team.getLineUp();
        for (int i = 0; i < lineUp.size(); i++)
        {
            MatchPlayer player = lineUp.get(i+1);
            Action action = new LineUp(team, player, i+1);
            Log.d(ACTION_TAG, action.toString());
            matchInfo.addAction(actualSet, action);
        }
    }

    @Override
    public void onActivityCreated() {
        view.setFields(leftTeam.getFullName(), rightTeam.getFullName(), serveTeam.getTeamSideId());
        view.setInfoFields(leftTeam, rightTeam);
    }

    @Override
    public void cancelChoosePlayerToLineUp(int teamSideId) {
        MatchTeam team = teamSideId == leftTeam.getTeamSideId() ? leftTeam : rightTeam;
        if (team.getIsLineUpSet())
            view.showPopUpWithConfirmLineUp(team);
    }

    @Override
    public void onCardClicked(int teamSideId, boolean isTeamPun) {
        if (canPlay) {
            if (isTeamPun) {
                view.showPopUpWithTeamPunish(teamSideId);
            } else {
                view.showPopUpWithMemberPunish(teamSideId == leftTeam.getTeamSideId() ? leftTeam :
                        rightTeam);
            }
        }
        else {
            view.showToast(LINE_UP_NOT_SET);
        }
    }

    @Override
    public void onPunishmentClicked(int teamSideId, int cardId) {
        MatchTeam team = teamSideId == leftTeam.getTeamSideId() ? leftTeam : rightTeam;
        Action action = new TeamPunishment(cardId, team, actualSet, getSecondTeam(team));
        Log.d(ACTION_TAG, action.toString());
        updateMatchState(action);
    }

    @Override
    public void onAttentionsSavedClicked(String attentions) {
        matchInfo.setAttentions(attentions);
    }

    @Override
    public void onDrawFinish(int serveTeamId, int leftTeamId) {
        MatchTeam left = leftTeam;
        MatchTeam right = rightTeam;
        leftTeam = leftTeamId == left.getId() ? left : right;
        rightTeam = leftTeamId == left.getId() ? right : left;
        serveTeam = leftTeam.getId() == serveTeamId ? leftTeam : rightTeam;
        if (!matchInfo.getServesOfSets().containsKey(actualSet))
            matchInfo.addServeOfSet(actualSet, serveTeam.getTeamId());
        leftTeam.setTeamSideId(LEFT_TEAM_ID);
        rightTeam.setTeamSideId(RIGHT_TEAM_ID);
        setTeamsParams();
        setFieldsAfterFinishSet();
    }

    @Override
    public void onPunishmentClicked(MatchTeam team, int cardId, TeamAdditionalMember member) {
        Action action = new PlayerPunishment(cardId, team, member, getSecondTeam(team), actualSet);
        Log.d(ACTION_TAG, action.toString());
        updateMatchState(action);
    }

    @Override
    public void onPunishmentClicked(MatchTeam team, int cardId, MatchPlayer player) {
        Action action = new PlayerPunishment(cardId, team, player, getSecondTeam(team), actualSet);
        Log.d(ACTION_TAG, action.toString());
        updateMatchState(action);
    }

    private void updateCanPlay() {
        canPlay = leftTeam.getIsLineUpSet() && rightTeam.getIsLineUpSet();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:ss");
        LocalDateTime now = LocalDateTime.now();
        matchInfo.getSettings().setStartTime(dtf.format(now));
        timeOfStartSet = System.currentTimeMillis();
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
