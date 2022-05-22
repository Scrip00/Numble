package com.Scrip0.numble;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.GridLayout;

import com.Scrip0.numble.Animations.LoadingAnimator;
import com.Scrip0.numble.Cells.CellManager;
import com.Scrip0.numble.CustomLayouts.Cell;
import com.Scrip0.numble.CustomLayouts.Keyboard;
import com.Scrip0.numble.Database.HistoryDaoClass;
import com.Scrip0.numble.Database.HistoryDatabaseClass;
import com.Scrip0.numble.Database.HistoryModel;

import java.util.List;

public class ViewSavedGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_saved_game);

        int key = getIntent().getIntExtra("key", 0);

        GridLayout gridLayout = findViewById(R.id.grid_layout);
        LoadingAnimator loadingAnimator = findViewById(R.id.loading_animator);

        HistoryDaoClass database = HistoryDatabaseClass.getDatabase(getApplicationContext()).getDao();
        HistoryModel game = database.getData(key);

        CellManager manage = new CellManager(getBaseContext(), gridLayout, findViewById(R.id.relative_layout), loadingAnimator, initCellsFromDatabase(game.getCells()), game.getEquation(), new Keyboard(getBaseContext()));
    }

    private Cell[][] initCellsFromDatabase(char[][] c) {
        // Convert char array to cells array because it's impossible to implement in database converters (cells need context)
        Cell[][] cells = new Cell[c.length][c[0].length];
        for (int i = 0; i < c.length; i++) {
            for (int j = 0; j < c[0].length; j++) {
                cells[i][j] = new Cell(getBaseContext(), new Keyboard(getBaseContext()));
                cells[i][j].getEditText().setText(String.valueOf(c[i][j]));
            }
        }
        return cells;
    }
}