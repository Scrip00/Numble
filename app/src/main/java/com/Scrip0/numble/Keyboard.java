package com.Scrip0.numble;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.LinearLayout;

public class Keyboard extends LinearLayout implements View.OnClickListener {

    public Keyboard(Context context) {
        this(context, null, 0);
    }

    public Keyboard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Keyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private Button mButton1;
    private Button mButton2;
    private Button mButton3;
    private Button mButton4;
    private Button mButton5;
    private Button mButton6;
    private Button mButton7;
    private Button mButton8;
    private Button mButton9;
    private Button mButton0;
    private Button mButtonPlus;
    private Button mButtonMinus;
    private Button mButtonMultiply;
    private Button mButtonDivide;
    private Button mButtonEquals;
    private Button mButtonFactorial;
    private Button mButtonPower;
    private Button mButtonLeftBracket;
    private Button mButtonRightBracket;

    SparseArray<String> keyValues = new SparseArray<>();

    InputConnection inputConnection;

    private void init(Context context, AttributeSet attrs) {

        LayoutInflater.from(context).inflate(R.layout.keyboard, this, true);
        mButton1 = findViewById(R.id.button_1);
        mButton2 = findViewById(R.id.button_2);
        mButton3 = findViewById(R.id.button_3);
        mButton4 = findViewById(R.id.button_4);
        mButton5 = findViewById(R.id.button_5);
        mButton6 = findViewById(R.id.button_6);
        mButton7 = findViewById(R.id.button_7);
        mButton8 = findViewById(R.id.button_8);
        mButton9 = findViewById(R.id.button_9);
        mButton0 = findViewById(R.id.button_0);
        mButtonPlus = findViewById(R.id.button_plus);
        mButtonMinus = findViewById(R.id.button_minus);
        mButtonMultiply = findViewById(R.id.button_multiply);
        mButtonDivide = findViewById(R.id.button_divide);
        mButtonEquals = findViewById(R.id.button_equals);
        mButtonFactorial = findViewById(R.id.button_factorial);
        mButtonPower = findViewById(R.id.button_power);
        mButtonLeftBracket = findViewById(R.id.button_left_bracket);
        mButtonRightBracket = findViewById(R.id.button_right_bracket);

        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);
        mButton3.setOnClickListener(this);
        mButton4.setOnClickListener(this);
        mButton5.setOnClickListener(this);
        mButton6.setOnClickListener(this);
        mButton7.setOnClickListener(this);
        mButton8.setOnClickListener(this);
        mButton9.setOnClickListener(this);
        mButton0.setOnClickListener(this);
        mButtonPlus.setOnClickListener(this);
        mButtonMinus.setOnClickListener(this);
        mButtonMultiply.setOnClickListener(this);
        mButtonDivide.setOnClickListener(this);
        mButtonEquals.setOnClickListener(this);
        mButtonFactorial.setOnClickListener(this);
        mButtonPower.setOnClickListener(this);
        mButtonLeftBracket.setOnClickListener(this);
        mButtonRightBracket.setOnClickListener(this);

        keyValues.put(R.id.button_1, "1");
        keyValues.put(R.id.button_2, "2");
        keyValues.put(R.id.button_3, "3");
        keyValues.put(R.id.button_4, "4");
        keyValues.put(R.id.button_5, "5");
        keyValues.put(R.id.button_6, "6");
        keyValues.put(R.id.button_7, "7");
        keyValues.put(R.id.button_8, "8");
        keyValues.put(R.id.button_9, "9");
        keyValues.put(R.id.button_0, "0");
        keyValues.put(R.id.button_plus, "+");
        keyValues.put(R.id.button_minus, "-");
        keyValues.put(R.id.button_multiply, "*");
        keyValues.put(R.id.button_divide, "/");
        keyValues.put(R.id.button_equals, "=");
        keyValues.put(R.id.button_factorial, "!");
        keyValues.put(R.id.button_power, "^");
        keyValues.put(R.id.button_left_bracket, "(");
        keyValues.put(R.id.button_right_bracket, ")");
    }

    @Override
    public void onClick(View v) {
        if (inputConnection == null) return;

        String value = keyValues.get(v.getId());
        inputConnection.commitText(value, 1);
    }

    public void updateKeyColor(String key, int color) {
        switch (key) {
            case "0":
                mButton0.setBackgroundColor(color);
                break;
            case "1":
                mButton1.setBackgroundColor(color);
                break;
            case "2":
                mButton2.setBackgroundColor(color);
                break;
            case "3":
                mButton3.setBackgroundColor(color);
                break;
            case "4":
                mButton4.setBackgroundColor(color);
                break;
            case "5":
                mButton5.setBackgroundColor(color);
                break;
            case "6":
                mButton6.setBackgroundColor(color);
                break;
            case "7":
                mButton7.setBackgroundColor(color);
                break;
            case "8":
                mButton8.setBackgroundColor(color);
                break;
            case "9":
                mButton9.setBackgroundColor(color);
                break;
            case "+":
                mButtonPlus.setBackgroundColor(color);
                break;
            case "-":
                mButtonMinus.setBackgroundColor(color);
                break;
            case "*":
                mButtonMultiply.setBackgroundColor(color);
                break;
            case "/":
                mButtonDivide.setBackgroundColor(color);
                break;
            case "!":
                mButtonFactorial.setBackgroundColor(color);
                break;
            case "^":
                mButtonPower.setBackgroundColor(color);
                break;
            case "(":
                mButtonLeftBracket.setBackgroundColor(color);
                break;
            case ")":
                mButtonRightBracket.setBackgroundColor(color);
                break;
            case "=":
                mButtonEquals.setBackgroundColor(color);
                break;
        }
    }

    public void setInputConnection(InputConnection ic) {
        this.inputConnection = ic;
    }
}