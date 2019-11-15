package com.druciak.escorerapp.presenter;

import com.druciak.escorerapp.interfaces.ILoginMVP;
import com.druciak.escorerapp.interfaces.IMainPanelMVP;
import com.druciak.escorerapp.entities.LoggedInUser;
import com.druciak.escorerapp.entities.Match;
import com.druciak.escorerapp.entities.NewUser;
import com.druciak.escorerapp.model.externalApiService.ExternalApiManager;
import com.druciak.escorerapp.model.firebaseService.FirebaseManager;
import com.druciak.escorerapp.model.firebaseService.Result;
import com.druciak.escorerapp.model.internalApiService.InternalApiManager;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MainPanelPresenter implements IMainPanelMVP.IPresenter {
    private IMainPanelMVP.IView view;
    private FirebaseUser user;
    private LoggedInUser loggedInUser;
    private ILoginMVP.IModel firebaseManager;
    private IMainPanelMVP.ILoggedInUserModel userManager;
    private IMainPanelMVP.IMatchModel externalManager;

    public MainPanelPresenter(IMainPanelMVP.IView view) {
        firebaseManager = new FirebaseManager();
        userManager = new InternalApiManager(this);
        externalManager = new ExternalApiManager(this);
        this.view = view;
        this.user = ((Result.Success<FirebaseUser>) firebaseManager.getLoggedIn()).getData();
    }

    @Override
    public void logout() {
        firebaseManager.logout();
        view.onLogoutCompleted();
    }

    @Override
    public void onGetUserEventComplete(Result<LoggedInUser> user) {
        if (user instanceof Result.Success) {
            loggedInUser = ((Result.Success<LoggedInUser>) user).getData();
            view.setLoggedInUserFields(loggedInUser.getFullName(), loggedInUser.getEmail());
        }
        else
            view.showErrorMsgAndFinish("Błąd połączenia z serwerem");
    }

    @Override
    public void onCustomUserFieldsSaveClicked(String name, String surname, String certificate, String class_) {
        userManager.setUserInformation(user, new NewUser(user.getUid(), name, surname, certificate, class_));
    }

    @Override
    public void onCustomUserFieldsSaveClicked(String name, String surname) {
        userManager.setUserInformation(user, new NewUser(user.getUid(), name, surname));
    }

    @Override
    public void clickOnDZPSMatch() {
        externalManager.getMatchedForReferee(user.getUid());
    }

    @Override
    public void onPrepareMatchesListCompleted(Result<List<Match>> result) {
        if (result instanceof Result.Success) {
            view.showPopUpWithMatchToChoose(((Result.Success<List<Match>>) result).getData(),
                    loggedInUser);
        }
    }

    @Override
    public void setLoggedInUser(LoggedInUser user) {
        this.loggedInUser = user;
        view.setLoggedInUserFields(user.getFullName(), user.getEmail());
    }

    @Override
    public boolean isRefereeUser() {
        return loggedInUser.getReferee();
    }

    @Override
    public void clickOnVolleyballMatch() {
        view.goToMatchSettings(loggedInUser);
    }
}
