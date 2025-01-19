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

    /**
     * Find cells that are marked as 'O' in the grid, and are adjacent to the robot cell in the given direction. The direction
     * to search in is specified by dr and dc (UP => (-1, 0), DOWN => (1, 0), LEFT => (0, -1), RIGHT => (0, 1)).
     *
     * @param robotLocation the cell the robot is in currently
     * @param grid the grid to search in
     * @param R the number of rows in the grid
     * @param C the number of columns in the grid
     * @param dr Row offset specifying the direction to search in
     * @param dc Column offset specifying the direction to search in
     *
     * @return a deque of cells that are marked as 'O' and are adjacent to the robot cell in the given direction
     */
    private static ArrayDeque<Cell> cellsInDirection(final Cell robotLocation, final char[][] grid, final int R,
                                                     final int C, final int dr, final int dc) {
        final ArrayDeque<Cell> cells = new ArrayDeque<>();
        int r = robotLocation.r() + dr;
        int c = robotLocation.c() + dc;

        while (r >= 0 && r < R && c >= 0 && c < C && grid[r][c] == 'O') {
            cells.add(new Cell(r, c));
            r += dr; c += dc;
        }

        return cells;
    }

    private static ArrayDeque<Cell> cellsToTheLeftOf(Cell cell, char[][] grid, int R, int C) {
        return cellsInDirection(cell, grid, R, C, 0, -1);
    }

    private static ArrayDeque<Cell> cellsToTheRightOf(Cell cell, char[][] grid, int R, int C) {
        return cellsInDirection(cell, grid, R, C, 0, 1);
    }

    private static ArrayDeque<Cell> cellsAbove(Cell cell, char[][] grid, int R, int C) {
        return cellsInDirection(cell, grid, R, C, -1, 0);
    }

    private static ArrayDeque<Cell> cellsBelow(Cell cell, char[][] grid, int R, int C) {
        return cellsInDirection(cell, grid, R, C, 1, 0);
    }

    /**
     * Move the cells in the given direction. The direction is specified by dr and dc (UP => (-1, 0), DOWN => (1, 0),
     * LEFT => (0, -1), RIGHT => (0, 1)). Note that there is a need to reverse the order of the cells in the deque
     * because the leading cell is the one that moves first, and the trailing cell is the one that moves last.
     *
     * @param cells the cells to move
     * @param grid the grid to move the cells in
     * @param robot the cell the robot is in currently
     * @param R the number of rows in the grid
     * @param C the number of columns in the grid
     * @param dr Row offset specifying the direction to move in
     * @param dc Column offset specifying the direction to move in
     *
     * @return the new location of the robot after moving the cells
     */
    private static Cell moveCells(final ArrayDeque<Cell> cells, final char[][] grid, final Cell robot, int R, int C,
                                  int dr, int dc) {
        final Cell leadingCell = cells.getLast();
        int newRow = leadingCell.r() + dr;
        int newCol = leadingCell.c() + dc;

        if (newRow < 0 || newRow >= R || newCol < 0 || newCol >= C) { return robot; }
        if (grid[newRow][newCol] == '#') { return robot; }

        for (Cell cell : cells.reversed()) {
            grid[cell.r() + dr][cell.c() + dc] = grid[cell.r()][cell.c()];
            grid[cell.r()][cell.c()] = '.';
        }

        return new Cell(robot.r() + dr, robot.c() + dc);
    }

    private static Cell moveCellsLeft(final ArrayDeque<Cell> cells, final char[][] grid, final int R, final int C,
                                      final Cell robot) {
        return moveCells(cells, grid, robot, R, C, 0, -1);
    }
    private static Cell moveCellsRight(final ArrayDeque<Cell> cells, final char[][] grid, final int R, final int C,
                                       final Cell robot) {
        return moveCells(cells, grid, robot, R, C, 0, 1);
    }
    private static Cell moveCellsUp(final ArrayDeque<Cell> cells, final char[][] grid, final int R, final int C,
                                    final Cell robot) {
        return moveCells(cells, grid, robot, R, C, -1, 0);
    }
    private static Cell moveCellsDown(final ArrayDeque<Cell> cells, final char[][] grid, final int R, final int C,
                                      final Cell robot) {
        return moveCells(cells, grid, robot, R, C, 1, 0);
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
                sum += grid[r][c] == 'O' ? 100L * (r + 1) + (c + 1) : 0;
            }
        }
        return sum;
    }

    public static long part1(final String fileName) {
        SolutionInput input = parse(fileName);
        Cell start = input.start();
        int R = input.grid().length;
        int C = input.grid()[0].length;

        for (Character move : input.moves) {
            switch (move) {
                case '<' -> {
                    var cells = cellsToTheLeftOf(start, input.grid(), R, C);
                    cells.addFirst(start);
                    start = moveCellsLeft(cells, input.grid(), R, C, start);
                }
                case '>' -> {
                    var cells = cellsToTheRightOf(start, input.grid(), R, C);
                    cells.addFirst(start);
                    start = moveCellsRight(cells, input.grid(), R, C, start);
                }
                case '^' -> {
                    var cells = cellsAbove(start, input.grid(), R, C);
                    cells.addFirst(start);
                    start = moveCellsUp(cells, input.grid(), R, C, start);
                }
                case 'v' -> {
                    var cells = cellsBelow(start, input.grid(), R, C);
                    cells.addFirst(start);
                    start = moveCellsDown(cells, input.grid(), R, C, start);
                }
            }
        }

        return gpsCoordSum(input.grid());
    }
}
