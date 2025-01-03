package dev.gmathur;

import dev.gmathur.Util.Pair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Day7BridgeRepair {
    private record SolutionInput(List<Pair<Long, List<Long>>> input) {}

    private static SolutionInput readFileFromResources(final String fileName) {
        List<Pair<Long, List<Long>>> input = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(Day7BridgeRepair.class.getClassLoader().getResourceAsStream(fileName))))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(": ");
                long target = Long.parseLong(tokens[0]);
                List<Long> operands = Arrays.stream(tokens[1].split(" ")).map(Long::parseLong).toList();
                var p = new Pair<>(target, operands);
                input.add(p);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new SolutionInput(input);
    }
    // a b c

    private static boolean solvePart1(long target, List<Long> operands, int idx, long result) {
        if (idx == operands.size()) {
            return result == target;
        }
        if (result > target) {
            return false;
        }

        return solvePart1(target, operands, idx + 1, result + (operands.get(idx))) ||
                solvePart1(target, operands, idx + 1, result * (operands.get(idx)));
    }

    public static int part1(String fileName) {
        SolutionInput input = readFileFromResources(fileName);
        int result = 0;
        for (Pair<Long, List<Long>> p : input.input) {
            if (solvePart1(p.first(), p.second(), 0, 0)) {
                result += p.first();
            }
        }
        return result;
    }

}
