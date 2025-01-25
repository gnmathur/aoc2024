package dev.gmathur.utils;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class Util {
    public record AocResult<A, B>(A part1, B part2) { }
    public record Pair<A, B>(A first, B second) {
        // Override toString() to print in (a, b) format
        @Override
        public String toString() {
            return "(" + first + ", " + second + ")";
        }
    }
    public record Triple<A, B, C>(A first, B second, C third) { }
    public record Grid<T>(T[][] g, int R, int C) {}
    // A record to represent a single cell, intersection of (row, col), in the grid
    public record Cell(int r, int c) {}
    // A record to represent a wide cell. It occupies two horizontally adjacent (row, col) pairs in the grid
    public record WCell(int row, int lc, int rc) {
        public boolean isValid(int R, int C) {
            return row >= 0 && row < R && lc >= 0 && lc < C && rc >= 0 && rc < C;
        }
        public boolean isAtTop() { return row == 0; }
        public boolean isAtBottom(int R) { return row == R - 1; }
        public boolean isAtLeft() { return lc == 0; }
        public boolean isAtRight(int C) { return rc == C - 1; }
        public boolean containsValidChar(char[][] grid, char[] validChars) {
            return (grid[row][lc] == validChars[0] || grid[row][rc] == validChars[1]);
        }
    }

    public static <T> List<T> listExcludingElementAtI(List<T> list, int index) {
        List<T> result = new ArrayList<>(list);
        result.remove(index);
        return result;
    }

    public static void runTimedWithLabel(String label, Runnable runnable) {
        long start = System.currentTimeMillis();
        runnable.run();
        long end = System.currentTimeMillis();
        System.out.printf("[%s] took: %dms%n", label, end - start);
    }

    public static void runTimed(Runnable runnable) {
        long start = System.currentTimeMillis();
        runnable.run();
        long end = System.currentTimeMillis();
        System.out.println("Time taken: " + (end - start) + "ms");
    }


    public static class FileLineIterator implements Iterator<String>, AutoCloseable {
        private final BufferedReader reader;
        private String nextLine;

        public FileLineIterator(String filename) throws IOException {
            this.reader = new BufferedReader(new FileReader(filename));
            this.nextLine = reader.readLine();
        }

        @Override
        public boolean hasNext() { return nextLine != null; }

        @Override
        public String next() {
            if (!hasNext()) { throw new NoSuchElementException("No more lines remaining in the file."); }
            String currentLine = nextLine;
            try {
                nextLine = reader.readLine();
            } catch (IOException e) { throw new RuntimeException("Error reading next line", e); }
            return currentLine;
        }

        @Override
        public void close() throws IOException { reader.close(); }

        /**
         * Example usage of the FileLineIterator
         */
        public static void vooid(String[] args) {
            String filename = "sample.txt";  // Replace with your file path

            try (FileLineIterator fileIterator = new FileLineIterator(filename)) {
                while (fileIterator.hasNext()) {
                    System.out.println("Line: " + fileIterator.next());
                }
            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    public static class GridDrawer extends JPanel {
        private int[][] grid = null;
        private List<int[][]> paths = null;

        GridDrawer(int[][] grid, List<Pair<Integer, Integer>> paths) {
            this.grid = grid;
            // Convert List<Pair<Integer, Integer>> to List<int[][]>
            this.paths = new ArrayList<>();
            for (Pair<Integer, Integer> path : paths) {
                this.paths.add(new int[][]{{path.first(), path.second()}});
            }
        }

        private final int cellSize = 50;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawGrid(g);
            drawPaths(g);
        }

        private void drawGrid(Graphics g) {
            g.setColor(Color.BLACK);
            for (int r = 0; r < grid.length; r++) {
                for (int c = 0; c < grid[r].length; c++) {
                    int x = c * cellSize;
                    int y = r * cellSize;
                    g.drawRect(x, y, cellSize, cellSize);
                    g.drawString(Integer.toString(grid[r][c]), x + cellSize / 2, y + cellSize / 2);
                }
            }
        }

        private void drawPaths(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(3));

            Color[] colors = {Color.RED};

            for (int i = 0; i < paths.size(); i++) {
                g2.setColor(colors[i % colors.length]);
                int[][] path = paths.get(i);
                for (int j = 0; j < path.length - 1; j++) {
                    int x1 = path[j][1] * cellSize + cellSize / 2;
                    int y1 = path[j][0] * cellSize + cellSize / 2;
                    int x2 = path[j + 1][1] * cellSize + cellSize / 2;
                    int y2 = path[j + 1][0] * cellSize + cellSize / 2;
                    g2.drawLine(x1, y1, x2, y2);
                    g2.fillOval(x1 - 5, y1 - 5, 10, 10);  // Draw a circle at each point for visibility
                }
                // Draw last point
                int xLast = path[path.length - 1][1] * cellSize + cellSize / 2;
                int yLast = path[path.length - 1][0] * cellSize + cellSize / 2;
                g2.fillOval(xLast - 5, yLast - 5, 10, 10);
            }
        }

        public void draw() {
            JFrame frame = new JFrame("Grid with Paths");
            frame.add(this);
            frame.setSize(600, 600);
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.setVisible(true);
        }
    }

}
