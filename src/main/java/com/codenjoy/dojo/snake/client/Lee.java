package com.codenjoy.dojo.snake.client;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Lee {
    private final int dimY;
    private final int dimX;
    private final int[][] board;
    private final static int OBSTACLE = -Integer.MAX_VALUE;
    private static final int EMPTY = 0;
    private final int[][] allowed = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1},
            {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
    };

    public Lee(int dimX, int dimY) {
        this.dimY = dimY;
        this.dimX = dimX;
        this.board = new int[dimY][dimX];
    }

    private int at(LPoint p) {
        return at(p.x, p.y);
    }

    private int at(int x, int y) {
        return board[y][x];
    }

    private void mark(LPoint p, int val) {
        mark(p.y, p.x, val);
    }

    private void mark(int y, int x, int val) {
        board[y][x] = val;
    }

    private void initBoard() {
        IntStream.range(0, dimY).forEach(y -> IntStream.range(0, dimX).forEach((x -> mark(y, x, EMPTY))));
    }

    private void populateObstacles(Iterable<LPoint> obstacles) {
        obstacles.forEach(p -> mark(p, OBSTACLE));
    }

    private boolean isInRange(int a, int lo, int hi) {
        return a >= lo && a < hi;
    }

    private boolean isOnboard(LPoint p) {
        return isInRange(p.x, 0, dimX)
                && isInRange(p.y, 0, dimY);
    }

    private boolean isUnvisited(LPoint p) {
        return at(p) == EMPTY;
    }

    private Stream<LPoint> nextUnvisited(LPoint p) {
        return nextMoves(p).filter(this::isUnvisited);
    }

    private Stream<LPoint> nextMoves(LPoint p) {
        return Arrays.stream(allowed)
                .map(m -> p.move(m[0], m[1]))
                .filter(this::isOnboard);
    }

    ;

    private LPoint findAnyNext(LPoint p, int val) {
        return nextMoves(p).filter(p0 -> at(p0) == val)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    private Iterable<LPoint> backStep(LPoint p, LinkedList<LPoint> outcome) {
        outcome.addFirst(p);
        if (at(p) == 1) return outcome;
        LPoint anyNext = findAnyNext(p, at(p) - 1);
        return backStep(anyNext, outcome);
    }

    private Iterable<LPoint> backTrace(LPoint finish) {
        LinkedList<LPoint> outcome = new LinkedList<>();
        return backStep(finish, outcome);
    }

    private String fmt(int x, int y) {
        int val = at(x, y);
        return val == OBSTACLE ? "XX " : String.format("%2d ", at(x, y));
    }

    private void printBoard() {
        IntStream.range(0, dimY).forEach(y -> {
            IntStream.range(0, dimX).forEach(x ->
                    System.out.print(fmt(x, y)));
            System.out.println();
        });
    }

    private boolean doStep(Set<LPoint> step, LPoint finish, int counter) {
        if (step.isEmpty()) return false;
        step.forEach(p -> mark(p, counter));
        if (step.contains(finish)) return true;
        Set<LPoint> next = step.stream().flatMap(this::nextUnvisited)
                .collect(Collectors.toSet());
        printBoard();
        return doStep(next, finish, counter + 1);
    }

    private Optional<Iterable<LPoint>> doTrace(LPoint start, LPoint finish) {
        return doStep(new HashSet<>() {{
            add(start);
        }}, finish, 1)
                ? Optional.of(backTrace(finish))
                : Optional.empty();

    }

    public Optional<Iterable<LPoint>> trace(LPoint start, LPoint finish, Iterable<LPoint> obstacles) {
        initBoard();
        populateObstacles(obstacles);
        return doTrace(start, finish);
    }
}