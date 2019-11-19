package com.druciak.escorerapp.model.internalApiService;

import com.druciak.escorerapp.entities.Team;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TeamService {
    @GET("teams/user/{userId}")
    Call<List<Team>> getTeamsOfUser(@Path("userId") String userId);
}
