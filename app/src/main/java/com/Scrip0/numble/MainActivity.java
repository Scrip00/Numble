package com.Scrip0.numble;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getWindow()
                    .getDecorView()
                    .setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS);
        }

        Button nextBtn = findViewById(R.id.nextBtn);
        GridLayout gridLayout = findViewById(R.id.grid_layout);

        Keyboard keyboard = findViewById(R.id.keyboard);

        String equation = "1+5=6";
        CellManager manager = new CellManager(this, gridLayout, 6, 5, equation, keyboard);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!manager.reachedEnd()) {
                    manager.next();
                } else
                    Toast.makeText(getBaseContext(), "You lose", Toast.LENGTH_SHORT).show();
            }
        });

    }
}