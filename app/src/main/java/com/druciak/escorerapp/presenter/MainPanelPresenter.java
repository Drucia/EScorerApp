package com.druciak.escorerapp.presenter;

import android.os.Parcelable;

import com.druciak.escorerapp.interfaces.IMainPanelMVP;
import com.druciak.escorerapp.model.entities.LoggedInUser;
import com.druciak.escorerapp.model.firebaseService.FirebaseManager;

public class MainPanelPresenter implements IMainPanelMVP.IPresenter {
    private IMainPanelMVP.IView view;
    private FirebaseManager firebaseManager;
    private LoggedInUser user;

    public MainPanelPresenter(IMainPanelMVP.IView view, Parcelable user) {
        this.view = view;
        firebaseManager = new FirebaseManager();
        this.user =
    }

    @Override
    public void logout() {
        firebaseManager.logout();
        view.onLogoutCompleted();
    }
}
