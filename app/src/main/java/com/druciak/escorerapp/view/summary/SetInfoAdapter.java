package com.druciak.escorerapp.view.summary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.entities.SetInfo;

import java.util.List;

public class SetInfoAdapter extends RecyclerView.Adapter<SetInfoAdapter.ViewHolder> {
    private List<SetInfo> list;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView timesA;
        public TextView shiftsA;
        public TextView pointsA;
        public TextView set;
        public TextView pointsB;
        public TextView shiftsB;
        public TextView timesB;
        public ViewHolder(View view) {
            super(view);
            timesA = view.findViewById(R.id.timesA);
            timesB = view.findViewById(R.id.timesB);
            shiftsA = view.findViewById(R.id.shiftsA);
            shiftsB = view.findViewById(R.id.shiftsB);
            pointsA = view.findViewById(R.id.pointsA);
            pointsB = view.findViewById(R.id.pointsB);
            set = view.findViewById(R.id.setNb);
        }
    }

    public SetInfoAdapter(List<SetInfo> list) {
        this.list = list;
    }

    @Override
    public SetInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_summary_set, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SetInfo info = list.get(position);

        if (info == null)
        {
            holder.timesA.setText(R.string.times);
            holder.timesB.setText(R.string.times);
            holder.pointsA.setText(R.string.points);
            holder.pointsB.setText(R.string.points);
            holder.shiftsA.setText(R.string.shifts);
            holder.shiftsB.setText(R.string.shifts);
            holder.set.setText(R.string.set);
        } else {
            holder.timesA.setText(String.valueOf(info.getTimesA()));
            holder.timesB.setText(String.valueOf(info.getTimesB()));
            holder.pointsA.setText(String.valueOf(info.getPointsA()));
            holder.pointsB.setText(String.valueOf(info.getPointsB()));
            holder.shiftsA.setText(String.valueOf(info.getShiftsA()));
            holder.shiftsB.setText(String.valueOf(info.getShiftsB()));
            holder.set.setText(info.getSet() + " ( " + info.getTime() + " min )");
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
