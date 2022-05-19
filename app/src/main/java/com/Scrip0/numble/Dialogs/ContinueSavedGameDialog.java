package com.Scrip0.numble.Dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.fragment.app.DialogFragment;

import com.Scrip0.numble.GameActivity;
import com.Scrip0.numble.R;

import java.util.Objects;

public class ContinueSavedGameDialog extends DialogFragment {
    @Override
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_saved_game, null);
        builder.setView(dialogView)
                .setPositiveButton("Start", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent gameIntent = new Intent(getActivity(), GameActivity.class);
                        gameIntent.putExtra("LoadSavedGame", true);
                        startActivity(gameIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Objects.requireNonNull(ContinueSavedGameDialog.this.getDialog()).cancel();
                    }
                });
        return builder.create();
    }
}
