package com.druciak.escorerapp.model.internalApiService;

import com.druciak.escorerapp.entities.Pair;
import com.druciak.escorerapp.entities.Player;
import com.druciak.escorerapp.entities.Team;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PlayerService {
    @GET("players/team/{teamId}")
    Call<List<Player>> getAllPlayersOfTeam(@Path("teamId") int teamId);

    @POST("players/team")
    Call<Pair<Team, Player>> saveWholeTeamWithPlayers(@Query("userId") String userId,
                                      @Body Pair<Team, Player> teamPlayerPair);
}
