package com.Scrip0.numble;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Scrip0.numble.Adapters.HistoryListAdapter;
import com.Scrip0.numble.Database.HistoryDaoClass;
import com.Scrip0.numble.Database.HistoryDatabaseClass;
import com.Scrip0.numble.Database.HistoryModel;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        HistoryDaoClass database = HistoryDatabaseClass.getDatabase(getApplicationContext()).getDao();

        List<HistoryModel> games = database.getAllData();

        RecyclerView history_list = findViewById(R.id.history_list);

        HistoryListAdapter adapter = new HistoryListAdapter(games);
        history_list.setAdapter(adapter);
        history_list.setLayoutManager(new LinearLayoutManager(this));
    }
}