package com.Scrip0.numble.Database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Arrays;
import java.util.Objects;

@Entity(tableName = "user")
public class HistoryModel {

    //Primary key
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int key;

    // key, time, Cell[][], equation, won

    @ColumnInfo(name = "time")
    private String time;

    @ColumnInfo(name = "cells")
    private char[][] cells;

    @ColumnInfo(name = "equation")
    private String equation;

    @ColumnInfo(name = "current_row")
    private int currentRow;

    @ColumnInfo(name = "won")
    private boolean won;

    @ColumnInfo(name = "finished")
    private boolean finished;

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public char[][] getCells() {
        return cells;
    }

    public void setCells(char[][] cells) {
        this.cells = cells;
    }

    public String getEquation() {
        return equation;
    }

    public void setEquation(String equation) {
        this.equation = equation;
    }

    public int getCurrentRow() {
        return currentRow;
    }

    public void setCurrentRow(int currentRow) {
        this.currentRow = currentRow;
    }

    public boolean isWon() {
        return won;
    }

    public void setWon(boolean won) {
        this.won = won;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoryModel that = (HistoryModel) o;
        return key == that.key && currentRow == that.currentRow && won == that.won && finished == that.finished && time.equals(that.time) && equation.equals(that.equation);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(key, time, equation, currentRow, won, finished);
        result = 31 * result + Arrays.hashCode(cells);
        return result;
    }
}