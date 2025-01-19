package dev.gmathur;

import dev.gmathur.Util.Cell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Day15WarehouseWoes {

    public record SolutionInput(char[][] grid, List<Character> moves, Cell start) {}

    public static SolutionInput parse(final String fileName) {
        try(var reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(Day15WarehouseWoes.class.getClassLoader().getResourceAsStream(fileName))))) {
            reader.readLine();
            Cell start = null;
            List<char[]> rows = new ArrayList<>();
            String line;
            int rowIdx = 0;
            while ((line = reader.readLine()) != null && line.startsWith("#")) {
                // Remove leading and trailing '#' characters
                line = line.substring(1, line.length() - 1);
                char[] row = line.toCharArray();
                for (int i = 0; i < row.length; i++) {
                    if (row[i] == '@') {
                        start = new Cell(rowIdx, i);
                    }
                }
                rows.add(row);
                rowIdx++;
            }

            rows.removeLast();
            char[][] grid = new char[rows.size()][rows.getFirst().length];
            for (int i = 0; i < rows.size(); i++) {
                grid[i] = rows.get(i);
            }

            List<Character> moves = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                for (char c : line.toCharArray()) {
                    moves.add(c);
                }
            }
            return new SolutionInput(grid, moves, start);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static ArrayDeque<Cell> cellsToTheLeftOf(Cell cell, char[][] grid, int R, int C) {
        ArrayDeque<Cell> cells = new ArrayDeque<>();
        for (int i = cell.c() - 1; i >= 0; i--) {
            if (grid[cell.r()][i] != 'O') { break; }
            cells.add(new Cell(cell.r(), i));
        }
        return cells;
    }

    private static ArrayDeque<Cell> cellsToTheRightOf(Cell cell, char[][] grid, int R, int C) {
        ArrayDeque<Cell> cells = new ArrayDeque<>();
        for (int i = cell.c() + 1; i < C; i++) {
            if (grid[cell.r()][i] != 'O') { break; }
            cells.add(new Cell(cell.r(), i));
        }
        return cells;
    }

    private static ArrayDeque<Cell> cellsAbove(Cell cell, char[][] grid, int R, int C) {
        ArrayDeque<Cell> cells = new ArrayDeque<>();
        for (int i = cell.r() - 1; i >= 0; i--) {
            if (grid[i][cell.c()] != 'O') { break; }
            cells.add(new Cell(i, cell.c()));
        }
        return cells;
    }

    private static ArrayDeque<Cell> cellsBelow(Cell cell, char[][] grid, int R, int C) {
        ArrayDeque<Cell> cells = new ArrayDeque<>();
        for (int i = cell.r() + 1; i < R; i++) {
            if (grid[i][cell.c()] != 'O') { break; }
            cells.add(new Cell(i, cell.c()));
        }
        return cells;
    }

    private static Cell moveCellsLeft(ArrayDeque<Cell> cells, char[][] grid, Cell robot) {
        // If the last cell in cells list is at the left edge of the grid, or is to the right of a a cell with #, then
        // we can't move the cells to the left
        if (cells.isEmpty()) { return robot; }
        if (cells.getLast().c() == 0) { return robot; }
        if (grid[cells.getLast().r()][cells.getLast().c() - 1] == '#') { return robot; }
        // Shift all cells in the list to the left on the grid
        for (Cell cell : cells.reversed()) {
            grid[cell.r()][cell.c() - 1] = grid[cell.r()][cell.c()];
            grid[cell.r()][cell.c()] = '.';
        }

        return new Cell(robot.r(), robot.c() - 1);
    }

    private static Cell moveCellsRight(ArrayDeque<Cell> cells, char[][] grid, Cell robot) {
        // If the last cell in cells list is at the right edge of the grid, or is to the left of a a cell with #, then
        // we can't move the cells to the right
        if (cells.isEmpty()) { return robot; }
        if (cells.getLast().c() == grid[0].length - 1) { return robot; }
        if (grid[cells.getLast().r()][cells.getLast().c() + 1] == '#') { return robot; }
        // Shift all cells in the list to the right on the grid
        for (Cell cell : cells.reversed()) {
            grid[cell.r()][cell.c() + 1] = grid[cell.r()][cell.c()];
            grid[cell.r()][cell.c()] = '.';
        }

        return new Cell(robot.r(), robot.c() + 1);
    }

    private static Cell moveCellsUp(ArrayDeque<Cell> cells, char[][] grid, Cell robot) {
        // If the last cell in cells list is at the top edge of the grid, or is below a a cell with #, then
        // we can't move the cells up
        if (cells.isEmpty()) { return robot; }
        if (cells.getLast().r() == 0) { return robot; }
        if (grid[cells.getLast().r() - 1][cells.getLast().c()] == '#') { return robot; }
        // Shift all cells in the list up on the grid
        for (Cell cell : cells.reversed()) {
            grid[cell.r() - 1][cell.c()] = grid[cell.r()][cell.c()];
            grid[cell.r()][cell.c()] = '.';
        }

        return new Cell(robot.r() - 1, robot.c());
    }

    private static Cell moveCellsDown(ArrayDeque<Cell> cells, char[][] grid, Cell robot) {
        // If the last cell in cells list is at the bottom edge of the grid, or is above a a cell with #, then
        // we can't move the cells down
        if (cells.isEmpty()) { return robot; }
        if (cells.getLast().r() == grid.length - 1) { return robot; }
        if (grid[cells.getLast().r() + 1][cells.getLast().c()] == '#') { return robot; }
        // Shift all cells in the list down on the grid
        for (Cell cell : cells.reversed()) {
            grid[cell.r() + 1][cell.c()] = grid[cell.r()][cell.c()];
            grid[cell.r()][cell.c()] = '.';
        }

        return new Cell(robot.r() + 1, robot.c());
    }

    private static void printGrid(char[][] grid) {
        for (char[] row : grid) {
            for (char c : row) {
                System.out.print(c);
            }
            System.out.println();
        }
        System.out.println("-----------");
    }

    private static long gpsCoordSum(char[][] grid) {
        long sum = 0;
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                if (grid[r][c] == 'O') {
                    sum += 100 * (long)(r+1) + (c+1);
                }
            }
        }
        return sum;
    }

    public static long part1(final String fileName) {
        SolutionInput input = parse(fileName);
        Cell start = input.start();

        for (Character move : input.moves) {
            //  <vv>^
            switch (move) {
                case '<' -> {
                    var cells = cellsToTheLeftOf(start, input.grid(), input.grid().length, input.grid()[0].length);
                    cells.addFirst(start);
                    start = moveCellsLeft(cells, input.grid(), start);
                }
                case '>' -> {
                    var cells = cellsToTheRightOf(start, input.grid(), input.grid().length, input.grid()[0].length);
                    cells.addFirst(start);
                    start = moveCellsRight(cells, input.grid(), start);
                }
                case '^' -> {
                    var cells = cellsAbove(start, input.grid(), input.grid().length, input.grid()[0].length);
                    cells.addFirst(start);
                    start = moveCellsUp(cells, input.grid(), start);
                }
                case 'v' -> {
                    var cells = cellsBelow(start, input.grid(), input.grid().length, input.grid()[0].length);
                    cells.addFirst(start);
                    start = moveCellsDown(cells, input.grid(), start);
                }
            }
        }

        return gpsCoordSum(input.grid());
    }
}
