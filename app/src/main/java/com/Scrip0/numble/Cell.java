package com.Scrip0.numble;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class Cell extends FrameLayout {

    public static final int DEFAULT = 0;
    public static final int WRONG = 1;
    public static final int CLOSE = 2;
    public static final int RIGHT = 3;

    private EditText textField;
    private Cell next;
    private String prevContent;
    private RelativeLayout layout;
    private boolean restoredText;

    public Cell(@NonNull Context context) {
        super(context);
        initView();
    }

    public Cell(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public Cell(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public Cell(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.cell_layout, this);
        layout = findViewById(R.id.layoutID);
        textField = findViewById(R.id.editText);
        textField.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b && textField.getText().length() == 1) {
                    prevContent = String.valueOf(textField.getText());
                    restoredText = false;
                    textField.setText("");
                }

                if (!b && textField.getText().length() == 0) {
                    restoredText = true;
                    textField.setText(prevContent);
                }

            }
        });

        textField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (textField.getText().length() == 1 && !restoredText) {
                    textField.clearFocus();
                    if (next != null) {
                        next.setFocus();
                    }
                }
            }
        });
    }

    public void setFocus() {
        textField.requestFocus();
    }

    public void enableFocus() {
        textField.setEnabled(true);
    }

    public void disableFocus() {
        textField.setEnabled(false);
    }

    public void setNext(Cell cell) {
        next = cell;
    }

    public String getContent() {
        return String.valueOf(textField.getText());
    }

    public void setSize(int width, int height) {
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        params.width = width;
        params.height = height;
        layout.setLayoutParams(params);
    }

    public void setBackground(int background) {
        switch (background) {
            case DEFAULT:
                layout.getBackground().setColorFilter(getResources().getColor(R.color.cell_default), PorterDuff.Mode.MULTIPLY);
                break;
            case WRONG:
                layout.getBackground().setColorFilter(getResources().getColor(R.color.cell_wrong), PorterDuff.Mode.MULTIPLY);
                break;
            case CLOSE:
                layout.getBackground().setColorFilter(getResources().getColor(R.color.cell_close), PorterDuff.Mode.MULTIPLY);
                break;
            case RIGHT:
                layout.getBackground().setColorFilter(getResources().getColor(R.color.cell_right), PorterDuff.Mode.MULTIPLY);
                break;
        }
    }
}
