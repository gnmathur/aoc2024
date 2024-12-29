package dev.gmathur;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Day5PrintQueue {
    private record SolutionInput(HashMap<Integer, HashSet<Integer>> graph, List<List<Integer>> updates, HashSet<Integer> allPages) { }

    private static SolutionInput parse(String filename) {
        HashMap<Integer, HashSet<Integer>> graph = new HashMap<>();
        List<List<Integer>> updates = new ArrayList<>();
        HashSet<Integer> allPages = new HashSet<>();

        try (var lines = Files.lines(new File(filename).toPath())) {
            boolean processingOrder = true;
            for (String line: lines.toList()) {
                if (line.isEmpty()) {
                    processingOrder = false;
                    continue;
                }

                if (processingOrder) {
                    String[] parts = line.split("\\|");
                    // add edge to graph
                    int page = Integer.parseInt(parts[0]);
                    int neighbor = Integer.parseInt(parts[1]);
                    if (!graph.containsKey(neighbor)) {
                        graph.put(neighbor, new HashSet<>());
                    }
                    graph.get(neighbor).add(page);
                    allPages.add(page);
                    allPages.add(neighbor);
                } else {
                    String[] numbers = line.split(",");
                    List<Integer> sequence = new ArrayList<>();
                    for (String number : numbers) {
                        sequence.add(Integer.parseInt(number));
                    }
                    updates.add(sequence);
                }
            }
            return new SolutionInput(graph, updates, allPages);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void topologicalSort(HashMap<Integer, HashSet<Integer>> graph, int page, HashSet<Integer> discovered, List<Integer> ordered, HashSet<Integer> exited) {
        discovered.add(page);

        if (graph.containsKey(page)) {
            for (int neighbor : graph.get(page)) {
                if (!discovered.contains(neighbor)) {
                    topologicalSort(graph, neighbor, discovered, ordered, exited);
                }
                if (!exited.contains(neighbor)) {
                    throw new RuntimeException("Cycle detected");
                }
            }
        }

        exited.add(page);
        ordered.add(page);
    }

    public static boolean isValidUpdate(List<Integer> sequence, HashMap<Integer, Integer> ranks) {
        for (int i = 1; i <= sequence.size() - 1; i++) {
            if (ranks.get(sequence.get(i-1)) > ranks.get(sequence.get(i))) {
                return false;
            }
        }
        return true;
    }

    public static int day1(String file) {
        var s = parse(file);

        List<Integer> ordered = new ArrayList<>();
        HashSet<Integer> discovered = new HashSet<>();
        HashSet<Integer> exited = new HashSet<>();

        for (int page: s.allPages) {
            if (!discovered.contains(page)) {
                topologicalSort(s.graph, page, discovered, ordered, exited);
            }
        }
        System.out.println(ordered);

        // Assign ranks to ordered
        HashMap<Integer, Integer> ranks = new HashMap<>();
        for (int i = 0; i < ordered.size(); i++) {
            ranks.put(ordered.get(i), i);
        }

        int result = 0;
        for (List<Integer> sequence: s.updates) {
            if (isValidUpdate(sequence, ranks)) {
                // find the middle element in update sequence
                var L = sequence.size();
                result += sequence.get(L/2);
                System.out.printf("Adding %d to result\n", sequence.get(L/2));
            }
        }
        return result;
    }

    public static void main(String[] args) {
        //var r1 = day1("src/main/resources/day5/d5_test.input");
        //assert(143 == r1);

        var r2 = day1("src/main/resources/day5/d5.input");
        System.out.println(r2);
    }
}
