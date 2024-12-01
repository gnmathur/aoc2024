package dev.gmathur;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;


public class Day1HistorianHysteriaTest {
    @Test
    public void testDay1() {
        System.out.println(Day1HistorianHysteria.solve());

        var result = Day1HistorianHysteria.solve();

        assertEquals(result.part1(), 1110981);
        assertEquals(result.part2(), 24869388);
    }
}
