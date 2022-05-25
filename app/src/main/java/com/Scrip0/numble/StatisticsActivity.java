package com.Scrip0.numble;

import android.os.Bundle;

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
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
//
        BarChart winChart = findViewById(R.id.winStatChart);
        initializeWinBarChart(winChart);

        BarChart winStatChartMonth = findViewById(R.id.winStatChartMonth);
        initializeWinBarChartMonth(winStatChartMonth);

        BarChart winStatChartToday = findViewById(R.id.winStatChartToday);
        initializeWinBarChartToday(winStatChartToday);
    }

    private void initializeWinBarChart(BarChart chart) {
        chart.setNoDataText("You haven't played games for now");

        HistoryDaoClass database = HistoryDatabaseClass.getDatabase(getApplicationContext()).getDao();
        ArrayList<String> names = new ArrayList<>(Arrays.asList("Won", "Lost"));

        float allGamesCount = database.getAllGames("").size();
        ArrayList<Float> dataset = new ArrayList<Float>(Arrays.asList((float) database.getWonGames("").size(), (float) database.getLostGames("").size()));

        ArrayList<Integer> colors = new ArrayList<>(Arrays.asList(ContextCompat.getColor(this, R.color.cell_right), ContextCompat.getColor(this, R.color.cell_wrong)));
        initializeBarChart(chart);
        if (dataset.size() > 0)
            createBarChart(chart, dataset, names, colors, allGamesCount);
    }

    private void initializeWinBarChartMonth(BarChart chart) {
        chart.setNoDataText("You haven't played games this month");

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
        initializeBarChart(chart);
        if (dataset.size() > 0)
            createBarChart(chart, dataset, names, colors, allGamesCount);
    }

    private void initializeWinBarChartToday(BarChart chart) {
        chart.setNoDataText("You haven't played games today yet");

        HistoryDaoClass database = HistoryDatabaseClass.getDatabase(getApplicationContext()).getDao();
        ArrayList<String> names = new ArrayList<>(Arrays.asList("Won", "Lost"));

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        String date = sdf.format(new Date());

        float allGamesCount = database.getAllGames(date).size();
        ArrayList<Float> dataset = new ArrayList<Float>(Arrays.asList((float) database.getWonGames(date).size(), (float) database.getLostGames(date).size()));

        ArrayList<Integer> colors = new ArrayList<>(Arrays.asList(ContextCompat.getColor(this, R.color.cell_right), ContextCompat.getColor(this, R.color.cell_wrong)));
        initializeBarChart(chart);
        if (dataset.size() > 0)
            createBarChart(chart, dataset, names, colors, allGamesCount);
    }

    private void initializeBarChart(BarChart chart) {
        chart.getDescription().setEnabled(false);

        chart.setMaxVisibleValueCount(2);
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
            xAxis.setValueFormatter(new IndexAxisValueFormatter(names));//setting String values in Xaxis
            for (IDataSet set : chart.getData().getDataSets())
                set.setDrawValues(!set.isDrawValuesEnabled());

            chart.getAxisLeft().setAxisMinimum(0);
            chart.getAxisLeft().setAxisMaximum(max);

            chart.invalidate();
        }
    }
}