package dev.gmathur;

import dev.gmathur.Util.Pair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Day7BridgeRepair {
    private record SolutionInput(List<Pair<BigInteger, List<BigInteger>>> input) {}

    private static SolutionInput readFileFromResources(final String fileName) {
        List<Pair<BigInteger, List<BigInteger>>> input = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(Day7BridgeRepair.class.getClassLoader().getResourceAsStream(fileName))))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(": ");
                BigInteger target = new BigInteger(tokens[0]);
                List<BigInteger> operands = Arrays.stream(tokens[1].split(" "))
                        .map(BigInteger::new)
                        .toList();
                var p = new Pair<>(target, operands);
                input.add(p);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new SolutionInput(input);
    }

    private static boolean solvePart2(final BigInteger target, final List<BigInteger> operands, final int idx,
                                      final BigInteger result) {
        if (idx == operands.size()) { return result.equals(target); }
        if (result.compareTo(target) > 0) { return false; }

        return solvePart2(target, operands, idx + 1, result.add(operands.get(idx))) ||
                solvePart2(target, operands, idx + 1, result.multiply(operands.get(idx))) ||
                solvePart2(target, operands, idx + 1, new BigInteger(result + operands.get(idx).toString()));
    }

    public static BigInteger part2(String fileName) {
        final SolutionInput input = readFileFromResources(fileName);
        return input.input.stream()
                .filter(p -> solvePart2(p.first(), p.second(), 0, BigInteger.ZERO))
                .map(Pair::first)
                .reduce(BigInteger.ZERO, BigInteger::add);
    }

    /**
     * At each index, we have two choices: add or multiply the current operand to the result. We recursively try both
     * choices and return true if we find a solution at the end of the operands list.
     *
     * @param target The target value to reach
     * @param operands The list of operands
     * @param idx The current index in the operands list
     * @param result The current result
     *
     * @return True if we find a solution, false otherwise
     */
    private static boolean solvePart1(final BigInteger target, final List<BigInteger> operands, final int idx,
                                      final BigInteger result) {
        if (idx == operands.size()) { return result.equals(target); }
        if (result.compareTo(target) > 0) { return false; }

        return solvePart1(target, operands, idx + 1, result.add(operands.get(idx))) ||
                solvePart1(target, operands, idx + 1, result.multiply(operands.get(idx)));
    }

    public static BigInteger part1(String fileName) {
        final SolutionInput input = readFileFromResources(fileName);
        return input.input.stream()
                .filter(p -> solvePart1(p.first(), p.second(), 0, BigInteger.ZERO))
                .map(Pair::first)
                .reduce(BigInteger.ZERO, BigInteger::add);
    }
}