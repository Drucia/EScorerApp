package com.druciak.escorerapp.interfaces;

import com.druciak.escorerapp.view.matchSettings.PlayersAdapter;

public interface IOnPlayerTouchCallback {
    void onPlayerClicked(Object player, int adapterPosition, PlayersAdapter adapter);
}
