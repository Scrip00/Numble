package com.Scrip0.numble;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.Scrip0.numble.Database.HistoryDaoClass;
import com.Scrip0.numble.Database.HistoryDatabaseClass;
import com.Scrip0.numble.Database.HistoryModel;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class StatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        BarChart winChart = findViewById(R.id.winStatChart);
        initializeWinBarChart(winChart);

        BarChart winStatChartMonth = findViewById(R.id.winStatChartMonth);
        initializeWinBarChartMonth(winStatChartMonth);

        BarChart winStatChartToday = findViewById(R.id.winStatChartToday);
        initializeWinBarChartToday(winStatChartToday);

        TextView textGamesPlayed = findViewById(R.id.text_games_played);
        initializeTextGamesPlayed(textGamesPlayed);

        TextView textWon = findViewById(R.id.text_won);
        initializeTextWon(textWon);

        TextView textSettings = findViewById(R.id.text_settings);
        initializeTextSettings(textSettings);

        Spinner spinner = findViewById(R.id.spinner);
        initializeSpinner(spinner);
    }

    private void initializeSpinner(Spinner spinner) {
        HistoryDaoClass database = HistoryDatabaseClass.getDatabase(getApplicationContext()).getDao();
        HashMap<String, HashMap<Integer, Integer>> map = new HashMap<>();
        for (HistoryModel model : database.getAllGames("")) {
            String settings = model.getCells()[0].length + " X " + model.getCells().length;
            if (model.isWon()) {
                if (!map.containsKey(settings))
                    map.put(settings, new HashMap<>());
                if (!map.get(settings).containsKey(model.getCurrentRow())) {
                    map.get(settings).put(model.getCurrentRow(), 0);
                }
                map.get(settings).put(model.getCurrentRow(), map.get(settings).get(model.getCurrentRow()) + 1);
            }
            if (!map.containsKey(settings))
                map.put(settings, new HashMap<>());
            if (!map.get(settings).containsKey(0)) {
                map.get(settings).put(0, 0);
            }
            map.get(settings).put(0, map.get(settings).get(0) + 1);
        }

        ArrayList<String> keys = new ArrayList<>();
        for (String key : map.keySet()) keys.add(key);
        spinner.setAdapter(new ArrayAdapter(this, android.R.layout.simple_spinner_item, keys));
        BarChart spinnerChart = findViewById(R.id.spinnerBarChart);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                initializeSpinnerBarChart(spinnerChart, map.get(spinner.getSelectedItem().toString()), Integer.parseInt(spinner.getSelectedItem().toString().split(" X ")[1]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                initializeSpinnerBarChart(spinnerChart, map.get(spinner.getSelectedItem().toString()), Integer.parseInt(spinner.getSelectedItem().toString().split(" X ")[1]));
            }
        });
    }

    private void initializeSpinnerBarChart(BarChart chart, HashMap<Integer, Integer> map, int max) {
        chart.setNoDataText("No data");
        chart.setNoDataTextColor(Color.BLACK);

        ArrayList<String> names = new ArrayList<>();
        for (int i = 1; i <= max; i++)
            names.add(String.valueOf(i));

        float allGamesCount = 0;
        if (map.containsKey(0)) allGamesCount = map.get(0);
        ArrayList<Integer> colors = new ArrayList<>();

        ArrayList<Float> dataset = new ArrayList<Float>();

        for (int i = 1; i <= max; i++) {
            if (map.containsKey(i)) {
                dataset.add(Float.valueOf(map.get(i)));
            } else {
                dataset.add(0f);
            }
            if (i <= max / 3) {
                colors.add(ContextCompat.getColor(this, R.color.cell_right));
            } else if (i > max * 2 / 3) {
                colors.add(ContextCompat.getColor(this, R.color.cell_wrong));
            } else colors.add(ContextCompat.getColor(this, R.color.cell_close));
        }

        if (map.keySet().size() <= 1) {
            chart.clear();
            chart.notifyDataSetChanged();
        } else {
            initializeBarChart(chart);
            createBarChart(chart, dataset, names, colors, allGamesCount);
        }
    }

    @SuppressLint("SetTextI18n")
    private void initializeTextSettings(TextView text) {
        HistoryDaoClass database = HistoryDatabaseClass.getDatabase(getApplicationContext()).getDao();
        HashMap<String, Integer> map = new HashMap<>();
        for (HistoryModel model : database.getAllGames("")) {
            String settings = model.getCells()[0].length + " X " + model.getCells().length;
            if (!map.containsKey(settings)) map.put(settings, 0);
            map.put(settings, map.get(settings) + 1);
        }
        String setting = "";
        int max = 0;
        for (String key : map.keySet()) {
            if (!map.containsKey(setting)) {
                setting = key;
                max = map.get(setting);
            }
            if (map.get(setting) > max) {
                setting = key;
                max = map.get(setting);
            }
        }
        text.setText(setting);
    }

    @SuppressLint("SetTextI18n")
    private void initializeTextWon(TextView text) {
        HistoryDaoClass database = HistoryDatabaseClass.getDatabase(getApplicationContext()).getDao();
        if (database.getWonGames("").size() > 0)
            text.setText((int) ((double) database.getWonGames("").size() / (double) database.getAllGames("").size() * 100) + "%");
    }

    @SuppressLint("SetTextI18n")
    private void initializeTextGamesPlayed(TextView text) {
        HistoryDaoClass database = HistoryDatabaseClass.getDatabase(getApplicationContext()).getDao();
        text.setText(Integer.toString(database.getAllGames("").size()));
    }

    private void initializeWinBarChart(BarChart chart) {
        chart.setNoDataText("No data");
        chart.setNoDataTextColor(Color.BLACK);

        HistoryDaoClass database = HistoryDatabaseClass.getDatabase(getApplicationContext()).getDao();
        ArrayList<String> names = new ArrayList<>(Arrays.asList("Won", "Lost"));

        float allGamesCount = database.getAllGames("").size();
        ArrayList<Float> dataset = new ArrayList<Float>(Arrays.asList((float) database.getWonGames("").size(), (float) database.getLostGames("").size()));

        ArrayList<Integer> colors = new ArrayList<>(Arrays.asList(ContextCompat.getColor(this, R.color.cell_right), ContextCompat.getColor(this, R.color.cell_wrong)));

        if (dataset.get(0) > 0) {
            initializeBarChart(chart);
            createBarChart(chart, dataset, names, colors, allGamesCount);
        }
    }

    private void initializeWinBarChartMonth(BarChart chart) {
        chart.setNoDataText("No data");
        chart.setNoDataTextColor(Color.BLACK);

        HistoryDaoClass database = HistoryDatabaseClass.getDatabase(getApplicationContext()).getDao();
        ArrayList<String> names = new ArrayList<>(Arrays.asList("Won", "Lost"));

        SimpleDateFormat sdf = new SimpleDateFormat("MM", Locale.getDefault());
        String month = sdf.format(new Date());
        sdf = new SimpleDateFormat("yyyy", Locale.getDefault());
        String year = sdf.format(new Date());

        List<HistoryModel> models = database.getAllGames(month);

        models.retainAll(database.getAllGames(year));

        List<HistoryModel> modelsWon = database.getWonGames(month);
        modelsWon.retainAll(database.getWonGames(year));

        List<HistoryModel> modelsLost = database.getLostGames(month);
        modelsLost.retainAll(database.getLostGames(year));

        float allGamesCount = models.size();
        ArrayList<Float> dataset = new ArrayList<Float>(Arrays.asList((float) modelsWon.size(), (float) modelsLost.size()));

        ArrayList<Integer> colors = new ArrayList<>(Arrays.asList(ContextCompat.getColor(this, R.color.cell_right), ContextCompat.getColor(this, R.color.cell_wrong)));

        if (dataset.get(0) > 0) {
            initializeBarChart(chart);
            createBarChart(chart, dataset, names, colors, allGamesCount);
        }
    }

    private void initializeWinBarChartToday(BarChart chart) {
        chart.setNoDataText("No data");
        chart.setNoDataTextColor(Color.BLACK);

        HistoryDaoClass database = HistoryDatabaseClass.getDatabase(getApplicationContext()).getDao();
        ArrayList<String> names = new ArrayList<>(Arrays.asList("Won", "Lost"));

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        String date = sdf.format(new Date());

        float allGamesCount = database.getAllGames(date).size();
        ArrayList<Float> dataset = new ArrayList<Float>(Arrays.asList((float) database.getWonGames(date).size(), (float) database.getLostGames(date).size()));

        ArrayList<Integer> colors = new ArrayList<>(Arrays.asList(ContextCompat.getColor(this, R.color.cell_right), ContextCompat.getColor(this, R.color.cell_wrong)));

        if (dataset.get(0) > 0) {
            initializeBarChart(chart);
            createBarChart(chart, dataset, names, colors, allGamesCount);
        }
    }

    private void initializeBarChart(BarChart chart) {
        chart.clear();
        chart.getDescription().setEnabled(false);

        chart.setMaxVisibleValueCount(100);
        chart.getXAxis().setDrawGridLines(false);
        chart.setPinchZoom(false);

        chart.setDrawBarShadow(false);
        chart.setDrawGridBackground(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawGridLines(false);

        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setEnabled(true);
        chart.getXAxis().setDrawGridLines(false);

        chart.animateY(1000, Easing.EaseOutBounce);

        chart.getLegend().setEnabled(false);

        chart.getAxisRight().setDrawLabels(false);
        chart.getAxisLeft().setDrawLabels(true);
        chart.setTouchEnabled(false);
        chart.setDoubleTapToZoomEnabled(false);
        chart.getXAxis().setEnabled(true);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        chart.invalidate();
    }

    private void createBarChart(BarChart chart, ArrayList<Float> dataset, ArrayList<String> names, ArrayList<Integer> colors, float max) {
        ArrayList<BarEntry> values = new ArrayList<>();

        for (int i = 0; i < dataset.size(); i++)
            values.add(new BarEntry(i, dataset.get(i)));

        BarDataSet barDataSet;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            barDataSet = (BarDataSet) chart.getData().getDataSetByIndex(0);
            barDataSet.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            barDataSet = new BarDataSet(values, "Data Set");
            barDataSet.setColors(colors);
            barDataSet.setDrawValues(true);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(barDataSet);

            BarData data = new BarData(dataSets);
            chart.setData(data);
            chart.setVisibleXRange(1, dataset.size());
            chart.setFitBars(true);

            XAxis xAxis = chart.getXAxis();
            xAxis.setGranularity(1f);
            xAxis.setGranularityEnabled(true);
            xAxis.setValueFormatter(new IndexAxisValueFormatter(names));
            for (IDataSet set : chart.getData().getDataSets())
                set.setDrawValues(!set.isDrawValuesEnabled());

            chart.getAxisLeft().setAxisMinimum(0);
            chart.getAxisLeft().setAxisMaximum(max);
            if (max < 12)
                chart.getAxisLeft().setLabelCount((int) max);
            else
                chart.getAxisLeft().setLabelCount(12);
            chart.invalidate();
        }
    }
}