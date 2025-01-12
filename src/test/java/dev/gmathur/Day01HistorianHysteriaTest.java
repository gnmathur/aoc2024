package dev.gmathur;

import static dev.gmathur.Util.runTimed;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;


class Day01HistorianHysteriaTest {
    @Test
    public void testDay1() {
        runTimed(() -> {
            var result = Day01HistorianHysteria.solve();

            assertEquals(1110981, result.part1());
            assertEquals(24869388, result.part2());
        });
    }
}
