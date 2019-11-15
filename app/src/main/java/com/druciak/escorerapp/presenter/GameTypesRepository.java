package com.druciak.escorerapp.presenter;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.entities.GameType;

import java.util.ArrayList;
import java.util.List;

public abstract class GameTypesRepository {
    public static final int DZPS_VOLLEYBALL_ID = 1;
    public static final int VOLLEYBALL_ID = 2;
    public static final int BEACH_VOLLEYBALL_ID = 3;

    public static List<GameType> getGameTypesListForOrganizer()
    {
        return new ArrayList<GameType>(){
            {
                add(new GameType(VOLLEYBALL_ID, "PIŁKA HALOWA",
                        R.drawable.volleyball, true));
                add(new GameType(BEACH_VOLLEYBALL_ID, "PIŁKA PLAŻOWA",
                        R.drawable.beach_volleyball, false));
            }
        };
    }

    public static List<GameType> getGameTypesListForReferee()
    {
        ArrayList<GameType> list = new ArrayList<>();
        list.add(new GameType(DZPS_VOLLEYBALL_ID,
                "", R.drawable.logo_dzps, true));
        list.addAll(getGameTypesListForOrganizer());
        return list;
    }
}
