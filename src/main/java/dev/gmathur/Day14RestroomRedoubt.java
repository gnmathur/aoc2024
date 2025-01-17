package dev.gmathur;

import dev.gmathur.Util.Pair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day14RestroomRedoubt {
    public record Position(int x, int y) {}
    public record Velocity(int dx, int dy) {}
    public record SolutionInput(List<Pair<Position, Velocity>> robots) {}

    public static SolutionInput parse(String filename) {
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

    public static int part1(final String filename, final int gridXMax, final int gridYMax) {
        var si = parse(filename);
        var robots = si.robots();
        for (int j = 0; j < 100; j++) {
            for (int i = 0; i < robots.size(); i++) {
                var robot = robots.get(i);
                final var currPosition = robot.first();
                final var robotVelocity = robot.second();
                final var newPosition = move(currPosition, robotVelocity, gridXMax, gridYMax);
                robots.set(i, new Pair<>(newPosition, robotVelocity));
            }
        }
        return evaluate(robots, gridXMax, gridYMax);
    }
}


