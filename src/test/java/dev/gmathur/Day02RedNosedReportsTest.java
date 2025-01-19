package dev.gmathur;

import dev.gmathur.problems.Day02RedNosedReports;
import org.junit.jupiter.api.Test;

import static dev.gmathur.utils.Util.runTimed;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Day02RedNosedReportsTest {
    @Test
    public void testPart2() {
        runTimed(() -> {
            var r = Day02RedNosedReports.solve();

            assertEquals(502, r.part1());
            assertEquals(544, r.part2());
        });
    }
}
