package com.druciak.escorerapp.view.runningMatch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.druciak.escorerapp.R;

import java.util.List;

public class StringAdapter extends RecyclerView.Adapter<StringAdapter.ViewHolder> {
    private List<String> list;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView number;
        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.playerName);
            number = view.findViewById(R.id.playerNb);
        }
    }

    public StringAdapter(List<String> list) {
        this.list = list;
    }

    @Override
    public StringAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_simply_player, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
