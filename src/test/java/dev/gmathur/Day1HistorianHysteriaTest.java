package dev.gmathur;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Day1HistorianHysteriaTest {

    @Test
    public void testPart1() throws IOException {
        assertTrue(Day1HistorianHysteria.part1() == 1110981);
    }

    @Test
    public void testPart2() throws IOException {
        assertTrue(Day1HistorianHysteria.part2() == 24869388);
    }
}
