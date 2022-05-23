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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.Scrip0.numble.Cells.CellManager;
import com.Scrip0.numble.CustomLayouts.Cell;
import com.Scrip0.numble.Database.HistoryDaoClass;
import com.Scrip0.numble.Database.HistoryDatabaseClass;
import com.Scrip0.numble.Database.HistoryModel;
import com.Scrip0.numble.GameActivity;
import com.Scrip0.numble.MainMenu;
import com.Scrip0.numble.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class EndGameDialog extends DialogFragment {
    private CellManager manager;
    private boolean won;

    @Override
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_end_game, null);
        this.setCancelable(false);

        initDialog(dialogView);

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

    private void initDialog(View dialogView) {
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

        TextView endGameTextView = dialogView.findViewById(R.id.endGameTextView);
        TextView statsText = dialogView.findViewById(R.id.statsText);
        ImageView endGameIcon = dialogView.findViewById(R.id.endGameIcon);
        if (won) {
            endGameTextView.setText("Congrats!");
            endGameIcon.setImageDrawable(ContextCompat.getDrawable(dialogView.getContext(), R.drawable.ic_cup));
            statsText.setText(generateStatsText(true));
        } else {
            endGameIcon.setImageDrawable(ContextCompat.getDrawable(dialogView.getContext(), R.drawable.ic_sad_guy));
            endGameTextView.setText("You lost");
            statsText.setText(generateStatsText(false));
        }

    }

    private String generateStatsText(boolean won) {
        String str = "";
        HistoryDaoClass database = HistoryDatabaseClass.getDatabase(getActivity()).getDao();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        String date = sdf.format(new Date());
        if (won) {
            str += "You already won " + database.getWonGames(date).size() + " games today!\n" + getPositiveComment();
        } else {
            str += "You already lost " + database.getLostGames(date).size() + " games today\n" + getNegativeComment();
        }
        return str;
    }

    public EndGameDialog(CellManager manager, boolean won) {
        this.manager = manager;
        this.won = won;
    }

    private String getNegativeComment() {
        String[] comments = new String[]{"Ur kinda bad tho", "Delete the game pls", "My mom plays better lol", "Maybe today is not your day", "C'mon u can do better"};
        return comments[getRandomNumber(0, comments.length)];
    }

    private String getPositiveComment() {
        String[] comments = new String[]{"U'r still not smarter than me", "Ok, not bad, maybe one more game??", "Nah these r cheats fr", "Maybe u should find a gf?"};
        return comments[getRandomNumber(0, comments.length)];
    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
