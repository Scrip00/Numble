package com.Scrip0.numble;

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

    private void initCells() {
        gridLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                int width = calculateCellWidth();
                LoadCellsTask task = new LoadCellsTask();
                task.execute(width);
                gridLayout.removeOnLayoutChangeListener(this);
            }
        });
    }

    private final class LoadCellsTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... integers) {
            for (int k = 0; k < rowCount; k++) {
                for (int j = 0; j < columnCount; j++) {
                    Cell temp = new Cell(context, keyboard);
                    temp.setSize(integers[0], integers[0]);
                    GridLayout.LayoutParams param = new GridLayout.LayoutParams(GridLayout.spec(k, GridLayout.CENTER, 1F), GridLayout.spec(j, GridLayout.CENTER, 1F));
                    temp.setLayoutParams(param);
                    if (k != 0) temp.disableFocus();
                    if (j != 0) grid[k][j - 1].setNext(temp);
                    grid[k][j] = temp;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
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
            Toast.makeText(context, "INCORRECT", Toast.LENGTH_SHORT).show();
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
            }

        }
        currentRow++;
        return won;
    }

    private void startCellAnim(final View view, Cell cell, int color, int delay) {
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

    public void disableAll() {
        for (Cell[] i : grid) {
            for (Cell j : i) {
                j.disableFocus();
            }
        }
    }
}
