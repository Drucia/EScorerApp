package com.druciak.escorerapp.presenter;

import com.druciak.escorerapp.interfaces.ICreateAccountMVP;
import com.druciak.escorerapp.model.entities.LoggedInUser;
import com.druciak.escorerapp.model.firebaseService.FirebaseManager;
import com.druciak.escorerapp.model.firebaseService.Result;

public class CreateAccountPresenter implements ICreateAccountMVP.IPresenter {

    private ICreateAccountMVP.IView view;
    private ICreateAccountMVP.IFirebaseModel firebaseManager;

    public CreateAccountPresenter(ICreateAccountMVP.IView view)
    {
        this.view = view;
        firebaseManager = new FirebaseManager(this);
    }

    @Override
    public void clickedCreateAccount(String name, String surname, String email, String password,
                                     String certificate, String class_) {
        firebaseManager.signIn(name, surname, email, password, certificate, class_);
    }

    @Override
    public void clickedCreateAccount(String name, String surname, String email, String password) {
        firebaseManager.signIn(name, surname, email, password);
    }

    @Override
    public void onCreateAccountEventComplete(Result<LoggedInUser> loggedInUser) {
        if (loggedInUser instanceof Result.Success)
            view.onCreateAccountSuccess(((Result.Success<LoggedInUser>) loggedInUser).getData());
        else
            view.onCreateAccountFailed(((Result.Error) loggedInUser).getError().getMessage());
    }
}
