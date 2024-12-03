package dev.gmathur;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day3MullItOverTest {
    @Test
    void testDay3() {
        assertEquals(188116424, Day3MullItOver.solve().part1());
        assertEquals(104245808, Day3MullItOver.solve().part2());
    }

}