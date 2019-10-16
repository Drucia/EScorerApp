package com.druciak.escorerapp.model.externalApiService;

import com.druciak.escorerapp.model.entities.Team;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TeamService {
    @GET("teams")
    Call<List<Team>> getTeams();
}
