package dev.gmathur.problems;


import dev.gmathur.utils.Util.Pair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class Day16ReindeerMaze {
    private record SolutionInput(char[][] maze, Pair<Integer, Integer> start, Pair<Integer, Integer> end) {}

    private static SolutionInput parse(String fileName) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(Day16ReindeerMaze.class.getClassLoader().getResourceAsStream(fileName))))) {

            char[][] maze = reader.lines()
                    .map(String::toCharArray)
                    .toArray(char[][]::new);

            // Find start and end in the maze
            Pair<Integer, Integer> start = null;
            Pair<Integer, Integer> end = null;
            for (int i = 0; i < maze.length; i++) {
                for (int j = 0; j < maze[i].length; j++) {
                    if (maze[i][j] == 'S') { start = new Pair<>(i, j); }
                    else if (maze[i][j] == 'E') { end = new Pair<>(i, j); }
                }
            }
            return new SolutionInput(maze, start, end);
        } catch (Exception e) {
            throw new RuntimeException("Error reading file: " + fileName, e);
        }
    }

    private static Map<Pair<Integer, Integer>, Pair<Integer, Integer>> bfs(char[][] maze, Pair<Integer, Integer> start) {
        // Implement BFS here
        int R = maze.length;
        int C = maze[0].length;

        Set<Pair<Integer, Integer>> discovered = new HashSet<>();
        ArrayDeque<Pair<Integer, Integer>> q = new ArrayDeque<>();
        Map<Pair<Integer, Integer>, Pair<Integer, Integer>> parent = new HashMap<>();

        q.add(start);
        discovered.add(start);

        while (!q.isEmpty()) {
            Pair<Integer, Integer> current = q.poll();
            int r = current.first();
            int c = current.second();

            int[][] dirs = new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
            for (int[] dir : dirs) {
                int newR = r + dir[0];
                int newC = c + dir[1];
                Pair<Integer, Integer> newCoor = new Pair<>(newR, newC);

                if (newR >= 0 && newR < R && newC >= 0 && newC < C && maze[newR][newC] != '#' && !discovered.contains(newCoor)) {
                    q.add(newCoor);
                    discovered.add(newCoor);
                    parent.put(newCoor, current);
                }
            }
        }
        return parent;
    }

    public static long part1(String fileName) {
        var si = parse(fileName);
        var parent = bfs(si.maze, si.start);
        var copyOfMaze = Arrays.stream(si.maze).map(char[]::clone).toArray(char[][]::new);

        // find the path from start to end
        Pair<Integer, Integer> current = si.end;
        long steps = 0;
        while (!current.equals(si.start)) {
            steps++;
            current = parent.get(current);
            // mark the path with '^'
            copyOfMaze[current.first()][current.second()] = '^';
        }

        // print the path
        for (char[] row : copyOfMaze) {
            System.out.println(row);
        }

        return 0L;
    }
}
