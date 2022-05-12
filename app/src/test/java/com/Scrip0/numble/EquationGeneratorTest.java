package com.Scrip0.numble;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.HashMap;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class EquationGeneratorTest {
    @Test
    public void testGetBracketEquation() {
        EquationSolver solver = new EquationSolver();
        String equation = "1+(1-3*(3!/2))";
        int[] result = solver.getBracketEquation(equation);
        assertEquals(7, result[0]);
        assertEquals(12, result[1]);
    }
    @Test
    public void testGetHighestEquationValueIndex() {
        EquationSolver solver = new EquationSolver();
        String equation = "3!/2+1";
        HashMap<Character, Integer> values = new HashMap<>();
        values.put('+', 1);
        values.put('-', 1);
        values.put('*', 2);
        values.put('/', 2);
        values.put('^', 3);
        values.put('!', 3);
        int result = solver.getHighestEquationValueIndex(equation, values);
        assertEquals(1, result);
    }

    @Test
    public void testSolveOneTask() {
        EquationSolver solver = new EquationSolver();
        String equation = "12+123-14";
        assertEquals("135-14", solver.solveOneTask(equation, 2));
        assertEquals("12+109", solver.solveOneTask(equation, 6));
    }
}