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
import com.druciak.escorerapp.entities.MatchSettings;
import com.druciak.escorerapp.interfaces.IMatchSettingsMVP;
import com.druciak.escorerapp.interfaces.ISaveData;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class MatchSettingsFragment extends Fragment implements ISaveData {
    private IMatchSettingsMVP.IView matchSettingsView;
    private MatchSettings matchSettings;

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
    private RadioButton fin;

    public MatchSettingsFragment(IMatchSettingsMVP.IView matchSettingsView,
                                 MatchSettings matchSettings) {
        this.matchSettingsView = matchSettingsView;
        this.matchSettings = matchSettings;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_match_settings, container, false);
        MaterialButton startMatch = root.findViewById(R.id.startMatchButton);
        tournamentName = root.findViewById(R.id.tournamentName);
        tournamentName.getEditText().setText(matchSettings.getTournamentName());
        town = root.findViewById(R.id.town);
        town.getEditText().setText(matchSettings.getTown());
        street = root.findViewById(R.id.street);
        street.getEditText().setText(matchSettings.getStreet());
        hall = root.findViewById(R.id.hall);
        hall.getEditText().setText(matchSettings.getHall());
        refereeFirst = root.findViewById(R.id.refereeFirst);
        refereeFirst.getEditText().setText(matchSettings.getRefereeFirst());
        refereeSnd = root.findViewById(R.id.refereeSnd);
        refereeSnd.getEditText().setText(matchSettings.getRefereeSnd());
        line1 = root.findViewById(R.id.line1);
        line2 = root.findViewById(R.id.line2);
        line3 = root.findViewById(R.id.line3);
        line4 = root.findViewById(R.id.line4);
        if (matchSettings.getLineReferees() != null) {
            line1.getEditText().setText(matchSettings.getLineReferees().get(0));
            line2.getEditText().setText(matchSettings.getLineReferees().get(1));
            line3.getEditText().setText(matchSettings.getLineReferees().get(2));
            line4.getEditText().setText(matchSettings.getLineReferees().get(3));
        }
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
        if (!matchSettings.isMan())
            sex.callOnClick();
        type = root.findViewById(R.id.spinnerType);
        String[] matchTypes = getResources().getStringArray(R.array.matchType);
        for (int i = 0; i < matchTypes.length; i++)
        {
            if (matchTypes[i].equals(matchSettings.getType())) {
                type.setSelection(i);
                break;
            }
        }
        zas = root.findViewById(R.id.radioButtonZas);
        fin = root.findViewById(R.id.radioButtonFin);

        if (matchSettings.isFin())
            fin.setChecked(true);
        else
            zas.setChecked(true);

        startMatch.setOnClickListener(view -> onMatchStartClicked());
        return root;
    }

    private void saveDataFromFields() {
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
        boolean isFin = fin.isChecked();
        String sType = type.getSelectedItem().toString();
        matchSettingsView.setMatchSettingsParams(sTournamentName, sType, isFin, sTown, sStreet,
                sHall, sRefereeFirst, sRefereeSnd, sLine1, sLine2, sLine3, sLine4, isMan);
    }

    private void onMatchStartClicked() {
        saveDataFromFields();
        matchSettingsView.onMatchStartClicked();
    }

    @Override
    public void save() {
        saveDataFromFields();
    }
}