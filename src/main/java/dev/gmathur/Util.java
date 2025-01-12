package dev.gmathur;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

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
