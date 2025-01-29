package dev.gmathur.problems;

import dev.gmathur.problems.Day12GardenGroups.SolutionInput;
import org.junit.jupiter.api.Test;

import static dev.gmathur.problems.Day12GardenGroups.parse;
import static dev.gmathur.utils.Util.runTimedWithLabel;
import static org.junit.jupiter.api.Assertions.*;

class Day12GardenGroupsTest {
    // Create solution input so that it can be used by all tests
    private static final SolutionInput testSolutionInput = parse("day12/d12_test.input");
    private static final SolutionInput puzzleSlutionInput = parse("day12/d12.input");

    @Test
    void testPart1WithTestInput() {
        runTimedWithLabel("D12 Part 1 Test Input", () -> {
            var r = Day12GardenGroups.part1(testSolutionInput);
            assertEquals(1930, r);
        });
    }

    @Test
    void testPart1WithProblemInput() {
        runTimedWithLabel("D12 Part 1 Puzzle Input", () -> {
            var r = Day12GardenGroups.part1(puzzleSlutionInput);
            assertEquals(1550156L, r);
        });
    }

    @Test
    void testPart2WithTestInput() {
        runTimedWithLabel("D12 Part 2 Test Input", () -> {
            var r = Day12GardenGroups.part2(testSolutionInput);
            assertEquals(1206, r);
        });
    }

    @Test
    void testPart2WithProblemInput() {
        runTimedWithLabel("D12 Part 2 Puzzle Input", () -> {
            var r = Day12GardenGroups.part2(puzzleSlutionInput);
            assertEquals(946084, r);
        });
    }
}