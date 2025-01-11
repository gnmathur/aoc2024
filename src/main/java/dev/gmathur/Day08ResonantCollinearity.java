package dev.gmathur;

import dev.gmathur.Util.Pair;
import dev.gmathur.Util.Triple;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.BiPredicate;

/**
 * Solution for Day 8 of Advent of Code 2021
 * Problem description: <a href="https://adventofcode.com/2021/day/8">...</a>
 * <p>
 * The approach -
 * 1. Read the input from the file and store the antennas in a list. Each Antenna is represented by a Triple of
 * (row, column, character).
 * 2. Create a set of antenna pairs where the character value of the antennas is the same.
 * 3. For part 1, for each pair of antennas, find all the collinear antennas in the grid and calculate the manhattan
 * distance from both the antennas.
 * 4. For part 2, for each pair of antennas, find all the collinear antennas in the grid.
 * <p>
 * Complexity and Runtimes:
 * The time complexity for both parts 1 and 2 is O(N^2 * R * C), where:
 *     N is the number of antennas
 *     R is the number of rows in the grid
 *     C is the number of columns in the grid
 *
 * Runtimes on MacBook Pro M3:
 *    Part 1: 27ms
 *    Part 2: 19ms
 *
 * Special Note:
 * 1. Part 1 turns out to be an easier version of the problem where we only need to find the antinodes, disregarding
 * the manhattan distance.
 */
public class Day08ResonantCollinearity {
    // Solution input is a record that stores the list of antennas and the size of the grid. The list of antennas is a
    // list of Triples where each Triple represents an antenna by its row, column, and character value -
    // (row, column, character).
    private record SolutionInput(List<Triple<Integer, Integer, Character>> antennas, int R, int C) {}

    private static SolutionInput readFileFromResources(final String fileName) {
        List<Triple<Integer, Integer, Character>> antennas = new ArrayList<>();
        int R = 0;
        int C = 0;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(Day08ResonantCollinearity.class.getClassLoader().getResourceAsStream(fileName))))) {
            String line;
            while ((line = reader.readLine()) != null) {
                C = line.length();
                String[] tokens = line.split("");
                for (int col = 0; col < tokens.length; col++) {
                    if (!tokens[col].equals(".")) {
                        antennas.add(new Triple<>(R, col, tokens[col].charAt(0)));
                    }
                }
                R++;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new SolutionInput(antennas, R, C);
    }

    // Collinearity check for three points - p1, p2, and p3 - using the cross product of the vectors formed by the
    // points. The points are collinear if the cross product is zero.
    private static boolean isCollinear(Triple<Integer, Integer, Character> p1,
                                Triple<Integer, Integer, Character> p2,
                                Triple<Integer, Integer, Character> p3) {
        return (p1.first() * (p2.second() - p3.second()) + p2.first() * (p3.second() - p1.second()) + p3.first() * (p1.second() - p2.second())) == 0;
    }

    // Manhattan distance between two points (p1x, p1y) and (p2x, p2y) is the sum of the absolute differences of their
    // x and y coordinates.
    private static int manhattanDistance(int p1x, int p1y, int p2x, int p2y) {
        return Math.abs(p1x - p2x) + Math.abs(p1y - p2y);
    }

    private static List<Pair<Triple<Integer, Integer, Character>, Triple<Integer, Integer, Character>>> getPairs(SolutionInput input) {
        List<Pair<Triple<Integer, Integer, Character>, Triple<Integer, Integer, Character>>> antennaPairs = new ArrayList<>();

        for (int i = 0; i < input.antennas.size(); i++) {
            for (int j = i + 1; j < input.antennas.size(); j++) {
                Triple<Integer, Integer, Character> p1 = input.antennas.get(i);
                Triple<Integer, Integer, Character> p2 = input.antennas.get(j);
                if (p1.third().equals(p2.third())) {
                    antennaPairs.add(new Pair<>(p1, p2));
                }
            }
        }
        return antennaPairs;
    }

    private static int solveWithPredicate(String fileName, BiPredicate<Integer, Integer> condition) {
        final SolutionInput input = readFileFromResources(fileName);
        // Get all pairs of antennas with the same character value. i.e. the same type of antennas.
        final List<Pair<Triple<Integer, Integer, Character>, Triple<Integer, Integer, Character>>> antennaPairs =
                getPairs(input);
        final Set<Pair<Integer, Integer>> antiNodes = new HashSet<>();

        // For each pair of antennas, find all the collinear antennas in the grid and test the condition.
        for (Pair<Triple<Integer, Integer, Character>, Triple<Integer, Integer, Character>> pair : antennaPairs) {
            Triple<Integer, Integer, Character> p1 = pair.first();
            Triple<Integer, Integer, Character> p2 = pair.second();

            for (int r = 0; r < input.R; r++) {
                for (int c = 0; c < input.C; c++) {
                    if (isCollinear(p1, p2, new Triple<>(r, c, '.'))) {
                        int d1 = manhattanDistance(p1.first(), p1.second(), r, c);
                        int d2 = manhattanDistance(p2.first(), p2.second(), r, c);
                        if (condition.test(d1, d2)) {
                            antiNodes.add(new Pair<>(r, c));
                        }
                    }
                }
            }
        }

        return antiNodes.size();
    }

    public static int part1(String fileName) {
        // For part 1 the distance between the antinode and one of the antennas has to be twice the distance between the
        // antinode and the other antenna.
        return solveWithPredicate(fileName, (d1, d2) -> d1 == 2 * d2 || d2 == 2 * d1);
    }

    public static int part2(String fileName) {
        // For part 2 the distance between the antinode and the two antennas can be anything.
        return solveWithPredicate(fileName, (d1, d2) -> true);
    }
}
