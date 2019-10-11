package com.druciak.escorerapp.interfaces;

public interface IMainPanelMVP {
    interface IPresenter{
        void logout();
    }

    interface IView{
        void onClick(int gameId);

        void onLogoutCompleted();
    }

    interface IModel{

    }
}
