package dev.gmathur;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day03MullItOverTest {
    @Test
    void testDay3() {
        assertEquals(188116424, Day03MullItOver.solve().part1());
        assertEquals(104245808, Day03MullItOver.solve().part2());
    }

}