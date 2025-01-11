package dev.gmathur;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;


public class Day01HistorianHysteriaTest {
    @Test
    public void testDay1() {
        System.out.println(Day01HistorianHysteria.solve());

        var result = Day01HistorianHysteria.solve();

        assertEquals(result.part1(), 1110981);
        assertEquals(result.part2(), 24869388);
    }
}
