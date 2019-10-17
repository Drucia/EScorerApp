package com.druciak.escorerapp.model.internalApiService;

import com.druciak.escorerapp.model.entities.NewUser;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface LoginService {
    @GET("users/{id}")
    Call<NewUser> getUser(@Path("id") String id);

    @POST("users/")
    Call<NewUser> updateUser(@Body NewUser user);
}

