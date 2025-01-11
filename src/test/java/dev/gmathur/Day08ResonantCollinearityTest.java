package dev.gmathur;

import org.junit.jupiter.api.Test;

import static dev.gmathur.Util.runTimed;
import static org.junit.jupiter.api.Assertions.*;

class Day08ResonantCollinearityTest {
    @Test
    void testTestInputPart1() {
        var r = Day08ResonantCollinearity.part1("day8/d8_test.input");
        assertEquals(14, r);
    }

    @Test
    void testPuzzleInputPart1() {
        // Run timed
        runTimed(() -> {
            var r = Day08ResonantCollinearity.part1("day8/d8.input");
            assertEquals(285, r);
        });
    }

    @Test
    void testTestInputPart2() {
        var r = Day08ResonantCollinearity.part2("day8/d8_test.input");
        assertEquals(34, r);
    }

    @Test
    void testPuzzleInputPart2() {
        // Run timed
        runTimed(() -> {
            var r = Day08ResonantCollinearity.part2("day8/d8.input");
            assertEquals(944, r);
        });
    }

}