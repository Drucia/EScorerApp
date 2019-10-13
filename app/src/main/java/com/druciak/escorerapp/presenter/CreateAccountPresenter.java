package com.druciak.escorerapp.presenter;

import com.druciak.escorerapp.interfaces.ICreateAccountMVP;
import com.druciak.escorerapp.model.entities.LoggedInUser;
import com.druciak.escorerapp.model.entities.NewUser;
import com.druciak.escorerapp.model.firebaseService.FirebaseManager;
import com.druciak.escorerapp.model.firebaseService.Result;

public class CreateAccountPresenter implements ICreateAccountMVP.IPresenter {

    private ICreateAccountMVP.IView view;
    private ICreateAccountMVP.IModel model;

    public CreateAccountPresenter(ICreateAccountMVP.IView view)
    {
        this.view = view;
        this.model = new FirebaseManager(this);
    }

    @Override
    public void clickedCreateAccount(String name, String surname, String email, String password,
                                     String certificate, String class_) {
        NewUser newUser = new NewUser(name, surname, email, password, certificate, class_);
        model.signIn(newUser);
    }

    @Override
    public void clickedCreateAccount(String name, String surname, String email, String password) {
        NewUser newUser = new NewUser(name, surname, email, password);
        model.signIn(newUser);
    }

    @Override
    public void onCreateAccountEventComplete(Result loggedInUser) {
        if (loggedInUser instanceof Result.Success)
            view.onCreateAccountSuccess(((Result.Success<LoggedInUser>) loggedInUser).getData());
        else
            view.onCreateAccountFailed(((Result.Error) loggedInUser).getError().getMessage());
    }
}
