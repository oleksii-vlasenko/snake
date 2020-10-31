package com.codenjoy.dojo.snake.client;

import jdk.jfr.Description;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class Lee {
    private final int dimY;
    private final int dimX;
    private final int[][] board;
    private final static int OBSTACLE = -Integer.MAX_VALUE;
    private static final int EMPTY = 0;
    private final int[][] allowed = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1}
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
        System.out.println();
    }

    private LPoint findAnyNext(LPoint p, int val) {
        return nextMoves(p)
                .filter(p0 -> at(p0) == val)
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

    private boolean doStep(Set<LPoint> step, LPoint finish, int counter) {
        if (step.isEmpty()) return false;
        step.forEach(p -> mark(p, counter));
        if (step.contains(finish)) return true;
        Set<LPoint> next = step.stream().flatMap(this::nextUnvisited)
                .collect(Collectors.toSet());
//        printBoard();
        return doStep(next, finish, counter + 1);
    }

    private Optional<Iterable<LPoint>> doTrace(LPoint start, LPoint finish) {
        return doStep(new HashSet<>() {{
            add(start);
        }}, finish, 1)
                ? Optional.of(backTrace(finish))
                : Optional.empty();
    }

    public LinkedList<LPoint> getVSnake(LinkedList<LPoint> snake, LinkedList<LPoint> path) {
        LinkedList<LPoint> vSnake = new LinkedList<>();
        LinkedList<LPoint> vPath = new LinkedList<>(path);
        while (vSnake.size() < snake.size() && vPath.size() > 0) {
            vSnake.addLast(vPath.removeLast());
        }
        if (snake.size() <= path.size()) return vSnake;
        int index = 1;
        while (vSnake.size() < snake.size()) {
            vSnake.addLast(snake.get(index++));
        }
        return vSnake;
    }

    private void resetBoard(Iterable<LPoint> obstacles) {
        initBoard();
        populateObstacles(obstacles);
    }

    private void resetBoard(Iterable<LPoint> obstacles, Iterable<LPoint> body) {
        resetBoard(obstacles);
        populateObstacles(body);
    }



    public LinkedList<LPoint> getMaxTrace(LPoint start, LPoint finish, Iterable<LPoint> body, Iterable<LPoint> obstacles) {

        resetBoard(obstacles, body);

        List<LPoint> from = nextUnvisited(start).collect(Collectors.toList());
        List<LPoint> to = nextUnvisited(finish).collect(Collectors.toList());

        LinkedList<LPoint> path = from.stream().flatMap(s ->
                to.stream().map(f -> {
                    resetBoard(obstacles, body);
                    return doTrace(s, f)
                            .orElse(new LinkedList<>());
                }))
                .map(ll -> (LinkedList<LPoint>) ll)
                .filter(l -> l.size() != 0)
                .findAny()
                .orElse(new LinkedList<>());

        path.addFirst(start);
        path.addLast(finish);

        populateObstacles(path);

        int index = 0;

        while (index < path.size() - 1 && path.size() != 0) {

            resetBoard(obstacles, body);
            populateObstacles(path);

            int i = index;

            LinkedList<LPoint> temp = (LinkedList<LPoint>) nextUnvisited(path.get(i)).flatMap(s ->
                    nextUnvisited(path.get(i + 1)).map(f ->
                            doTrace(s, f))).findAny().orElse(Optional.empty()).orElse(new LinkedList<>());

            if (temp.size() == 0) {
                index++;
                continue;
            }
            path.addAll(index + 1, temp);
        }

        return path.size() > 2 ? path : new LinkedList<>();
    }

    public LinkedList<LPoint> trace(LPoint head, LPoint tail, List<LPoint> body, LPoint apple, Iterable<LPoint> obstacles) {

        resetBoard(obstacles, body);

        LinkedList<LPoint> path0 = (LinkedList<LPoint>) doTrace(head, apple).orElse(new LinkedList<>());

        if (path0.size() != 0) {

            LinkedList<LPoint> snake = new LinkedList<>(body);
            LinkedList<LPoint> vSnake = getVSnake(snake, path0);

            resetBoard(obstacles, vSnake);

            List<LPoint> vHead = nextUnvisited(vSnake.getFirst()).collect(Collectors.toList());
            List<LPoint> vTail = nextUnvisited(tail).collect(Collectors.toList());

            LinkedList<LPoint> path2 = (LinkedList<LPoint>) vHead.stream().flatMap(s ->
                    vTail.stream().map(f ->
                            doTrace(s, f))).findAny().orElse(Optional.empty()).orElse(new LinkedList<>());

            if (path2.size() != 0) return path0;
        }

        LinkedList<LPoint> path3 = getMaxTrace(head, tail, body, obstacles);
        if (path3.size() > 2) return path3;

        return getMaxTrace(head, apple, body, obstacles);
    }
}

