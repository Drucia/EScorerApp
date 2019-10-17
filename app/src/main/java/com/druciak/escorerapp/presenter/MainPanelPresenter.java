package com.druciak.escorerapp.presenter;

import com.druciak.escorerapp.interfaces.ILoginMVP;
import com.druciak.escorerapp.interfaces.IMainPanelMVP;
import com.druciak.escorerapp.model.entities.LoggedInUser;
import com.druciak.escorerapp.model.entities.NewUser;
import com.druciak.escorerapp.model.firebaseService.FirebaseManager;
import com.druciak.escorerapp.model.firebaseService.Result;
import com.druciak.escorerapp.model.internalApiService.InternalApiManager;
import com.google.firebase.auth.FirebaseUser;

public class MainPanelPresenter implements IMainPanelMVP.IPresenter {
    private IMainPanelMVP.IView view;
    private FirebaseUser user;
    private LoggedInUser loggedInUser;
    private ILoginMVP.IModel firebaseManager;
    private IMainPanelMVP.ILoggedInUserModel userManager;

    public MainPanelPresenter(IMainPanelMVP.IView view) {
        firebaseManager = new FirebaseManager(this);
        userManager = new InternalApiManager(this);
        this.view = view;
        this.user = ((Result.Success<FirebaseUser>) firebaseManager.getLoggedIn()).getData();
    }

    @Override
    public void logout() {
        firebaseManager.logout();
        view.onLogoutCompleted();
    }

    @Override
    public void createdActivity() {
        userManager.getUserInformation(user);
    }

    @Override
    public void onGetUserEventComplete(Result<LoggedInUser> user) {
        if (user instanceof Result.Success) {
            loggedInUser = ((Result.Success<LoggedInUser>) user).getData();
            view.setLoggedInUserFields(loggedInUser.getFullName(), loggedInUser.getEmail());
        }
        else
            view.showPopUpWithSetUserFields();
    }

    @Override
    public void onCustomUserFieldsSaveClicked(String name, String surname, String certificate, String class_) {
        userManager.setUserInformation(user, new NewUser(user.getUid(), name, surname, certificate, class_));
    }

    @Override
    public void onCustomUserFieldsSaveClicked(String name, String surname) {
        userManager.setUserInformation(user, new NewUser(user.getUid(), name, surname));
    }
}
