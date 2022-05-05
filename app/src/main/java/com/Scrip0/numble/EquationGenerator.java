package com.Scrip0.numble;

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
                    break;
                case (2):
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

    private void addSum() {
        // TODO add expansion
        if (length - equation.length() < 2) return;

    }

    private int pickRandomNumber() {
        ArrayList<Integer> numbers = new ArrayList<>();
        if (isNumber(equation.charAt(0))) numbers.add(0);
        for (int i = 1; i < equation.length(); i++) {
            if (isNumber(equation.charAt(i)) && !isNumber(equation.charAt(i - 1))){
                numbers.add(i);
            }
        }
        return numbers.get(getRandomNumber(0, numbers.size()));
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
