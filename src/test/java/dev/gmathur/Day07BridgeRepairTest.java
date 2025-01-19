package dev.gmathur;

import dev.gmathur.problems.Day07BridgeRepair;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static dev.gmathur.utils.Util.runTimedWithLabel;
import static org.junit.jupiter.api.Assertions.*;

class Day07BridgeRepairTest {
    @Test
    void testTestInputPart1() {
        var r = Day07BridgeRepair.part1("day7/d7_test.input");
        assertEquals(BigInteger.valueOf(3749), r);
    }

    @Test
    void testPuzzleInputPart1() {
        runTimedWithLabel("D7 Part 1", () -> {
            var r = Day07BridgeRepair.part1("day7/d7.input");
            assertEquals(BigInteger.valueOf(8401132154762L), r);
        });
    }

    @Test
    void testTestInputPart2() {
        var r = Day07BridgeRepair.part2("day7/d7_test.input");
        assertEquals(BigInteger.valueOf(11387), r);
    }

    @Test
    void testPuzzleInputPart2() {
        runTimedWithLabel("D7 Part 2", () -> {
            var r = Day07BridgeRepair.part2("day7/d7.input");
            assertEquals(new BigInteger("95297119227552"), r);
        });
    }
}