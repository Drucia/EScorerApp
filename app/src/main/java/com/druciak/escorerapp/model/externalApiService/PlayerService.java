package com.druciak.escorerapp.model.externalApiService;

import com.druciak.escorerapp.entities.Player;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlayerService {
    @GET("players")
    Call<List<Player>> getPlayersOfTeam(@Query("partOfTeam") int teamId);
}
