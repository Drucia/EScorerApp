package com.druciak.escorerapp.interfaces;

public interface IGenerateSheetMVP {
    interface IModel {
        void generateSheet();
    }

    interface IPresenter {
        void onActivityCreated();
    }

    interface IView {
        void showProgressBarPopUp();
    }
}
