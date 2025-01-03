package dev.gmathur;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class Day7BridgeRepairTest {
    @Test
    void testTestInputPart1() {
        var r = Day7BridgeRepair.part1("day7/d7_test.input");
        assertEquals(BigInteger.valueOf(3749), r);
    }

    @Test
    void testPuzzleInputPart1() {
        var r = Day7BridgeRepair.part1("day7/d7.input");
        // compare to 8401132154762
        assertEquals(BigInteger.valueOf(8401132154762L), r);
    }

}