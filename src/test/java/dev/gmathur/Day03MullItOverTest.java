package dev.gmathur;


import dev.gmathur.problems.Day03MullItOver;
import org.junit.jupiter.api.Test;

import static dev.gmathur.utils.Util.runTimedWithLabel;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Day03MullItOverTest {
    @Test
    void testDay3() {
        runTimedWithLabel("Day 3 for Part 1", () -> {
            assertEquals(188116424, Day03MullItOver.solve().part1());
        });
        runTimedWithLabel("Day 3 for Part 2", () -> {
            assertEquals(104245808, Day03MullItOver.solve().part2());
        });
    }

}