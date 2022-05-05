package com.Scrip0.numble;

public class EquationGenerator {
    private String equation;
    private final String[] operators = new String[]{"+", "-", "*", "/", "^", "!"};

    public EquationGenerator(int length) {
        if (length < 3) {
            throw new IllegalArgumentException();
        }
        equation = "";
        initializeEquation();
        generateEquation(length);
    }

    private void generateEquation(int length) {
        while (equation.length() != length) {
            int random = getRandomNumber(0, 7);
            switch (random) {
                case (0):
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
