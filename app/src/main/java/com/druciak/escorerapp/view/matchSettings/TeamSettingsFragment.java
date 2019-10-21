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
import com.druciak.escorerapp.model.entities.Team;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

import java.util.List;

public class TeamSettingsFragment extends Fragment implements IMatchSettingsMVP.IFragmentView{
    private static final int MAX_NUMBER_OF_COACH = 3;
    private static final int MAX_NUMBER_OF_MEDICINE = 2;
    private static final int MAX_NUMBER_OF_LIBERO = 2;
    private static final int MAX_NUMBER_OF_PLAYERS = 14;
    private static final int MAX_NUMBER_OF_PLAYERS_WITH_ONE_LIBERO = 12;
    private static final int MIN_NUMBER_OF_PLAYERS_FOR_LIBERO = 6;

    private int chipCoachCounter = 0;
    private int chipMedicineCounter = 0;
    private int liberoCounter = 0;
    private boolean isCaptainChosen = false;

    private PlayersAdapter playersAdapter;
    private List<Player> players;
    private RecyclerView playerRecycler;
    private IMatchSettingsMVP.IView matchSettingsView;
    private SpeedDialView speedDialView;
    private ChipGroup coachChipGroup;
    private ChipGroup medicineChipGroup;
    private Team team;

    public TeamSettingsFragment(IMatchSettingsMVP.IView mContext, Team team,
                                List<Player> playersOfHost) {
        matchSettingsView = mContext;
        this.team = team;
        players = playersOfHost;
        playersAdapter = new PlayersAdapter(this, players);
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
                new SpeedDialActionItem.Builder(R.id.coachFab, R.drawable.coach)
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
                    showPopUpForPlayer(null, -1);
                    break;
                case R.id.coachFab:
                    if (chipCoachCounter < MAX_NUMBER_OF_COACH)
                        showPopUpWithAddedTeamMemberFields(actionItem.getId(), "", -1);
                    else {
                        Toast.makeText(getActivity(), "Maksymalna liczba trenerów",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.medicineFab:
                    if (chipMedicineCounter < MAX_NUMBER_OF_MEDICINE)
                        showPopUpWithAddedTeamMemberFields(actionItem.getId(), "", -1);
                    else {
                        Toast.makeText(getActivity(), "Maksymalna liczba masażystów/lekarzy",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
            return false;
        });
        ((TextView) root.findViewById(R.id.teamName)).setText(team.getFullName());
        return root;
    }

    void showPopUpWithAddedTeamMemberFields(int id, String data, int chipId)
    {
        boolean isCoach = id == R.id.coachFab;
        boolean isNew = data.equals("");

        String title = isCoach ? "Trener" : "Masażysta";

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.pop_up_add_new_team_member, null);
        dialogBuilder.setView(view);
        dialogBuilder.setTitle(title);
        dialogBuilder.setCancelable(true);
        final TextInputLayout nameLayout = view.findViewById(R.id.textInputNameAdditionalMember);
        final TextInputLayout surnameLayout = view.findViewById(R.id.textInputSurnameAdditionalMember);

        if (!isNew)
        {
            String[] splitted = data.split(" ");
            nameLayout.getEditText().setText(splitted[0]);
            surnameLayout.getEditText().setText(splitted[1]);
        }

        dialogBuilder.setPositiveButton(isNew ? "Dodaj" : "Zmień", (dialogInterface, i) -> {
            String name = nameLayout.getEditText().getText().toString();
            String surname = surnameLayout.getEditText().getText().toString();
            if (isNew) {
                Chip chip = new Chip(isCoach ? coachChipGroup.getContext() : medicineChipGroup.getContext());
                chip.setText(name + " " + surname);
                chip.setChipBackgroundColorResource(R.color.colorPrimary);
                chip.setCloseIconVisible(true);
                chip.setOnCloseIconClickListener(chipView -> {
                    if (R.id.coachFab == id) {
                        coachChipGroup.removeView(chipView);
                        chipCoachCounter--;
                    }
                    else {
                        medicineChipGroup.removeView(chipView);
                        chipMedicineCounter--;
                    }
                });

                chip.setOnClickListener(chipView -> showPopUpWithAddedTeamMemberFields(id,
                        name + " " + surname, chipView.getId()));

                if (R.id.coachFab == id) {
                    coachChipGroup.addView(chip);
                    chipCoachCounter++;
                } else {
                    medicineChipGroup.addView(chip);
                    chipMedicineCounter++;
                }
            } else {
                Chip chip = isCoach ? coachChipGroup.findViewById(chipId)
                        : medicineChipGroup.findViewById(chipId);
                chip.setText(name + " " + surname);
            }

            dialogInterface.dismiss();
        });

        dialogBuilder.setNegativeButton("Anuluj", (dialogInterface, i) -> dialogInterface.cancel());

        dialogBuilder.create().show();
    }

    @Override
    public void onPlayerClicked(Player player, int adapterPosition) {
        showPopUpForPlayer(player, adapterPosition);
    }

    public void showPopUpForPlayer(Player player, int adapterPosition)
    {
        boolean isNew = player == null;

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.pop_up_player_fields, null);
        dialogBuilder.setView(view);
        dialogBuilder.setTitle("Zawodnik");
        dialogBuilder.setCancelable(true);
        TextInputLayout name = view.findViewById(R.id.textInputNamePlayer);
        TextInputLayout surname = view.findViewById(R.id.textInputSurnamePLayer);
        TextInputLayout number = view.findViewById(R.id.textInputNumberPlayer);
        SwitchMaterial captain = view.findViewById(R.id.switchCaptain);
        SwitchMaterial libero = view.findViewById(R.id.switchLibero);

        if (!isNew)
        {
            name.getEditText().setText(player.getName());
            surname.getEditText().setText(player.getSurname());
            number.getEditText().setText(player.getNumber() == 0 ? "" : String.valueOf(player.getNumber()));
            captain.setChecked(player.isCaptain());
            libero.setChecked(player.isLibero());
        }

        captain.setOnCheckedChangeListener((compoundButton, isOn) -> {
            if (isOn && isCaptainChosen) {
                Toast.makeText(getContext(), "Wybrano już kapitana", Toast.LENGTH_SHORT).show();
                captain.setChecked(false);
            } else if (isOn){
                if (libero.isChecked()) {
                    libero.setChecked(false);
                    liberoCounter--;
                }
                isCaptainChosen = true;
            } else {
                isCaptainChosen = false;
            }
        });

        libero.setOnCheckedChangeListener((compoundButton, isOn) -> {
            if (isOn && liberoCounter < MAX_NUMBER_OF_LIBERO) {
                libero.setChecked(true);
                liberoCounter++;
                if (captain.isChecked()) {
                    captain.setChecked(false);
                    isCaptainChosen = false;
                }
            } else if (isOn) {
                libero.setChecked(false);
                Toast.makeText(getContext(), "Masz za dużo libero", Toast.LENGTH_SHORT).show();
            }
        });

        dialogBuilder.setPositiveButton(isNew ? "Dodaj" : "Zmień", (dialogInterface, i) -> {
            String sName = name.getEditText().getText().toString();
            String sSurname = surname.getEditText().getText().toString();
            boolean bLibero = libero.isChecked();
            boolean bCaptain = captain.isChecked();
            String sNumber = number.getEditText().getText().toString();
            int iNumber = sNumber.equals("") ? 0 : Integer.valueOf(sNumber);

            // todo make check for empty values

            Player p;

            if (isNew)
            {
                p = new Player(sName, sSurname, team, bLibero, bCaptain, iNumber);
                int position = players.size();
                players.add(p);
                playersAdapter.notifyItemInserted(position);
                matchSettingsView.addPlayer(p);
            } else
            {
                p = players.get(adapterPosition);
                p.setName(sName);
                p.setSurname(sSurname);
                p.setLibero(bLibero);
                p.setCaptain(bCaptain);
                p.setNumber(iNumber);
                playersAdapter.notifyItemChanged(adapterPosition);
            }
            dialogInterface.dismiss();
        });

        if (!isNew)
        {
            dialogBuilder.setNegativeButton("Usuń", (dialogInterface, i) -> {
                final AlertDialog.Builder db = new AlertDialog.Builder(getActivity());
                db.setTitle("Usuwanie");
                db.setMessage("Jesteś pewny?");
                db.setNegativeButton("NIE", (dialogInterface1, i1) -> {dialogInterface1.cancel(); showPopUpForPlayer(player, adapterPosition);});
                db.setPositiveButton("TAK", (dialogInterface1, i1) -> {
                    players.remove(adapterPosition);
                    playersAdapter.notifyItemRemoved(adapterPosition);
                    matchSettingsView.removePlayer(player);
                    dialogInterface1.dismiss();
                });
                db.create().show();
            });
        }
        dialogBuilder.create().show();
    }

    public void updatePlayersAdapter(List<Player> players) {
        int size = playersAdapter.getItemCount();
        this.players.addAll(players);
        playersAdapter.notifyItemRangeChanged(size, players.size());
    }
}
