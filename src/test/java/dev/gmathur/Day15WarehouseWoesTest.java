package dev.gmathur;

import dev.gmathur.problems.Day15WarehouseWoes;
import org.junit.jupiter.api.Test;

import static dev.gmathur.utils.Util.runTimedWithLabel;
import static org.junit.jupiter.api.Assertions.*;

class Day15WarehouseWoesTest {
    @Test
    public void testPart1WithTestInput1() {
        var r = Day15WarehouseWoes.part1("day15/d15_test.dat");
        assertEquals(10092, r);
    }

    @Test
    public void testPart1WithTestInput2() {
        var r = Day15WarehouseWoes.part1("day15/d15_test2.dat");
        assertEquals( 2028, r);
    }

    @Test
    public void testPart1WithProblemInput() {
        runTimedWithLabel("D15 part 1 problem input", () -> {
            var r = Day15WarehouseWoes.part1("day15/d15.dat");
            assertEquals(1448589, r);
        });
    }

    @Test
    public void testPart2WithTestInput3() {
        var r = Day15WarehouseWoes.part2("day15/d15_test3.dat");
        assertEquals(618, r);
    }

    @Test
    public void testPart2WithTestInput4() {
        var r = Day15WarehouseWoes.part2("day15/d15_test4.dat");
    }

    @Test
    public void testPart2WithTestInput() {
        var r = Day15WarehouseWoes.part2("day15/d15_test.dat");
        assertEquals(618, r);
    }
    @Test
    public void testPart2WithProblemInput() {
        var r = Day15WarehouseWoes.part2("day15/d15.dat");
        assertEquals(618, r);
    }
}