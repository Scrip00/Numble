package com.Scrip0.numble.Dialogs;

import static com.Scrip0.numble.ViewSavedGameActivity.generateShareText;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.DialogFragment;

import com.Scrip0.numble.Cells.CellManager;
import com.Scrip0.numble.CustomLayouts.Cell;
import com.Scrip0.numble.Database.HistoryDaoClass;
import com.Scrip0.numble.Database.HistoryDatabaseClass;
import com.Scrip0.numble.Database.HistoryModel;
import com.Scrip0.numble.GameActivity;
import com.Scrip0.numble.MainMenu;
import com.Scrip0.numble.R;

import java.util.Objects;

public class EndGameDialog extends DialogFragment {
    private CellManager manager;

    @Override
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_end_game, null);
        this.setCancelable(false);

        ImageView shareIcon = dialogView.findViewById(R.id.shareIcon);
        shareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HistoryDaoClass database = HistoryDatabaseClass.getDatabase(getActivity()).getDao();
                HistoryModel game = database.selectLast();
                Intent si = new Intent(Intent.ACTION_SEND);
                si.setType("text/plain");
                si.putExtra(Intent.EXTRA_TEXT, generateShareText(game, manager));
                startActivity(Intent.createChooser(si, "Choose Mail App"));
            }
        });

        builder.setView(dialogView)
                .setPositiveButton("New game", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent gameIntent = new Intent(getActivity(), GameActivity.class);
                        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                        int length = sharedPrefs.getInt("Length", 7);
                        int numTries = sharedPrefs.getInt("Numtries", 7);
                        boolean withMult = sharedPrefs.getBoolean("With_mult", true);
                        boolean withPower = sharedPrefs.getBoolean("With_power", true);
                        boolean withFact = sharedPrefs.getBoolean("With_fact", true);
                        gameIntent.putExtra("With_mult", withMult);
                        gameIntent.putExtra("With_power", withPower);
                        gameIntent.putExtra("With_fact", withFact);
                        gameIntent.putExtra("Length", length);
                        gameIntent.putExtra("Numtries", numTries);
                        startActivity(gameIntent);
                    }
                })
                .setNegativeButton("Menu", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Objects.requireNonNull(EndGameDialog.this.getDialog()).cancel();
                        Intent gameIntent = new Intent(getActivity(), MainMenu.class);
                        startActivity(gameIntent);
                    }
                });
        return builder.create();
    }

    public EndGameDialog(CellManager manager) {
        this.manager = manager;
    }
}
