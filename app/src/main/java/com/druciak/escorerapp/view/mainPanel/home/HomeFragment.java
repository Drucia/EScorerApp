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
        getActivity().setTitle(R.string.title_activity_main_panel);
        RecyclerView recyclerView = root.findViewById(R.id.recyclerCardView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        IMainPanelMVP.IView view = ((IMainPanelMVP.IView) getActivity());
        GameTypesAdapter adapter = new GameTypesAdapter(view, view.isRefereeUser() ?
                GameTypesRepository.getGameTypesListForReferee() :
                GameTypesRepository.getGameTypesListForOrganizer());
        recyclerView.setAdapter(adapter);
        return root;
    }
}