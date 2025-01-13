package dev.gmathur;

import org.junit.jupiter.api.Test;

import static dev.gmathur.Util.runTimedWithLabel;
import static org.junit.jupiter.api.Assertions.*;

class Day12GardenGroupsTest {
    @Test
    void testPart1WithTestInput() {
        runTimedWithLabel("D12 Part 1", () -> {
            var r = Day12GardenGroups.part1("day12/d12_test.input");
            assertEquals(1930, r);
        });
    }

    @Test
    void testPart1WithProblemInput() {
        runTimedWithLabel("D12 Part 2", () -> {
            var r = Day12GardenGroups.part1("day12/d12.input");
            assertEquals(1550156L, r);
        });
    }
}