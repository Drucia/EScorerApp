package com.druciak.escorerapp.presenter;

import com.druciak.escorerapp.model.firebaseService.LoginManager;
import com.druciak.escorerapp.presenter.interfaces.IMainPanelPresenter;
import com.druciak.escorerapp.view.mainPanel.IMainPanelView;

public class MainPanelPresenter implements IMainPanelPresenter {
    private IMainPanelView view;
    private LoginManager loginManager;

    public MainPanelPresenter(IMainPanelView view) {
        this.view = view;
        loginManager = new LoginManager();
    }

    @Override
    public void logout() {
        loginManager.logout();
        view.onLogoutCompleted();
    }
}
