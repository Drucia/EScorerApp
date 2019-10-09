package com.druciak.escorerapp.presenter.interfaces;

import com.druciak.escorerapp.model.firebaseService.Result;
import com.druciak.escorerapp.model.login.LoggedInUser;

public interface ILoginPresenterForManager {
    void onLoginEventComplete(Result<LoggedInUser> result);
}
