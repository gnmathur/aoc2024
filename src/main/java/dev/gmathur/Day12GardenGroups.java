package dev.gmathur;

import dev.gmathur.Util.FileLineIterator;
import dev.gmathur.Util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Day12GardenGroups {
    private record SolutionInput(char[][] garden, int R, int C) {}

    private static SolutionInput parse(String fileName) {
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

    private static void part1Solver(char[][] garden, int r, int c, int R, int C, Set<Pair<Integer, Integer>> discovered,
                                    long[] fenceAndCount, char region) {
        var coor = new Pair<>(r, c);

        discovered.add(coor);

        int[][] dirs = new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

        for (int[] dir : dirs) {
            int newR = r + dir[0];
            int newC = c + dir[1];
            var newCoor = new Pair<>(newR, newC);

            if (!(newR >= 0 && newR < R && newC >= 0 && newC < C && garden[newR][newC] == region)) {
                fenceAndCount[0] += 1;
            } else if (!discovered.contains(newCoor)) {
                fenceAndCount[1] += 1;
                part1Solver(garden, newR, newC, R, C, discovered, fenceAndCount, region);
            }
        }
    }

    public static long part1(String fileName) {
        var si = parse(fileName);
        var discovered = new HashSet<Pair<Integer, Integer>>();
        var totalPrices = 0L;

        for (int r = 0; r < si.R; r++) {
            for (int c = 0; c < si.C; c++) {
                if (!discovered.contains(new Pair<>(r, c))) {
                    long[] fenceAndCount = new long[]{0, 1};
                    part1Solver(si.garden, r, c, si.R, si.C, discovered, fenceAndCount, si.garden[r][c]);
                    totalPrices += fenceAndCount[0] * fenceAndCount[1];
                }
            }
        }
        return totalPrices;
    }
}
