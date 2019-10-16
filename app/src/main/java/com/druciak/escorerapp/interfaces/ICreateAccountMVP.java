package com.druciak.escorerapp.interfaces;

import com.druciak.escorerapp.model.entities.LoggedInUser;
import com.druciak.escorerapp.model.entities.NewUser;
import com.druciak.escorerapp.model.firebaseService.Result;

public interface ICreateAccountMVP {
    interface IPresenter{
        void clickedCreateAccount(String name, String surname, String email, String password,
                                  String certificate, String class_);
        void clickedCreateAccount(String name, String surname, String email, String password);
        void onCreateAccountEventComplete(Result<LoggedInUser> loggedInUserSuccess);
    }

    interface IView{
        void onCreateAccountSuccess(LoggedInUser data);
        void onCreateAccountFailed(String error);
    }

    interface IFirebaseModel{
        void signIn(String name, String surname, String email, String password,
                    String certificate, String class_);
        void signIn(String name, String surname, String email, String password);
    }
    interface IModel {
        void createUser(NewUser user, String email);
    }
}
