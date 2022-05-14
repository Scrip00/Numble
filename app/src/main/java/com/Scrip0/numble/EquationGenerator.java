package com.Scrip0.numble;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public class EquationGenerator {
    private String equation;
    private int length;
    private final String[] operators = new String[]{"+", "-", "*", "/", "^", "!"};
    private EquationSolver solver;

    public EquationGenerator(int length) {
        if (length < 3)
            throw new IllegalArgumentException();

        this.length = length;
        solver = new EquationSolver();
        equation = "";
        initializeEquation();
        generateEquation(length);
    }

    private void generateEquation(int length) {
        while (equation.length() != length) {
            Log.d("TEST", equation);
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
                    addDivision();
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

    private void addDivision() {
        int index = pickRandomNumber();
        int number = getNumberFromEquation(index);
        if (number == 1) return;
        int dividend = pickDivident(number);
        if (dividend == -1) return;
        String equation = this.equation;
        equation = equation.substring(0, index) + dividend + "/" + number + equation.substring(index + calcNumLen(number));
        int answer = solver.solve(equation);
        if (answer > 0 && answer < 1000 && this.equation.split("=")[0].length() <= length && solver.isAnswerInt())
            this.equation = equation.split("=")[0] + "=" + answer;
    }

    private void addMultiplication() { // 4*2=8
        // TODO change add f-n
        if (length - equation.length() < 2) return;
        int index = pickRandomNumber();
        int number = getNumberFromEquation(index);
        if (number < 2) return;
        String equation;
        int random;
        equation = this.equation;
        random = pickRandomDivider(number);
        if (random == -1) return;
        while (random == 1 || random == number) {
            random = pickRandomDivider(number);
        }
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
            int index = pickRandomStandaloneNumber();
            int number = getNumberFromEquation(index);
            int numLen = calcNumLen(number);
            number -= random;
            if (number < 1 || areBracketsNeeded(index, number)) return;
            equation = sides[0].substring(0, index) + number + sides[0].substring(index + numLen) + "=" + answer;
            if (equation.length() <= length) this.equation = equation;
        } else {
            if (length - equation.length() < 2) return;
            int index = pickRandomNumber();
            int number = getNumberFromEquation(index);
            if (number == 1) return;
            String equation;
            equation = this.equation;
            int random = getRandomNumber(number + 1, 999);
            if (areBracketsNeeded(index, number) || (index != 0 && equation.charAt(index - 1) == '-')) {
                equation = equation.substring(0, index) + "(" + random + "-" + (random - number) + ")" + equation.substring(index + calcNumLen(number));
            } else {
                equation = equation.substring(0, index) + random + "-" + (random - number) + equation.substring(index + calcNumLen(number));
            }
            if (equation.length() <= length)
                this.equation = equation;
        }
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
            int index = pickRandomStandaloneNumber();
            int number = getNumberFromEquation(index);
            int numLen = calcNumLen(number);
            Log.d("TEST", String.valueOf(areBracketsNeeded(index, number)));
            if (number > 999 || areBracketsNeeded(index, number)) return;
            number += random;
            equation = sides[0].substring(0, index) + number + sides[0].substring(index + numLen) + "=" + answer;
            if (equation.length() <= length) this.equation = equation;
        } else {
            if (length - equation.length() < 2) return;
            int index = pickRandomNumber();
            int number = getNumberFromEquation(index);
            if (number == 1) return;
            String equation;
            equation = this.equation;
            int random = getRandomNumber(1, number - 1);
            if (areBracketsNeeded(index, number)) {
                equation = equation.substring(0, index) + "(" + random + "+" + (number - random) + ")" + equation.substring(index + calcNumLen(number));
            } else {
                equation = equation.substring(0, index) + random + "+" + (number - random) + equation.substring(index + calcNumLen(number));
            }
            if (equation.length() <= length)
                this.equation = equation;
        }
    }

    private boolean areBracketsNeeded(int index, int number) {
        if (index != 0) {
            if (equation.charAt(index - 1) == '*' || equation.charAt(index - 1) == '/' || equation.charAt(index - 1) == '^')
                return true;
        }
        if (index != equation.length() - 1) {
            int numLen = calcNumLen(number);
            if ((index + numLen) < equation.length())
                Log.d("TEST", equation.charAt(index + numLen) + " " + index + " " + numLen + " " + equation);
            if ((index + numLen) < equation.length() && (equation.charAt(index + numLen) == '*' || equation.charAt(index + numLen) == '/' || equation.charAt(index + numLen) == '^' || equation.charAt(index + numLen) == '!'))
                return true;
        }
        return false;
    }

    private int pickRandomDivider(int n) {
        ArrayList<Integer> dividers = new ArrayList<>();
        for (int i = 1; i < Math.sqrt(n); i++) {
            if (n % i == 0) {
                dividers.add(i);
            }
        }
        if (dividers.size() == 1) {
            return -1;
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

    private int pickRandomStandaloneNumber() {
        // TODO чекать скобки, * / ^ делать отдельно в методах
        ArrayList<Integer> numbers = new ArrayList<>();
        if (isNumber(equation.charAt(0))) numbers.add(0);
        int brackets = 0;
        for (int i = 1; i < equation.length(); i++) {
            if (equation.charAt(i) == ')') brackets--;
            if (equation.charAt(i) == '(') brackets++;
            if (isNumber(equation.charAt(i)) && !isNumber(equation.charAt(i - 1)) && brackets == 0 && !areBracketsNeeded(i, getNumberFromEquation(i))) { // 1+(252*1-61)*403=594
                numbers.add(i);
            }
        }
        return numbers.get(getRandomNumber(0, numbers.size() - 1));
    }

    private int pickDivident(int number) {
        ArrayList<Integer> dividents = new ArrayList<>();
        for (int i = number * 2; i < 1000; i++) {
            if (i % number == 0) {
                dividents.add(i);
            }
        }
        if (dividents.size() == 0) return -1;
        return dividents.get(getRandomNumber(0, dividents.size() - 1));
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
