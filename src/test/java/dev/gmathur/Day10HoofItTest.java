package dev.gmathur;

import org.junit.jupiter.api.Test;

import static dev.gmathur.Util.runTimed;
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

}