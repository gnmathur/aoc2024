package dev.gmathur;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day2RedNosedReportsTest {
    @Test
    public void testPart2() {
        var r = Day2RedNosedReports.solve();

        assertEquals(r.part1(), 502);
        assertEquals(r.part2(), 544);
    }
}
