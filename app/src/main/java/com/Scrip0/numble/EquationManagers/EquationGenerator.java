package com.Scrip0.numble.EquationManagers;

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

    public EquationGenerator(int length, boolean withMult, boolean withPower, boolean withFact) {
        if (length < 3)
            throw new IllegalArgumentException();
        this.length = length;
        solver = new EquationSolver();
        equation = "";
        initializeEquation();
        generateEquationWithRestrictions(length, withMult, withPower, withFact);
    }

    private void generateEquationWithRestrictions(int length, boolean withMult, boolean withPower, boolean withFact) {
        int loopNum = 0;
        while (equation.length() != length) {
            loopNum++;
            if (loopNum > 5000 * length) {
                loopNum = 0;
                initializeEquation();
            }
            int random = getRandomNumber(0, 6);
            switch (random) {
                case (0):
                    addSum();
                    break;
                case (1):
                    addSubtraction();
                    break;
                case (2):
                    if (withMult)
                        addMultiplication();
                    break;
                case (3):
                    if (withMult)
                        addDivision();
                    break;
                case (4):
                    if (withFact)
                        addFactorial();
                    break;
                case (5):
                    if (withPower)
                        addPower();
                    break;
            }
        }
    }

    private void generateEquation(int length) {
        int loopNum = 0;
        while (equation.length() != length) {
            loopNum++;
            if (loopNum > 1000 * length) {
                loopNum = 0;
                initializeEquation();
            }
            int random = getRandomNumber(0, 6);
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
                    addFactorial();
                    break;
                case (5):
                    addPower();
                    break;
            }
        }
    }

    private void addPower() {
        if (getRandomNumber(0, 100) > 10) return;
        int index = pickRandomNumber();
        int number = getNumberFromEquation(index);
        if (number <= 1) return;
        if (this.equation.charAt(index + calcNumLen(number)) == '!' || index == 1 && this.equation.charAt(0) == '-')
            return;
        int[] power = getClosestPower(number);
        if (power[0] == 1) return;
        String equation = this.equation.substring(0, index) + power[0] + "^" + power[1] + this.equation.substring(index + calcNumLen(number));
        int answer = solver.solve(equation);
        if (answer > 0 && answer < 1000 && equation.split("=")[0].length() + 1 + String.valueOf(answer).length() <= length && solver.isAnswerInt())
            this.equation = equation.split("=")[0] + "=" + answer;
    }

    private void addFactorial() {
        if (getRandomNumber(0, 100) > 10) return;
        int index = pickRandomNumber();
        int number = getNumberFromEquation(index);
        if (number <= 1) return;
        if (this.equation.charAt(index + calcNumLen(number)) == '!') return;
        int factorial = getClosestFactorial(number);
        String equation = this.equation.substring(0, index) + factorial + "!" + this.equation.substring(index + calcNumLen(number));
        int answer = solver.solve(equation);
        if (answer > 0 && answer < 1000 && equation.split("=")[0].length() + 1 + String.valueOf(answer).length() <= length && solver.isAnswerInt())
            this.equation = equation.split("=")[0] + "=" + answer;
    }

    private void addDivision() {
        int index = pickRandomNumber();
        int number = getNumberFromEquation(index);
        if (number <= 1 && number >= -1) return;
        int dividend = pickDivident(number);
        if (dividend == -1) return;
        String equation = this.equation;
        equation = equation.substring(0, index) + dividend + "/" + number + equation.substring(index + calcNumLen(number));
        int answer = solver.solve(equation);
        if (answer > 0 && answer < 1000 && equation.split("=")[0].length() + 1 + String.valueOf(answer).length() <= length && solver.isAnswerInt())
            this.equation = equation.split("=")[0] + "=" + answer;
    }

    private void addMultiplication() {
        int index = pickRandomNumber();
        int number = getNumberFromEquation(index);
        if (number <= 1 && number >= -1) return;
        int random = pickRandomDivider(number);
        if (random == -1) return;
        while (random == 1 || random == number) {
            random = pickRandomDivider(number);
        }
        String equation = this.equation;
        equation = equation.substring(0, index) + number / random + "*" + random + equation.substring(index + calcNumLen(number));
        int answer = solver.solve(equation);
        if (answer > 0 && answer < 1000 && equation.split("=")[0].length() + 1 + String.valueOf(answer).length() <= length && solver.isAnswerInt())
            this.equation = equation.split("=")[0] + "=" + answer;
    }

    private void addSubtraction() {
        int choice = getRandomNumber(0, 100);
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

    private void addSum() {
        int choice = getRandomNumber(0, 100);
        if (choice < 10) {
            int index = pickRandomNumber();
            int number = getNumberFromEquation(index);
            int random = getRandomNumber(1, 1000 - number);
            String equation = this.equation.split("=")[0];
            EquationSolver solver = new EquationSolver();
            int isNegative = 0;
            if (solver.testIfNegativeNumber(equation, index)) isNegative = 1;
            int sum = number + random;
            if (isNegative == 1) sum = number - random;
            equation = equation.substring(0, index - isNegative) + sum + equation.substring(index + calcNumLen(number));
            int answer = solver.solve(equation);
            if (answer > -1000 && answer < 1000 && equation.split("=")[0].length() + 1 + String.valueOf(answer).length() <= length && solver.isAnswerInt())
                this.equation = equation.split("=")[0] + "=" + answer;
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
            if (equation.charAt(index - 1) == '*' || equation.charAt(index - 1) == '/' || equation.charAt(index - 1) == '^' || equation.charAt(index - 1) == '-')
                return true;
        }
        if (index != equation.length() - 1) {
            int numLen = calcNumLen(number);
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
//
//    private int pickRandomStandaloneNumber() {
//        ArrayList<Integer> numbers = new ArrayList<>();
//        if (isNumber(equation.charAt(0))) numbers.add(0);
//        int brackets = 0;
//        for (int i = 1; i < equation.length(); i++) {
//            if (equation.charAt(i) == ')') brackets--;
//            if (equation.charAt(i) == '(') brackets++;
//            if (isNumber(equation.charAt(i)) && !isNumber(equation.charAt(i - 1)) && brackets == 0 && !areBracketsNeeded(i, getNumberFromEquation(i))) { // 1+(252*1-61)*403=594
//                numbers.add(i);
//            }
//        }
//        return numbers.get(getRandomNumber(0, numbers.size() - 1));
//    }

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

    private int getClosestFactorial(int n) {
        int f = 1;
        int difference;
        for (int i = 1; i <= n; i++) {
            f *= i;
            difference = n - f;
            if (difference < 0) {
                return i;
            }
        }
        return 0;
    }

    private int[] getClosestPower(int n) {
        ArrayList<int[]> powers = new ArrayList<>();
        for (int i = 2; i <= Math.sqrt(n); i++) {
            for (int j = 1; j <= Math.sqrt(n) * 2; j++) {
                if (Math.pow(i, j) - n > (float) n * 0.1 && j - 1 != 1) {
                    powers.add(new int[]{i, j - 1});
                    break;
                }
            }
        }
        if (powers.size() == 0) return new int[]{-1, -1};
        return powers.get(getRandomNumber(0, powers.size() - 1));
    }

    private boolean isNumber(char c) {
        ArrayList<Character> numbers = new ArrayList<>(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'));
        return numbers.contains(c);
    }

    private void initializeEquation() {
        int random = getRandomNumber(1, 10);
        equation = random + "=" + random;
    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public void generateNewEquation(int length) {
        initializeEquation();
        generateEquation(length);
    }

    public String getEquation() {
        return equation;
    }
}
