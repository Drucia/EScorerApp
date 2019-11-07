package com.druciak.escorerapp.interfaces;

import com.druciak.escorerapp.model.entities.MatchPlayer;
import com.druciak.escorerapp.model.entities.MatchSettings;
import com.druciak.escorerapp.model.entities.MatchTeam;

import java.util.List;
import java.util.Optional;

public interface IRunningMatchMVP {
    interface IView extends IOnPlayerTouchCallback{
        void showPopUpWithConfirmLineUp(MatchTeam team);
        void makeShiftInLineUp(int teamSideId);
        void setScore(String score);
        void updateAdapterWithPlayersLineUp(MatchPlayer player, int areaNb, int teamSideId);

        void showPopUpWithEndOf(String winner, String title, boolean isEnd);
        void setFields(String fullNameLeft, String fullNameRight, int serveTeamId);
        void setSets(int setsLeft, int setsRight);
        void showPopUpWithInfo(String title, String msg, String positiveButton);
        void addTimeFor(int teamId, int timeCount);
        void showTimeCountDown(String teamName);
        void resetTimes();
        void showPopUpWithShift(List<MatchPlayer> players, int adapterPosition, int teamSideId,
                                boolean isOnMatch);
        void makeChangeInAdapter(MatchPlayer playerOut, MatchPlayer playerIn, int teamSideId);
        void showToast(String msg);
        void showPopUpWithConfirmTime(int teamId);
        void showPopUpWithAttentions(String attentions);
        void resetAdapters();
        void showPopUpWithTeamPunish(int teamSideId);
        void showPopUpWithMemberPunish(MatchTeam team);
        void setInfoFields(MatchTeam teamA, MatchTeam teamB);
        void showTeamsInfo();
        void showDrawActivity(MatchSettings settings);
    }

    interface IPresenter{
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
        void chosenPlayerToLineUp(Optional<MatchPlayer> player, MatchPlayer out,
                                  int areaNb, int teamSideId);
        void onConfirmLineUp(boolean isSetLineUp, MatchTeam team);
        void onActivityCreated();
        void cancelChoosePlayerToLineUp(int teamSideId);
        void onCardClicked(int teamSideId, boolean isTeamPun);
        void onPunishmentClicked(int teamSideId, int cardId);
        void onPunishmentClicked(MatchTeam team, int cardId, int memberId, int memberNb);
        void onAttentionsSavedClicked(String attentions);
        void onDrawFinish(int serveTeamId, int leftSideTeamId);
    }
}
