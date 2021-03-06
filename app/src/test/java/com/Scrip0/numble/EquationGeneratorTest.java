package com.Scrip0.numble;

import static org.junit.Assert.assertEquals;

import com.Scrip0.numble.EquationManagers.EquationGenerator;
import com.Scrip0.numble.EquationManagers.EquationSolver;

import org.junit.Test;

import java.util.HashMap;

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
        HashMap<Character, Integer> priority = new HashMap<>();
        priority.put('+', 1);
        priority.put('-', 1);
        priority.put('*', 2);
        priority.put('/', 2);
        priority.put('^', 3);
        priority.put('!', 4);
        int result = solver.getHighestEquationValueIndex(equation, priority);
        assertEquals(1, result);
    }

    @Test
    public void testSolveOneTask() {
        EquationSolver solver = new EquationSolver();
        String equation = "12+123-14";
        assertEquals("135-14", solver.solveOneTask(equation, 2));
        assertEquals("12+109", solver.solveOneTask(equation, 6));
        equation = "11*10/5";
        assertEquals("110/5", solver.solveOneTask(equation, 2));
        assertEquals("11*2", solver.solveOneTask(equation, 5));
        equation = "2^4!";
        assertEquals("16!", solver.solveOneTask(equation, 1));
        assertEquals("2^24", solver.solveOneTask(equation, 3));
    }

    @Test
    public void testSolve() {
        EquationSolver solver = new EquationSolver();
        String equation = "2+3*(3!/(7-5))+4*(2^3!)/4";
        assertEquals(75, solver.solve(equation));
    }

    @Test
    public void testNegative() {
        EquationSolver solver = new EquationSolver();
        String equation = "-11";
        HashMap<Character, Integer> priority = new HashMap<>();
        priority.put('+', 1);
        priority.put('-', 1);
        priority.put('*', 2);
        priority.put('/', 2);
        priority.put('^', 3);
        priority.put('!', 4);
        assertEquals(true, solver.equationSolved(equation, priority));
        equation = "-242-126";
        assertEquals(true, solver.testIfNegativeNumber(equation, 1));
        equation = "458";
        assertEquals(true, solver.equationSolved(equation, priority));
        equation = "-1^-1=-1";
        assertEquals(-1, solver.solve(equation));
        equation = "937-7^0!*6+1=896";
        assertEquals(896, solver.solve(equation));
        for (int i = 3; i < 100; i++) {
            for (int j = 0; j < 10; j++) {
                EquationGenerator generator = new EquationGenerator(i);
                equation = generator.getEquation();
                System.out.println(i);
                System.out.println(equation);
                assertEquals(equation.split("=")[1], String.valueOf(solver.solve(equation)));
            }
        }
    }
}