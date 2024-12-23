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

    private static int day2(List<String> lines) {
        var count = 0;
        var R = lines.size();
        var C = lines.get(0).length();
        var topLeftToBottomRight = new int[][]{{-1,-1},{0,0},{1,1}};
        var bottomRightToTopLeft = new int[][]{{1,1},{0,0},{-1,-1}};
        var topRightToBottomLeft = new int[][]{{-1,1},{0,0},{1,-1}};
        var bottomLeftToTopRight = new int[][]{{1,-1},{0,0},{-1,1}};
        var target = "MAS";


        for (int r = 0; r < lines.size(); r++) {
            for (int c = 0; c < lines.get(0).length(); c++) {
                if ((search(topLeftToBottomRight, lines, r, c, target, R, C) ||
                    search(bottomRightToTopLeft, lines, r, c, target, R, C)) &&

                    (search(topRightToBottomLeft, lines, r, c, target, R, C) ||
                    search(bottomLeftToTopRight, lines, r, c, target, R, C))) {
                    count ++;
                }
            }
        }
        return count;
    }

    private static int day1(List<String> lines) {
        var directions = List.of(
                new int[][]{{0,0},{0,1},{0,2},{0,3}}, // horizontal right
                new int[][]{{0,0},{0,-1},{0,-2},{0,-3}}, // horizontal left
                new int[][]{{0,0},{1,0},{2,0},{3,0}}, // vertical down
                new int[][]{{0,0},{-1,0},{-2,0},{-3,0}}, // vertical up
                new int[][]{{0,0},{1,1},{2,2},{3,3}}, // diagonal right down
                new int[][]{{0,0},{-1,-1},{-2,-2},{-3,-3}}, // diagonal left up
                new int[][]{{0,0},{1,-1},{2,-2},{3,-3}}, // diagonal left down
                new int[][]{{0,0},{-1,1},{-2,2},{-3,3}} // diagonal right up
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

    public static void main(String[] args) {
        List<String> lines = parse("src/main/resources/day4/input_d4_test.lst");
        assert(18 == day1(lines));

        List<String> lines2 = parse("src/main/resources/day4/input_d4.lst");
        assert(2578 == day1(lines2));

        List<String> lines3 = parse("src/main/resources/day4/input_d4_test.lst");
        assert(9 == day2(lines3));

        List<String> lines4 = parse("src/main/resources/day4/input_d4.lst");
        assert(1972 == day2(lines4));
    }
}
