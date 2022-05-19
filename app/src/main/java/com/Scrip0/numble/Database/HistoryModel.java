package com.Scrip0.numble.Database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

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
}