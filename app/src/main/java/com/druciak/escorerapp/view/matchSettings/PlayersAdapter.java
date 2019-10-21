package com.druciak.escorerapp.view.matchSettings;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.interfaces.IMatchSettingsMVP;
import com.druciak.escorerapp.model.entities.Player;

import java.util.List;

public class PlayersAdapter extends RecyclerView.Adapter<PlayersAdapter.ViewHolder> {
    private List<Player> listItems;
    private IMatchSettingsMVP.IFragmentView callback;

    public PlayersAdapter(IMatchSettingsMVP.IFragmentView view, List<Player> players) {
        this.callback = view;
        this.listItems = players;
    }

    @NonNull
    @Override
    public PlayersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_row, parent, false);
        return new PlayersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayersAdapter.ViewHolder holder, int position) {
        Player player = listItems.get(position);
        holder.name.setText(player.getName());
        holder.surname.setText(player.getSurname());

        if (player.isCaptain())
            holder.number.setPaintFlags(holder.number.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        else
            holder.number.setPaintFlags(holder.number.getPaintFlags() & ~Paint.UNDERLINE_TEXT_FLAG);

        holder.number.setText(player.getNumber() == 0 ? "" : String.valueOf(player.getNumber()));

        if (player.isLibero())
            holder.shirt.setImageResource(R.drawable.libero_shirt);
        else
            holder.shirt.setImageResource(R.drawable.player_shirt);
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
                    callback.onPlayerClicked(listItems.get(getAdapterPosition()), getAdapterPosition()));
        }
    }
}
