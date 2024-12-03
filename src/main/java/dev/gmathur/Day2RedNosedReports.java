package dev.gmathur;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day2RedNosedReports {
    private record Report(List<Integer> levels) { }
    private record Reports(List<Report> reports) { }

    private static Reports parseReport() {
        try {
            var lines = Files.readAllLines(Paths.get("src/main/resources/day2/input_d2_red-nosed_reports.lst"));
            return new Reports(lines.stream()
                    .map(line -> new Report(
                            Stream.of(line.split(" "))
                                    .map(Integer::parseInt)
                                    .toList()))
                    .toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean checkReport(Report report) {
        var levels = report.levels;
        var increasing = levels.get(1) > levels.get(0);
        return IntStream.range(1, levels.size())
                .allMatch(i -> {
                    var diff = Math.abs(levels.get(i) - levels.get(i - 1));
                    return diff >= 1 && diff <= 3 &&
                            !(increasing && levels.get(i) < levels.get(i - 1)) &&
                            !(!increasing && levels.get(i) > levels.get(i - 1));
                });
    }

    private static int day1(Reports reports) {
        return (int) reports.reports.stream()
                .filter(Day2RedNosedReports::checkReport)
                .count();
    }

    private static int day2(Reports reports) {
        return (int) reports.reports().stream()
                .filter(report -> {
                    if (!checkReport(report)) {
                        var levels = report.levels();
                        for (int j = 0; j < levels.size(); j++) {
                            var newReport = new Report(Util.listExcludingElementAtI(levels, j));
                            if (checkReport(newReport)) {
                                return true;
                            }
                        }
                        return false;
                    }
                    return true;
                })
                .count();
    }

    public static Util.AocResult<Integer, Integer> solve() {
        Reports reports = parseReport();
        return new Util.AocResult<>(day1(reports), day2(reports));
    }
}
