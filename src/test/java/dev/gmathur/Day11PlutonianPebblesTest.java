package dev.gmathur;

import dev.gmathur.problems.Day11PlutonianPebbles;
import org.junit.jupiter.api.Test;

import static dev.gmathur.utils.Util.runTimedWithLabel;
import static org.junit.jupiter.api.Assertions.*;

class Day11PlutonianPebblesTest {

    @Test
    void testPart1WithTestInput() {
        var r = Day11PlutonianPebbles.part1("day11/d11_test.input");
        assertEquals(55312, r);
    }

    @Test
    void testPart1WithProblemInput() {
        runTimedWithLabel("D11 Part 1", () -> {
            var r = Day11PlutonianPebbles.part1("day11/d11.input");
            assertEquals(198089, r);
        });
    }

    @Test
    void testPart2WithProblemInput() {
        runTimedWithLabel("D11 Part 2", () -> {
            var r = Day11PlutonianPebbles.part2("day11/d11.input");
            assertEquals(236302670835517L, r);
        });
    }
}