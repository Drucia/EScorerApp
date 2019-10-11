package com.druciak.escorerapp.presenter;

import com.druciak.escorerapp.interfaces.IMainPanelMVP;
import com.druciak.escorerapp.model.firebaseService.LoginManager;

public class MainPanelPresenter implements IMainPanelMVP.IPresenter {
    private IMainPanelMVP.IView view;
    private LoginManager loginManager;

    public MainPanelPresenter(IMainPanelMVP.IView view) {
        this.view = view;
        loginManager = new LoginManager();
    }

    @Override
    public void logout() {
        loginManager.logout();
        view.onLogoutCompleted();
    }
}
