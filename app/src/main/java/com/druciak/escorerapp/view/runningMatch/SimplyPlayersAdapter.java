package com.druciak.escorerapp.view.runningMatch;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.druciak.escorerapp.R;

import java.util.List;

public class SimplyPlayersAdapter extends RecyclerView.Adapter<SimplyPlayersAdapter.ViewHolder> {
    private List<String> list;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ViewHolder(TextView view) {
            super(view);
            textView = view;
        }
    }

    public SimplyPlayersAdapter(List<String> list) {
        this.list = list;
    }

    @Override
    public SimplyPlayersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        TextView view = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.text_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
