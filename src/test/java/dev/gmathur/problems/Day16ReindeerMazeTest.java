package dev.gmathur.problems;

import org.junit.jupiter.api.Test;

import static dev.gmathur.utils.Util.runTimedWithLabel;
import static org.junit.jupiter.api.Assertions.*;

class Day16ReindeerMazeTest {
    @Test
    public void testPart1WithTest1Input() {
        runTimedWithLabel("D16 part 1 test 1 input", () -> {
            var r = Day16ReindeerMaze.part1("day16/d16_test1.dat");
            assertEquals(7036, r);
        });
    }

    @Test
    public void testPart1WithTest2Input() {
        runTimedWithLabel("D16 part 1 test 2 input", () -> {
            var r = Day16ReindeerMaze.part1("day16/d16_test2.dat");
            assertEquals(11048, r);
        });
    }

    @Test
    public void testPart1WithProblemInput() {
        runTimedWithLabel("D16 part 1 puzzle input", () -> {
            var r = Day16ReindeerMaze.part1("day16/d16.dat");
            assertEquals(105496, r);
        });
    }

    @Test
    public void testPart2WithTest1Input() {
        runTimedWithLabel("D16 part 2 test 1 input", () -> {
            var r = Day16ReindeerMaze.part2("day16/d16_test1.dat", true);
            assertEquals(45, r);
        });
    }

    @Test
    public void testPart2WithTest2Input() {
        runTimedWithLabel("D16 part 2 test 2 input", () -> {
            var r = Day16ReindeerMaze.part2("day16/d16_test2.dat", true);
            assertEquals(64, r);
        });
    }

    @Test
    public void testPart2WithProblemInput() {
        runTimedWithLabel("D16 part 2 puzzle input", () -> {
            var r = Day16ReindeerMaze.part2("day16/d16.dat", false);
            assertEquals(524, r);
        });
    }
}