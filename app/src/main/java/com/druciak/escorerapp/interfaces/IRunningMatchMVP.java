package com.druciak.escorerapp.interfaces;

import com.druciak.escorerapp.model.entities.MatchPlayer;

import java.util.List;
import java.util.Map;

public interface IRunningMatchMVP {
    interface IModel{

    }

    interface IView extends IOnPlayerTouchCallback{
        void makeShiftInLineUp(int teamSideId);
        void setScore(String score);
        void showPopUpForLineUp(String teamName, List<MatchPlayer> players, boolean isFirst);
        void setAdapterWithPlayersLineUp(Map<Integer, MatchPlayer> lineUpA, Map<Integer, MatchPlayer> lineUpB);
        void showPopUpWithEndOf(String winner, String title, boolean isEnd);
        void setFields(String fullNameLeft, String fullNameRight, int serveTeamId);
        void setSets(int setsLeft, int setsRight);
        void showPopUpWithInfo(String title, String msg, String positiveButton);
        void addTimeFor(int teamId, int timeCount);
        void showTimeCountDown(String teamName);
        void showPopUpWithConfirm(int teamId);
        void resetTimes();
        void showPopUpWithShift(List<MatchPlayer> players, int adapterPosition, int teamSideId);
        void makeChangeInAdapter(MatchPlayer playerOut, MatchPlayer playerIn, int teamSideId);
    }

    interface IPresenter{
        void onActivityCreated();
        void onFirstLineUpSet();
        void onSecondLineUpSet();
        void onAttentionsClicked();
        void onReturnActionClicked();
        void onTeamsInfoClicked();
        void onFinishMatchClicked();
        void onAddPointClicked(int lineUpId);
        void onNextSetClicked();
        void onTimeClicked(int teamId);
        void onTimeConfirmClicked(int teamId);
        void onPlayerClicked(MatchPlayer mPlayer, int adapterPosition, int teamSideId);
        void chosenPlayerToShift(MatchPlayer playerToShift, MatchPlayer player, int teamSideId);
    }
}
