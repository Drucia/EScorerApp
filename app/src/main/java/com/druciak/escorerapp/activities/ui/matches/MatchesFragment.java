package com.druciak.escorerapp.activities.ui.matches;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.druciak.escorerapp.R;

public class MatchesFragment extends Fragment {

    private MatchesViewModel matchesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        matchesViewModel =
                ViewModelProviders.of(this).get(MatchesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_matches, container, false);
        final TextView textView = root.findViewById(R.id.text_matches);
        matchesViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}