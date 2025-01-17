package dev.gmathur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Solution for Day 13 - Claw Contraption
 * <a href="https://adventofcode.com/2024/day/13">...</a>
 *
 * Notes:
 * 1. Learnt something new - Cramer's rule to solve a system of linear equations. This was a fun problem to implement.
 * 2. Solution for part 1 and part 2 turned out to exactly the same. I was expecting the part 2 number too be too large
 * and needing to use a BigInteger. But the number fit into a double, same as part 1.
 * 3. Parsing turned out to be hard, esp. the part figuring out the regular expressions to use to extract the integers
 * from each input line. Beefed-up my knowledge of regexes too in the process (it being admittedly poor :-))
 *
 * Runtimes:
 *
 * Part 1: 1ms
 * Part 2: 3ms
 */
public class Day13ClawContraption {
    public record SingleInput(int buttonAX, int buttonAY, int buttonBX, int buttonBY, int prizeX, int prizeY) { }
    public record SolutionInput(List<SingleInput> inputs) { }

    public static SolutionInput parse(String filename) {
        final List<SingleInput> inputs = new ArrayList<>();
        Pattern buttonPattern = Pattern.compile("X\\+(\\d+),\\s*Y\\+(\\d+)");
        Pattern prizePattern = Pattern.compile("X\\=(\\d+),\\s*Y\\=(\\d+)");

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(Day13ClawContraption.class.getClassLoader().getResourceAsStream(filename))))) {
            while (reader.ready()) {

                String line = reader.readLine();

                Matcher matcher = buttonPattern.matcher(line);
                if (!matcher.find()) {
                    throw new RuntimeException("Invalid input format");
                }
                int buttonAX = Integer.parseInt(matcher.group(1));
                int buttonAY = Integer.parseInt(matcher.group(2));

                line = reader.readLine();
                matcher = buttonPattern.matcher(line);
                if (!matcher.find()) {
                    throw new RuntimeException("Invalid input format");
                }
                int buttonBX = Integer.parseInt(matcher.group(1));
                int buttonBY = Integer.parseInt(matcher.group(2));

                line = reader.readLine();
                matcher = prizePattern.matcher(line);
                if (!matcher.find()) {
                    throw new RuntimeException("Invalid input format");
                }
                int prizeX = Integer.parseInt(matcher.group(1));
                int prizeY = Integer.parseInt(matcher.group(2));

                inputs.add(new SingleInput(buttonAX, buttonAY, buttonBX, buttonBY, prizeX, prizeY));
                // skip a line
                reader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + filename, e);
        }
        return new SolutionInput(inputs);
    }

    // Solve a system of two linear equations to solve for aPress and bPress using Cramer's rule
    // Solve the equations
    // buttonAX * aPress + buttonBX * bPress = C
    // buttonAY * aPress + buttonBY * bPress = D
    public static Util.Pair<Double, Double> solve(int buttonAX, int buttonAY, int buttonBX, int buttonBY,
                                                  double C, double D) {
        double determinant = buttonAX * buttonBY - buttonAY * buttonBX;

        double aPress = (C * buttonBY - D * buttonBX) / determinant;
        double bPress = (buttonAX * D - buttonAY * C) / determinant;

        return new Util.Pair<>(aPress, bPress);

    }

    private static double solve(SolutionInput si, double offset) {
        return si.inputs.stream()
                .map(input -> solve(
                        input.buttonAX, input.buttonAY,
                        input.buttonBX, input.buttonBY,
                        input.prizeX + offset,
                        input.prizeY + offset
                ))
                .filter(solution -> solution.first() % 1 == 0 && solution.second() % 1 == 0)
                .mapToDouble(solution -> solution.first() * 3 + solution.second())
                .sum();
    }

    public static double part1(String fileName) {
        return solve(parse(fileName), 0);
    }

    public static double part2(String fileName) {
        return solve(parse(fileName), 1e13);
    }
}
