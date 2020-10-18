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
import com.codenjoy.dojo.services.RandomDice;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * User: your name
 */
public class YourSolver implements Solver<Board> {

    private Dice dice;
    private Board board;


    private Set<Integer>[] graph;

    public YourSolver(Dice dice) {
        this.dice = dice;
        this.graph = init();
    }

    private Set<Integer>[] init() {
        Set<Integer>[] graph = new Set[225];
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                graph[i * 15 + j] = new HashSet<>();
                if (i - 1 >= 0) graph[i * 15 + j].add((i - 1) * 15 + j);
                if (j + 1 <= 15 - 1) graph[i * 15 + j].add(i * 15 + j + 1);
                if (i + 1 <= 15 - 1) graph[i * 15 + j].add((i + 1) * 15 + j);
                if (j - 1 >= 0) graph[i * 15 + j].add(i * 15 + j - 1);
            }
        }
        return graph;
    }

    public String bfs(int v, int w) {

        int[] color = new int[this.graph.length];
        int[] parent = new int[this.graph.length];
        int[] dist = new int[this.graph.length];

        Arrays.fill(color, -1);
        Arrays.fill(parent, -1);
        Arrays.fill(dist, 0);

        color[v] = color[v] + 1;
        LinkedList<Integer> q = new LinkedList<>();
        q.addLast(v);
        while (!q.isEmpty()) {
            int u = q.removeFirst();
            for (Integer item : this.graph[u]) {
                if (color[item] == -1) {
                    color[item] = color[item] + 1;
                    dist[item] = dist[u] + 1;
                    parent[item] = u;
                    q.addLast(item);
                }
            }
            color[u] = color[u] + 1;
        }
        int curr = w;
        LinkedList<Integer> temp = new LinkedList<>();
        while (curr != v) {
            temp.addFirst(curr);
            curr = parent[curr];
        }
        temp.addFirst(v);

        String result;
        switch (v - curr) {
            case -1: result = Direction.RIGHT.toString();
            break;
            case 1: result = Direction.LEFT.toString();
            break;
            case 15: result = Direction.UP.toString();
            break;
            default: result = Direction.DOWN.toString();
        }

        return result;
    }

    @Override
    public String get(Board board) {
        this.board = board;
        System.out.println(board.toString());
        return bfs(this.board.getHead().getY() * 15 + this.board.getHead().getX(),
                this.board.getApples().get(0).getY() * 15 + this.board.getApples().get(0).getX());
    }

    public static void main(String[] args) {
        WebSocketRunner.runClient(
                // paste here board page url from browser after registration
                "http://165.227.151.163/codenjoy-contest/board/player/jyxtdrwz19harkcw5e8l?code=5086553899922126410",
                new YourSolver(new RandomDice()),
                new Board());
    }

}
