package dev.gmathur;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day06GuardGallivantTest {

    @Test
    void testTestInputPart1() {
        var r = Day06GuardGallivant.part1("day6/d6_test.input");
        assertEquals(41, r);
    }

    @Test
    void testPuzzleInputPart1() {
        var r = Day06GuardGallivant.part1("day6/d6.input");
        assertEquals(4454, r);
    }

    @Test
    void testTestInputPart2() {
        var r = Day06GuardGallivant.part2("day6/d6_test.input");
        assertEquals(6, r);
    }

    @Test
    void testPuzzleInputPart2() {
        var start = System.currentTimeMillis();
        var r = Day06GuardGallivant.part2("day6/d6.input");
        System.out.println("Time taken: " + (System.currentTimeMillis() - start) + "ms");
        assertEquals(1503, r);
    }


}