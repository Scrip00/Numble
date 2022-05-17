package com.Scrip0.numble;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import java.util.Objects;

public class StartNewGameDialog extends DialogFragment {
    @Override
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_new_game, null);
        builder.setView(dialogView)
                .setPositiveButton("Start", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent gameIntent = new Intent(getActivity(), GameActivity.class);
                        TextView textViewSeekbarLength = dialogView.findViewById(R.id.textViewSeekbarLength);
                        TextView textViewSeekbarNumtries = dialogView.findViewById(R.id.textViewSeekbarNumtries);
                        gameIntent.putExtra("Length", Integer.parseInt(String.valueOf(textViewSeekbarLength.getText())));
                        gameIntent.putExtra("Numtries", Integer.parseInt(String.valueOf(textViewSeekbarNumtries.getText())));

                        Switch withMult = dialogView.findViewById(R.id.switchMult);
                        Switch withPower = dialogView.findViewById(R.id.switchPower);
                        Switch withFact = dialogView.findViewById(R.id.switchFact);
                        gameIntent.putExtra("With_mult", withMult.isChecked());
                        gameIntent.putExtra("With_power", withPower.isChecked());
                        gameIntent.putExtra("With_fact", withFact.isChecked());
                        startActivity(gameIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Objects.requireNonNull(StartNewGameDialog.this.getDialog()).cancel();
                    }
                });
        initializeSeekBar(dialogView.findViewById(R.id.seekBarLength), dialogView.findViewById(R.id.textViewSeekbarLength), 3, 60, 4);
        initializeSeekBar(dialogView.findViewById(R.id.seekBarNumtries), dialogView.findViewById(R.id.textViewSeekbarNumtries), 1, 50, 6);
        return builder.create();
    }

    private void initializeSeekBar(SeekBar seekBar, TextView textView, int min, int max, int initialProgress) {
        int step = 1;
        seekBar.setMax((max - min) / step);

        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        double value = min + (progress * step);
                        textView.setText(String.valueOf((int) value));
                    }
                }
        );

        seekBar.setProgress(initialProgress);
    }

}
