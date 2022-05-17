package com.Scrip0.numble;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import java.util.Objects;

public class StartNewGameDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_new_game, null);
        builder.setView(dialogView)
                .setPositiveButton("Start", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(getActivity(), MainActivity.class));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Objects.requireNonNull(StartNewGameDialog.this.getDialog()).cancel();
                    }
                });
        initializeSeekBar(dialogView.findViewById(R.id.seekBarLength), dialogView.findViewById(R.id.textViewSeekbarLength), 60);
        initializeSeekBar(dialogView.findViewById(R.id.seekBarNumtries), dialogView.findViewById(R.id.textViewSeekbarNumtries), 50);
        return builder.create();
    }

    private void initializeSeekBar(SeekBar seekBar, TextView textView, int max) {
        int step = 1;
        int min = 1;
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

        seekBar.setProgress(6);
    }

}
