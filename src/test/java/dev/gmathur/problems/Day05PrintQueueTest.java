package dev.gmathur.problems;

import org.junit.jupiter.api.Test;

import static dev.gmathur.utils.Util.runTimedWithLabel;
import static org.junit.jupiter.api.Assertions.*;

class Day05PrintQueueTest {
    @Test
    void testDay5() {
        int r1 = Day05PrintQueue.part1("src/main/resources/day5/d5_test.input");
        assertEquals(143, r1);
        int r2 = Day05PrintQueue.part2("src/main/resources/day5/d5_test.input");
        assertEquals(123, r2);

        var time = System.currentTimeMillis();

        runTimedWithLabel("D5 Part 1", () -> {
            int r = Day05PrintQueue.part1("src/main/resources/day5/d5.input");
            assertEquals(7198, r);
        });

        runTimedWithLabel("D5 Part 2", () -> {
            int r = Day05PrintQueue.part2("src/main/resources/day5/d5.input");
            assertEquals(4230, r);
        });
    }

}