package dev.gmathur;

import java.util.ArrayList;
import java.util.List;

public class Util {
    public record AocResult<A, B>(A part1, B part2) { }
    public record Pair<A, B>(A first, B second) { }
    public record Triple<A, B, C>(A first, B second, C third) { }

    public static <T> List<T> listExcludingElementAtI(List<T> list, int index) {
        List<T> result = new ArrayList<>(list);
        result.remove(index);
        return result;
    }
}
