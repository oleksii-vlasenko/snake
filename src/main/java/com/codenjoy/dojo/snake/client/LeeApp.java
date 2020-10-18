package com.codenjoy.dojo.snake.client;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LeeApp {

    public static void main(String[] args) {
        Lee lee = new Lee(15, 10);

        LPoint start = LPoint.of(0, 0);
        LPoint finish = LPoint.of(14, 9);

        List<LPoint> walls = Stream.of(
                IntStream.rangeClosed(3, 9).mapToObj(y -> LPoint.of(5, y)),
                IntStream.rangeClosed(0, 7).mapToObj(y -> LPoint.of(10, y))
        ).flatMap(a -> a).collect(Collectors.toList());

        String r = lee.trace(start, finish, walls)
                .map(Object::toString)
                .orElse("Path not found");

        System.out.println(r);
    }
}
