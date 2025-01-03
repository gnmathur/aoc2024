package dev.gmathur;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Util {
    public record AocResult<A, B>(A part1, B part2) { }
    public record Pair<A, B>(A first, B second) { }
    public record Triple<A, B, C>(A first, B second, C third) { }

    public static <T> List<T> listExcludingElementAtI(List<T> list, int index) {
        List<T> result = new ArrayList<>(list);
        result.remove(index);
        return result;
    }

    public static void runTimed(Runnable runnable) {
        long start = System.currentTimeMillis();
        runnable.run();
        long end = System.currentTimeMillis();
        System.out.println("Time taken: " + (end - start) + "ms");
    }
}
