package dev.gmathur.problems;

import dev.gmathur.utils.Util.Direction;
import dev.gmathur.utils.Util.Pair;
import dev.gmathur.utils.Util.Triple;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

import static dev.gmathur.utils.Util.printGrid;

public class Day16ReindeerMaze {
    private record SolutionInput(char[][] maze, Pair<Integer, Integer> start, Pair<Integer, Integer> end) { }
    private record MoveInfo(Pair<Integer, Integer> nextCell, Direction dir, long cost) { }
    private static SolutionInput parse(String fileName) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(Day16ReindeerMaze.class.getClassLoader().getResourceAsStream(fileName))))) {

            List<char[]> mazeList = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                mazeList.add(line.toCharArray());
            }
            // drop the first and last rows
            mazeList.removeFirst();
            mazeList.removeLast();

            Pair<Integer, Integer> start = null;
            Pair<Integer, Integer> end = null;

            char[][] maze = new char[mazeList.size()][mazeList.get(0).length - 2];
            for (int i = 0; i < mazeList.size(); i++) {
                for (int j = 1; j < mazeList.get(i).length - 1; j++) {
                    maze[i][j - 1] = mazeList.get(i)[j];
                    if (maze[i][j - 1] == 'S') {
                        start = new Pair<>(i, j - 1);
                    } else if (maze[i][j - 1] == 'E') {
                        end = new Pair<>(i, j - 1);
                    }
                }
            }

            return new SolutionInput(maze, start, end);
        } catch (Exception e) {
            throw new RuntimeException("Error reading file: " + fileName, e);
        }
    }

    /**
     * Given a cell curr, return the next cells that can be reached from curr and the cost to reach them. At each cell,
     * there are three options: move forward, turn right, and turn left. The cost to move forward is 1, and the cost to
     * turn right or left is 1000.
     *
     * @param maze The problem maze. We need it to check if the next cell is a wall or not
     * @param R Number of rows in the maze
     * @param C Number of columns in the maze
     * @param curr The current cell
     * @param dir The direction the reindeer is facing
     *
     * @return A list of MoveInfo objects that contain the next cell, the direction to reach it, and the cost to reach
     * it
     */
    private static List<MoveInfo> getNextCellWithCost(final char[][] maze, final int R, final int C,
                                                      final Pair<Integer, Integer> curr, final Direction dir) {
        int row = curr.first();
        int col = curr.second();
        List<MoveInfo> nextCells = new ArrayList<>();
        switch (dir) {
            case NORTH -> {
                // . ^ .
                // < ^ >
                // . . .
                if (row - 1 >= 0 && maze[row - 1][col] != '#') { // Continue moving north as one of the options
                    nextCells.add(new MoveInfo(new Pair<>(row - 1, col), Direction.NORTH, 1));
                }
                nextCells.add(new MoveInfo(curr, Direction.EAST, 1000)); // turn right as another
                nextCells.add(new MoveInfo(curr, Direction.WEST, 1000)); // and turn left as another
            }
            case EAST -> {
                // . ^ .
                // . > >
                // . v .
                if (col + 1 < C && maze[row][col + 1] != '#') {
                    nextCells.add(new MoveInfo(new Pair<>(row, col + 1), Direction.EAST, 1));
                }
                nextCells.add(new MoveInfo(curr, Direction.SOUTH, 1000));
                nextCells.add(new MoveInfo(curr, Direction.NORTH, 1000));
            }
            case SOUTH -> {
                // . . .
                // < V >
                // . V .
                if (row + 1 < R && maze[row + 1][col] != '#') {
                    nextCells.add(new MoveInfo(new Pair<>(row+1, col), Direction.SOUTH, 1));
                }
                nextCells.add(new MoveInfo(curr, Direction.WEST, 1000));
                nextCells.add(new MoveInfo(curr, Direction.EAST, 1000));
            }
            case WEST -> {
                // . ^ .
                // < < .
                // . v .
                if (col - 1 >= 0 && maze[row][col - 1] != '#') {
                    nextCells.add(new MoveInfo(new Pair<>(row, col - 1), Direction.WEST, 1));
                }
                nextCells.add(new MoveInfo(curr, Direction.NORTH, 1000));
                nextCells.add(new MoveInfo(curr, Direction.SOUTH, 1000));
            }
        }
        return nextCells;
    }

    public record PQState(Pair<Integer, Integer> cell, long cost, Direction dir, List<Pair<Integer, Integer>> path)
            implements Comparable<PQState> {
        @Override
        public int compareTo(PQState o) {
            return Long.compare(cost, o.cost);
        }
    }

    public static long part2(String fileName, boolean debugPrint) {
        var si = parse(fileName);

        var R = si.maze.length;
        var C = si.maze[0].length;

        // The shortest path from the start cell to each of the <row, col, Direction> tuples
        final HashMap<Pair<Pair<Integer, Integer>, Direction>, Long> dist = new HashMap<>();

        PriorityQueue<PQState> pq = new PriorityQueue<>();

        pq.add(new PQState(si.start, 0L, Direction.EAST, new ArrayList<>()));
        long minDist = Long.MAX_VALUE;
        Set<List<Pair<Integer, Integer>>> shortestPaths = new HashSet<>();

        while (!pq.isEmpty()) {
            final var curr = pq.poll();
            final var currCell = curr.cell();
            final var currDist = curr.cost();
            final var currDir = curr.dir();

            // we have a new shortest path from start to <r, c, dir>
            dist.put(new Pair<>(currCell, currDir), currDist);

            // note that we can reach the end cell from potentially multiple directions, so that's why we don't break
            // as soon as we reach the end cell
            if (currCell.equals(si.end)) {
                if (currDist < minDist) {
                    minDist = currDist;
                    shortestPaths.clear();
                    shortestPaths.add(curr.path());
                } else if (currDist == minDist) {
                    shortestPaths.add(curr.path());
                }
            }

            final var nextCells = getNextCellWithCost(si.maze, R, C, currCell, currDir);
            for (var nextCell : nextCells) {
                var next = nextCell.nextCell();
                var nextDir = nextCell.dir();
                var nextCost = nextCell.cost();

                if (!dist.containsKey(new Pair<>(next, nextDir))) {
                    // if we moved forward, we add the cell to the path
                    List<Pair<Integer, Integer>> newPath = new ArrayList<>(curr.path());
                    if (nextDir == currDir) {
                        newPath.add(next);
                    }
                    pq.add(new PQState(next, currDist + nextCost, nextDir, newPath));
                }
            }
        }

        if (debugPrint) {
            for (var path : shortestPaths) {
                char[][] mazeCopy = new char[si.maze.length][];
                for (int i = 0; i < si.maze.length; i++) {
                    mazeCopy[i] = Arrays.copyOf(si.maze[i], si.maze[i].length);
                }
                for (var cell : path) { mazeCopy[cell.first()][cell.second()] = '*'; }
                printGrid(mazeCopy);
            }
        }

        // find all the distinct coordinates in the shortest paths
        Set<Pair<Integer, Integer>> distinctCoords = new HashSet<>();
        for (var path : shortestPaths) {
            distinctCoords.addAll(path);
        }

        return distinctCoords.size() + 1; // add 1 to account for the start cell
    }

    /**
     * The solution uses a modified version of Dijkstra's algorithm to find the shortest path from the start cell to the
     * end cell. Each "node" in the graph is a tuple of the cell coordinates and the direction the reindeer is facing.
     *
     * @param fileName The name of the input file
     * @return The minimum distance from the start cell to the end cell
     */
    public static long part1(String fileName) {
        var si = parse(fileName);

        var R = si.maze.length;
        var C = si.maze[0].length;

        // The shortest path from the start cell to each of the <row, col, Direction> tuples
        final HashMap<Pair<Pair<Integer, Integer>, Direction>, Long> dist = new HashMap<>();

        PriorityQueue<Triple<Pair<Integer, Integer>, Long, Direction>> pq =
                new PriorityQueue<>(Comparator.comparing(Triple::second));

        pq.add(new Triple<>(si.start, 0L, Direction.EAST));
        long minDist = Long.MAX_VALUE;

        while (!pq.isEmpty()) {
            final var curr = pq.poll();
            final var currCell = curr.first();
            final var currDist = curr.second();
            final var currDir = curr.third();

            if (dist.containsKey(new Pair<>(currCell, currDir))) { continue; }

            // we have a new shortest path from start to <r, c, dir>
            dist.put(new Pair<>(currCell, currDir), currDist);

            // note that we can reach the end cell from potentially multiple directions, so that's why we don't break
            // as soon as we reach the end cell
            if (currCell.equals(si.end)) {
                minDist = Math.min(minDist, currDist);
            }

            final var nextCells = getNextCellWithCost(si.maze, R, C, currCell, currDir);
            for (var nextCell : nextCells) {
                var next = nextCell.nextCell();
                var nextDir = nextCell.dir();
                var nextCost = nextCell.cost();

                if (!dist.containsKey(new Pair<>(next, nextDir))) {
                    pq.add(new Triple<>(next, currDist + nextCost, nextDir));
                }
            }
        }

        return minDist;
    }
}