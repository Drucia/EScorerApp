package com.druciak.escorerapp.view.matchSettings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.interfaces.IMatchSettingsMVP;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class MatchSettingsFragment extends Fragment{
    private IMatchSettingsMVP.IView matchSettingsView;
    private TextInputLayout tournamentName;
    private TextInputLayout town;
    private TextInputLayout street;
    private TextInputLayout hall;
    private TextInputLayout refereeFirst;
    private TextInputLayout refereeSnd;
    private TextInputLayout line1;
    private TextInputLayout line2;
    private TextInputLayout line3;
    private TextInputLayout line4;
    private ImageView sex;
    private Spinner type;
    private RadioButton zas;

    public MatchSettingsFragment(IMatchSettingsMVP.IView matchSettingsView) {
        this.matchSettingsView = matchSettingsView;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_match_settings, container, false);
        MaterialButton startMatch = root.findViewById(R.id.startMatchButton);
        tournamentName = root.findViewById(R.id.tournamentName);
        town = root.findViewById(R.id.tournamentName);
        street = root.findViewById(R.id.street);
        hall = root.findViewById(R.id.hall);
        refereeFirst = root.findViewById(R.id.refereeFirst);
        refereeSnd = root.findViewById(R.id.refereeSnd);
        line1 = root.findViewById(R.id.line1);
        line2 = root.findViewById(R.id.line2);
        line3 = root.findViewById(R.id.line3);
        line4 = root.findViewById(R.id.line4);
        sex = root.findViewById(R.id.imageViewSex);
        sex.setOnClickListener(view -> {
            if (sex.getContentDescription().toString().equals("man")){
                sex.setImageResource(R.drawable.woman);
                sex.setContentDescription("woman");
            } else
            {
                sex.setImageResource(R.drawable.man);
                sex.setContentDescription("man");
            }
        });
        type = root.findViewById(R.id.spinnerType);
        zas = root.findViewById(R.id.radioButtonZas);
        startMatch.setOnClickListener(view -> onMatchStartClicked());
        return root;
    }

    private void onMatchStartClicked() {
        String sTournamentName = tournamentName.getEditText().getText().toString();
        String sTown = town.getEditText().getText().toString();
        String sStreet = street.getEditText().getText().toString();
        String sHall = hall.getEditText().getText().toString();
        String sRefereeFirst = refereeFirst.getEditText().getText().toString();
        String sRefereeSnd = refereeSnd.getEditText().getText().toString();
        String sLine1 = line1.getEditText().getText().toString();
        String sLine2 = line2.getEditText().getText().toString();
        String sLine3 = line3.getEditText().getText().toString();
        String sLine4 = line4.getEditText().getText().toString();
        boolean isMan = sex.getContentDescription().toString().equals("man");
        boolean isZas = zas.isChecked();
        String sType = type.getSelectedItem().toString();
        matchSettingsView.setMatchSettingsParams(sTournamentName, sType, isZas, sTown, sStreet,
                sHall, sRefereeFirst, sRefereeSnd, sLine1, sLine2, sLine3, sLine4, isMan);
        matchSettingsView.onMatchStartClicked();
    }
}