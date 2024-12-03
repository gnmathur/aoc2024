package dev.gmathur;

import java.util.ArrayList;
import java.util.List;

public class Util {
    public record AocResult<A, B>(A part1, B part2) {
    }

    public static <T> List<T> listExcludingElementAtI(List<T> list, int index) {
        List<T> result = new ArrayList<>(list);
        result.remove(index); // Removes the element at the specified index
        return result;
    }
}
