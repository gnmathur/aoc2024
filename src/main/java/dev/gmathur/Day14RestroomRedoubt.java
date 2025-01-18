package dev.gmathur;

import dev.gmathur.Util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Solution for Day 14 - Restroom Redoubt
 * <a href="https://adventofcode.com/2024/day/14">...</a>
 *
 * Notes:
 * 1. Part 1 of this problem was easy to solve. The challenging parts for me were
 *  - finding the right regex to extract the position and velocity values
 *  - evalute() method took a little bit of thought - finding the right way to divide the grid into quadrants and
 *      ignoring the middle row and column if the grid size was odd
 * 2. Part 2 took a very long time. Firstly, it was hard to determine whether we should look for ALL robots forming the
 * tree, or a subset. It was also hard to imaging what the tree might look like. I actually looked up the AOC reddit
 * for this problem and saw someone post a screenshot of the tree. That at least told me that the tree was actually
 * a subset of the robots. From there I tried two approaches -
 * - the first one - canBeTree() - was to find all grids with a row with K robots. This did me a result but it took a
 * while to figure out that I needed to go up to 10000 "seconds" to find the tree. Nevertheless, even with this approach
 * there were not too many generated candidate grids (writeGridToFile()) so it was not all that bad
 * - the second one - canBeTree2() - was to find all grids that have a KxK block of robots. With this approach, with
 * seconds set to 10000, the algorithm only produces a single candidate grid, which is the answer. This turns out to
 * a more effective way to find the tree. The runtime for this approach is much faster than the first approach.
 * 3. Check out the discovered tree in the file day14_output_7686.txt in resources/day14
 *
 * Runtimes:
 * Part 1: 1ms
 * Part 2: 286ms (with canBeTree2()) and 1780ms (with canBeTree2())
 */
public class Day14RestroomRedoubt {
    public record Position(int x, int y) {}
    public record Velocity(int dx, int dy) {}
    public record SolutionInput(List<Pair<Position, Velocity>> robots) {}

    public static SolutionInput parse(final String filename) {
        try (var reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(Day14RestroomRedoubt.class.getClassLoader().getResourceAsStream(filename))))) {

            final Pattern pattern = Pattern.compile("p=(\\-?\\d+),(\\-?\\d+)\\s+v=(\\-?\\d+),(\\-?\\d+)");

            final var robots = reader.lines()
                    .map(line -> {
                        Matcher matcher = pattern.matcher(line);
                        if (!matcher.find()) {
                            throw new RuntimeException("Invalid input format");
                        }
                        int x = Integer.parseInt(matcher.group(1));
                        int y = Integer.parseInt(matcher.group(2));
                        int dx = Integer.parseInt(matcher.group(3));
                        int dy = Integer.parseInt(matcher.group(4));
                        return new Pair<>(new Position(x, y), new Velocity(dx, dy));
                    })
                    // create mutable list
                    .collect(Collectors.toList());
            return new SolutionInput(robots);
        } catch (Exception e) {
            throw new RuntimeException("Error reading file: " + filename, e);
        }
    }

    public static Position move(final Position position, final Velocity velocity, final int gridXMax,
                                final int gridYMax) {
        // Positive x velocity means we are moving right and positive y velocity means we are moving down. Negative x
        // velocity means we are moving left and negative y velocity means we are moving up. Wrap around the grid if we
        // go out of bounds
        int x = (position.x + velocity.dx) % gridXMax;
        int y = (position.y + velocity.dy) % gridYMax;
        if (x < 0) x += gridXMax;
        if (y < 0) y += gridYMax;
        return new Position(x, y);
    }

    public static int evaluate(final List<Pair<Position, Velocity>> robots, final int gridXMax, final int gridYMax) {
        final int midX = gridXMax / 2;
        final int midY = gridYMax / 2;
        final boolean ignoreMidX = gridXMax % 2 == 1;
        final boolean ignoreMidY = gridYMax % 2 == 1;

        final int[] quadrants = new int[4];

        for (var robot : robots) {
            final var position = robot.first();
            if ((ignoreMidX && position.x == midX) || (ignoreMidY && position.y == midY)) { continue; }
            final int quadIndex = (position.x >= midX ? 1 : 0) + (position.y >= midY ? 2 : 0);
            quadrants[quadIndex]++;
        }

        return quadrants[0] * quadrants[1] * quadrants[2] * quadrants[3];
    }

    public static void writeGridToFile(List<Position> coordinates, int xMax, int yMax, String fileName) {
        char[][] grid = new char[yMax][xMax];

        for (int y = 0; y < yMax; y++) {
            for (int x = 0; x < xMax; x++) {
                grid[y][x] = '.';
            }
        }

        for (Position p : coordinates) {
            if (p.x >= 0 && p.x < xMax && p.y >= 0 && p.y < yMax) {
                grid[p.y][p.x] = '*';
            }
        }

        try (var writer = new PrintWriter(fileName)) {
            for (int y = 0; y < yMax; y++) {
                for (int x = 0; x < xMax; x++) {
                    writer.print(grid[y][x]);
                }
                writer.println();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing to file: " + fileName, e);
        }
    }

    // This is a better method to find candidate grid that might have a christmas tree. Its based on the premise that
    // a christmas tree like structure would have some clustering of robots in a grid. So, we find a sub-grid of size
    // KxK that has all cells occupied by robots. If such a grid exists, then we have a candidate grid that might have
    // a christmas tree.
    public static boolean canBeTree2(final List<Position> positions, final int gridXMax, final int gridYMax,
                                     final int K) {
        final boolean[][] grid = new boolean[gridYMax][gridXMax];

        for (Position p : positions) {
            grid[p.y()][p.x()] = true;
        }

        for (int r = 0; r <= gridYMax - K; r++) {
            for (int c = 0; c <= gridXMax - K; c++) {
                if (isKxKBlock(grid, r, c, K)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean isKxKBlock(final boolean[][] grid, final int startRow, final int startCol, final int K) {
        for (int r = startRow; r < startRow + K; r++) {
            for (int c = startCol; c < startCol + K; c++) {
                if (!grid[r][c]) {
                    return false;
                }
            }
        }
        return true;
    }

    // Function to find if a given (list of Position) with K robots in one of rows of the grid, where grid size is
    // gridXMax x gridYMax
    public static boolean canBeTree(List<Position> positions, int gridXMax, int gridYMax, int K) {
        char[][] grid = new char[gridYMax][gridXMax];
        for (Position p : positions) {
            grid[p.y()][p.x()] = '*';
        }

        // Check if there is a row with K robots
        for (int r = 0; r < gridYMax; r++) {
            int count = 0;
            for (int c= 0; c < gridXMax; c++) { if (grid[r][c] == '*') { count++; } }
            if (count >= K) { return true; }
        }
        return false;
    }

    public static void part2(final String filename, final int gridXMax, final int gridYMax) {
        final var si = parse(filename);
        final var robots = si.robots();
        final var TRY_SECONDS = 10000;
        final var CLUSTER_SIZE = 3; // 3x3

        for (int j = 0; j < TRY_SECONDS; j++) {
            for (int i = 0; i < robots.size(); i++) {
                var robot = robots.get(i);
                final var currPosition = robot.first();
                final var robotVelocity = robot.second();
                final var newPosition = move(currPosition, robotVelocity, gridXMax, gridYMax);
                robots.set(i, new Pair<>(newPosition, robotVelocity));
            }
            //if (canBeTree(robots.stream().map(Pair::first).collect(Collectors.toList()), gridXMax, gridYMax,
            //        CLUSTER_SIZE)) {
            if (canBeTree2(robots.stream().map(Pair::first).collect(Collectors.toList()), gridXMax, gridYMax,
                    CLUSTER_SIZE)) {
                writeGridToFile(
                        robots.stream()
                                .map(Pair::first)
                                .collect(Collectors.toList()),
                        gridXMax, gridYMax, "day14_output_" + j + ".txt");
            }
        }
    }

    public static int part1(final String filename, final int gridXMax, final int gridYMax) {
        final var si = parse(filename);
        final var robots = si.robots();
        final var SECONDS = 100;

        for (int i = 0; i < robots.size(); i++) {
            var robot = robots.get(i);
            final var currPosition = robot.first();
            final var robotVelocity = robot.second();
            final var scaledVelocity = new Velocity(robotVelocity.dx * SECONDS, robotVelocity.dy * SECONDS);
            final var newPosition = move(currPosition, scaledVelocity, gridXMax, gridYMax);
            robots.set(i, new Pair<>(newPosition, robotVelocity));
        }
        return evaluate(robots, gridXMax, gridYMax);
    }
}


