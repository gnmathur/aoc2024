package dev.gmathur;

import dev.gmathur.Util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Day10HoofIt {
    private record SolutionInput(int[][] grid, int R, int C) {}

    private static SolutionInput parse(String filename) {
        try (var reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(Day9DiskFragmenter.class.getClassLoader().getResourceAsStream(filename))))) {

            var grid = reader.lines()
                    .map(line -> line.chars()
                            .map(Character::getNumericValue)
                            .toArray())
                    .toList();

            final int R = grid.size();
            final int C = grid.isEmpty() ? 0 : grid.getFirst().length;

            if (grid.stream().anyMatch(row -> row.length != C)) {
                throw new IllegalArgumentException("Inconsistent row lengths detected.");
            }

            final int[][] gridArray = grid.toArray(new int[R][C]);
            return new SolutionInput(gridArray, R, C);
        } catch (IOException | NullPointerException e) {
            throw new RuntimeException("Error reading file: " + filename, e);
        }
    }

    /**
     * A backtracking solution to solve part 1 of the problem. The backtracking explores all adjoining cells that have
     * a value 1 greater than the current cell. It will visit all of them, and then from there visit all cells that
     * have a value 1 greater than the current cell, and so on. The number of 9s encountered in the path is the number
     * of ways to reach the end from a given starting point.
     *
     * @param grid the grid to traverse
     * @param r the row of the current cell
     * @param c the column of the current cell
     * @param R the number of rows in the grid
     * @param C the number of columns in the grid
     * @param memo a memoization map to store the number of ways to reach the end from a given cell. This improves
     *             performance by avoiding redundant calculations.
     * @param num9s a set to store the coordinates of all 9s encountered in the path
     *
     * @return the number of ways to reach the end from the current cell (r, c)
     */
    public static int part1Solver(int[][] grid, int r, int c, int R, int C,
                                  Map<Pair<Integer, Integer>, Integer> memo,
                                  Set<Pair<Integer, Integer>> num9s) {
        var coor = new Pair<>(r, c);
        if (memo.containsKey(coor)) { return memo.get(coor); }
        if (grid[r][c] == 9) { num9s.add(coor); return 1; }

        // visit neighbors in all 4 directions

        int ways = 0;
        for (int[] dir : new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}}) {
            int newR = r + dir[0];
            int newC = c + dir[1];
            if (newR >= 0 && newR < R && newC >= 0 && newC < C && grid[newR][newC] - grid[r][c] == 1) {
                ways += part1Solver(grid, newR, newC, R, C, memo, num9s);
            }
        }

        memo.put(coor, ways);

        return ways;
    }

    public static int part1(String filename) {
        var si = parse(filename);
        int ways = 0;

        for (int r= 0; r < si.R; r++) {
            for (int c = 0; c < si.C; c++) {
                if (si.grid[r][c] == 0) {
                    // visit all cells in sequence starting from 0 till 9
                    var memo = new HashMap<Pair<Integer, Integer>, Integer>();
                    var num9s = new HashSet<Pair<Integer, Integer>>();
                    part1Solver(si.grid, r, c, si.R, si.C, memo, num9s);
                    ways += num9s.size();
                }
            }
        }

        return ways;
    }
}
