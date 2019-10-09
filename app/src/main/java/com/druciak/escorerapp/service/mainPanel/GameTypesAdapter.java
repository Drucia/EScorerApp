package com.druciak.escorerapp.service.mainPanel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.model.interfaces.GameTypesClickCallback;
import com.druciak.escorerapp.model.mainPanel.GameType;

import java.util.List;

public class GameTypesAdapter extends RecyclerView.Adapter<GameTypesAdapter.ViewHolder> {
    private List<GameType> listItems;
    private GameTypesClickCallback gameTypesClickCallback;

    public GameTypesAdapter(GameTypesClickCallback gameTypesClickCallback, List<GameType> listItems) {
        this.listItems = listItems;
        this.gameTypesClickCallback = gameTypesClickCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_type_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameTypesAdapter.ViewHolder holder, int position) {
        holder.background.setBackgroundResource(listItems.get(position).getImageId());
        holder.title.setText(listItems.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ConstraintLayout background;
        public TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            background = itemView.findViewById(R.id.cardLayout);
            title = itemView.findViewById(R.id.cardTitle);
            background.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            gameTypesClickCallback.onClick(listItems.get(getAdapterPosition()).getId());
        }
    }
}
