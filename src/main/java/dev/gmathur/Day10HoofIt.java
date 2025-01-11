package dev.gmathur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class Day10HoofIt {

    private void parse(String filename) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(
                        Day9DiskFragmenter.class.getClassLoader().getResourceAsStream(filename))))) {

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
