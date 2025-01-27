package dev.gmathur.problems;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day16ReindeerMazeTest {
    @Test
    public void testPart1WithTestInput() {
        var r = Day16ReindeerMaze.part1("day16/d16_test1.dat");
        assertEquals(605, r);
    }

}