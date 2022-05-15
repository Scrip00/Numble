package com.Scrip0.numble;

import android.content.Context;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

public class CellManager {
    private final Context context;
    private final GridLayout gridLayout;
    private final Cell[][] grid;
    private int currentRow;
    private final int rowCount;
    private final int columnCount;
    private final String equation;
    private final Keyboard keyboard;

    public CellManager(Context context, GridLayout gridLayout, int rowCount, int columnCount, String equation, Keyboard keyboard) {
        this.context = context;
        this.gridLayout = gridLayout;
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.equation = equation;
        this.keyboard = keyboard;
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
                        Cell temp = new Cell(context, keyboard);
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
        return currentRow != rowCount - 1;
    }

    public boolean reachedEnd() {
        return currentRow == rowCount;
    }

    public boolean next() {
        if (!isCurrentEquationCorrect()) {
            Toast.makeText(context, "INCORRECT", Toast.LENGTH_SHORT).show();
            return false;
        }
        boolean won = true;
        for (int i = 0; i < columnCount; i++) {
            Cell temp = grid[currentRow][i];
            temp.disableFocus();
            if (equation.charAt(i) == temp.getContent().charAt(0)) {
                temp.setBackground(Cell.RIGHT);
                keyboard.updateKeyColor(temp.getContent(), ContextCompat.getColor(context, R.color.cell_right));
            } else if (equation.contains(temp.getContent())) {
                won = false;
                temp.setBackground(Cell.CLOSE);
                keyboard.updateKeyColor(temp.getContent(), ContextCompat.getColor(context, R.color.cell_close));
            } else {
                won = false;
                temp.setBackground(Cell.WRONG);
                keyboard.updateKeyColor(temp.getContent(), ContextCompat.getColor(context, R.color.cell_wrong));
            }
            if (hasNext()) {
                temp = grid[currentRow + 1][i];
                temp.enableFocus();
            }
        }
        currentRow++;
        return won;
    }

    private boolean isCurrentEquationCorrect() {
        String equation = "";
        for (int i = 0; i < columnCount; i++) {
            String content = grid[currentRow][i].getContent();
            if (content.isEmpty()) return false;
            equation += content;
        }
        EquationSolver solver = new EquationSolver();
        int answer;
        try {
            answer = solver.solve(equation);
        } catch (Exception e) {
            return false;
        }
        if (!solver.isAnswerInt() || equation.split("=").length != 2 || Integer.parseInt(equation.split("=")[1]) != answer)
            return false;
        return true;
    }
}
