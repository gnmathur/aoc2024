package dev.gmathur.problems;

import dev.gmathur.utils.Util.Pair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

/**
 * --- Day 11: Plutonian Pebbles ---
 * https://adventofcode.com/2024/day/11
 *
 * Notes:
 * In the first pass at Part 1 I used a technique where the stones being generated at each stage were stored in a queue.
 * The queue elements were then removed and processed at each iteration (blink). The stones of the just processed
 * iteration were first removed from the queue and then the newly generated stones were added to the queue. This was
 * done for a fixed number of iterations. This technique worked for part 1 because the number of stones generated at
 * each iteration was fixed. This technique did not work for part2 because it relied on storing the stones in a queue
 * and the number of stones generated at each iteration keeps growing exponentially. Eventually the JVM runs out of
 * memory. Its also extremely CPU intensive. This approach was left for reference in method part1Old.
 *
 * The new solution uses a recursive approach with memoization. This approach works beautifully for both parts 1 and 2.
 * The recursive approach means that we don't have to store the generated queue ever. The memoization then further
 * ensures that we don't have to recompute the same sub-problem multiple times. Memoization reduces the CPU time
 * drastically and the recursive approach ensures that we don't run out of memory.
 *
 * Runtime on MacBook Pro M3:
 *
 * Part 1: 3ms
 * Part 2: 86ms
 */
public class Day11PlutonianPebbles {
    private record SolutionInput(String line) { }

    private static SolutionInput parse(String fileName) {
        try (var reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(Day09DiskFragmenter.class.getClassLoader().getResourceAsStream(fileName))))) {
            return new SolutionInput(reader.readLine());
        } catch (Exception e) {
            throw new RuntimeException("Error reading file: " + fileName, e);
        }
    }

    private static boolean canApplyRule1(Long num) {
        return num == 0;
    }

    private static boolean canApplyRule2(Long num) {
        if (num == 0) return false;
        return ((int) Math.log10(num) + 1) % 2 == 0;
    }

    private static Long applyRule1(Long stone) {
        return 1L;
    }

    private static Pair<Long, Long> applyRule2(Long stone) {
        int digits = (int) Math.log10(stone) + 1;
        int power = digits / 2;
        int divisor = (int) Math.pow(10, power);
        long secondHalf = (stone % divisor);
        long firstHalf = (stone / divisor);

        return new Pair<>(firstHalf, secondHalf);
    }

    private static Long applyRule3(Long stone) {
        return stone * 2024;
    }

    private static long solver(long stone, int blinkCount, int blinkLimit, Map<Pair<Long, Long>, Long> memo) {
        if (blinkCount == blinkLimit) {
            return 1;
        }

        var key = new Pair<>(stone, (long) blinkCount);
        if (memo.containsKey(key)) {
            return memo.get(key);
        }

        long result = 0;
        if (canApplyRule1(stone)) {
            var nextStone = applyRule1(stone);
            result += solver(nextStone, blinkCount + 1, blinkLimit, memo);
        } else if (canApplyRule2(stone)) {
            var pair = applyRule2(stone);
            result += solver(pair.first(), blinkCount + 1, blinkLimit, memo);
            result += solver(pair.second(), blinkCount + 1, blinkLimit, memo);
        } else {
            var nextStone = applyRule3(stone);
            result += solver(nextStone, blinkCount + 1, blinkLimit, memo);
        }

        memo.put(key, result);
        return result;
    }

    private static long driver(String fileName, int times) {
        var input = parse(fileName);
        var stones = Arrays.stream(input.line().split(" ")).map(Long::parseLong).toList();
        var result = 0L;

        for (Long stone : stones) {
            result += solver(stone, 0, times, new HashMap<>());
        }
        return result;
    }

    public static long part2(String fileName) {
        return driver(fileName, 75);
    }

    public static long part1(String fileName) {
        return driver(fileName, 25);
    }

    public static int part1Old(String fileName) {
        var input = parse(fileName);
        var stones = new ArrayDeque<>(Arrays.stream(input.line().split(" ")).map(Long::parseLong).toList());
        var times = 25;

        for (int i = 0; i < times; i++) {
            var L = stones.size();
            for (int j = 0; j < L; j++) {
                var stone = stones.pollFirst();
                if (canApplyRule1(stone)) {
                    stones.addLast(applyRule1(stone));
                } else if (canApplyRule2(stone)) {
                    var pair = applyRule2(stone);
                    stones.addLast(pair.first());
                    stones.addLast(pair.second());
                } else {
                    stones.addLast(applyRule3(stone));
                }
            }
        }
        return stones.size();
    }
}
