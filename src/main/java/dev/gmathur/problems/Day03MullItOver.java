package dev.gmathur.problems;

import dev.gmathur.utils.Util.AocResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.regex.Pattern;

public class Day03MullItOver {
    private static long solver(final String buffer, final Boolean ignore) {
        final var regex = "mul\\((\\d{1,3}),(\\d{1,3})\\)|do\\(\\)|don't\\(\\)";
        final var matcher = Pattern.compile(regex).matcher(buffer);
        final var domul = new boolean[]{true};

        return matcher.results()
                .mapToLong(mr -> switch (mr.group()) {
                    case "do()" -> { domul[0] = true; yield 0; }
                    case "don't()" -> { domul[0] = false; yield 0; }
                    default -> (ignore || domul[0]) ? Long.parseLong(mr.group(1)) * Long.parseLong(mr.group(2)) : 0;
                })
                .sum();
    }

    private static long part2(final String buffer) { return solver(buffer, false); }
    private static long part1(final String buffer) { return solver(buffer, true); }

    public static AocResult<Long, Long> solve(){
        try {
            String input = Files.readString(new File("src/main/resources/day3/input_d3_mull_it_over.lst").toPath());
            return new AocResult<>(part1(input), part2(input));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
