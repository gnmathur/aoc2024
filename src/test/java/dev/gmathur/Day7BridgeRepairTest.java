package dev.gmathur;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day7BridgeRepairTest {
    @Test
    void testTestInputPart1() {
        var r = Day7BridgeRepair.part1("day7/d7_test.input");
        assertEquals(3749, r);
    }

    @Test
    void testPuzzleInputPart1() {
        var r = Day7BridgeRepair.part1("day7/d7.input");
        System.out.println(r);
    }

}