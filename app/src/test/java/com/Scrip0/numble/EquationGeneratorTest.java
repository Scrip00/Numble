package com.Scrip0.numble;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

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
        assertEquals(result[0], 7);
        assertEquals(result[1], 12);
    }
}