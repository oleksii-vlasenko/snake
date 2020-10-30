package com.codenjoy.dojo.snake.client;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.RandomDice;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * User: your name
 */
public class YourSolver implements Solver<Board> {

    private Dice dice;
    private Board board;

    private static final int[] up = {0, 1};
    private static final int[] right = {1, 0};
    private static final int[] down = {0, -1};
    private static final int[] left = {-1, 0};


    public YourSolver(Dice dice) {
        this.dice = dice;
    }

    public LinkedList<LPoint> getCurrSnake() {
        LinkedList<LPoint> snake = new LinkedList<>();
        snake.addLast(LPoint.of(board.getHead().getX(), board.getHead().getY()));
        while (snake.size() < board.getSnake().size()) {
            char prev = board.getField()[snake.getLast().x][snake.getLast().y];

            switch (prev) {
                case '▼':
                    snake.addLast(LPoint.of(snake.getLast().x, snake.getLast().y + 1));
                    break;
                case '▲':
                    snake.addLast(LPoint.of(snake.getLast().x, snake.getLast().y - 1));
                    break;
                case '◄':
                    snake.addLast(LPoint.of(snake.getLast().x + 1, snake.getLast().y));
                    break;
                case '►':
                    snake.addLast(LPoint.of(snake.getLast().x - 1, snake.getLast().y));
                    break;
                case '╗':
                    if (!snake.contains(LPoint.of(snake.getLast().x - 1, snake.getLast().y)))
                        snake.addLast(LPoint.of(snake.getLast().x - 1, snake.getLast().y));
                    else
                        snake.addLast(LPoint.of(snake.getLast().x, snake.getLast().y - 1));
                    break;
                case '╔':
                    if (!snake.contains(LPoint.of(snake.getLast().x + 1, snake.getLast().y)))
                        snake.addLast(LPoint.of(snake.getLast().x + 1, snake.getLast().y));
                    else
                        snake.addLast(LPoint.of(snake.getLast().x, snake.getLast().y - 1));
                    break;
                case '╝':
                    if (!snake.contains(LPoint.of(snake.getLast().x - 1, snake.getLast().y)))
                        snake.addLast(LPoint.of(snake.getLast().x - 1, snake.getLast().y));
                    else
                        snake.addLast(LPoint.of(snake.getLast().x, snake.getLast().y + 1));
                    break;
                case '╚':
                    if (!snake.contains(LPoint.of(snake.getLast().x, snake.getLast().y + 1)))
                        snake.addLast(LPoint.of(snake.getLast().x, snake.getLast().y + 1));
                    else
                        snake.addLast(LPoint.of(snake.getLast().x + 1, snake.getLast().y));
                    break;
                case '═':
                    if (!snake.contains(LPoint.of(snake.getLast().x + 1, snake.getLast().y)))
                        snake.addLast(LPoint.of(snake.getLast().x + 1, snake.getLast().y));
                    else
                        snake.addLast(LPoint.of(snake.getLast().x - 1, snake.getLast().y));
                    break;
                case '║':
                    if (!snake.contains(LPoint.of(snake.getLast().x, snake.getLast().y + 1)))
                        snake.addLast(LPoint.of(snake.getLast().x, snake.getLast().y + 1));
                    else
                        snake.addLast(LPoint.of(snake.getLast().x, snake.getLast().y - 1));
                    break;
                default:
                    break;
            }
        }
        return snake;
    }

    @Override
    public String get(Board board) {
        this.board = board;
        Lee lee = new Lee(15, 15);


        LPoint head = LPoint.of(board.getHead().getX(), board.getHead().getY());

        List<Point> apples = board.getApples();
        LPoint apple = LPoint.of(apples.get(0).getX(), apples.get(0).getY());

        LinkedList<LPoint> snake = getCurrSnake();

        List<Point> stones = board.getStones();

        List<LPoint> obstacles = Stream.of
                (board.getWalls().stream().map(w -> LPoint.of(w.getX(), w.getY())),
                        stones.stream().map(s -> LPoint.of(s.getX(), s.getY())))
                .flatMap(a -> a)
                .collect(Collectors.toList());
        System.out.printf("Snake length: %d\n", snake.size());
        LinkedList<LPoint> trace = lee.trace(snake.getFirst(), snake.getLast(), snake, apple, obstacles);
        if (trace.size() > 1) {
            LPoint nextMove = trace.get(1);

            int[] move = new int[]{nextMove.x - head.x, nextMove.y - head.y};

            if (Arrays.equals(move, left)) return Direction.LEFT.toString();
            if (Arrays.equals(move, right)) return Direction.RIGHT.toString();
            if (Arrays.equals(move, down)) return Direction.DOWN.toString();
        }
        return Direction.UP.toString();
    }

    public static void main(String[] args) {
        WebSocketRunner.runClient(
                // paste here board page url from browser after registration
                "http://165.227.151.163/codenjoy-contest/board/player/jyxtdrwz19harkcw5e8l?code=5086553899922126410",
                new YourSolver(new RandomDice()),
                new Board());
    }

}
