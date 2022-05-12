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
        while (str.contains("(")){

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
}
