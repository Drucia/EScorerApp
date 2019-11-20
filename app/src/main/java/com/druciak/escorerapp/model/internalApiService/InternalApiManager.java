package com.druciak.escorerapp.model.internalApiService;

import com.druciak.escorerapp.entities.LoggedInUser;
import com.druciak.escorerapp.entities.NewUser;
import com.druciak.escorerapp.entities.RefereeUser;
import com.druciak.escorerapp.interfaces.ICreateAccountMVP;
import com.druciak.escorerapp.interfaces.IMainPanelMVP;
import com.druciak.escorerapp.interfaces.IMatchSettingsMVP;
import com.druciak.escorerapp.interfaces.IUserInfo;
import com.druciak.escorerapp.model.firebaseService.Result;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InternalApiManager implements ICreateAccountMVP.IModel, IMainPanelMVP.ILoggedInUserModel,
        IUserInfo.IModel, IMatchSettingsMVP {
    private static final String BASE_APP_SERVER_URL = "https://e-scorer-service.herokuapp.com/";

    private Retrofit retrofit;
    private ICreateAccountMVP.IPresenter createAccountPresenter;
    private IMainPanelMVP.IPresenter mainPanelPresenter;
    private IUserInfo.IPresenter userInfoPresenter;

    public InternalApiManager(ICreateAccountMVP.IPresenter createAccountPresenter)
    {
        this.createAccountPresenter = createAccountPresenter;
    }

    public InternalApiManager(IMainPanelMVP.IPresenter mainPanelPresenter) {
        this.mainPanelPresenter = mainPanelPresenter;
    }

    public InternalApiManager(IUserInfo.IPresenter userInfoPresenter) {
        this.userInfoPresenter = userInfoPresenter;
    }

    public InternalApiManager() {
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

    private UserService getUserService()
    {
        initializeRetrofit();
        return retrofit.create(UserService.class);
    }

    public TeamService getTeamService()
    {
        initializeRetrofit();
        return retrofit.create(TeamService.class);
    }

    public PlayerService getPlayerService()
    {
        initializeRetrofit();
        return retrofit.create(PlayerService.class);
    }

    public SummaryService getSummaryService()
    {
        initializeRetrofit();
        return retrofit.create(SummaryService.class);
    }

    @Override
    public void createUser(NewUser user, final String email) {
        getUserService().updateUser(user).enqueue(new Callback<NewUser>() {
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
                else {
                    createAccountPresenter.onCreateAccountEventComplete(new Result.Error(new Exception(response.message())));
                }
            }

            @Override
            public void onFailure(Call<NewUser> call, Throwable t) {
                createAccountPresenter.onCreateAccountEventComplete(new Result.Error(new Exception(t.getMessage())));
            }
        });
    }

//    @Override
//    public void getUserInformation(final FirebaseUser firebaseUser) {
////        getUserService().getUser(firebaseUser.getUid()).enqueue(new Callback<NewUser>() {
////            @Override
////            public void onResponse(Call<NewUser> call, Response<NewUser> response) {
////                NewUser userInfo = response.body();
////                if (userInfo != null)
////                {
////                    if (userInfo.getIsReferee())
////                    {
////                        RefereeUser refereeUser = new RefereeUser(userInfo, firebaseUser.getEmail());
////                        mainPanelPresenter.onGetUserEventComplete(new Result.Success(refereeUser));
////                    } else
////                    {
////                        LoggedInUser user = new LoggedInUser(userInfo, firebaseUser.getEmail());
////                        mainPanelPresenter.onGetUserEventComplete(new Result.Success(user));
////                    }
////                }
////                else
////                    mainPanelPresenter.onGetUserEventComplete(new Result.Error(new Exception(String.valueOf(response.code()))));
////            }
////
////            @Override
////            public void onFailure(Call<NewUser> call, Throwable t) {
////                    mainPanelPresenter.onGetUserEventComplete(new Result.Error(new Exception(t.getMessage())));
////            }
////        });
//    }

    @Override
    public void setUserInformation(final FirebaseUser firebaseUser, NewUser user) {
        getUserService().updateUser(user).enqueue(new Callback<NewUser>() {
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

    @Override
    public void getUserAdditionalInfo(FirebaseUser firebaseUser) {
        getUserService().getUser(firebaseUser.getUid()).enqueue(new Callback<NewUser>() {
            @Override
            public void onResponse(Call<NewUser> call, Response<NewUser> response) {
                NewUser userInfo = response.body();
                if (userInfo != null)
                {
                    if (userInfo.getIsReferee())
                    {
                        RefereeUser refereeUser = new RefereeUser(userInfo, firebaseUser.getEmail());
                        userInfoPresenter.onGetUserAdditionalInfoEventCompleteSuccess(new Result.Success(refereeUser));
                    } else
                    {
                        LoggedInUser user = new LoggedInUser(userInfo, firebaseUser.getEmail());
                        userInfoPresenter.onGetUserAdditionalInfoEventCompleteSuccess(new Result.Success(user));
                    }
                }
                else {
                    if (response.code() == HttpURLConnection.HTTP_NOT_FOUND)
                        userInfoPresenter.onGetUserAdditionalInfoEventCompleteFailure(new Result.Success(firebaseUser));
                    else
                        userInfoPresenter.onGetUserAdditionalInfoEventCompleteFailure(new Result.Error(new Exception(response.message())));
                }
            }

            @Override
            public void onFailure(Call<NewUser> call, Throwable t) {
                userInfoPresenter.onGetUserAdditionalInfoEventCompleteFailure(new Result.Error(new Exception("Błąd połączenia z serwerem")));
            }
        });
    }
}
