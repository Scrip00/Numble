package com.Scrip0.numble;

import android.content.Context;
import android.widget.GridLayout;

public class CellManager {
    private Context context;
    private GridLayout gridLayout;
    private Cell[][] grid;
    private int currentRow, rowCount, columnCount;

    public CellManager(Context context, GridLayout gridLayout, int rowCount, int columnCount) {
        this.context = context;
        this.gridLayout = gridLayout;
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        grid = new Cell[columnCount][rowCount];
        gridLayout.setRowCount(rowCount);
        gridLayout.setColumnCount(columnCount);
        currentRow = 0;
        initCells();
    }

    private void initCells() {
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                Cell temp = new Cell(context);
                temp.disableFocus();
                grid[i][j] = temp;
                gridLayout.addView(temp);
            }
        }
    }
}
