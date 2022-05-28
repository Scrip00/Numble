package com.Scrip0.numble.Cells;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.Scrip0.numble.Animations.LoadingAnimator;
import com.Scrip0.numble.CustomLayouts.Cell;
import com.Scrip0.numble.CustomLayouts.Keyboard;
import com.Scrip0.numble.EquationManagers.EquationSolver;
import com.Scrip0.numble.R;

public class CellManager {
    private final Context context;
    private final GridLayout gridLayout;
    private final Cell[][] grid;
    private int currentRow;
    private final int rowCount;
    private final int columnCount;
    private final String equation;
    private final Keyboard keyboard;
    private final RelativeLayout gameLayout;
    private final LoadingAnimator loadingAnimator;

    public CellManager(Context context, GridLayout gridLayout, RelativeLayout gameLayout, LoadingAnimator loadingAnimator, int rowCount, int columnCount, String equation, Keyboard keyboard) {
        this.context = context;
        this.gridLayout = gridLayout;
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.equation = equation;
        this.keyboard = keyboard;
        this.gameLayout = gameLayout;
        this.loadingAnimator = loadingAnimator;
        grid = new Cell[rowCount][columnCount];
        gridLayout.setRowCount(rowCount);
        gridLayout.setColumnCount(columnCount);
        currentRow = 0;
        initCells();
    }

    public CellManager(Context context, GridLayout gridLayout, RelativeLayout gameLayout, LoadingAnimator loadingAnimator, Cell[][] cells, String equation, Keyboard keyboard) {
        this.context = context;
        this.gridLayout = gridLayout;
        if (cells.length > 0)
            this.rowCount = cells.length;
        else
            this.rowCount = 0;
        this.columnCount = cells[0].length;
        this.equation = equation;
        this.keyboard = keyboard;
        this.gameLayout = gameLayout;
        this.loadingAnimator = loadingAnimator;
        grid = cells;
        gridLayout.setRowCount(rowCount);
        gridLayout.setColumnCount(columnCount);
        currentRow = 0;
        initExistingCells();
    }

    private void initExistingCells() {
        gridLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                int width = calculateCellWidth();
                LoadExistingCellsTask task = new LoadExistingCellsTask();
                task.execute(width);
                gridLayout.removeOnLayoutChangeListener(this);
            }
        });
    }

    private void initCells() {
        gridLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                int width = calculateCellWidth();
                LoadNewCellsTask task = new LoadNewCellsTask();
                task.execute(width);
                gridLayout.removeOnLayoutChangeListener(this);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private final class LoadNewCellsTask extends AsyncTask<Integer, Void, Void> {
        // The task to load cells of the new game

        @Override
        protected Void doInBackground(Integer... integers) {
            for (int k = 0; k < rowCount; k++) {
                for (int j = 0; j < columnCount; j++) {
                    Cell temp = new Cell(context, keyboard);
                    temp.setSize(integers[0], integers[0]); // Set cell size to the calculated one
                    GridLayout.LayoutParams param = new GridLayout.LayoutParams(GridLayout.spec(k, GridLayout.CENTER, 1F), GridLayout.spec(j, GridLayout.CENTER, 1F));
                    temp.setLayoutParams(param);
                    if (k != 0) temp.disableFocus(); // If first row, enable focus
                    if (j != 0) grid[k][j - 1].setNext(temp);
                    grid[k][j] = temp;
                }
            }
            grid[0][0].setFocus();
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            // Add generated calls to the grid and disable loading anim
            for (int k = 0; k < rowCount; k++) {
                for (int j = 0; j < columnCount; j++) {
                    gridLayout.addView(grid[k][j]);
                }
            }
            loadingAnimator.setVisibility(View.INVISIBLE);
            gameLayout.setVisibility(View.VISIBLE);
            Animation fadeOutAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_out);
            fadeOutAnimation.setDuration(1000);
            fadeOutAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
            gameLayout.setAnimation(fadeOutAnimation);
            Animation fadeInAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
            fadeInAnimation.setDuration(1000);
            fadeInAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
            loadingAnimator.setAnimation(fadeInAnimation);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private final class LoadExistingCellsTask extends AsyncTask<Integer, Void, Void> {
        // The task to load cells of the already existing game

        @Override
        protected Void doInBackground(Integer... integers) {
            for (int k = 0; k < rowCount; k++) {
                boolean isRowFull = isRowFull(grid[k]);
                for (int j = 0; j < columnCount; j++) {
                    grid[k][j].setSize(integers[0], integers[0]); // Set cell size to the calculated one
                    GridLayout.LayoutParams param = new GridLayout.LayoutParams(GridLayout.spec(k, GridLayout.CENTER, 1F), GridLayout.spec(j, GridLayout.CENTER, 1F));
                    grid[k][j].setLayoutParams(param);
                    if (isRowFull && isCurrentEquationCorrect() || k > currentRow)
                        grid[k][j].disableFocus();  // If row is full or it's number is greater that playable row, disable focus
                    else {
                        grid[k][j].setFocus();
                        grid[k][j].setFocus();
                        grid[k][j].clearFocus();
                    }
                    if (j != 0) grid[k][j - 1].setNext(grid[k][j]);
                }
                if (isRowFull && isCurrentEquationCorrect()) { // Find playable row number
                    currentRow++;
                    paintRow(grid[k]);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            // Add generated calls to the grid and disable loading anim
            for (int k = 0; k < rowCount; k++) {
                for (int j = 0; j < columnCount; j++) {
                    gridLayout.addView(grid[k][j]);
                }
            }
            loadingAnimator.setVisibility(View.INVISIBLE);
            gameLayout.setVisibility(View.VISIBLE);
            Animation fadeOutAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_out);
            fadeOutAnimation.setDuration(1000);
            fadeOutAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
            gameLayout.setAnimation(fadeOutAnimation);
            Animation fadeInAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
            fadeInAnimation.setDuration(1000);
            fadeInAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
            loadingAnimator.setAnimation(fadeInAnimation);
        }
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
            Toast.makeText(context, "The equation is incorrect", Toast.LENGTH_SHORT).show();
            return false;
        }
        boolean won = true;

        for (int i = 0; i < columnCount; i++) {
            Cell temp = grid[currentRow][i];
            temp.disableFocus();
            if (equation.charAt(i) == temp.getContent().charAt(0)) {
                startCellAnim(temp.getView(), temp, Cell.RIGHT, i);
                keyboard.updateKeyColor(temp.getContent(), ContextCompat.getColor(context, R.color.cell_right));
            } else if (equation.contains(temp.getContent())) {
                won = false;
                startCellAnim(temp.getView(), temp, Cell.CLOSE, i);
                keyboard.updateKeyColor(temp.getContent(), ContextCompat.getColor(context, R.color.cell_close));
            } else {
                won = false;
                startCellAnim(temp.getView(), temp, Cell.WRONG, i);
                keyboard.updateKeyColor(temp.getContent(), ContextCompat.getColor(context, R.color.cell_wrong));
            }
            if (hasNext()) {
                temp = grid[currentRow + 1][i];
                temp.enableFocus();
                if (i == 0) temp.setFocus();
            }

        }
        currentRow++;
        return won;
    }

    public void clearRowFocus() {
        for (int i = 0; i < columnCount; i++) {
            if (grid[currentRow][i] != null && grid[currentRow][i].isFocused() && grid[currentRow][i].isEnabled())
                grid[currentRow][i].clearFocus();
        }
    }

    private boolean isRowFull(Cell[] cells) {
        for (Cell cell : cells) {
            if (cell.getContent() == null || cell.getContent().equals(" ") || cell.getContent().equals("")) {
                return false;
            }
        }
        return true;
    }

    private void startCellAnim(final View view, Cell cell, int color, int delay) {
        // Animation of flipping cells
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.scale_in);
        animation.setStartOffset(delay * 100L);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cell.setBackground(color);
                Animation scaleOutAnimation = AnimationUtils.loadAnimation(context, R.anim.scale_out);
                scaleOutAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
                view.startAnimation(scaleOutAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation);
    }

    private boolean isCurrentEquationCorrect() {
        String equation = "";
        for (int i = 0; i < columnCount; i++) {
            grid[currentRow][i].clearFocus();
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

    private void paintRow(Cell[] cell) {
        // Paints row according to the generated equation
        boolean won = true;
        for (int i = 0; i < cell.length; i++) {
            if (cell[i].getContent().charAt(0) == equation.charAt(i)) {
                startCellAnim(cell[i].getView(), cell[i], Cell.RIGHT, i);
                keyboard.updateKeyColor(cell[i].getContent(), ContextCompat.getColor(context, R.color.cell_right));
            } else if (equation.contains(cell[i].getContent())) {
                startCellAnim(cell[i].getView(), cell[i], Cell.CLOSE, i);
                keyboard.updateKeyColor(cell[i].getContent(), ContextCompat.getColor(context, R.color.cell_close));
                won = false;
            } else {
                startCellAnim(cell[i].getView(), cell[i], Cell.WRONG, i);
                keyboard.updateKeyColor(cell[i].getContent(), ContextCompat.getColor(context, R.color.cell_wrong));
                won = false;
            }
        }
        if (won) disableAll(); // If game is finished, disable all cells
    }

    public boolean isGameFinished() {
        for (int i = 0; i < rowCount; i++) {
            boolean won = true;
            for (int j = 0; j < columnCount; j++) {
                String temp = grid[i][j].getContent();
                if (temp.equals("") || temp.equals(" "))
                    return false;
                if (temp.length() == 0 || temp.charAt(0) != equation.charAt(j)) {
                    won = false;
                }
            }
            if (won) return true;
        }
        return true;
    }

    public boolean isWon() {
        for (int i = 0; i < rowCount; i++) {
            boolean won = true;
            for (int j = 0; j < columnCount; j++) {
                String temp = grid[i][j].getContent();
                if (temp.length() == 0 || temp.charAt(0) != equation.charAt(j)) {
                    won = false;
                }
            }
            if (won) return true;
        }
        return false;
    }

    public void disableAll() {
        for (Cell[] i : grid) {
            for (Cell j : i) {
                j.disableFocus();
            }
        }
    }

    public Cell[][] getCells() {
        return grid;
    }

    public String getEquation() {
        return equation;
    }

    public int getCurrentRow() {
        return currentRow;
    }
}
