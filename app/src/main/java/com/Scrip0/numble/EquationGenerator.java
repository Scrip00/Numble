package com.Scrip0.numble;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public class EquationGenerator {
    private String equation;
    private int length;
    private final String[] operators = new String[]{"+", "-", "*", "/", "^", "!"};

    public EquationGenerator(int length) {
        if (length < 3)
            throw new IllegalArgumentException();

        this.length = length;
        equation = "";
        initializeEquation();
        generateEquation(length);
    }

    private void generateEquation(int length) {
        while (equation.length() != length) {
            int random = getRandomNumber(0, 7);
            switch (random) {
                case (0):
                    addSum();
                    break;
                case (1):
                    addSubtraction();
                    break;
                case (2):
                    addMultiplication();
                    break;
                case (3):
                    break;
                case (4):
                    break;
                case (5):
                    break;
                case (6):
                    break;
            }
        }
    }

    private void addMultiplication() {
        if (length - equation.length() < 2) return;
        int index = pickRandomNumber();
        int number = getNumberFromEquation(index);
        if (number < 6) return;
        String equation;
        equation = this.equation;
        int random = pickRandomDivider(number);
        if (random == 1) return;
        equation = equation.substring(0, index) + number / random + "*" + random + equation.substring(index + calcNumLen(number));
        if (equation.length() <= length)
            this.equation = equation;
    }

    private void addSubtraction() {
        // TODO add expansion, recalc function, restart
        int choice = getRandomNumber(0, 100);
        if (choice < 10) {
            String equation = this.equation;
            String[] sides = equation.split("=");
            int answer = Integer.parseInt(sides[1]);
            int random = getRandomNumber(1, answer);
            answer -= random;
            int index = pickRandomNumber();
            int number = getNumberFromEquation(index);
            int numLen = calcNumLen(number);
            number -= random;
            if (number < 1) return;
            equation = sides[0].substring(0, index) + number + sides[0].substring(index + numLen) + "=" + answer;
            if (equation.length() <= length) this.equation = equation;
        } else {
            if (length - equation.length() < 2) return;
            int index = pickRandomNumber();
            int number = getNumberFromEquation(index);
            if (number == 1) return;
            String equation;
            do {
                equation = this.equation;
                int random = getRandomNumber(number + 1, 999);
                equation = equation.substring(0, index) + random + "-" + (random - number) + equation.substring(index + calcNumLen(number));
            } while (equation.length() > length);
            this.equation = equation;
        }
        Log.d("TEST", this.equation);

    }

    private void addSum() {
        // TODO add expansion, recalc function, restart
        int choice = getRandomNumber(0, 100);
        if (choice < 10) {
            String equation = this.equation;
            String[] sides = equation.split("=");
            int answer = Integer.parseInt(sides[1]);
            int random = getRandomNumber(1, 999 - answer);
            answer += random;
            int index = pickRandomNumber();
            int number = getNumberFromEquation(index);
            int numLen = calcNumLen(number);
            number += random;
            if (number > 999) return;
            equation = sides[0].substring(0, index) + number + sides[0].substring(index + numLen) + "=" + answer;
            if (equation.length() <= length) this.equation = equation;
        } else {
            if (length - equation.length() < 2) return;
            int index = pickRandomNumber();
            int number = getNumberFromEquation(index);
            if (number == 1) return;
            String equation;
            do {
                equation = this.equation;
                int random = getRandomNumber(1, number - 1);
                equation = equation.substring(0, index) + random + "+" + (number - random) + equation.substring(index + calcNumLen(number));
            } while (equation.length() > length);
            this.equation = equation;
        }
        Log.d("TEST", this.equation);
    }

    private int pickRandomDivider(int n) {
        ArrayList<Integer> dividers = new ArrayList<>();
        for (int i = 1; i < Math.sqrt(n); i++) {
            if (n % i == 0) {
                dividers.add(i);
            }
        }
        int rand = getRandomNumber(0, dividers.size());
        return dividers.get(rand);
    }

    private int pickRandomNumber() {
        ArrayList<Integer> numbers = new ArrayList<>();
        if (isNumber(equation.charAt(0))) numbers.add(0);
        for (int i = 1; i < equation.length(); i++) {
            if (isNumber(equation.charAt(i)) && !isNumber(equation.charAt(i - 1))) {
                numbers.add(i);
            }
        }
        return numbers.get(getRandomNumber(0, numbers.size() - 1));
    }

    private int getNumberFromEquation(int i) {
        String number = "";
        while (i != equation.length() && isNumber(equation.charAt(i))) {
            number += equation.charAt(i);
            i++;
        }
        return Integer.parseInt(number);
    }

    private int calcNumLen(int n) {
        int c = 0;
        if (n == 0) return 1;
        while (n > 0) {
            c++;
            n = n / 10;
        }
        return c;
    }

    private boolean isNumber(char c) {
        ArrayList<Character> numbers = new ArrayList<>(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'));
        return numbers.contains(c);
    }

    private void initializeEquation() {
        int random = getRandomNumber(1, 10);
        equation += random + "=" + random;
    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public void generateNewEquation(int length) {
        equation = "";
        initializeEquation();
        generateEquation(length);
    }

    public String getEquation() {
        return equation;
    }
}
