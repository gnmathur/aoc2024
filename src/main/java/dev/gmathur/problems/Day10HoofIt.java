package dev.gmathur.problems;

import dev.gmathur.utils.Util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.IntStream;

/**
 * Solution for Day 10 - Hoof It
 * <a href="https://adventofcode.com/2024/day/10">...</a>
 *
 *
 * Time complexity: O(R * C * 4^9) where R is the number of rows in the grid, and C is the number of columns in the grid. Note
 * that at each cell in the grid, we can move in 4 directions, and the maximum number of steps to reach the end is 9.
 *
 * Run time on MacBook Pro M3
 *
 * Part1 test input: 10ms
 * Part1 problem input: 1ms
 *
 * Part2 test input: 7ms
 * Part2 problem input: 4ms
 *
 * Notes:
 * 1. Happy with the way I was able to use a core backtracking algorithm to solve both parts of this problem
 * 2. I was able to use functional code in all parts of the solution, so that was a good exercise
 * 3. The absolute runtimes are very low, so I am happy with the performance of the solution
 *
 */
public class Day10HoofIt {
    private record SolutionInput(int[][] grid, int R, int C) {}

    private static SolutionInput parse(String filename) {
        try (var reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(Day09DiskFragmenter.class.getClassLoader().getResourceAsStream(filename))))) {

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
    public static int solver(int[][] grid, int r, int c, int R, int C,
                             Map<Pair<Integer, Integer>, Integer> memo,
                             Set<Pair<Integer, Integer>> num9s) {
        var coor = new Pair<>(r, c);
        if (memo.containsKey(coor)) { return memo.get(coor); }
        if (grid[r][c] == 9) { num9s.add(coor); return 1; }

        // visit neighbors in all 4 directions
        final int ways = Arrays.stream(new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}})
                .mapToInt(dir -> {
                    int newR = r + dir[0];
                    int newC = c + dir[1];
                    return (newR >= 0 && newR < R && newC >= 0 && newC < C && grid[newR][newC] - grid[r][c] == 1)
                            ? solver(grid, newR, newC, R, C, memo, num9s)
                            : 0;
                })
                .sum();

        memo.put(coor, ways);

        return ways;
    }

    private static int solve(String fileName, boolean count9s) {
        var si = parse(fileName);
        return IntStream.range(0, si.R)
                .flatMap(r -> IntStream.range(0, si.C)
                        .filter(c -> si.grid[r][c] == 0)
                        .map(c -> {
                            var memo = new HashMap<Pair<Integer, Integer>, Integer>();
                            var num9s = new HashSet<Pair<Integer, Integer>>();
                            int result = solver(si.grid, r, c, si.R, si.C, memo, num9s);
                            return count9s ? num9s.size() : result;
                        }))
                .sum();
    }

    public static int part2(String filename) {
        return solve(filename, false);
    }

    public static int part1(String filename) {
        return solve(filename, true);
    }
}
