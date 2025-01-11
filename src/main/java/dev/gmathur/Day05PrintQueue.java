package dev.gmathur;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Day05PrintQueue {
    private record SolutionInput(Map<Integer, Set<Integer>> ordering, List<List<Integer>> updates) { }

    private static SolutionInput parse(String filename) {
        Map<Integer, Set<Integer>> ordering = new HashMap<>();
        List<List<Integer>> updates = new ArrayList<>();

        try (var lines = Files.lines(new File(filename).toPath())) {
            boolean processingOrder = true;
            for (String line: lines.toList()) {
                if (line.isEmpty()) {
                    processingOrder = false;
                    continue;
                }

                if (processingOrder) {
                    String[] parts = line.split("\\|");
                    int page = Integer.parseInt(parts[0]);
                    int nextInOrder = Integer.parseInt(parts[1]);
                    ordering.computeIfAbsent(page, k -> new HashSet<>()).add(nextInOrder);
                } else {
                    List<Integer> sequence = Arrays.stream(line.split(","))
                            .map(Integer::parseInt)
                            .toList();
                    updates.add(sequence);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new SolutionInput(ordering, updates);
    }

    private static boolean isValidSequence(List<Integer> update, Map<Integer, Set<Integer>> ordering) {
        for (int i = 1; i < update.size(); i++) {
            int page = update.get(i - 1);
            int nextInOrder = update.get(i);
            if (!ordering.containsKey(page) || !ordering.get(page).contains(nextInOrder)) {
                return false;
            }
        }
        return true;
    }

    private static List<Integer> part2SortUpdate(List<Integer> update, Map<Integer, Set<Integer>> ordering) {
        var sorted = new ArrayList<>(update);
        sorted.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if (ordering.containsKey(o1) && ordering.get(o1).contains(o2)) {
                    return -1;
                } else if (ordering.containsKey(o2) && ordering.get(o2).contains(o1)) {
                    return 1;
                }
                return 0;
            }
        });
        return sorted;
    }

    public static int part2(String file) {
        var s = parse(file);

        var ordering = s.ordering;
        var result = 0;
        for (var update : s.updates) {
            if (!isValidSequence(update, ordering)) {
                result += part2SortUpdate(update, ordering).get(update.size() / 2);
            }
        }
        return result;
    }

    public static int part1(String file) {
        var s = parse(file);

        var ordering = s.ordering;
        var result = 0;
        for (var update : s.updates) {
            if (isValidSequence(update, ordering)) {
                result += update.get(update.size() / 2);
            }
        }
        return result;
    }
}
