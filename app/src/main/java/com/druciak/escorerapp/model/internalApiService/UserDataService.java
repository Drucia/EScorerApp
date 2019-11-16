package com.druciak.escorerapp.model.internalApiService;

import com.druciak.escorerapp.entities.Player;
import com.druciak.escorerapp.entities.Team;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserDataService {
    @GET("users/{id}/teams")
    Call<List<Team>> getUserTeams(@Path("id") String id);

    @GET("users/{id}/teams/{teamId}/players")
    Call<List<Player>> getUserSpecificTeamPlayers(@Path("id") String id, @Path("teamId") int teamId);
}
