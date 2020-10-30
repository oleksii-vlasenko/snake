package com.codenjoy.dojo.snake.client;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LeeApp {

    public static void main(String[] args) {
        Lee lee = new Lee(15, 15);

        LPoint start = LPoint.of(0, 0);
        LPoint finish = LPoint.of(3, 4);
        LinkedList<LPoint> body = new LinkedList<LPoint>() {{
           add(LPoint.of(0, 1));
           add(LPoint.of(0, 2));
        }};

        List<LPoint> walls = Stream.of(
                IntStream.rangeClosed(1, 1).mapToObj(y -> LPoint.of(1, y))
        ).flatMap(a -> a).collect(Collectors.toList());

        LinkedList<LPoint> r = lee.trace(start, start, body, finish, new LinkedList<>());

        System.out.println(r);
    }
}
