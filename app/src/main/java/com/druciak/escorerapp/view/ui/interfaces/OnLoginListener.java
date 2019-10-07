package com.druciak.escorerapp.view.ui.interfaces;

import com.druciak.escorerapp.viewModel.Result;
import com.druciak.escorerapp.viewModel.model.LoggedInUser;

public interface OnLoginListener {
    void onLoginEventComplete(Result<LoggedInUser> result);
}
