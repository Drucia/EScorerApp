package com.druciak.escorerapp.view.matchSettings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.entities.Team;
import com.druciak.escorerapp.interfaces.ITeamCallback;

import java.util.ArrayList;

class TeamsAdapter extends RecyclerView.Adapter<TeamsAdapter.ViewHolder> {
    private ITeamCallback callback;
    private ArrayList<Team> teams;

    public TeamsAdapter(ITeamCallback teamSettingsFragment) {
        callback = teamSettingsFragment;
        teams = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_simply_player,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(teams.get(position).getFullName());
    }

    @Override
    public int getItemCount() {
        return teams.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.playerName);
            name.setOnClickListener(view -> callback.onTeamClicked(
                    teams.get(getAdapterPosition())
            ));
        }
    }
}
