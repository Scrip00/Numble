package com.Scrip0.numble;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.Scrip0.numble.Database.HistoryDaoClass;
import com.Scrip0.numble.Database.HistoryDatabaseClass;
import com.Scrip0.numble.Database.HistoryModel;
import com.Scrip0.numble.Dialogs.ContinueSavedGameDialog;
import com.Scrip0.numble.Dialogs.StartNewGameDialog;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        finishGame(); // Check if there is unfinished game and propose to finish it

        Button newGameBtn = findViewById(R.id.newGameBtn);

        newGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                new StartNewGameDialog().show(getSupportFragmentManager(), "dialog");
            }
        });
    }

    private void finishGame() {
        HistoryDaoClass database = HistoryDatabaseClass.getDatabase(getBaseContext().getApplicationContext()).getDao();
        HistoryModel model = database.selectLast();
        if (model != null && !model.isFinished()) {
            new ContinueSavedGameDialog().show(getSupportFragmentManager(), "gamedialog");
        }
    }
}