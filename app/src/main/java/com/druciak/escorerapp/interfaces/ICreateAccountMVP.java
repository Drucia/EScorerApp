package com.druciak.escorerapp.interfaces;

import com.druciak.escorerapp.model.entities.LoggedInUser;
import com.druciak.escorerapp.model.entities.NewUser;
import com.druciak.escorerapp.model.firebaseService.Result;

public interface ICreateAccountMVP {
    interface IPresenter{
        void clickedCreateAccount(String name, String surname, String email, String password,
                                  String certificate, String class_);
        void clickedCreateAccount(String name, String surname, String email, String password);
        void onCreateAccountEventComplete(Result loggedInUserSuccess);
    }

    interface IView{
        void onCreateAccountSuccess(LoggedInUser data);
        void onCreateAccountFailed(String error);
    }

    interface IModel{
        void signIn(NewUser newUser);
    }
}
