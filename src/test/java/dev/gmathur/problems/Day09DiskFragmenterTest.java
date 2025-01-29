package dev.gmathur.problems;

import dev.gmathur.utils.Util;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day09DiskFragmenterTest {
    @Test
    void testPart1WithTestInput() {
        var day9DiskFragmenter = new Day09DiskFragmenter();
        var r = day9DiskFragmenter.part1("day9/d9_test.input");
        assertEquals(1928, r);
    }

    @Test
    void testPart1WithProblemInput() {
        Util.runTimed(() -> {
            var day9DiskFragmenter = new Day09DiskFragmenter();
            var r = day9DiskFragmenter.part1("day9/d9.input");
            assertEquals(6398252054886L, r);
        });
    }

    @Test
    void testPart2WithTestInput() {
        var day9DiskFragmenter = new Day09DiskFragmenter();
        var r = day9DiskFragmenter.part2("day9/d9_test.input");
        assertEquals(2858, r);
    }

    @Test
    void testPart2WithProblemInput() {
        Util.runTimed(() -> {
            var day9DiskFragmenter = new Day09DiskFragmenter();
            var r = day9DiskFragmenter.part2("day9/d9.input");
            assertEquals(6415666220005L, r);
        });
    }
}