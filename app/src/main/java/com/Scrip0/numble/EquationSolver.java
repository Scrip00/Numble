package com.Scrip0.numble;

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
            while (!equationSolved(str, values)) {
            }
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

    public void solveOneTask(String str, int index) {

    }
}
