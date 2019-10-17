package com.druciak.escorerapp.model.internalApiService;

import com.druciak.escorerapp.interfaces.ICreateAccountMVP;
import com.druciak.escorerapp.interfaces.IMainPanelMVP;
import com.druciak.escorerapp.model.entities.LoggedInUser;
import com.druciak.escorerapp.model.entities.NewUser;
import com.druciak.escorerapp.model.entities.RefereeUser;
import com.druciak.escorerapp.model.firebaseService.Result;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InternalApiManager implements ICreateAccountMVP.IModel, IMainPanelMVP.ILoggedInUserModel {
    private static final String BASE_APP_SERVER_URL = "https://e-scorer-service.herokuapp.com/";
//    private static final String BASE_APP_SERVER_URL = "http://192.168.43.115:8080/";

    private Retrofit retrofit;
    private ICreateAccountMVP.IPresenter createAccountPresenter;
    private IMainPanelMVP.IPresenter mainPanelPresenter;

    public InternalApiManager(ICreateAccountMVP.IPresenter createAccountPresenter)
    {
        this.createAccountPresenter = createAccountPresenter;
    }

    public InternalApiManager(IMainPanelMVP.IPresenter mainPanelPresenter) {
        this.mainPanelPresenter = mainPanelPresenter;
    }

    private void initializeRetrofit()
    {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
        if (retrofit == null) {
            retrofit = new Retrofit
                    .Builder()
                    .baseUrl(BASE_APP_SERVER_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
    }

    private LoginService getLoginService()
    {
        initializeRetrofit();
        return retrofit.create(LoginService.class);
    }

    @Override
    public void createUser(NewUser user, final String email) {
        getLoginService().addUser(user).enqueue(new Callback<NewUser>() {
            @Override
            public void onResponse(Call<NewUser> call, Response<NewUser> response) {
                NewUser responseUser = response.body();
                if (responseUser != null)
                {
                    if (responseUser.getIsReferee())
                    {
                        RefereeUser refereeUser = new RefereeUser(responseUser, email);
                        createAccountPresenter.onCreateAccountEventComplete(new Result.Success(refereeUser));
                    } else
                    {
                        LoggedInUser user = new LoggedInUser(responseUser, email);
                        createAccountPresenter.onCreateAccountEventComplete(new Result.Success(user));
                    }
                }
                else
                    createAccountPresenter.onCreateAccountEventComplete(new Result.Error(new Exception(response.message())));
            }

            @Override
            public void onFailure(Call<NewUser> call, Throwable t) {
                createAccountPresenter.onCreateAccountEventComplete(new Result.Error(new Exception(t.getMessage())));
            }
        });
    }

    @Override
    public void getUserInformations(final FirebaseUser firebaseUser) {
        getLoginService().getUser(firebaseUser.getUid()).enqueue(new Callback<NewUser>() {
            @Override
            public void onResponse(Call<NewUser> call, Response<NewUser> response) {
                NewUser userInfo = response.body();
                if (userInfo != null)
                {
                    if (userInfo.getIsReferee())
                    {
                        RefereeUser refereeUser = new RefereeUser(userInfo, firebaseUser.getEmail());
                        mainPanelPresenter.onGetUserEventComplete(new Result.Success(refereeUser));
                    } else
                    {
                        LoggedInUser user = new LoggedInUser(userInfo, firebaseUser.getEmail());
                        mainPanelPresenter.onGetUserEventComplete(new Result.Success(user));
                    }
                }
                else
                    mainPanelPresenter.onGetUserEventComplete(new Result.Error(new Exception(response.message())));
            }

            @Override
            public void onFailure(Call<NewUser> call, Throwable t) {
                mainPanelPresenter.onGetUserEventComplete(new Result.Error(new Exception(t.getMessage())));
            }
        });
    }
}
