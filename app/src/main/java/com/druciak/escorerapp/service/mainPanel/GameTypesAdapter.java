package com.druciak.escorerapp.service.mainPanel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.interfaces.IMainPanelMVP;
import com.druciak.escorerapp.model.entities.GameType;

import java.util.List;

public class GameTypesAdapter extends RecyclerView.Adapter<GameTypesAdapter.ViewHolder> {
    private List<GameType> listItems;
    private IMainPanelMVP.IView gameTypesCallback;

    public GameTypesAdapter(IMainPanelMVP.IView gameTypesCallback, List<GameType> listItems) {
        this.listItems = listItems;
        this.gameTypesCallback = gameTypesCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_type_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameTypesAdapter.ViewHolder holder, int position) {
        holder.card.setAlpha((float) (listItems.get(position).isAvailable() ? 1 : 0.5));
        holder.background.setBackgroundResource(listItems.get(position).getImageId());
        holder.title.setText(listItems.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CardView card;
        public RelativeLayout background;
        public TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.cardView);
            background = itemView.findViewById(R.id.cardLayout);
            title = itemView.findViewById(R.id.cardTitle);
            background.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            gameTypesCallback.onClick(listItems.get(getAdapterPosition()).getId());
        }
    }
}
