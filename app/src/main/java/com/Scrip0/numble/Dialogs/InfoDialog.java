package com.Scrip0.numble.Dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.Scrip0.numble.CustomLayouts.Cell;
import com.Scrip0.numble.GameActivity;
import com.Scrip0.numble.R;

import java.util.Objects;

public class InfoDialog extends DialogFragment {
    @Override
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_info, null);
        builder.setView(dialogView)
                .setPositiveButton("", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Objects.requireNonNull(InfoDialog.this.getDialog()).cancel();
                    }
                })
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Objects.requireNonNull(InfoDialog.this.getDialog()).cancel();
                    }
                });
        initDialog(dialogView);
        return builder.create();
    }

    @SuppressLint({"UseSwitchCompatOrMaterialCode", "SetTextI18n"})
    private void initDialog(View dialogView) {
        TextView descriptionTextView = dialogView.findViewById(R.id.descriptionTextView);
        descriptionTextView.setText("Here are the rules:\n\nYou can use suggested numbers and symbols to guess the equation.\n\n" +
                "Your equation should be correct, contain one '=' sign and follow the standard order of operations.\n\n" +
                "Your goal is to guess randomly generated equation by typing yours and see how close you are to the final one.\n\nExample:");
        Cell cell1 = new Cell(dialogView.getContext());
        Cell cell2 = new Cell(dialogView.getContext());
        Cell cell3 = new Cell(dialogView.getContext());
        Cell cell4 = new Cell(dialogView.getContext());
        Cell cell5 = new Cell(dialogView.getContext());
        cell1.getEditText().setText("1");
        cell2.getEditText().setText("+");
        cell3.getEditText().setText("4");
        cell4.getEditText().setText("=");
        cell5.getEditText().setText("5");
        cell1.setBackground(Cell.RIGHT);
        cell3.setBackground(Cell.CLOSE);
        cell5.setBackground(Cell.WRONG);

        LinearLayout cellLayout = dialogView.findViewById(R.id.cellLayout);
        cellLayout.addView(cell1);
        cellLayout.addView(cell2);
        cellLayout.addView(cell3);
        cellLayout.addView(cell4);
        cellLayout.addView(cell5);
        int width = 130;
        cell1.setSize(width, width);
        cell2.setSize(width, width);
        cell3.setSize(width, width);
        cell4.setSize(width, width);
        cell5.setSize(width, width);

        TextView description2TextView = dialogView.findViewById(R.id.description2TextView);
        description2TextView.setText("1 is in the solution and in the correct spot.\n" +
                "4 is in the solution but in the wrong spot.\n" +
                "5 is not in the solution in any spot.");

    }
}
