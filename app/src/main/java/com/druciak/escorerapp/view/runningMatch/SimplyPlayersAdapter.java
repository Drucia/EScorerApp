package com.druciak.escorerapp.view.runningMatch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.entities.MatchPlayer;

import java.util.List;

public class SimplyPlayersAdapter extends RecyclerView.Adapter<SimplyPlayersAdapter.ViewHolder> {
    private List<MatchPlayer> list;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView number;
        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.playerName);
            number = view.findViewById(R.id.playerNb);
        }
    }

    public SimplyPlayersAdapter(List<MatchPlayer> list) {
        this.list = list;
    }

    @Override
    public SimplyPlayersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_simply_player, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MatchPlayer p = list.get(position);
        holder.name.setText(p.getSurname() + " " + p.getName());
        holder.number.setText(String.valueOf(p.getNumber()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
