package com.Scrip0.numble;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Scrip0.numble.Database.HistoryDaoClass;
import com.Scrip0.numble.Database.HistoryDatabaseClass;
import com.Scrip0.numble.Database.HistoryModel;

import java.util.Date;

public class GameActivity extends AppCompatActivity {

    private Button nextBtn;
    private GridLayout gridLayout;
    private Keyboard keyboard;
    private LoadingAnimator loadingAnimator;
    private RelativeLayout gameLayout;
    private CellManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getWindow()
                    .getDecorView()
                    .setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS);
        }

        nextBtn = findViewById(R.id.nextBtn);
        gridLayout = findViewById(R.id.grid_layout);

        keyboard = findViewById(R.id.keyboard);
        loadingAnimator = findViewById(R.id.loadingAnimator);
        loadingAnimator.cancelAnimation();

        gameLayout = findViewById(R.id.gameLayout);
        gameLayout.setVisibility(View.INVISIBLE);

        int equationLength = getIntent().getIntExtra("Length", 3);
        int numtries = getIntent().getIntExtra("Numtries", 3);
        boolean withMult = getIntent().getBooleanExtra("With_mult", true);
        boolean withPower = getIntent().getBooleanExtra("With_power", true);
        boolean withFact = getIntent().getBooleanExtra("With_fact", true);

        boolean loadSavedGame = getIntent().getBooleanExtra("LoadSavedGame", false);

        if (loadSavedGame) {
            initSavedGame();
        } else {
            initNewGame(equationLength, numtries, withMult, withPower, withFact);
        }
    }

    private void initNewGame(int equationLength, int numtries, boolean withMult, boolean withPower, boolean withFact) {
        String equation = new EquationGenerator(equationLength, withMult, withPower, withFact).getEquation();
        manager = new CellManager(getBaseContext(), gridLayout, gameLayout, loadingAnimator, numtries, equationLength, equation, keyboard);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!manager.reachedEnd()) {
                    if (manager.next()) {
                        Toast.makeText(getBaseContext(), "You won", Toast.LENGTH_SHORT).show();
                        manager.disableAll();
                        nextBtn.setOnClickListener(null);
                    }
                } else {
                    Toast.makeText(getBaseContext(), "You lost", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getBaseContext(), equation, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initSavedGame() {
        HistoryDaoClass database = HistoryDatabaseClass.getDatabase(getBaseContext().getApplicationContext()).getDao();
        HistoryModel model = database.selectLast();
        Cell[][] cells = initCellsFromDatabase(model.getCells());
        manager = new CellManager(getBaseContext(), gridLayout, gameLayout, loadingAnimator, cells, model.getEquation(), keyboard);

        if (!manager.isGameFinished()) {
            nextBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!manager.reachedEnd()) {
                        if (manager.next()) {
                            Toast.makeText(getBaseContext(), "You won", Toast.LENGTH_SHORT).show();
                            manager.disableAll();
                            nextBtn.setOnClickListener(null);
                        }
                    } else {
                        Toast.makeText(getBaseContext(), "You lost", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getBaseContext(), model.getEquation(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private Cell[][] initCellsFromDatabase(char[][] c) {
        Cell[][] cells = new Cell[c.length][c[0].length];
        for (int i = 0; i < c.length; i++) {
            for (int j = 0; j < c[0].length; j++) {
                cells[i][j] = new Cell(getBaseContext(), keyboard);
                cells[i][j].getEditText().setText(String.valueOf(c[i][j]));
            }
        }
        return cells;
    }

    private char[][] charArrayFromCells(Cell[][] c) {
        char[][] cells = new char[c.length][c[0].length];
        for (int i = 0; i < c.length; i++) {
            for (int j = 0; j < c[0].length; j++) {
                if (c[i][j].getContent().length() > 0)
                    cells[i][j] = c[i][j].getContent().charAt(0);
            }
        }
        return cells;
    }

    @Override
    public void onBackPressed() {
        saveGameToHistory();
        super.onBackPressed();
    }

    private void saveGameToHistory() {
        deleteUnsavedGame();
        Log.d("LOLPP", String.valueOf(manager.getCells().length));
        HistoryDaoClass database = HistoryDatabaseClass.getDatabase(getBaseContext().getApplicationContext()).getDao();
        HistoryModel model = new HistoryModel();
        model.setCells(charArrayFromCells(manager.getCells()));
        model.setEquation(manager.getEquation());
        model.setFinished(manager.isGameFinished());
        model.setWon(manager.isWon());
        model.setTime(String.valueOf(new Date().getDate()));
        database.insertData(model);
    }

    private void deleteUnsavedGame() {
        HistoryDaoClass database = HistoryDatabaseClass.getDatabase(getBaseContext().getApplicationContext()).getDao();
        HistoryModel model = database.selectLast();
        if (model != null && !model.isFinished())
            database.deleteData(model.getKey());
    }
}