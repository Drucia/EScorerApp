package com.druciak.escorerapp.view.matchSettings;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.entities.Player;
import com.druciak.escorerapp.entities.Team;
import com.druciak.escorerapp.entities.TeamAdditionalMember;
import com.druciak.escorerapp.interfaces.IMatchSettingsMVP;
import com.druciak.escorerapp.interfaces.ISaveData;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TeamSettingsFragment extends Fragment implements IMatchSettingsMVP.IFragmentView,
        ISaveData {
    private static final int MAX_NUMBER_OF_COACH = 3;
    private static final int MAX_NUMBER_OF_MEDICINE = 2;
    private static final int MAX_NUMBER_OF_LIBERO = 2;
    private static final int MAX_NUMBER_OF_PLAYERS = 14;
    private static final int MAX_NUMBER_OF_PLAYERS_WITH_ONE_LIBERO = 12;
    private static final int MIN_NUMBER_OF_PLAYERS_FOR_LIBERO = 6;
    private static final int NO_CAPTAIN_CHOSEN = -1;

    private int chipCoachCounter = 0;
    private int chipMedicineCounter = 0;
    private List<Integer> liberoNumbers = new ArrayList<>();
    private int captainNumber = NO_CAPTAIN_CHOSEN;

    private PlayersAdapter playersAdapter;
    private ArrayList<Player> players;
    private IMatchSettingsMVP.IView matchSettingsView;
    private Context context;
    private ChipGroup coachChipGroup;
    private ChipGroup medicineChipGroup;
    private Team team;
    private TextView captainShirtNumber;
    private TextInputLayout teamName;
    private boolean isSimplyMatch;

    public TeamSettingsFragment(Context context, Team team,
                                List<Player> playersOfHost, boolean isSimplyMatch) {
        matchSettingsView = (IMatchSettingsMVP.IView) context;
        this.context = context;
        this.team = team;
        players = new ArrayList<>(playersOfHost);
        playersAdapter = new PlayersAdapter(this, players);
        this.isSimplyMatch = isSimplyMatch;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_team_setting_page, container, false);
        RecyclerView playerRecycler = root.findViewById(R.id.playersRecyclerView);
        playerRecycler.setAdapter(playersAdapter);
        playerRecycler.setLayoutManager(new GridLayoutManager(getContext(), 4));
        SpeedDialView speedDialView = root.findViewById(R.id.speedDial);
        coachChipGroup = root.findViewById(R.id.coachChips);
        medicineChipGroup = root.findViewById(R.id.medicineChips);
        captainShirtNumber = root.findViewById(R.id.capitanNumber);
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

        teamName = root.findViewById(R.id.teamName);
        teamName.getEditText().setText(team.getFullName());
        ImageView playerShirtImage = root.findViewById(R.id.playerShirtImage);
        playerShirtImage.setOnClickListener(view -> showPopUpWithColors());

        LinearLayout liberoLayout = root.findViewById(R.id.liberoLayout);
        LinearLayout captainLayout = root.findViewById(R.id.captainLayout);
        liberoLayout.setOnClickListener(view -> showFunctionsPopUp(false));
        captainLayout.setOnClickListener(view -> showFunctionsPopUp(true));
        return root;
    }

    private void showFunctionsPopUp(boolean isCaptainPopUp)
    {
        List<Player> playersWithNumbers = players.stream().filter(player -> player.getNumber() != 0)
                .sorted(Comparator.comparingInt(Player::getNumber)).collect(Collectors.toList());
        List<Integer> numbers = playersWithNumbers.stream()
                .map(Player::getNumber).sorted(Comparator.comparingInt(integer -> integer))
                .collect(Collectors.toList());

        if (isCaptainPopUp)
            showFunctionsPopUp(numbers.stream()
                    .filter(n -> !liberoNumbers.contains(n))
                    .collect(Collectors.toList()), true);
        else
            showFunctionsPopUp(numbers.stream()
                    .filter(n -> n != captainNumber)
                    .collect(Collectors.toList()), false);
    }

    private void showFunctionsPopUp(List<Integer> numbers, boolean isCaptainPopUp) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.pop_up_functions, null);
        dialogBuilder.setView(view);
        dialogBuilder.setTitle(isCaptainPopUp ? "Wybierz kapitana" : "Wybierz libero");
        dialogBuilder.setCancelable(true);
        ChipGroup group = view.findViewById(R.id.functionsChipGroup);
        group.setSingleSelection(isCaptainPopUp);
        group.setOnCheckedChangeListener((chipGroup, i) -> {
            changeCaptain(i);
        });

        for (Integer number : numbers)
        {
            // todo if will be time
            Chip newChip = (Chip) getLayoutInflater().inflate(R.layout.default_chip, group, false);
            newChip.setId(number);
            newChip.setText(number < 10 ? " " + number + " " : String.valueOf(number));
            group.addView(newChip);
            if (isCaptainPopUp && captainNumber == number){
                group.check(newChip.getId());
            }
        }

        dialogBuilder.setPositiveButton("Ok", (dialogInterface, i) -> {
            if (isCaptainPopUp) {
                Chip selected = view.findViewById(group.getCheckedChipId());
                if (selected != null) {
                    int number = Integer.valueOf(selected.getText().toString());
                    if (number != captainNumber) {
                        Player p = players.stream()
                                .filter(player -> player.getNumber() == number).findAny().get();
                        int idx = players.indexOf(p);
                        p.setCaptain(true);
                        playersAdapter.notifyItemChanged(idx);
                    }
                }
            } else {
                // todo make implementation for ok event
            }
            dialogInterface.dismiss();});

        dialogBuilder.create().show();
    }

    private void changeCaptain(int captainNumber) {
        int oldCap = this.captainNumber;
        if (oldCap != NO_CAPTAIN_CHOSEN){
            if (oldCap == captainNumber)
                return;
            Player oldCapP = players.stream()
                    .filter(player -> player.getNumber() == oldCap).findAny().get();
            oldCapP.setCaptain(false);
            playersAdapter.notifyItemChanged(players.indexOf(oldCapP));
            if (captainNumber == NO_CAPTAIN_CHOSEN)
                return;
        }

        Player newCap = players.stream()
                .filter(player -> player.getNumber() == captainNumber).findAny().get();
        newCap.setCaptain(true);
        playersAdapter.notifyItemChanged(players.indexOf(newCap));
        captainShirtNumber.setText(String.valueOf(captainNumber));
        this.captainNumber = captainNumber;
    }

    private void showPopUpWithColors() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.pop_up_colors, null);
        dialogBuilder.setView(view);
        dialogBuilder.setTitle("Wybierz kolor koszulki");
        dialogBuilder.setPositiveButton("Anuluj", (dialogInterface, i) -> {});
        // todo set on click
        dialogBuilder.create().show();
    }

    private void showPopUpWithAddedTeamMemberFields(int id, String data, int chipId)
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
                chip.setChipBackgroundColorResource(R.color.defaultColor);
                chip.setCloseIconVisible(true);
                chip.setOnCloseIconClickListener(chipView -> {
                    if (R.id.coachFab == id) {
                        coachChipGroup.removeView(chipView);
                        chipCoachCounter--;
                        matchSettingsView.removeAdditionalMember(chip.getText().toString(),
                                team.getId(), TeamAdditionalMember.COACH_MEMBER_ID);
                    }
                    else {
                        medicineChipGroup.removeView(chipView);
                        chipMedicineCounter--;
                        matchSettingsView.removeAdditionalMember(chip.getText().toString(),
                                team.getId(), TeamAdditionalMember.MEDICINE_MEMBER_ID);
                    }
                });

                chip.setOnClickListener(chipView -> showPopUpWithAddedTeamMemberFields(id,
                        name + " " + surname, chipView.getId()));

                if (R.id.coachFab == id) {
                    coachChipGroup.addView(chip);
                    chipCoachCounter++;
                    matchSettingsView.addAdditionalMember(chip.getText().toString(), team.getId(),
                            TeamAdditionalMember.COACH_MEMBER_ID);
                } else {
                    medicineChipGroup.addView(chip);
                    chipMedicineCounter++;
                    matchSettingsView.addAdditionalMember(chip.getText().toString(), team.getId(),
                            TeamAdditionalMember.MEDICINE_MEMBER_ID);
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
    public void onPlayerClicked(Object player, int adapterPosition, PlayersAdapter adapter) {
        showPopUpForPlayer((Player) player, adapterPosition);
    }

    private void showPopUpForPlayer(Player player, int adapterPosition)
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
            if (isOn && captainNumber != NO_CAPTAIN_CHOSEN) {
                Toast.makeText(getContext(), "Wybrano już kapitana", Toast.LENGTH_SHORT).show();
                captain.setChecked(false);
            } else if (isOn){
                String sNumber = number.getEditText().getText().toString();
                if (sNumber.equals("")) {
                    Toast.makeText(getContext(), "Wprowadź numer zawodnika", Toast.LENGTH_SHORT).show();
                    captain.setChecked(false);
                }
                else {
                    captain.setChecked(true);
                    Integer iNumber = Integer.valueOf(sNumber);
                    if (libero.isChecked()) {
                        libero.setChecked(false);
                        liberoNumbers.remove(iNumber);
                    }
                    captainNumber = iNumber;
                }
            } else {
                captainNumber = NO_CAPTAIN_CHOSEN;
            }
        });

        libero.setOnCheckedChangeListener((compoundButton, isOn) -> {
            if (isOn && liberoNumbers.size() < MAX_NUMBER_OF_LIBERO) { // if can be libero
                String sNumber = number.getEditText().getText().toString();
                if (sNumber.equals("")) {
                    libero.setChecked(false);
                    Toast.makeText(getContext(), "Wprowadź numer zawodnika", Toast.LENGTH_SHORT).show();
                } else
                {
                    libero.setChecked(true);
                    liberoNumbers.add(Integer.valueOf(sNumber));
                    if (captain.isChecked()) {
                        captain.setChecked(false);
                        captainNumber = NO_CAPTAIN_CHOSEN;
                    }
                }
            } else if (isOn) { // if too much libero
                libero.setChecked(false);
                Toast.makeText(getContext(), "Masz za dużo libero", Toast.LENGTH_SHORT).show();
            } else {
                liberoNumbers.remove(Integer.valueOf(number.getEditText().getText().toString()));
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
                db.setNegativeButton("NIE", (dialogInterface1, i1) -> {dialogInterface1.cancel();
                showPopUpForPlayer(player, adapterPosition);});
                db.setPositiveButton("TAK", (dialogInterface1, i1) -> {
                    players.remove(adapterPosition);
                    if (player.isLibero())
                        liberoNumbers.remove(player.getNumber());
                    if (player.isCaptain())
                        captainNumber = NO_CAPTAIN_CHOSEN;
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
        List<Player> playersOfTeam = players.stream()
                .filter(player -> player.getTeam().getId() == team.getId())
                .collect(Collectors.toList());
        int size = playersAdapter.getItemCount();
        this.players.addAll(playersOfTeam);
        playersAdapter.notifyItemRangeChanged(size, playersOfTeam.size());
    }

    @Override
    public void save() {
        if (isSimplyMatch)
            matchSettingsView.updateTeamName(teamName.getEditText().getText().toString(),
                    team.getId());
    }

    public void setTeam(Team team) {
        this.team = team;
        teamName.getEditText().setText(team.getFullName());
    }
}
