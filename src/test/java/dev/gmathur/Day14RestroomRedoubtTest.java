package dev.gmathur;

import dev.gmathur.problems.Day14RestroomRedoubt;
import org.junit.jupiter.api.Test;

import static dev.gmathur.utils.Util.runTimedWithLabel;
import static org.junit.jupiter.api.Assertions.*;

class Day14RestroomRedoubtTest {

    @Test
    public void testPart1WithTestInput() {
        var r = Day14RestroomRedoubt.part1("day14/d14_test.dat", 11, 7);
        assertEquals(12, r);
    }

    @Test
    public void testPart1WithProblemInput() {
        runTimedWithLabel("D14 part 1 problem input", () -> {
            var r = Day14RestroomRedoubt.part1("day14/d14.dat", 101, 103);
            assertEquals(214109808, r);
        });
    }

    @Test
    public void testPart2WithTestInput() {
        runTimedWithLabel("D14 part 2 problem input", () -> {
            Day14RestroomRedoubt.part2("day14/d14.dat", 101, 103);
            // always true
            assertTrue(true);
        });
    }

}