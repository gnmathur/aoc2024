package dev.gmathur;

import org.junit.jupiter.api.Test;

import static dev.gmathur.Util.runTimedWithLabel;
import static org.junit.jupiter.api.Assertions.*;

class Day06GuardGallivantTest {

    @Test
    void testTestInputPart1() {
        var r = Day06GuardGallivant.part1("day6/d6_test.input");
        assertEquals(41, r);
    }

    @Test
    void testPuzzleInputPart1() {
        runTimedWithLabel("D6 Part 1", () -> {
            var r = Day06GuardGallivant.part1("day6/d6.input");
            assertEquals(4454, r);
        });
    }

    @Test
    void testTestInputPart2() {
        var r = Day06GuardGallivant.part2("day6/d6_test.input");
        assertEquals(6, r);
    }

    @Test
    void testPuzzleInputPart2() {
        runTimedWithLabel("D6 Part 2", () -> {
            var r = Day06GuardGallivant.part2("day6/d6.input");
            assertEquals(1503, r);
        });
    }


}