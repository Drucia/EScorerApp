package com.druciak.escorerapp.view.mainPanel.userData;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.druciak.escorerapp.R;

public class UserDataFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user_data, container, false);
        getActivity().setTitle(R.string.title_fragment_user_data);
        return root;
    }
}