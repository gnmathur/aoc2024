package dev.gmathur;

import dev.gmathur.problems.Day10HoofIt;
import org.junit.jupiter.api.Test;

import static dev.gmathur.utils.Util.runTimed;
import static org.junit.jupiter.api.Assertions.*;

class Day10HoofItTest {
    @Test
    void testTestInputPart1() {
        runTimed(() -> {
            var r = Day10HoofIt.part1("day10/d10_test.input");
            assertEquals(36, r);
        });
    }

    @Test
    void testProblemInputPart1() {
        runTimed(() -> {
            var r = Day10HoofIt.part1("day10/d10.input");
            assertEquals(786, r);
        });
    }

    @Test
    void testTestInputPart2() {
        runTimed(() -> {
            var r = Day10HoofIt.part2("day10/d10_test.input");
            assertEquals(81, r);
        });
    }

    @Test
    void testProblemInputPart2() {
        runTimed(() -> {
            var r = Day10HoofIt.part2("day10/d10.input");
            assertEquals(1722, r);
        });
    }
}