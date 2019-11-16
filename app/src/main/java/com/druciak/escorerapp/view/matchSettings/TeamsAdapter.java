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
    private boolean isHomeTeam;

    public TeamsAdapter(ITeamCallback callback, ArrayList<Team> teams, boolean isHomeTeam) {
        this.callback = callback;
        this.teams = teams;
        this.isHomeTeam = isHomeTeam;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_team,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(teams.get(position).getFullName());
        if (position == 0)
            holder.divider.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return teams.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public View divider;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.teamName);
            divider = itemView.findViewById(R.id.divider);
            name.setOnClickListener(view -> callback.onTeamClicked(
                    teams.get(getAdapterPosition()), isHomeTeam));
        }
    }
}
