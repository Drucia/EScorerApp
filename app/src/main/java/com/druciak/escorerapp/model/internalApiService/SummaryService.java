package com.druciak.escorerapp.model.internalApiService;

import com.druciak.escorerapp.entities.MatchSummary;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SummaryService {
    @POST("summaries/user/{userId}")
    Call<MatchSummary> saveSummary(@Path("userId") String userId, @Body MatchSummary matchSummary);

    @GET("summaries/user/{userId}")
    Call<List<MatchSummary>> getAllSummaries(@Path("userId") String userId);
}
