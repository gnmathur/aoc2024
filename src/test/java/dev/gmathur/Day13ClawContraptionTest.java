package dev.gmathur;

import org.junit.jupiter.api.Test;

import static dev.gmathur.Util.runTimedWithLabel;
import static org.junit.jupiter.api.Assertions.*;

class Day13ClawContraptionTest {
    @Test
    public void testPart1WithTestInput() {
        runTimedWithLabel("D13 Part 1 Test Input", () -> {
            var r = Day13ClawContraption.part1("day13/d13_test.input");
            assertEquals(480.0, r);
        });
    }

    @Test
    public void testPart1WithProblemInput() {
        runTimedWithLabel("D13 Part 1 Puzzle Input", () -> {
            var r = Day13ClawContraption.part1("day13/d13.input");
            assertEquals(33921.0, r);
        });
    }

    @Test
    public void testPart2WithTestInput() {
        runTimedWithLabel("D13 Part 2 Test Input", () -> {
            var r = Day13ClawContraption.part2("day13/d13_test.input");
            assertEquals(875318608908L, r);
        });
    }

    @Test
    public void testPart2WithPuzzleInput() {
        runTimedWithLabel("D13 Part 2 Puzzle Input", () -> {
            var r = Day13ClawContraption.part2("day13/d13.input");
            assertEquals(82261957837868L, r);
        });
    }
}