package dev.gmathur;

import dev.gmathur.Util.AocResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day3MullItOver {
    private static long day2(String buffer) {
        var regex = "mul\\((\\d{1,3}),(\\d{1,3})\\)|do\\(\\)|don't\\(\\)";
        var matcher = Pattern.compile(regex).matcher(buffer);
        var domul = new boolean[]{true};

        return matcher.results()
                .mapToLong(m -> switch (m.group()) {
                    case "do()" -> { domul[0] = true; yield 0; }
                    case "don't()" -> { domul[0] = false; yield 0; }
                    default -> domul[0] ? Long.parseLong(m.group(1)) * Long.parseLong(m.group(2)) : 0;
                })
                .sum();
    }

    private static long day1(String buffer) {
        String regex = "mul\\((\\d{1,3}),(\\d{1,3})\\)";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(buffer);

        return matcher.results()
                .mapToLong(matchResult ->
                        Long.parseLong(matchResult.group(1)) * Long.parseLong(matchResult.group(2)))
                .sum();
    }

    public static AocResult<Long, Long> solve(){
        try {
            String input = Files.readString(new File("src/main/resources/day3/input_d3_mull_it_over.lst").toPath());
            return new AocResult<>(day1(input), day2(input));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
