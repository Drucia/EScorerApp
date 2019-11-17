package com.druciak.escorerapp.view.matchSettings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.druciak.escorerapp.R;

public class TabLayoutAdapter {
    private static final int[] navIcons = {
            R.drawable.home_dark,
            R.drawable.person,
            R.drawable.edit_dark,
            R.drawable.rules_dark
    };
    private static final int[] navLabels = {
            R.string.tab_text_host,
            R.string.tab_text_guest,
            R.string.tab_text_rest,
            R.string.tab_text_rules
    };
    private static final int[] navIconsActive = {
            R.drawable.home_yellow,
            R.drawable.person_yellow,
            R.drawable.edit_yellow,
            R.drawable.rules_yellow
    };

    private Context context;

    public TabLayoutAdapter(Context context) {
        this.context = context;
    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.nav_bar, null);
        TextView tabTextView = view.findViewById(R.id.nav_label);
        ImageView tabImageView = view.findViewById(R.id.nav_icon);
        tabTextView.setText(navLabels[position]);
        tabImageView.setImageResource(navIcons[position]);
        return view;
    }

    public View getSelectedTabView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.nav_bar, null);
        TextView tabTextView = view.findViewById(R.id.nav_label);
        tabTextView.setText(navLabels[position]);
        tabTextView.setTextColor(context.getColor(R.color.colorAccent));
        tabTextView.setVisibility(View.VISIBLE);
        ImageView tabImageView = view.findViewById(R.id.nav_icon);
        tabImageView.setImageResource(navIconsActive[position]);
        return view;
    }
}
