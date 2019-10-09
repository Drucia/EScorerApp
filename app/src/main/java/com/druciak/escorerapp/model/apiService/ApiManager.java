package com.druciak.escorerapp.model.apiService;

import retrofit2.Retrofit;

public class ApiManager {
    private static final String BASE_DZPS_SERVER_URL = "";

    private Retrofit retrofit;

    private void initializeRetrofit(String serverUrl)
    {
        retrofit = new Retrofit.Builder()
                .baseUrl(serverUrl)
                .build();
    }

    public TeamService getTeamService()
    {
        initializeRetrofit(BASE_DZPS_SERVER_URL);
        return retrofit.create(TeamService.class);
    }

    public PlayerService getPlayerService()
    {
        initializeRetrofit(BASE_DZPS_SERVER_URL);
        return retrofit.create(PlayerService.class);
    }
}
