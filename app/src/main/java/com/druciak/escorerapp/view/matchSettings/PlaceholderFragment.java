package com.druciak.escorerapp.view.matchSettings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.druciak.escorerapp.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_LAYOUT_ID = "layout_id";

    public static PlaceholderFragment newInstance(int layoutId) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_LAYOUT_ID, layoutId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        int layout = R.layout.fragment_team_setting_page;
        if (getArguments() != null) {
            layout = getArguments().getInt(ARG_SECTION_LAYOUT_ID);
        }
        View root = inflater.inflate(layout, container, false);
        return root;
    }
}