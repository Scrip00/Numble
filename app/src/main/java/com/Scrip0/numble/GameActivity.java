package com.Scrip0.numble;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.Scrip0.numble.Animations.LoadingAnimator;
import com.Scrip0.numble.Cells.CellManager;
import com.Scrip0.numble.CustomLayouts.Cell;
import com.Scrip0.numble.CustomLayouts.Keyboard;
import com.Scrip0.numble.Database.HistoryDaoClass;
import com.Scrip0.numble.Database.HistoryDatabaseClass;
import com.Scrip0.numble.Database.HistoryModel;
import com.Scrip0.numble.Dialogs.EndGameDialog;
import com.Scrip0.numble.EquationManagers.EquationGenerator;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {

    private Button nextBtn;
    private GridLayout gridLayout;
    private Keyboard keyboard;
    private LoadingAnimator loadingAnimator;
    private RelativeLayout gameLayout;
    private CellManager manager;
    private boolean savedAndCompleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getWindow()
                    .getDecorView()
                    .setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS);
        }

        loadAd(this);

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

        if (!withMult) {
            keyboard.updateKeyColor("*", ContextCompat.getColor(getBaseContext(), R.color.cell_wrong));
            keyboard.updateKeyColor("/", ContextCompat.getColor(getBaseContext(), R.color.cell_wrong));
        }
        if (!withPower) keyboard.updateKeyColor("^", ContextCompat.getColor(getBaseContext(), R.color.cell_wrong));
        if (!withFact) keyboard.updateKeyColor("!", ContextCompat.getColor(getBaseContext(), R.color.cell_wrong));

        boolean loadSavedGame = getIntent().getBooleanExtra("LoadSavedGame", false);

        if (loadSavedGame) { // If it's not a new game
            initSavedGame();
        } else {
            initNewGame(equationLength, numtries, withMult, withPower, withFact);
        }
        setUpGameLayoutListener();
    }

    public static void loadAd(Activity activity) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(activity.getBaseContext());
        int numAds = sharedPrefs.getInt("Ads", 0);
        int maxAds = 7;
        if (numAds >= maxAds) {
            MobileAds.initialize(activity.getBaseContext(), new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });
            AdRequest adRequest = new AdRequest.Builder().build();

            InterstitialAd.load(activity.getBaseContext(), "ca-app-pub-3940256099942544/1033173712", adRequest,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            // The mInterstitialAd reference will be null until
                            // an ad is loaded.
                            interstitialAd.show(activity);
                            SharedPreferences.Editor sharedPrefsEditor = sharedPrefs.edit();
                            sharedPrefsEditor.putInt("Ads", 0);
                            sharedPrefsEditor.apply();
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error
                            Log.d("Failed", String.valueOf(loadAdError));
                        }
                    });
        } else {
            SharedPreferences.Editor sharedPrefsEditor = sharedPrefs.edit();
            sharedPrefsEditor.putInt("Ads", numAds + 1);
            sharedPrefsEditor.apply();
        }
    }

    private void initNewGame(int equationLength, int numtries, boolean withMult, boolean withPower, boolean withFact) {
        String equation = new EquationGenerator(equationLength, withMult, withPower, withFact).getEquation();
        manager = new CellManager(getBaseContext(), gridLayout, gameLayout, loadingAnimator, numtries, equationLength, equation, keyboard);
//        Toast.makeText(getBaseContext(), equation, Toast.LENGTH_LONG).show();
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!manager.reachedEnd()) {
                    if (manager.next()) {
                        saveGameToHistory();
//                        Toast.makeText(getBaseContext(), "You won", Toast.LENGTH_SHORT).show();
                        manager.disableAll();
                        nextBtn.setOnClickListener(null);
                        startNewEndGameDialog(true);
                    } else if (manager.reachedEnd()) gameLost(equation);
                } else {
                    gameLost(equation);
                }
            }
        });
    }

    private void gameLost(String equation) {
        saveGameToHistory();
        startNewEndGameDialog(false);
//        Toast.makeText(getBaseContext(), "You lost", Toast.LENGTH_SHORT).show();
//        Toast.makeText(getBaseContext(), equation, Toast.LENGTH_LONG).show();
    }

    private void startNewEndGameDialog(boolean won) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new EndGameDialog(manager, won).show(getSupportFragmentManager(), "end game dialog");
                this.cancel();
            }
        }, 0, 1000);
    }

    private void initSavedGame() {
        HistoryDaoClass database = HistoryDatabaseClass.getDatabase(getBaseContext().getApplicationContext()).getDao();
        HistoryModel model = database.selectLast();
        Cell[][] cells = initCellsFromDatabase(model.getCells());
        manager = new CellManager(getBaseContext(), gridLayout, gameLayout, loadingAnimator, cells, model.getEquation(), keyboard);
        savedAndCompleted = true;
        if (!manager.isGameFinished()) {
            savedAndCompleted = false;
            nextBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!manager.reachedEnd()) {
                        if (manager.next()) {
                            saveGameToHistory();
//                        Toast.makeText(getBaseContext(), "You won", Toast.LENGTH_SHORT).show();
                            manager.disableAll();
                            nextBtn.setOnClickListener(null);
                            startNewEndGameDialog(true);
                        } else if (manager.reachedEnd()) gameLost(model.getEquation());
                    } else {
                        gameLost(model.getEquation());
                    }
                }
            });
        }
    }

    private void setUpGameLayoutListener() {
        gameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (manager != null)
                    manager.clearRowFocus();
            }
        });
    }

    private Cell[][] initCellsFromDatabase(char[][] c) {
        // Convert char array to cells array because it's impossible to implement in database converters (cells need context)
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
        if (!savedAndCompleted)
            saveGameToHistory(); // If loaded game was not previously completed
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        finish();
    }

    private void saveGameToHistory() {
        savedAndCompleted = true;
        deleteUnsavedGame();
        HistoryDaoClass database = HistoryDatabaseClass.getDatabase(getBaseContext().getApplicationContext()).getDao();
        HistoryModel model = new HistoryModel();
        model.setCells(charArrayFromCells(manager.getCells()));
        model.setEquation(manager.getEquation());
        model.setFinished(manager.isGameFinished());
        model.setWon(manager.isWon());
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy, HH:mm", Locale.getDefault());
        model.setTime(sdf.format(new Date()));
        model.setCurrentRow(manager.getCurrentRow());
        database.insertData(model);
    }

    @Override
    protected void onPause() {
        if (!savedAndCompleted) saveGameToHistory();
        super.onPause();
    }

    private void deleteUnsavedGame() {
        HistoryDaoClass database = HistoryDatabaseClass.getDatabase(getBaseContext().getApplicationContext()).getDao();
        HistoryModel model = database.selectLast();
        if (model != null && !model.isFinished())
            database.deleteData(model.getKey());
    }
}