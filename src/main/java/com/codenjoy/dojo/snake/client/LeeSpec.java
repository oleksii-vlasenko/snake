package com.codenjoy.dojo.snake.client;

import jdk.jfr.Description;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class LeeSpec {

    @Description("Virtual snake (path > snake)")
    @Test
    public void test1() {
        Lee lee = new Lee(5, 5);

        LinkedList<LPoint> snake = new LinkedList<>() {{
            add(LPoint.of(2, 1));
            add(LPoint.of(1, 1));
            add(LPoint.of(0, 1));
            add(LPoint.of(0, 0));
        }};
        LinkedList<LPoint> path = new LinkedList<>() {{
            add(LPoint.of(2, 1));
            add(LPoint.of(3, 1));
            add(LPoint.of(4, 1));
            add(LPoint.of(4, 2));
            add(LPoint.of(4, 3));
            add(LPoint.of(4, 4));
        }};

        LinkedList<Object> result = new LinkedList<>() {{
            add(LPoint.of(4, 4));
            add(LPoint.of(4, 3));
            add(LPoint.of(4, 2));
            add(LPoint.of(4, 1));
        }};
        assertEquals(result, lee.getVSnake(snake, path));
    }


    @Description("Virtual snake (path == snake)")
    @Test
    public void test2() {
        Lee lee = new Lee(5, 5);

        LinkedList<LPoint> snake = new LinkedList<>() {{
            add(LPoint.of(2, 1));
            add(LPoint.of(1, 1));
            add(LPoint.of(0, 1));
            add(LPoint.of(0, 0));
        }};
        LinkedList<LPoint> path = new LinkedList<>() {{
            add(LPoint.of(2, 1));
            add(LPoint.of(3, 1));
            add(LPoint.of(4, 1));
            add(LPoint.of(4, 2));
        }};

        LinkedList<Object> result = new LinkedList<>() {{
            add(LPoint.of(4, 2));
            add(LPoint.of(4, 1));
            add(LPoint.of(3, 1));
            add(LPoint.of(2, 1));
        }};
        assertEquals(result, lee.getVSnake(snake, path));
    }

    @Description("Virtual snake (path < snake)")
    @Test
    public void test3() {
        Lee lee = new Lee(5, 5);

        LinkedList<LPoint> snake = new LinkedList<>() {{
            add(LPoint.of(2, 1));
            add(LPoint.of(1, 1));
            add(LPoint.of(0, 1));
            add(LPoint.of(0, 0));
        }};
        LinkedList<LPoint> path = new LinkedList<>() {{
            add(LPoint.of(2, 1));
            add(LPoint.of(3, 1));
            add(LPoint.of(4, 1));
        }};

        LinkedList<Object> result = new LinkedList<>() {{
            add(LPoint.of(4, 1));
            add(LPoint.of(3, 1));
            add(LPoint.of(2, 1));
            add(LPoint.of(1, 1));
        }};
        assertEquals(result, lee.getVSnake(snake, path));
    }

    @Description("Path_1 (head - apple)")
    @Test
    public void test10() {
        Lee lee = new Lee(5, 5);

        LPoint apple = LPoint.of(4, 1);
        LinkedList<LPoint> body = new LinkedList<>() {{
            add(LPoint.of(2, 1));
            add(LPoint.of(1, 1));
            add(LPoint.of(0, 1));
            add(LPoint.of(0, 0));
        }};
        LinkedList<LPoint> trace = lee.trace(body.getFirst(), body.getLast(), body, apple, new LinkedList<>());
        LinkedList<Object> result = new LinkedList<>() {{
            add(LPoint.of(2, 1));
            add(LPoint.of(3, 1));
            add(LPoint.of(4, 1));
        }};
        assertEquals(result, trace);
    }

    @Description("Virtual snake (path == snake)")
    @Test
    public void test11() {
        Lee lee = new Lee(5, 5);

        LPoint apple = LPoint.of(4, 1);
        LinkedList<LPoint> body = new LinkedList<>() {{
            add(LPoint.of(4, 2));
            add(LPoint.of(3, 2));
            add(LPoint.of(2, 2));
            add(LPoint.of(1, 2));
            add(LPoint.of(0, 2));
            add(LPoint.of(0, 3));
            add(LPoint.of(0, 4));
            add(LPoint.of(1, 4));
        }};
        LinkedList<LPoint> trace = lee.trace(body.getFirst(), body.getLast(), body, apple, new LinkedList<>());
        LinkedList<Object> result = new LinkedList<>() {{
            add(LPoint.of(4, 2));
            add(LPoint.of(4, 1));
        }};
        assertNotEquals(result, trace);
    }

    @Test
    public void test20() {
        Lee lee = new Lee(5, 5);

        LPoint apple = LPoint.of(4, 1);
        LinkedList<LPoint> body = new LinkedList<>() {{
            add(LPoint.of(4, 2));
            add(LPoint.of(3, 2));
            add(LPoint.of(2, 2));
            add(LPoint.of(1, 2));
            add(LPoint.of(0, 2));
            add(LPoint.of(0, 3));
            add(LPoint.of(0, 4));
            add(LPoint.of(1, 4));
            add(LPoint.of(2, 4));
        }};
        LinkedList<LPoint> trace = lee.trace(body.getFirst(), body.getLast(), body, apple, new LinkedList<>());
        LinkedList<Object> result = new LinkedList<>() {{
            add(LPoint.of(4, 2));
            add(LPoint.of(4, 1));
        }};
        assertNotEquals(result, trace);
    }

    @Test
    public void test31() {
        Lee lee = new Lee(5, 5);

        LPoint apple = LPoint.of(2, 3);
        LinkedList<LPoint> body = new LinkedList<>() {{
            add(LPoint.of(4, 3));
            add(LPoint.of(4, 2));
            add(LPoint.of(3, 2));
            add(LPoint.of(2, 2));
            add(LPoint.of(1, 2));
            add(LPoint.of(1, 3));
            add(LPoint.of(1, 4));
            add(LPoint.of(0, 4));
            add(LPoint.of(0, 3));
            add(LPoint.of(0, 2));
            add(LPoint.of(0, 1));
        }};
        LinkedList<LPoint> trace = lee.trace(body.getFirst(), body.getLast(), body, apple, new LinkedList<>());
        LinkedList<Object> result = new LinkedList<>() {{
            add(LPoint.of(4, 3));
            add(LPoint.of(4, 4));
            add(LPoint.of(3, 4));
            add(LPoint.of(3, 3));
            add(LPoint.of(2, 3));
        }};
        assertEquals(result, trace);
    }

    @Test
    public void snake_1() {
        Lee lee = new Lee(15, 15);
        LPoint apple = LPoint.of(4, 2);
        LinkedList<LPoint> body = new LinkedList<>() {{
            add(LPoint.of(9, 7));
            add(LPoint.of(6, 3));
            add(LPoint.of(6, 4));
            add(LPoint.of(6, 5));
            add(LPoint.of(6, 6));
            add(LPoint.of(6, 7));
            add(LPoint.of(6, 10));
            add(LPoint.of(6, 11));
            add(LPoint.of(7, 1));
            add(LPoint.of(7, 2));
            add(LPoint.of(7, 3));
            add(LPoint.of(7, 7));
            add(LPoint.of(7, 8));
            add(LPoint.of(7, 9));
            add(LPoint.of(7, 10));
            add(LPoint.of(7, 11));
            add(LPoint.of(7, 12));
            add(LPoint.of(8, 8));
            add(LPoint.of(8, 9));
            add(LPoint.of(8, 10));
            add(LPoint.of(8, 11));
            add(LPoint.of(8, 12));
            add(LPoint.of(9, 8));
            add(LPoint.of(10, 7));
            add(LPoint.of(10, 8));
            add(LPoint.of(11, 7));
            add(LPoint.of(11, 8));
            add(LPoint.of(12, 7));
            add(LPoint.of(12, 8));
            add(LPoint.of(13, 7));
            add(LPoint.of(13, 8));
        }};
        LinkedList<LPoint> obstacles = new LinkedList<>();
        for (int i = 0; i < 15; i++) {
            obstacles.add(LPoint.of(0, i));
            obstacles.add(LPoint.of(14, i));
            obstacles.add(LPoint.of(i, 0));
            obstacles.add(LPoint.of(i, 14));
        }
        LinkedList<LPoint> trace = lee.trace(body.getFirst(), LPoint.of(7, 1), body, apple, obstacles);
        LinkedList<Object> result = new LinkedList<>() {{
            add(LPoint.of(4, 3));
            add(LPoint.of(4, 4));
            add(LPoint.of(3, 4));
            add(LPoint.of(3, 3));
            add(LPoint.of(2, 3));
        }};
        assertNotEquals(result, trace);
    }
}