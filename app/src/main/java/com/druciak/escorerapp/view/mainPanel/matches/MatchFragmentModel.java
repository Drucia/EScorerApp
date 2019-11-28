package com.druciak.escorerapp.view.mainPanel.matches;

import com.druciak.escorerapp.entities.MatchSummary;
import com.druciak.escorerapp.interfaces.IMatchFragmentMVP;
import com.druciak.escorerapp.model.firebaseService.Result;
import com.druciak.escorerapp.model.internalApiService.InternalApiManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchFragmentModel implements IMatchFragmentMVP.IModel {
    private String userId;
    private IMatchFragmentMVP.IPresenter presenter;
    private InternalApiManager manager;

    public MatchFragmentModel(String userId, IMatchFragmentMVP.IPresenter presenter) {
        this.userId = userId;
        this.presenter = presenter;
        manager = new InternalApiManager();
    }

    @Override
    public void prepareAllSummariesOfUserMatches() {
        manager.getSummaryService().getAllSummaries(userId).enqueue(new Callback<List<MatchSummary>>() {
            @Override
            public void onResponse(Call<List<MatchSummary>> call, Response<List<MatchSummary>> response) {
                List<MatchSummary> summaries = response.body();
                if (summaries != null && !summaries.isEmpty())
                    presenter.onPrepareMatchListEventCompleted(new Result.Success<>(summaries));
                else
                    presenter.onPrepareMatchListEventCompleted(new Result.Error(new Exception(response.message())));
            }

            @Override
            public void onFailure(Call<List<MatchSummary>> call, Throwable t) {
                presenter.onPrepareMatchListEventCompleted(new Result.Error(new Exception(t.getMessage())));
            }
        });
    }
}
