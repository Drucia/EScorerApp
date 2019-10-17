package com.druciak.escorerapp.view.matchSettings;

import android.graphics.Typeface;
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

import java.util.ArrayList;
import java.util.List;

public class PlayersAdapter extends RecyclerView.Adapter<PlayersAdapter.ViewHolder> {
    private List<Player> listItems = new ArrayList<>();
    private IMatchSettingsMVP.IView callback;

    public PlayersAdapter(IMatchSettingsMVP.IView view) {
        this.callback = view;
    }

    public List<Player> getListItems() {
        return listItems;
    }

    public void setListItems(List<Player> listItems) {
        this.listItems = listItems;
        this.notifyDataSetChanged();
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
            holder.number.setTypeface(holder.number.getTypeface(), Typeface.BOLD);
        else
            holder.number.setTypeface(holder.number.getTypeface(), Typeface.NORMAL);
        holder.number.setText(player.getNumber() == 0 ? "" : String.valueOf(player.getNumber()));
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
            playerContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.onPlayerClicked(listItems.get(getAdapterPosition()), getAdapterPosition());
                }
            });
        }
    }
}
