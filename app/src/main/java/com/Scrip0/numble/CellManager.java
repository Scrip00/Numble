package com.Scrip0.numble;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
        grid = new Cell[rowCount][columnCount];
        gridLayout.setRowCount(rowCount);
        gridLayout.setColumnCount(columnCount);
        currentRow = 0;
        initCells();
    }

    private void initCells() {
        gridLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                int width = calculateCellWidth();
                for (int k = 0; k < rowCount; k++) {
                    for (int j = 0; j < columnCount; j++) {
                        Cell temp = new Cell(context);
                        temp.setSize(width, width);
                        GridLayout.LayoutParams param = new GridLayout.LayoutParams(GridLayout.spec(k, GridLayout.CENTER, 1F), GridLayout.spec(j, GridLayout.CENTER, 1F));
                        temp.setLayoutParams(param);
                        if (k != 0) temp.disableFocus();
                        if (j != 0) grid[k][j - 1].setNext(temp);
                        grid[k][j] = temp;
                        gridLayout.addView(temp);
                    }
                }
                gridLayout.removeOnLayoutChangeListener(this);
            }
        });
    }

    public int calculateCellWidth() {
        return Math.min(gridLayout.getWidth() / columnCount, gridLayout.getHeight() / rowCount);
    }

    public boolean hasNext() {
        return currentRow != columnCount - 1;
    }

    public boolean next() {
        return false;
    }
}
