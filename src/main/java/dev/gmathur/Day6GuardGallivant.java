package dev.gmathur;

import dev.gmathur.Util.Pair;
import dev.gmathur.Util.Triple;

import java.io.*;
import java.util.*;

public class Day6GuardGallivant {
    private record Coordinate(int x, int y) {}
    private record SolutionInput(Set<Coordinate> obstacles, Coordinate start, Pair<Integer, Integer> size) {}

    // Direction enums for the four cardinal directions with next valid direction
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
        // Try with resources to ensure the stream is closed automatically
        Set<Coordinate> obstacles = new HashSet<>();
        int y = 0;
        int x_max = 0;
        Coordinate start = null;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(Day6GuardGallivant.class.getClassLoader().getResourceAsStream(fileName))
        ))) {
            String line;
            while ((line = reader.readLine()) != null) {
                for (int x = 0; x < line.length(); x++) {
                    char c = line.charAt(x);
                    if (c == '^') { start = new Coordinate(x, y); }
                    if (c == '#') { obstacles.add(new Coordinate(x, y)); }
                }
                x_max = line.length();
                y++;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new SolutionInput(obstacles, start, new Pair<>(x_max, y));
    }

    private static boolean dfs(int x, int y, Direction direction, int R, int C, Set<Coordinate> obstacles,
                               Set<Triple<Integer, Integer, Direction>> visited) {

        visited.add(new Triple<>(x, y, direction));

        switch (direction) { case NORTH -> y--; case EAST -> x++; case SOUTH -> y++; case WEST -> x--; }

        if (obstacles.contains(new Coordinate(x, y))) {
            var new_direction = direction.nextClockwise();
            switch (direction) { case NORTH -> y++; case EAST -> x--; case SOUTH -> y--; case WEST -> x++; }
            direction = new_direction;
        }
        if (x < 0 || y < 0 || x >= C || y >= R) {
            return false;
        }
        if (visited.contains(new Triple<>(x, y, direction))) {
            return true;
        }

        return dfs(x, y, direction, R, C, obstacles, visited);
    }

    public static int part2(final String fileName) {
        final SolutionInput input = readFileFromResources(fileName);
        final int start_x = input.start().x();
        final int start_y = input.start().y();
        final Direction direction = Direction.NORTH;

        int count = 0;
        for (int x = 0; x < input.size().first(); x++) {
            for (int y = 0; y < input.size().second(); y++) {
                if (input.obstacles().contains(new Coordinate(x, y))) {
                    continue;
                }
                if (x == start_x && y == start_y) {
                    continue;
                }
                // simulate an obstacle at (x, y)
                input.obstacles.add(new Coordinate(x, y));
                if (dfs(start_x, start_y, direction, input.size().second(), input.size().first(), input.obstacles(), new HashSet<>())) {
                    count++;
                }
                input.obstacles.remove(new Coordinate(x, y));
            }
        }
        return count;
    }

    public static int part1(String fileName) {
        SolutionInput input = readFileFromResources(fileName);
        int x = input.start().x();
        int y = input.start().y();
        Direction direction = Direction.NORTH;
        Set<Pair<Integer, Integer>> visited = new HashSet<>();

        visited.add(new Pair<>(x, y));
        while (true) {
            switch (direction) {
                case NORTH -> y--;
                case EAST -> x++;
                case SOUTH -> y++;
                case WEST -> x--;
            }

            if (x < 0 || y < 0 || x >= input.size().first() || y >= input.size().second()) {
                break;
            }

            if (input.obstacles().contains(new Coordinate(x, y))) {
                switch (direction) {
                    case NORTH -> y++;
                    case EAST -> x--;
                    case SOUTH -> y--;
                    case WEST -> x++;
                }
                direction = direction.nextClockwise();
            } else {
                visited.add(new Pair<>(x, y));
            }
        }
        return visited.size();
    }
}
