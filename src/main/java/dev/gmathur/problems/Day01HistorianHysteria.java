package dev.gmathur.problems;

import dev.gmathur.utils.Util.AocResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day01HistorianHysteria {
    private record GroupLocation(int left, int right) { }

    private static List<GroupLocation> parse() {
        try (var lines = Files.lines(new File("src/main/resources/day1/input_d1_historian_hysteria.lst").toPath())) {
            return lines.map(line -> {
                String[] parts = line.split("\\s+");
                return new GroupLocation(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
            }).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param gl List of GroupLocation objects
     * @return The sum of the absolute differences between the left and right values of each GroupLocation object
     * @throws IOException
     *
     * Using a mutable PriorityQueue to store the left and right values of each GroupLocation object. Deliberately using
     * two PriorityQueues to demonstrate the use of this technique.
     */
    private static long part1(List<GroupLocation> gl) throws IOException {
        PriorityQueue<Integer> left = new PriorityQueue<>();
        PriorityQueue<Integer> right = new PriorityQueue<>();

        gl.forEach(g -> {
            left.add(g.left);
            right.add(g.right);
        });

        return IntStream.generate(() -> Math.abs(left.poll() - right.poll()))
                .limit(gl.size())
                .sum();
    }

    /**
     * @param gl List of GroupLocation objects
     * @return The sum of the product of the left and right values of each GroupLocation object
     * @throws IOException
     *
     * This is a pure functional solution with no side effects.
     */
    private static long part2(List<GroupLocation> gl) throws IOException {
        var leftUniqueVals = gl.stream()
                .map(g -> g.left)
                .collect(Collectors.toSet());
        var rightFMap = gl.stream()
                .collect(
                        Collectors.groupingBy(
                                g -> g.right, Collectors.counting()));

        return leftUniqueVals.stream()
                .mapToLong(leftVal -> leftVal * rightFMap.getOrDefault(leftVal, 0L))
                .sum();
    }

    public static AocResult<Long, Long> solve() {
        List<GroupLocation> gl = parse();
        try {
            return new AocResult<>(part1(gl), part2(gl));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
