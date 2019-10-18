package com.druciak.escorerapp.view.matchSettings;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.interfaces.IMatchSettingsMVP;
import com.druciak.escorerapp.model.entities.Player;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

import java.util.List;

public class TeamSettingsFragment extends Fragment implements IMatchSettingsMVP.IFragmentView{
    private static final int MAX_NUMBER_OF_COACH = 3;
    private static final int MAX_NUMBER_OF_MEDICINE = 2;

    private int chipCoachCounter = 0;
    private int chipMedicineCounter = 0;

    private PlayersAdapter playersAdapter;
    private RecyclerView playerRecycler;
    private IMatchSettingsMVP.IView context;
    private SpeedDialView speedDialView;
    private ChipGroup coachChipGroup;
    private ChipGroup medicineChipGroup;
    private String teamName;

    public TeamSettingsFragment(IMatchSettingsMVP.IView mContext, String fullName,
                                List<Player> playersOfHost) {
        context = mContext;
        teamName = fullName;
        playersAdapter = new PlayersAdapter(this);
        playersAdapter.setListItems(playersOfHost);
        playersAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_team_setting_page, container, false);
        playerRecycler = root.findViewById(R.id.playersRecyclerView);
        playerRecycler.setAdapter(playersAdapter);
        playerRecycler.setLayoutManager(new GridLayoutManager(getContext(), 4));
        speedDialView = root.findViewById(R.id.speedDial);
        coachChipGroup = root.findViewById(R.id.coachChips);
        medicineChipGroup = root.findViewById(R.id.medicineChips);
        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.medicineFab, R.drawable.medicine)
                        .setLabel("Masażysta")
                        .setFabImageTintColor(ResourcesCompat.getColor(getResources(), R.color.black_overlay, getActivity().getTheme()))
                        .create());
        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.couachFab, R.drawable.coach)
                        .setLabel("Trener")
                        .setFabImageTintColor(ResourcesCompat.getColor(getResources(), R.color.black_overlay, getActivity().getTheme()))
                        .create());
        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.playerFab, R.drawable.player)
                        .setLabel("Zawodnik")
                        .setFabImageTintColor(ResourcesCompat.getColor(getResources(), R.color.black_overlay, getActivity().getTheme()))
                        .create());
        speedDialView.setOnActionSelectedListener(actionItem -> {
            switch (actionItem.getId()){
                case R.id.playerFab:
                    showPopUpWithAddedPlayerFields();
                    break;
                case R.id.couachFab:
                    if (chipCoachCounter < MAX_NUMBER_OF_COACH)
                        showPopUpWithAddedTeamMemberFields(actionItem.getId());
                    else {
                        Toast.makeText(getActivity(), "Maksymalna liczba trenerów",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.medicineFab:
                    if (chipMedicineCounter < MAX_NUMBER_OF_MEDICINE)
                        showPopUpWithAddedTeamMemberFields(actionItem.getId());
                    else {
                        Toast.makeText(getActivity(), "Maksymalna liczba masażystów/lekarzy",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
            return false;
        });
        ((TextView) root.findViewById(R.id.teamName)).setText(teamName);
        return root;
    }

    void showPopUpWithAddedPlayerFields()
    {

    }

    void showPopUpWithAddedTeamMemberFields(int id)
    {
        String title = id == R.id.couachFab ? "Trener" : "Masażysta";

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.pop_up_add_new_team_member, null);
        dialogBuilder.setView(view);
        dialogBuilder.setTitle(title);
        dialogBuilder.setCancelable(true);
        final TextInputLayout nameLayout = view.findViewById(R.id.textInputNameAdditionalMember);
        final TextInputLayout surnameLayout = view.findViewById(R.id.textInputSurnameAdditionalMember);

        dialogBuilder.setPositiveButton("Dodaj", (dialogInterface, i) -> {
            String name = nameLayout.getEditText().getText().toString();
            String surname = surnameLayout.getEditText().getText().toString();
            Chip chip = new Chip(R.id.couachFab == id ? coachChipGroup.getContext() : medicineChipGroup.getContext());
            chip.setText(name + " " + surname);
            chip.setChipBackgroundColorResource(R.color.chip_background);
            chip.setOnCloseIconClickListener(chipView -> {
                if (R.id.couachFab == id)
                    coachChipGroup.removeView(chipView);
                else
                    medicineChipGroup.removeView(chipView);
            });
            chip.setOnClickListener(chipView -> showPopUpWithAddedTeamMemberFields(id));
            if (R.id.couachFab == id){
                coachChipGroup.addView(chip);
                chipCoachCounter++;
            }
            else{
                medicineChipGroup.addView(chip);
                chipMedicineCounter++;
            }
            dialogInterface.dismiss();
        });

        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    public void updatePlayersAdapter(List<Player> players)
    {
        playersAdapter.setListItems(players);
        playersAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPlayerClicked(Player player, int adapterPosition) {
        // todo
    }
}
