package dev.gmathur;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day02RedNosedReportsTest {
    @Test
    public void testPart2() {
        var r = Day02RedNosedReports.solve();

        assertEquals(r.part1(), 502);
        assertEquals(r.part2(), 544);
    }
}
