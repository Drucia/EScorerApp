package com.druciak.escorerapp.view.matchSettings;

import android.graphics.Paint;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.interfaces.IOnPlayerTouchCallback;
import com.druciak.escorerapp.entities.MatchPlayer;
import com.druciak.escorerapp.entities.Player;

import java.util.ArrayList;
import java.util.Map;

import static com.druciak.escorerapp.view.runningMatch.RunningMatchActivity.LEFT_TEAM_ID;
import static com.druciak.escorerapp.view.runningMatch.RunningMatchActivity.leftLineUpConversion;
import static com.druciak.escorerapp.view.runningMatch.RunningMatchActivity.rightLineUpConversion;

public class PlayersAdapter extends RecyclerView.Adapter<PlayersAdapter.ViewHolder> {
    private ArrayList<? extends Player> listItems;
    private IOnPlayerTouchCallback callback;
    private int teamSideId;

    public PlayersAdapter(IOnPlayerTouchCallback view, ArrayList<? extends Player> players) {
        this.callback = view;
        this.listItems = players;
    }

    public int getTeamSideId() {
        return teamSideId;
    }

    public void setTeamSideId(int teamSideId) {
        this.teamSideId = teamSideId;
    }

    @NonNull
    @Override
    public PlayersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = listItems.get(0) instanceof MatchPlayer || listItems.get(0) == null
                ? R.layout.row_player_match : R.layout.row_player;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new PlayersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayersAdapter.ViewHolder holder, int position) {
        Player player = listItems.get(position);

        if (player != null) {

            holder.name.setText(player.getName());
            holder.surname.setText(player.getSurname());

            if (player.isCaptain())
                holder.number.setPaintFlags(holder.number.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            else
                holder.number.setPaintFlags(holder.number.getPaintFlags() & ~Paint.UNDERLINE_TEXT_FLAG);

            holder.number.setText(player.getNumber() == 0 ? "" : String.valueOf(player.getNumber()));

            if (player.isLibero())
                holder.shirt.setImageResource(R.drawable.libero_shirt);
            else {
                holder.shirt.setBackgroundResource(R.drawable.player_shirt);
            }
        } else {
            Map<Integer, Pair<Integer, String>> conversion = teamSideId == LEFT_TEAM_ID ?
                    leftLineUpConversion : rightLineUpConversion;
            holder.name.setText("");
            holder.surname.setText("");
            holder.shirt.setBackgroundResource(R.drawable.player_shirt);
            holder.number.setPaintFlags(holder.number.getPaintFlags() & ~Paint.UNDERLINE_TEXT_FLAG);
            holder.number.setText(conversion.get(position).second);
        }
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout playerContent;
        public TextView name;
        public TextView surname;
        public TextView number;
        public ImageView shirt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameTextView);
            surname = itemView.findViewById(R.id.surnameTextView);
            number = itemView.findViewById(R.id.numberTextView);
            shirt = itemView.findViewById(R.id.shirtImageView);
            playerContent = itemView.findViewById(R.id.playerLayout);
            playerContent.setOnClickListener(view ->
                    callback.onPlayerClicked(listItems.get(getAdapterPosition()),
                            getAdapterPosition(), PlayersAdapter.this));
        }
    }
}
