package com.Scrip0.numble;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class Cell extends FrameLayout {

    public static final int DEFAULT = 0;
    public static final int WRONG = 1;
    public static final int CLOSE = 2;
    public static final int RIGHT = 3;

    private String content;
    private EditText textField;

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
        textField = findViewById(R.id.editText);
        textField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (textField.getText().length() == 1) {
                    textField.clearFocus();
                }
            }
        });
    }

    public void enableFocus() {
        textField.setEnabled(true);
    }

    public void disableFocus() {
        textField.setEnabled(false);
    }

    public void setBackground(int background) {
        switch (background) {
            case DEFAULT:
                findViewById(R.id.layoutID).getBackground().setColorFilter(getResources().getColor(R.color.cell_default), PorterDuff.Mode.MULTIPLY);
                break;
            case WRONG:
                findViewById(R.id.layoutID).getBackground().setColorFilter(getResources().getColor(R.color.cell_wrong), PorterDuff.Mode.MULTIPLY);
                break;
            case CLOSE:
                findViewById(R.id.layoutID).getBackground().setColorFilter(getResources().getColor(R.color.cell_close), PorterDuff.Mode.MULTIPLY);
                break;
            case RIGHT:
                findViewById(R.id.layoutID).getBackground().setColorFilter(getResources().getColor(R.color.cell_right), PorterDuff.Mode.MULTIPLY);
                break;
        }
    }
}
