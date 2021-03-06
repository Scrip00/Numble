package com.Scrip0.numble.CustomLayouts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.Scrip0.numble.R;

@SuppressLint("ViewConstructor")
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
    private final Keyboard keyboard;
    private int color;

    public Cell(@NonNull Context context, Keyboard keyboard) {
        super(context);
        this.keyboard = keyboard;
        initView();
        color = DEFAULT;
    }

    public Cell(@NonNull Context context) {
        super(context);
        initView();
        disableFocus();
        this.keyboard = null;
        color = DEFAULT;
    }

    private void initView() {
        inflate(getContext(), R.layout.cell_layout, this);
        layout = findViewById(R.id.layoutID);
        textField = findViewById(R.id.editText);
        textField.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) { // If focus gained, setup input connection
                    textField.setRawInputType(InputType.TYPE_CLASS_TEXT);
                    textField.setTextIsSelectable(true);
                    textField.setShowSoftInputOnFocus(false);

                    InputConnection ic = textField.onCreateInputConnection(new EditorInfo());
                    keyboard.setInputConnection(ic);
                }

                if (b && textField.getText().length() > 0) { // If focused gained, delete and remember previous content
                    prevContent = String.valueOf(textField.getText());
                    restoredText = false;
                    textField.setText("");
                }

                if (!b && textField.getText().length() < 1 && prevContent != null && prevContent.length() > 0) { // If focus lost, restore previous text
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
                if (textField.getText().length() > 0 && !restoredText) { // If new text and text is not restored
                    textField.clearFocus();
                    restoredText = false;
                    if (next != null)
                        next.setFocus();
                }
            }
        });
    }

    @Override
    public void clearFocus() {
        textField.clearFocus();
        super.clearFocus();
    }

    @Override
    public boolean isFocused() {
        return textField.isFocused();
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

    public EditText getEditText() {
        return textField;
    }

    public View getView() {
        return this;
    }

    public void setSize(int width, int height) {
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        params.width = width;
        params.height = height;
        layout.setLayoutParams(params);
        textField.setTextSize((float) width / 6);

        GradientDrawable background = (GradientDrawable) layout.getBackground();
        background.setCornerRadius((float) width / 5);
        background.setStroke(width / 14, Color.BLACK);
    }

    public void setBackground(int background) {
        color = background;
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

    public int getBackgroundColor() {
        return color;
    }
}
