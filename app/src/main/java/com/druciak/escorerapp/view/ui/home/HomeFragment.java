package com.druciak.escorerapp.view.ui.home;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.view.ui.interfaces.FragmentCommunicator;

public class HomeFragment extends Fragment {
    public static final int VOLLEYBALL_ID = 1;
    public static final int BEACH_VOLLEYBALL_ID = 2;

    FragmentCommunicator fragmentCommunicator;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        View volleyballCardView = root.findViewById(R.id.volleyballCard);
        volleyballCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentCommunicator.onCardClicked(HomeFragment.VOLLEYBALL_ID);
            }
        });
        return root;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            fragmentCommunicator = (FragmentCommunicator) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement FragmentCommunicator");
        }
    }
}