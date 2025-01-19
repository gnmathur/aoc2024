package dev.gmathur.problems;

import dev.gmathur.utils.Util.Pair;
import dev.gmathur.utils.Util.Triple;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Day06GuardGallivant {
    private record Coordinate(int x, int y) {}

    /**
     * Record to store the input for the solution
     *
     * @param obstacles Set of coordinates of obstacles
     * @param start Starting coordinate
     * @param size Pair of the size of the grid (x, y)
     */
    private record SolutionInput(Set<Coordinate> obstacles, Coordinate start, Pair<Integer, Integer> size) {}

    // Direction enums for the four cardinal directions with next valid direction in clockwise order
    private enum Direction {
        NORTH, EAST, SOUTH, WEST;

        public Direction nextClockwise() {
            return switch (this) {
                case NORTH -> EAST;
                case EAST -> SOUTH;
                case SOUTH -> WEST;
                case WEST -> NORTH;
            };
        }
    }

    private static SolutionInput readFileFromResources(final String fileName) {
        Set<Coordinate> obstacles = new HashSet<>();
        int y_max = 0;
        int x_max = 0;
        Coordinate start = null;
        try (BufferedReader reader =
                     new BufferedReader(
                             new InputStreamReader(
                                     Objects.requireNonNull(Day06GuardGallivant.class.getClassLoader().getResourceAsStream(fileName))
        ))) {
            String line;
            while ((line = reader.readLine()) != null) {
                for (int x = 0; x < line.length(); x++) {
                    char c = line.charAt(x);
                    if (c == '^') { start = new Coordinate(x, y_max); }
                    if (c == '#') { obstacles.add(new Coordinate(x, y_max)); }
                }
                x_max = line.length();
                y_max++;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new SolutionInput(obstacles, start, new Pair<>(x_max, y_max));
    }

    /**
     * Find a cycle in the grid. It simulates the movement of the guard and checks if it has
     * visited a point before. If it has, then it has found a cycle.
     *
     * @param x x coordinate of the starting point
     * @param y y coordinate of the starting point
     * @param direction Direction the guard is facing at the starting point
     * @param R Number of rows in the grid
     * @param C Number of columns in the grid
     * @param obstacles Set of coordinates of obstacles
     * @param visited Set of visited coordinates
     *
     * @return True if a cycle is found, false otherwise
     */

    private static boolean findCycle(int x, int y, Direction direction, int R, int C, Set<Coordinate> obstacles,
                                     Set<Triple<Integer, Integer, Direction>> visited) {
        while (true) {
            if (visited.contains(new Triple<>(x, y, direction))) {
                return true; // Cycle found
            }

            visited.add(new Triple<>(x, y, direction));

            switch (direction) { case NORTH -> y--; case EAST -> x++; case SOUTH -> y++; case WEST -> x--; }

            if (obstacles.contains(new Coordinate(x, y))) { // if you hit an obstacle
                var new_direction = direction.nextClockwise(); // turn clockwise 90 degrees
                switch (direction) { case NORTH -> y++; case EAST -> x--; case SOUTH -> y--; case WEST -> x++; } // go back
                direction = new_direction;
            }

            if (x < 0 || y < 0 || x >= C || y >= R) {
                return false; // No cycle found, we hit the edge
            }
        }
    }

    /**
     * Part 2 of the solution. This solution works by simulating an obstacle at every point in the grid that's a
     * non-obstacle and not a starting point. It then runs a DFS from the starting point to see if there's a cycle.
     *
     * @param fileName Name of the input file
     * @return Number of unique paths
     */

    public static int part2(final String fileName) {
        final SolutionInput input = readFileFromResources(fileName);
        final int start_x = input.start().x();
        final int start_y = input.start().y();
        final int R = input.size().first();
        final int C = input.size().second();
        final Direction direction = Direction.NORTH;

        AtomicInteger count = new AtomicInteger(0);
        List<Thread> threads = new ArrayList<>();

        for (int x = 0; x < input.size().first(); x++) {
            for (int y = 0; y < input.size().second(); y++) {
                final int finalX = x;
                final int finalY = y;
                Thread thread = Thread.ofVirtual().start(() -> {
                    if (input.obstacles().contains(new Coordinate(finalX, finalY))) {
                        return;
                    }
                    if (finalX == start_x && finalY == start_y) {
                        return;
                    }
                    // simulate an obstacle at (finalX, finalY)
                    final Set<Coordinate> localObstacles = new HashSet<>(input.obstacles());
                    localObstacles.add(new Coordinate(finalX, finalY));
                    if (findCycle(start_x, start_y, direction, C, R, localObstacles, new HashSet<>())) {
                        count.incrementAndGet();
                    }
                });
                threads.add(thread);
            }
        }

        for (Thread thread : threads) {
            try { thread.join(); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }

        return count.get();
    }

    /**
     * Part 1 of the solution. This solution works by simulating the movement of the guard in the grid. If the guard
     * hits an obstacle, it turns clockwise 90 degrees and goes back one step. If it hits the edge of the grid,
     * it stops.
     *
     * @param fileName Name of the input file
     *
     * @return Number of unique paths
     */
    public static int part1(final String fileName) {
        final SolutionInput input = readFileFromResources(fileName);
        int x = input.start().x();
        int y = input.start().y();
        Direction direction = Direction.NORTH;
        Set<Pair<Integer, Integer>> visited = new HashSet<>();

        visited.add(new Pair<>(x, y));
        while (true) {
            switch (direction) { case NORTH -> y--; case EAST -> x++; case SOUTH -> y++; case WEST -> x--; }

            if (x < 0 || y < 0 || x >= input.size().first() || y >= input.size().second()) {
                break;
            }

            if (input.obstacles().contains(new Coordinate(x, y))) {
                // If you hit an obstacle, turn clockwise 90 degrees and go back one step
                switch (direction) { case NORTH -> y++; case EAST -> x--; case SOUTH -> y--; case WEST -> x++; }
                direction = direction.nextClockwise();
            } else {
                visited.add(new Pair<>(x, y));
            }
        }
        return visited.size();
    }
}
