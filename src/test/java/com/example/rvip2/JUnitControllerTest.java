package com.example.rvip2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JUnitControllerTest {
    @Test
    public void testHomeController() {
        ThreadController homeController = new ThreadController(new ThreadService());
        String result = homeController.h();
        assertEquals(result, "h");
    }
    @Test
    public void testHomeController2() {
        ThreadController homeController = new ThreadController(new ThreadService());
        String result = homeController.hello();
        assertEquals(result, "hello");
    }
}
