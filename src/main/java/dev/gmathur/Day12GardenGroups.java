package dev.gmathur;

import dev.gmathur.Util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * --- Day 12: Garden Groups ---
 * https://adventofcode.com/2024/day/12
 *
 * Notes:
 * 1. Both parts use a classic DFS to find the distinct regions in the garden.
 * 2. Part 1 can use the information returned by the DFS to calculate the number of fences needed to enclose each region.
 * 3. For Part 2, the intuition to count the corners was relatively straightforward. However, the implementation was
 * very hard to get right. After a lot of trial and error, I think I have managed to come up with the canonical set
 * of criterion. Implementation-wise it's actually possible to make it more concise, but I think the current
 * implementation * is more readable and makes the logic more clear.
 *
 * Time Complexity of Part 1: O(R * C) where R is the number of rows and C is the number of columns
 * Time Complexity of Part 2: O(R * C) where R is the number of rows and C is the number of columns
 *
 * Runtime on MacBook Pro M3:
 *
 * Part 1: 21ms
 * Part 2: 16ms
 */
public class Day12GardenGroups {
    public record SolutionInput(char[][] garden, int R, int C) {}

    public static SolutionInput parse(String fileName) {
        try (var reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(Day09DiskFragmenter.class.getClassLoader().getResourceAsStream(fileName))))) {
            var garden = reader.lines()
                    .map(String::toCharArray)
                    .toArray(char[][]::new);

            final int R = garden.length;
            final int C = garden[0].length;

            return new SolutionInput(garden, R, C);
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + fileName, e);
        }
    }

    /**
     * Populates the region of the garden starting at (r, c) with the region character region.
     *
     * @param gardenPlots All the garden plots
     * @param r We need to start completing a region starting at this row
     * @param c We need to start completing a region starting at this column
     * @param R Number of rows in the garden
     * @param C Number of columns in the garden
     * @param discovered DFS discovered set
     * @param fenceAndCount An array of size 2, where the first element is the number of fences needed to complete the
     *                      region, and the second element is the number of cells in the region
     * @param region The region character
     * @param regionGardens A list of all the cells in the region. Populated by this method
     */
    private static void populateRegion(final char[][] gardenPlots, final int r, final int c, final int R, final int C,
                                       final Set<Pair<Integer, Integer>> discovered, final long[] fenceAndCount,
                                       final char region, final List<Pair<Integer, Integer>> regionGardens) {
        var coordinate = new Pair<>(r, c);

        discovered.add(coordinate);
        regionGardens.add(coordinate);

        int[][] dirs = new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

        for (int[] dir : dirs) {
            int newR = r + dir[0];
            int newC = c + dir[1];
            var newCoor = new Pair<>(newR, newC);

            if (!(newR >= 0 && newR < R && newC >= 0 && newC < C && gardenPlots[newR][newC] == region)) {
                fenceAndCount[0] += 1;
            } else if (!discovered.contains(newCoor)) {
                fenceAndCount[1] += 1;
                populateRegion(gardenPlots, newR, newC, R, C, discovered, fenceAndCount, region, regionGardens);
            }
        }
    }

    public static int countCorners(List<Pair<Integer, Integer>> regionGardens, char[][] grid, int rows, int cols,
                                   char region) {
        int corners = 0;
        for (Pair<Integer, Integer> coor : regionGardens) {
            int r = coor.first();
            int c = coor.second();

            // if the top and left cells are region cells, but the top let cell is not, then this is a corner
            // ... | X | R | ...
            // ... | R | R | ...
            if (r > 0 && c > 0 && grid[r - 1][c] == region && grid[r][c - 1] == region && grid[r - 1][c - 1] != region) {
                corners++;
            }
            // if the top and right cells are region cells, but the top right cell is not, then this is a corner
            // ... | R | X | ...
            // ... | R | R | ...
            if (r > 0 && c < cols - 1 && grid[r - 1][c] == region && grid[r][c + 1] == region && grid[r - 1][c + 1] != region) {
                corners++;
            }
            // if the bottom and left cells are region cells, but the bottom left cell is not, then this is a corner
            // ... | R | R | ...
            // ... | X | R | ...
            if (r < rows - 1 && c > 0 && grid[r + 1][c] == region && grid[r][c - 1] == region && grid[r + 1][c - 1] != region) {
                corners++;
            }
            // if the bottom and right cells are region cells, but the bottom right cell is not, then this is a corner
            // ... | R | R | ...
            // ... | R | X | ...
            if (r < rows - 1 && c < cols - 1 && grid[r + 1][c] == region && grid[r][c + 1] == region && grid[r + 1][c + 1] != region) {
                corners++;
            }

            // if the top cell is out of bounds or not region, and left cell is out of bounds or not region
            if ((r == 0 || grid[r - 1][c] != region) && (c == 0 || grid[r][c - 1] != region)) {
                corners++;
            }
            // if the top cell is out of bounds or not region, and right cell is out of bounds or not region
            if ((r == 0 || grid[r - 1][c] != region) && (c == cols - 1 || grid[r][c + 1] != region)) {
                corners++;
            }
            // if the bottom cell is out of bounds or not region, and left cell is out of bounds or not region
            if ((r == rows - 1 || grid[r + 1][c] != region) && (c == 0 || grid[r][c - 1] != region)) {
                corners++;
            }
            // if the bottom cell is out of bounds or not region, and right cell is out of bounds or not region
            if ((r == rows - 1 || grid[r + 1][c] != region) && (c == cols - 1 || grid[r][c + 1] != region)) {
                corners++;
            }
        }
        return corners;
    }

    public static long part2(SolutionInput si) {
        var discovered = new HashSet<Pair<Integer, Integer>>();
        var totalPrices = 0L;

        for (int r = 0; r < si.R; r++) {
            for (int c = 0; c < si.C; c++) {
                if (!discovered.contains(new Pair<>(r, c))) {
                    long[] fenceAndCount = new long[]{0, 1};
                    List<Pair<Integer, Integer>> regionGardens = new ArrayList<>();

                    populateRegion(si.garden, r, c, si.R, si.C, discovered, fenceAndCount, si.garden[r][c], regionGardens);
                    // We'll ignore the fence count and just count the corners. The number of corners is the same as
                    // the number of sides of the region
                    int corners = countCorners(regionGardens, si.garden, si.R, si.C, si.garden[r][c]);
                    totalPrices += corners * fenceAndCount[1];
                }
            }
        }
        return totalPrices;
    }

    public static long part1(SolutionInput si) {
        var discovered = new HashSet<Pair<Integer, Integer>>();
        var totalPrices = 0L;

        for (int r = 0; r < si.R; r++) {
            for (int c = 0; c < si.C; c++) {
                if (!discovered.contains(new Pair<>(r, c))) {
                    long[] fenceAndCount = new long[]{0, 1};
                    List<Pair<Integer, Integer>> regionGardens = new ArrayList<>();

                    populateRegion(si.garden, r, c, si.R, si.C, discovered, fenceAndCount, si.garden[r][c], regionGardens);
                    totalPrices += fenceAndCount[0] * fenceAndCount[1];
                }
            }
        }
        return totalPrices;
    }
}
