package dev.gmathur;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

public class Day4CeresSearch {
    private static List<String> parse(String filename) {
        try (var lines = Files.lines(new File(filename).toPath())) {
            return lines.collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean search(int[][] offsets, List<String> lines, int r, int c, String target, int R, int C) {
        int targetIndex = 0;
        for (int[] offset : offsets) {
            int dr = offset[0];
            int dc = offset[1];
            int i = r + dr;
            int j = c + dc;
            if (i < 0 || i >= R || j < 0 || j >= C) {
                return false;
            }
            if (lines.get(i).charAt(j) != target.charAt(targetIndex++)) {
                return false;
            }
        }
        return true;
    }


    private static int day1(List<String> lines) {
        var directions = List.of(
                new int[][]{{0,0},{0,1},{0,2},{0,3}},
                new int[][]{{0,0},{0,-1},{0,-2},{0,-3}},
                new int[][]{{0,0},{1,0},{2,0},{3,0}},
                new int[][]{{0,0},{-1,0},{-2,0},{-3,0}},
                new int[][]{{0,0},{1,1},{2,2},{3,3}},
                new int[][]{{0,0},{-1,-1},{-2,-2},{-3,-3}},
                new int[][]{{0,0},{1,-1},{2,-2},{3,-3}},
                new int[][]{{0,0},{-1,1},{-2,2},{-3,3}}
        );

        int R = lines.size();
        int C = lines.get(0).length();
        int count = 0;

        for (int r = 0; r < R; r++) {
            for (int c = 0; c < C; c++) {
                for (var direction : directions) {
                    if (search(direction, lines, r, c, "XMAS", R, C)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public static void main(String[] args) throws IOException {
        List<String> lines = parse("src/main/resources/day4/input_d4_test1.lst");
        assert(18 == day1(lines));

        List<String> lines2 = parse("src/main/resources/day4/input_d4.lst");
        assert(2578 == day1(lines2));
    }
}
