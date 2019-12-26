package com.druciak.escorerapp.interfaces;

import android.content.Intent;
import android.view.View;

import com.druciak.escorerapp.entities.LoggedInUser;
import com.druciak.escorerapp.entities.Match;
import com.druciak.escorerapp.entities.MatchSummary;
import com.druciak.escorerapp.entities.NewUser;
import com.druciak.escorerapp.model.firebaseService.Result;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public interface IMainPanelMVP {
    interface IPresenter{
        void logout();
        void onGetUserEventComplete(Result<LoggedInUser> user);
        void onCustomUserFieldsSaveClicked(String name, String surname, String certificate,
                                           String class_);
        void onCustomUserFieldsSaveClicked(String name, String surname);
        void clickOnDZPSMatch();
        void onPrepareMatchesListCompleted(Result<List<Match>> result);
        void setLoggedInUser(LoggedInUser user);
        boolean isRefereeUser();
        void clickOnVolleyballMatch();
        String getUserId();
        LoggedInUser getLoggedInUser();
    }

    interface IView{
        void onClick(int gameId);
        void onLogoutCompleted();
        void showErrorMsgAndFinish(String msg);
        void setLoggedInUserFields(String fullName, String email);
        void showPopUpWithMatchToChoose(List<Match> matches, LoggedInUser user);
        boolean isRefereeUser();
        String getUserId();
        void goToMatchSettings(LoggedInUser loggedInUser);
        void goToDetails(String transitionName, MatchSummary matchSummary, View foreground);

        void goToDetails(Intent intent);
    }

    interface ILoggedInUserModel{
        void setUserInformation(FirebaseUser firebaseUser, NewUser user);
    }

    interface IMatchModel{
        void getMatchedForReferee(String refereeId);
    }
}
