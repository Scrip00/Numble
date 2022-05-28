package com.Scrip0.numble;

import static com.Scrip0.numble.GameActivity.loadAd;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.Scrip0.numble.Animations.LoadingAnimator;
import com.Scrip0.numble.Cells.CellManager;
import com.Scrip0.numble.CustomLayouts.Cell;
import com.Scrip0.numble.CustomLayouts.Keyboard;
import com.Scrip0.numble.Database.HistoryDaoClass;
import com.Scrip0.numble.Database.HistoryDatabaseClass;
import com.Scrip0.numble.Database.HistoryModel;

public class ViewSavedGameActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_saved_game);

        loadAd(this);

        int key = getIntent().getIntExtra("key", 0);

        GridLayout gridLayout = findViewById(R.id.grid_layout);
        LoadingAnimator loadingAnimator = findViewById(R.id.loading_animator);

        HistoryDaoClass database = HistoryDatabaseClass.getDatabase(getApplicationContext()).getDao();
        HistoryModel game = database.getData(key);

        CellManager manager = new CellManager(getBaseContext(), gridLayout, findViewById(R.id.relative_layout), loadingAnimator, initCellsFromDatabase(game.getCells()), game.getEquation(), new Keyboard(getBaseContext()));

        TextView equation = findViewById(R.id.equation);
        equation.setText(game.getEquation());

        TextView numtries = findViewById(R.id.tries);
        numtries.setText(game.getCurrentRow() + " out of " + game.getCells().length);

        TextView time = findViewById(R.id.time);
        time.setText(game.getTime());

        Button shareBtn = findViewById(R.id.share_button);

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent si = new Intent(Intent.ACTION_SEND);
                si.setType("text/plain");
                si.putExtra(Intent.EXTRA_TEXT, generateShareText(game, manager));
                startActivity(Intent.createChooser(si, "Choose Mail App"));
            }
        });
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

    public static String generateShareText(HistoryModel game, CellManager manager) {
        StringBuilder str = new StringBuilder();
        str.append("Numble game\n");
        str.append(game.getEquation()).append("\n");
        str.append("Tries: ").append(game.getCurrentRow()).append(" out of ").append(game.getCells().length).append("\n");
        Cell[][] cells = manager.getCells();
        for (int i = 0; i < game.getCurrentRow(); i++) {
            for (int j = 0; j < cells[0].length; j++) {
                switch (cells[i][j].getBackgroundColor()) {
                    case Cell.DEFAULT:
                        str.append("⬜");
                        break;
                    case Cell.WRONG:
                        str.append("\uD83D\uDFE5");
                        break;
                    case Cell.CLOSE:
                        str.append("\uD83D\uDFE8");
                        break;
                    case Cell.RIGHT:
                        str.append("\uD83D\uDFE9");
                        break;
                }
            }
            str.append("\n");
        }
        str.deleteCharAt(str.length() - 1);
        return str.toString();
    }
}