package com.Scrip0.numble;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getWindow()
                    .getDecorView()
                    .setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS);
        }

        Button nextBtn = findViewById(R.id.nextBtn);
        GridLayout gridLayout = findViewById(R.id.grid_layout);

        Keyboard keyboard = findViewById(R.id.keyboard);

        int equationLength = getIntent().getIntExtra("Length", 3);
        int numtries = getIntent().getIntExtra("Numtries", 3);
        boolean withMult = getIntent().getBooleanExtra("With_mult", true);
        boolean withPower = getIntent().getBooleanExtra("With_power", true);
        boolean withFact = getIntent().getBooleanExtra("With_fact", true);

        String equation = new EquationGenerator(equationLength, withMult, withPower, withFact).getEquation();
        CellManager manager = new CellManager(this, gridLayout, numtries, equationLength, equation, keyboard);

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
}