package com.druciak.escorerapp.view.mainPanel.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.interfaces.IMainPanelMVP;
import com.druciak.escorerapp.presenter.GameTypesAdapter;
import com.druciak.escorerapp.presenter.GameTypesRepository;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.recyclerCardView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        GameTypesAdapter adapter = new GameTypesAdapter((IMainPanelMVP.IView) getActivity(),
                GameTypesRepository.getGameTypesListForReferee());
        recyclerView.setAdapter(adapter);
        return root;
    }
}