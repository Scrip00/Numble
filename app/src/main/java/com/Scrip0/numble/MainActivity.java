package com.Scrip0.numble;

import android.os.Bundle;
import android.util.Log;
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

        Button nextBtn = findViewById(R.id.nextBtn);
        GridLayout gridLayout = findViewById(R.id.grid_layout);

        String equation = "1+5=6";
        CellManager manager = new CellManager(this, gridLayout, 7, 5, equation);

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