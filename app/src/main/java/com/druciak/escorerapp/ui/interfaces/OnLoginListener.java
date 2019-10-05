package com.druciak.escorerapp.ui.interfaces;

import com.druciak.escorerapp.data.Result;
import com.druciak.escorerapp.data.model.LoggedInUser;

public interface OnLoginListener {
    void onLoginEventComplete(Result<LoggedInUser> result);
}
