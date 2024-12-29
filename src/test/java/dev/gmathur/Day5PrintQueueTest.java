package dev.gmathur;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day5PrintQueueTest {
    @Test
    void testDay5() {
        int r1 = Day5PrintQueue.part1("src/main/resources/day5/d5_test.input");
        assertEquals(143, r1);
        int r2 = Day5PrintQueue.part2("src/main/resources/day5/d5_test.input");
        assertEquals(123, r2);

        var time = System.currentTimeMillis();

        int r3 = Day5PrintQueue.part1("src/main/resources/day5/d5.input");
        assertEquals(7198, r3);

        int r4 = Day5PrintQueue.part2("src/main/resources/day5/d5.input");
        assertEquals(4230, r4);

        System.out.println("Time taken: " + (System.currentTimeMillis() - time) + "ms");
    }

}