package com.Scrip0.numble.EquationManagers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class EquationSolver {

    private boolean isAnswerInt;

    public int solve(String str) {
        isAnswerInt = true;
        HashMap<Character, Integer> priority = new HashMap<>();
        priority.put('+', 1);
        priority.put('-', 1);
        priority.put('*', 2);
        priority.put('/', 2);
        priority.put('^', 3);
        priority.put('!', 4);
        if (str.contains("="))
            str = str.split("=")[0];
        while (str.contains("(")) {
            int[] indexes = getBracketEquation(str);
            String subEquation = str.substring(indexes[0] + 1, indexes[1]);
            while (!equationSolved(subEquation, priority)) {
                subEquation = solveOneTask(subEquation, getHighestEquationValueIndex(subEquation, priority));
                subEquation = getRidOfMinus(subEquation);
            }
            str = str.substring(0, indexes[0]) + subEquation + str.substring(indexes[1] + 1);
        }
        while (!equationSolved(str, priority)) {
            str = solveOneTask(str, getHighestEquationValueIndex(str, priority));
            str = getRidOfMinus(str);
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return (int) Math.sqrt(Integer.MAX_VALUE);
        }
    }

    public int[] getBracketEquation(String str) {
        int[] indexes = new int[2];
        int bracketNum = 0, maxBracketNum = -1;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '(') {
                bracketNum++;
                if (bracketNum > maxBracketNum) {
                    maxBracketNum = bracketNum;
                    indexes[0] = i;
                    indexes[1] = -1;
                }
            } else if (str.charAt(i) == ')') {
                bracketNum--;
                if (indexes[1] == -1) indexes[1] = i;
            }
        }
        return indexes;
    }

    public boolean equationSolved(String str, HashMap<Character, Integer> values) {
        for (char c : values.keySet()) {
            if (str.contains(String.valueOf(c)) && str.substring(1).contains(String.valueOf(c))) {
                return false;
            }
        }
        return true;
    }

    private String getRidOfMinus(String str) {
        for (int i = 0; i < str.length() - 1; i++) {
            if (str.charAt(i) == str.charAt(i + 1) && str.charAt(i + 1) == '-') {
                if (i == 0) {
                    str = str.substring(2);
                } else {
                    str = str.substring(0, i) + "+" + str.substring(i + 2);
                }
            }
        }
        return str;
    }

    public int getHighestEquationValueIndex(String str, HashMap<Character, Integer> values) {
        int index = 0;
        int maxIndex = -1;
        for (int i = 0; i < str.length(); i++) {
            if (values.containsKey(str.charAt(i)) && values.get(str.charAt(i)) > maxIndex && !testIfNegativeNumber(str, i + 1)) {
                index = i;
                maxIndex = values.get(str.charAt(i));
            }
        }
        return index;
    }

    public String solveOneTask(String str, int index) {
        int result;
        switch (str.charAt(index)) {
            case '+':
                result = getNumberFromString(str, getNumberStartIndex(str, index - 1)) + getNumberFromString(str, index + 1);
                if (testIfNegativeNumber(str, getNumberStartIndex(str, index - 1))) {
                    str = str.substring(0, getNumberStartIndex(str, index - 1) - 1) + result + str.substring(index + getNumberLength(str, index + 1) + 1);
                } else {
                    str = str.substring(0, getNumberStartIndex(str, index - 1)) + result + str.substring(index + getNumberLength(str, index + 1) + 1);
                }
                break;
            case '-':
                result = getNumberFromString(str, getNumberStartIndex(str, index - 1)) - getNumberFromString(str, index + 1);
                if (testIfNegativeNumber(str, getNumberStartIndex(str, index - 1))) {
                    str = str.substring(0, getNumberStartIndex(str, index - 1) - 1) + result + str.substring(index + getNumberLength(str, index + 1) + 1);
                } else {
                    str = str.substring(0, getNumberStartIndex(str, index - 1)) + result + str.substring(index + getNumberLength(str, index + 1) + 1);
                }
                break;
            case '*':
                result = getNumberFromString(str, getNumberStartIndex(str, index - 1)) * getNumberFromString(str, index + 1);
                str = str.substring(0, getNumberStartIndex(str, index - 1)) + result + str.substring(index + getNumberLength(str, index + 1) + 1);
                break;
            case '/':
                if (getNumberFromString(str, index + 1) == 0) {
                    str = str.substring(0, getNumberStartIndex(str, index - 1)) + (int) Math.sqrt(Integer.MAX_VALUE) + str.substring(index + getNumberLength(str, index + 1) + 1);
                    break;
                }
                if ((getNumberFromString(str, getNumberStartIndex(str, index - 1)) % getNumberFromString(str, index + 1)) != 0)
                    isAnswerInt = false;
                result = getNumberFromString(str, getNumberStartIndex(str, index - 1)) / getNumberFromString(str, index + 1);
                str = str.substring(0, getNumberStartIndex(str, index - 1)) + result + str.substring(index + getNumberLength(str, index + 1) + 1);
                break;
            case '^':
                result = (int) Math.pow(getNumberFromString(str, getNumberStartIndex(str, index - 1)), getNumberFromString(str, index + 1));
                if (result == 0) result = (int) Math.sqrt(Integer.MAX_VALUE);
                if (testIfNegativeNumber(str, getNumberStartIndex(str, index - 1))) {
                    str = str.substring(0, getNumberStartIndex(str, index - 1) - 1) + result + str.substring(index + getNumberLength(str, index + 1) + 1);
                } else {
                    str = str.substring(0, getNumberStartIndex(str, index - 1)) + result + str.substring(index + getNumberLength(str, index + 1) + 1);
                }
                break;
            case '!':
                result = calcFactorial(getNumberFromString(str, getNumberStartIndex(str, index - 1)));
                str = str.substring(0, getNumberStartIndex(str, index - 1)) + result + str.substring(index + 1);
                break;
        }
        return str;
    }

    private int calcFactorial(int n) {
        if (n > 10) return (int) Math.sqrt(Integer.MAX_VALUE);
        int result = 1;
        if (n < 0) n = -n;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    private int getNumberFromString(String str, int startIndex) {
        String number = "";
        ArrayList<Character> numbers = new ArrayList<>(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'));
        if (!numbers.contains(str.charAt(startIndex))) startIndex++;
        for (int i = startIndex; i <= str.length() - 1; i++) {
            if (numbers.contains(str.charAt(i))) {
                number += str.charAt(i);
            } else break;
        }
        int returnValue;
        try {
            if (testIfNegativeNumber(str, startIndex)) {
                returnValue = -Integer.parseInt(number);
            } else
                returnValue = Integer.parseInt(number);
        } catch (NumberFormatException e) {
            System.out.println("WTF");
            returnValue = (int) Math.sqrt(Integer.MAX_VALUE);
        }
        return returnValue;
    }

    public boolean testIfNegativeNumber(String str, int startIndex) {
        ArrayList<Character> numbers = new ArrayList<>(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'));
        if (startIndex - 1 >= 0 && str.charAt(startIndex - 1) == '-' && (startIndex - 2 < 0 || !numbers.contains(str.charAt(startIndex - 2)))) {
            return true;
        }
        return false;
    }

    private int getNumberStartIndex(String str, int index) {
        int startIndex = index;
        ArrayList<Character> numbers = new ArrayList<>(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'));
        for (int i = index - 1; i >= 0; i--) {
            if (numbers.contains(str.charAt(i))) {
                startIndex--;
            } else break;
        }
        return startIndex;
    }

    private int getNumberLength(String str, int startIndex) {
        int length = 1;
        ArrayList<Character> numbers = new ArrayList<>(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'));
        for (int i = startIndex + 1; i <= str.length() - 1; i++) {
            if (numbers.contains(str.charAt(i))) {
                length++;
            } else break;
        }
        return length;
    }

    public boolean isAnswerInt() {
        return isAnswerInt;
    }
}
