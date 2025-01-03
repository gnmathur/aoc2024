package dev.gmathur;

import dev.gmathur.Util.Pair;
import dev.gmathur.Util.Triple;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class Day8ResonantCollinearity {
    private record SolutionInput(List<Triple<Integer, Integer, Character>> antennas, int R, int C) {}

    private static SolutionInput readFileFromResources(final String fileName) {
        List<Triple<Integer, Integer, Character>> antennas = new ArrayList<>();
        int R = 0;
        int C = 0;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(Day7BridgeRepair.class.getClassLoader().getResourceAsStream(fileName))))) {
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

    private static boolean isCollinear(Triple<Integer, Integer, Character> p1,
                                Triple<Integer, Integer, Character> p2,
                                Triple<Integer, Integer, Character> p3) {
        return (p1.first() * (p2.second() - p3.second()) + p2.first() * (p3.second() - p1.second()) + p3.first() * (p1.second() - p2.second())) == 0;
    }

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

    public static int part1(String fileName) {
        SolutionInput input = readFileFromResources(fileName);
        // Pair all antennas with each other if their character value is the same
        List<Pair<Triple<Integer, Integer, Character>, Triple<Integer, Integer, Character>>> antennaPairs = getPairs(input);

        // Set of unique antinodes
        Set<Pair<Integer, Integer>> antinodes = new HashSet<>();
        // for each pair find all collinear antennas within the grid and calculate the manhattan distance from both the antennas
        for (Pair<Triple<Integer, Integer, Character>, Triple<Integer, Integer, Character>> pair : antennaPairs) {
            Triple<Integer, Integer, Character> p1 = pair.first();
            Triple<Integer, Integer, Character> p2 = pair.second();

            for (int r = 0; r < input.R; r++) {
                for (int c = 0; c < input.C; c++) {
                    if (isCollinear(p1, p2, new Triple<>(r, c, '.'))) {
                        var manhattanDistance1 = manhattanDistance(p1.first(), p1.second(), r, c);
                        var manhattanDistance2 = manhattanDistance(p2.first(), p2.second(), r, c);
                        // increment count if the manhattan distance from one is twice the manhattan distance from the other
                        if (manhattanDistance1 == 2 * manhattanDistance2 || manhattanDistance2 == 2 * manhattanDistance1) {
                            antinodes.add(new Pair<>(r, c));
                        }
                    }
                }
            }
        }

        return antinodes.size();
    }

    public static int part2(String fileName) {
        SolutionInput input = readFileFromResources(fileName);
        // Pair all antennas with each other if their character value is the same
        List<Pair<Triple<Integer, Integer, Character>, Triple<Integer, Integer, Character>>> antennaPairs = getPairs(input);

        // Set of unique antinodes
        Set<Pair<Integer, Integer>> antinodes = new HashSet<>();
        // for each pair find all collinear antennas within the grid and calculate the manhattan distance from both the antennas
        for (Pair<Triple<Integer, Integer, Character>, Triple<Integer, Integer, Character>> pair : antennaPairs) {
            Triple<Integer, Integer, Character> p1 = pair.first();
            Triple<Integer, Integer, Character> p2 = pair.second();

            for (int r = 0; r < input.R; r++) {
                for (int c = 0; c < input.C; c++) {
                    if (isCollinear(p1, p2, new Triple<>(r, c, '.'))) {
                        antinodes.add(new Pair<>(r, c));
                    }
                }
            }
        }

        return antinodes.size();

    }
}
