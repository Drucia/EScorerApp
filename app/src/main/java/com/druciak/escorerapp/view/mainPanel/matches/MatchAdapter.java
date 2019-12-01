package com.druciak.escorerapp.view.mainPanel.matches;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.entities.MatchSummary;
import com.druciak.escorerapp.entities.Team;

import java.util.ArrayList;

class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder> {
    private ArrayList<MatchSummary> items;
    private Context context;

    public MatchAdapter(Context context, ArrayList<MatchSummary> items) {
        this.items = items;
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_summary_card,
                parent, false);
        return new MatchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MatchSummary summary = items.get(position);
        Team hostTeam = summary.getMatch().getHostTeam();
        holder.hostName.setText(hostTeam.getFullName());
        holder.hostName.setTextColor(context.getColor(hostTeam.getId() == summary.getWinner().getId()
                ? R.color.light_green : R.color.red));
        Team guestTeam = summary.getMatch().getGuestTeam();
        holder.guestName.setText(guestTeam.getFullName());
        holder.guestName.setTextColor(context.getColor(guestTeam.getId() == summary.getWinner().getId()
                ? R.color.light_green : R.color.red));
        holder.score.setText(summary.getHostSets() + " : " + summary.getGuestSets());
        holder.date.setText(summary.getDate());
        holder.matchNb.setText("Mecz " + (position+1));
    }

    public void removeItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount() - position);
    }

    public void restoreItem(MatchSummary item, int position) {
        items.add(position, item);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, getItemCount() - position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView hostName;
        public TextView guestName;
        public TextView score;
        public TextView date;
        public TextView matchNb;
        public View foreground;
        public View background;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hostName = itemView.findViewById(R.id.hostTeam);
            guestName = itemView.findViewById(R.id.guestTeam);
            score = itemView.findViewById(R.id.score);
            date = itemView.findViewById(R.id.date);
            matchNb = itemView.findViewById(R.id.matchNb);
            foreground = itemView.findViewById(R.id.foreground);
            background = itemView.findViewById(R.id.background);
        }
    }
}
