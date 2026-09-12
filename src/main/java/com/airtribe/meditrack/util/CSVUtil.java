package com.airtribe.meditrack.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

// kept this basic on purpose, just String.split(","), no quotes or
// escaping. works fine for what we need, a real project would just pull
// in an actual csv library
public class CSVUtil {

    private CSVUtil() {
    }

    public static void write(String filePath, List<String> lines) throws IOException {
        Path path = Paths.get(filePath);
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    public static List<String[]> read(String filePath) throws IOException {
        List<String[]> rows = new ArrayList<>();
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            return rows;
        }
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                rows.add(line.split(","));
            }
        }
        return rows;
    }
}
