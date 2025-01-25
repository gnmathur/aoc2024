package dev.gmathur.problems;

import dev.gmathur.utils.Util.Cell;
import dev.gmathur.utils.Util.WCell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Day15WarehouseWoes {
    public record SolutionInput(char[][] grid, List<Character> moves, Cell start) {}
    private static final char[] validChars = new char[]{'[', ']'};

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

    private static ArrayDeque<WCell> findAdjacentBoxesToMoveUp(WCell wc, final char[][] grid, final int R, final int C) {
        final ArrayDeque<WCell> bfsQueue = new ArrayDeque<>();
        final ArrayDeque<WCell> visitedQueue = new ArrayDeque<>();
        final Set<WCell> discovered = new HashSet<>();

        discovered.add(wc);
        visitedQueue.add(wc);
        // 0 1 2 3 4
        //   [ ]
        //   @ .
        // 0 1 2 3 4
        //   [ ] -
        //   . @ .
        // 0 1 2 3 4
        //       [ ] -
        //       . @ -
        WCell topLeft = new WCell(wc.row() - 1, wc.lc()-1, wc.lc());
        WCell topRight = new WCell(wc.row() - 1, wc.lc(), wc.lc() + 1);

        if (topLeft.isValid(R, C) && topLeft.containsValidChar(grid, validChars)) {
            discovered.add(topLeft); bfsQueue.add(topLeft);
        }
        if (topRight.isValid(R, C) && topRight.containsValidChar(grid, validChars)) {
            discovered.add(topRight); bfsQueue.add(topRight);
        }

        while (!bfsQueue.isEmpty()) {
            int L = bfsQueue.size();
            while (L-- > 0) {
                WCell current = bfsQueue.poll();
                visitedQueue.add(current);

                WCell top = new WCell(current.row() - 1, current.lc(), current.rc());
                topLeft = new WCell(current.row() - 1, current.lc() - 1, current.lc());
                topRight = new WCell(current.row() - 1, current.rc(), current.rc() + 1);

                if (top.isValid(R, C) && top.containsValidChar(grid, validChars) && !discovered.contains(top)) {
                    discovered.add(top); bfsQueue.add(top);
                }
                if (topLeft.isValid(R, C) && topLeft.containsValidChar(grid, validChars) && !discovered.contains(topLeft)) {
                    discovered.add(topLeft); bfsQueue.add(topLeft);
                }
                if (topRight.isValid(R, C) && topRight.containsValidChar(grid, validChars) && !discovered.contains(topRight)) {
                    discovered.add(topRight); bfsQueue.add(topRight);
                }
            }
        }
        return visitedQueue;
   }

    private static Cell moveAdjacentBoxesUp(final Cell robotLocation, final char[][] grid, final int R, final int C) {
        WCell robot = new WCell(robotLocation.r(), robotLocation.c(), robotLocation.c() + 1); // pseudo wide cell

        ArrayDeque<WCell> cellsToMove = findAdjacentBoxesToMoveUp(robot, grid, R, C);
        // Find out whether all cells at the top can be moved one row up
        boolean canMove = true;
        for (WCell wc : cellsToMove) {
            // if robot
            if (wc.equals(robot) && (wc.isAtTop() || grid[wc.row()-1][wc.lc()] == '#')) {
                canMove = false;
                break;
            } else if (!wc.equals(robot) && (wc.isAtTop() || grid[wc.row()-1][wc.lc()] == '#' || grid[wc.row()-1][wc.rc()] == '#')) {
                canMove = false;
                break;
            }
        }
        if (canMove) {
            // move all cells except the robot cell up
            for (WCell wc : cellsToMove.reversed()) {
                if (wc.equals(robot)) { continue; }
                grid[wc.row()-1][wc.lc()] = '[';
                grid[wc.row()-1][wc.rc()] = ']';
                grid[wc.row()][wc.lc()] = '.';
                grid[wc.row()][wc.rc()] = '.';
            }
            // move the robot cell up
            grid[robotLocation.r()-1][robotLocation.c()] = '@';
            grid[robotLocation.r()][robotLocation.c()] = '.';
            return new Cell(robotLocation.r() - 1, robotLocation.c());
        }
        // No cells can be moved up
        return robotLocation;
    }

    // Write routines to move boxes down
    private static ArrayDeque<WCell> findAdjacentBoxesToMoveDown(WCell wc, final char[][] grid, final int R, final int C) {
        ArrayDeque<WCell> bfsQueue = new ArrayDeque<>();
        ArrayDeque<WCell> visitedQueue = new ArrayDeque<>();
        Set<WCell> discovered = new HashSet<>();

        discovered.add(wc);
        visitedQueue.add(wc);

        // 0 1 2 3 4
        //   @ . . .
        //   [ ] . .
        WCell bottomRight = new WCell(wc.row() + 1, wc.lc(), wc.lc() + 1);
        // 0 1 2 3 4
        // . . @ . .
        // . [ ] . .
        WCell bottomLeft = new WCell(wc.row() + 1, wc.lc() - 1, wc.lc());

        if (bottomRight.isValid(R, C) && bottomRight.containsValidChar(grid, validChars)) {
            discovered.add(bottomRight); bfsQueue.add(bottomRight);
        }
        if (bottomLeft.isValid(R, C) && bottomLeft.containsValidChar(grid, validChars)) {
            discovered.add(bottomLeft); bfsQueue.add(bottomLeft);
        }

        while (!bfsQueue.isEmpty()) {
            // poll all at this level
            int L = bfsQueue.size();
            while (L-- > 0) {
                WCell current = bfsQueue.poll();
                visitedQueue.add(current);

                // 0 1 2 3 4
                // . [ ] . . current
                // . [ ] . . bottom
                WCell bottom = new WCell(current.row() + 1, current.lc(), current.rc());
                // 0 1 2 3 4
                // . . [ ] .  current
                // . [ ] . .  bottomLeft
                bottomLeft = new WCell(current.row() + 1, current.lc() - 1, current.lc());
                // 0 1 2 3 4
                // . [ ] . . current
                // . . [ ] . bottomRight
                bottomRight = new WCell(current.row() + 1, current.rc(), current.rc() + 1);

                if (bottom.isValid(R, C) && bottom.containsValidChar(grid, validChars) && !discovered.contains(bottom)) {
                    discovered.add(bottom); bfsQueue.add(bottom);
                }
                if (bottomLeft.isValid(R, C) && bottomLeft.containsValidChar(grid, validChars) && !discovered.contains(bottomLeft)) {
                    discovered.add(bottomLeft); bfsQueue.add(bottomLeft);
                }
                if (bottomRight.isValid(R, C) && bottomRight.containsValidChar(grid, validChars) && !discovered.contains(bottomRight)) {
                    discovered.add(bottomRight); bfsQueue.add(bottomRight);
                }
            }
        }
        return visitedQueue;
    }


    private static Cell moveAdjacentBoxesDown(final Cell robotLocation, final char[][] grid, final int R, final int C) {
        WCell robot = new WCell(robotLocation.r(), robotLocation.c(), robotLocation.c() + 1); // pseudo wide cell

        ArrayDeque<WCell> cellsToMove = findAdjacentBoxesToMoveDown(robot, grid, R, C);
        boolean canMove = true;
        for (WCell wc : cellsToMove) {
            // if robot
            if (wc.equals(robot) && (wc.isAtBottom(R) || grid[wc.row()+1][wc.lc()] == '#')) {
                canMove = false;
                break;
            } else if (!wc.equals(robot) && (wc.isAtBottom(R) || grid[wc.row()+1][wc.lc()] == '#' || grid[wc.row()+1][wc.rc()] == '#')) {
                canMove = false;
                break;
            }
        }
        if (canMove) {
            // move all cells except the robot cell up
            for (WCell wc : cellsToMove.reversed()) {
                if (wc.equals(robot)) { continue; }
                grid[wc.row()+1][wc.lc()] = '[';
                grid[wc.row()+1][wc.rc()] = ']';
                grid[wc.row()][wc.lc()] = '.';
                grid[wc.row()][wc.rc()] = '.';
            }
            // move the robot cell down
            grid[robotLocation.r()+1][robotLocation.c()] = '@';
            grid[robotLocation.r()][robotLocation.c()] = '.';
            // return new location of the robot
            return new Cell(robotLocation.r() + 1, robotLocation.c());
        }
        // No cells can be moved down
        return robotLocation;
    }

    private static ArrayDeque<WCell> findAdjacentBoxesToLeft(WCell start, final char[][] grid, final int R, final int C) {
        ArrayDeque<WCell> q = new ArrayDeque<>();

        q.add(start);

        WCell left = new WCell(start.row(), start.lc()-2, start.lc()-1);
        while (left.isValid(R, C) && left.containsValidChar(grid, validChars)) {
            q.add(left);
            left = new WCell(left.row(), left.lc() - 2, left.rc() - 2);
        }

        return q;
    }

    private static Cell moveAdjacentBoxesLeft(final Cell robotLocation, final char[][] grid, final int R, final int C) {
        WCell robot = new WCell(robotLocation.r(), robotLocation.c(), robotLocation.c()+1); // pseudo wide cell

        ArrayDeque<WCell> leftBoxes = findAdjacentBoxesToLeft(robot, grid, R, C);
        // Find if we can move the rightmost box to the right
        boolean canMove = true;
        WCell leftMost = leftBoxes.getLast();
        if (!leftMost.equals(robot) && (leftMost.isAtLeft() || grid[leftMost.row()][leftMost.lc()-1] == '#')) {
            // 0 1 2 3
            // [ ] @ .
            // # [ ] @
            canMove = false;
        } else if (leftMost.equals(robot) && ((robot.lc() == 0 || grid[robot.row()][robot.lc() - 1] == '#'))) {
            // 0 1 2 3
            // # @ .
            // @ .
            canMove = false;
        }
        if (canMove) {
            for (WCell wc : leftBoxes.reversed()) {
                if (wc.equals(robot)) { continue; }
                grid[wc.row()][wc.lc()] = '.';
                grid[wc.row()][wc.rc()] = '.';
                grid[wc.row()][wc.lc()-1] = '[';
                grid[wc.row()][wc.rc()-1] = ']';
            }
            // move the robot cell to the left
            grid[robotLocation.r()][robotLocation.c()-1] = '@';
            grid[robotLocation.r()][robotLocation.c()] = '.';
            return new Cell(robotLocation.r(), robotLocation.c() - 1);
        }
        return robotLocation;
    }


    private static ArrayDeque<WCell> findAdjacentBoxesToRight(WCell start, final char[][] grid, final int R, final int C) {
        ArrayDeque<WCell> q = new ArrayDeque<>();

        q.add(start);

        WCell right = new WCell(start.row(), start.lc()+1, start.lc() + 2);
        while (right.isValid(R, C) && right.containsValidChar(grid, validChars)) {
            q.add(right);
            right = new WCell(right.row(), right.lc() + 2, right.rc() + 2);
        }

        return q;
    }

    private static Cell moveAdjacentBoxesRight(final Cell robotLocation, final char[][] grid, final int R, final int C) {
        WCell robot = new WCell(robotLocation.r(), robotLocation.c(), robotLocation.c()+1); // pseudo wide cell

        ArrayDeque<WCell> rightBoxes = findAdjacentBoxesToRight(robot, grid, R, C);
        // Find if we can move the rightmost box to the right
        boolean canMove = true;
        WCell rightMost = rightBoxes.getLast();
        if (!(rightMost.equals(robot)) && (rightMost.isAtRight(C) || grid[rightMost.row()][rightMost.rc()+1] == '#')) {
            // 0 1 2 3
            //     [ ]
            //    [] #
            canMove = false;
        } else if (rightMost.equals(robot) && (robot.rc() == C || grid[robot.row()][robot.rc()] == '#')) {
            // 0 1 2 3
            //     @ #
            //     . @
            canMove = false;
        }
        if (canMove) {
            for (WCell wc : rightBoxes.reversed()) {
                if (wc.equals(robot)) { continue; }
                grid[wc.row()][wc.lc()] = '.';
                grid[wc.row()][wc.rc()] = '.';
                grid[wc.row()][wc.lc()+1] = '[';
                grid[wc.row()][wc.rc()+1] = ']';
            }
            // move the robot cell to the right
            grid[robotLocation.r()][robotLocation.c()+1] = '@';
            grid[robotLocation.r()][robotLocation.c()] = '.';
            return new Cell(robotLocation.r(), robotLocation.c() + 1);
        }
        return robotLocation;
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

    private static ArrayDeque<Cell> cellsToTheLeftOf(final Cell cell, final char[][] grid, final int R, final int C) {
        return cellsInDirection(cell, grid, R, C, 0, -1);
    }

    private static ArrayDeque<Cell> cellsToTheRightOf(final Cell cell, final char[][] grid, final int R, final int C) {
        return cellsInDirection(cell, grid, R, C, 0, 1);
    }

    private static ArrayDeque<Cell> cellsAbove(final Cell cell, final char[][] grid, final int R, final int C) {
        return cellsInDirection(cell, grid, R, C, -1, 0);
    }

    private static ArrayDeque<Cell> cellsBelow(final Cell cell, final char[][] grid, final int R, final int C) {
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
    private static Cell moveCells(final ArrayDeque<Cell> cells, final char[][] grid, final Cell robot, final int R,
                                  final int C, final int dr, final int dc) {
        final Cell leadingCell = cells.getLast();
        final int newRow = leadingCell.r() + dr;
        final int newCol = leadingCell.c() + dc;

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

    private static long gpsCoordSum(char[][] grid, char boxChar, int topBottomPadding, int leftRightPadding) {
        long sum = 0;
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                sum += grid[r][c] == boxChar ? 100L * (r + topBottomPadding) + (c + leftRightPadding) : 0;
            }
        }
        return sum;
    }

    private static char[][] generatePart2Warehouse(final char[][] part1Warehouse) {
        final int R = part1Warehouse.length;
        final int C = part1Warehouse[0].length;
        final char[][] w = new char[R][C * 2];

        for (int r = 0; r < R; r++) {
            for (int c = 0; c < C; c++) {
                if (part1Warehouse[r][c] == 'O') { w[r][c*2] = '['; w[r][c*2 + 1] = ']'; }
                else if (part1Warehouse[r][c] == '#') { w[r][c*2] = '#'; w[r][c*2 + 1] = '#'; }
                else if (part1Warehouse[r][c] == '@') { w[r][c*2] = '@'; w[r][c*2 + 1] = '.'; }
                else { w[r][c*2] = '.'; w[r][c*2 + 1] = '.'; }
            }
        }

        return w;
    }

    public static long part2(final String fileName) {
        final SolutionInput input = parse(fileName);
        final int R = input.grid().length;
        final int C = input.grid()[0].length * 2;
        final char[][] w = generatePart2Warehouse(input.grid());
        final Cell startSmaller = input.start();
        Cell start = new Cell(startSmaller.r(), startSmaller.c() * 2);

        for (Character move : input.moves) {
            switch (move) {
                case '<' -> start = moveAdjacentBoxesLeft(start, w, R, C);
                case '>' -> start = moveAdjacentBoxesRight(start, w, R, C);
                case '^' -> start = moveAdjacentBoxesUp(start, w, R, C);
                case 'v' -> start = moveAdjacentBoxesDown(start, w, R, C);
            }
        }
        return gpsCoordSum(w, '[', 1, 2);
    }

    public static long part1(final String fileName) {
        final SolutionInput input = parse(fileName);
        final int R = input.grid().length;
        final int C = input.grid()[0].length;
        Cell start = input.start();
        List<char[][]> grids = new ArrayList<>();

        for (Character move : input.moves) {
            switch (move) {
                case '<' -> {
                    final var cells = cellsToTheLeftOf(start, input.grid(), R, C); cells.addFirst(start);
                    start = moveCellsLeft(cells, input.grid(), R, C, start);
                }
                case '>' -> {
                    final var cells = cellsToTheRightOf(start, input.grid(), R, C); cells.addFirst(start);
                    start = moveCellsRight(cells, input.grid(), R, C, start);
                }
                case '^' -> {
                    final var cells = cellsAbove(start, input.grid(), R, C); cells.addFirst(start);
                    start = moveCellsUp(cells, input.grid(), R, C, start);
                }
                case 'v' -> {
                    final var cells = cellsBelow(start, input.grid(), R, C); cells.addFirst(start);
                    start = moveCellsDown(cells, input.grid(), R, C, start);
                }
            }
            grids.add(input.grid().clone());
        }
        return gpsCoordSum(input.grid(), 'O', 1, 1);
    }
}
