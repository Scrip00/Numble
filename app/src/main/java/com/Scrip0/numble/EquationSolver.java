package com.Scrip0.numble;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class EquationSolver {

    public int solve(String str) {
        HashMap<Character, Integer> values = new HashMap<>();
        values.put('+', 1);
        values.put('-', 1);
        values.put('*', 2);
        values.put('/', 2);
        values.put('^', 3);
        values.put('!', 3);
        while (str.contains("(")) {
            int[] indexes = getBracketEquation(str);
            String subEquation = str.substring(indexes[0], indexes[1] + 1);
            while (!equationSolved(subEquation, values)) {
                subEquation = solveOneTask(subEquation, getHighestEquationValueIndex(subEquation, values));
            }
            // TODO add subequation to equation
        }

        return 0;
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

    private boolean equationSolved(String str, HashMap<Character, Integer> values) {
        for (char c : values.keySet()) {
            if (str.contains(String.valueOf(c))) return false;
        }
        return true;
    }

    public int getHighestEquationValueIndex(String str, HashMap<Character, Integer> values) {
        int index = 0;
        int maxIndex = -1;
        for (int i = 0; i < str.length(); i++) {
            if (values.containsKey(str.charAt(i)) && values.get(str.charAt(i)) > maxIndex) {
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
                str = str.substring(0, getNumberStartIndex(str, index - 1)) + result + str.substring(index + getNumberLength(str, index + 1) + 1);
                break;
            case '-':
                result = getNumberFromString(str, getNumberStartIndex(str, index - 1)) - getNumberFromString(str, index + 1);
                str = str.substring(0, getNumberStartIndex(str, index - 1)) + result + str.substring(index + getNumberLength(str, index + 1) + 1);
                break;
            case '*':
                result = getNumberFromString(str, getNumberStartIndex(str, index - 1)) * getNumberFromString(str, index + 1);
                str = str.substring(0, getNumberStartIndex(str, index - 1)) + result + str.substring(index + getNumberLength(str, index + 1) + 1);
                break;
            case '/':
                // Assume that division results in integer
                result = getNumberFromString(str, getNumberStartIndex(str, index - 1)) / getNumberFromString(str, index + 1);
                str = str.substring(0, getNumberStartIndex(str, index - 1)) + result + str.substring(index + getNumberLength(str, index + 1) + 1);
                break;
            case '^':
                result = (int) Math.pow(getNumberFromString(str, getNumberStartIndex(str, index - 1)), getNumberFromString(str, index + 1));
                str = str.substring(0, getNumberStartIndex(str, index - 1)) + result + str.substring(index + getNumberLength(str, index + 1) + 1);
                break;
            case '!':
                result = calcFactorial(getNumberFromString(str, getNumberStartIndex(str, index - 1)));
                str = str.substring(0, getNumberStartIndex(str, index - 1)) + result + str.substring(index + 1);
                break;
        }
        return str;
    }

    private int calcFactorial(int n) {
        int result = 1;
        for (int i = 2; i <= n; i++) {
            result*=i;
        }
        return result;
    }

    private int getNumberFromString(String str, int startIndex) {
        String number = "";
        ArrayList<Character> numbers = new ArrayList<>(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'));
        for (int i = startIndex; i <= str.length() - 1; i++) {
            if (numbers.contains(str.charAt(i))) {
                number += str.charAt(i);
            } else break;
        }
        return Integer.parseInt(number);
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
}
