package com.example.rvip2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Controller
public class ThreadController {
    private final ThreadService threadService;
    private int[][] matrix;

    public ThreadController(ThreadService threadService) {
        this.threadService = threadService;
    }

    @RequestMapping("/h")
    public String h() {
        return "h";
    }

    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/execute")
    public String execute(HashMap<String, Object> model) throws ExecutionException, InterruptedException {
        matrix = createArray(100,100);

        long start = System.currentTimeMillis();
        int threadCount = 2;

        List<Future<int[][]>> futuresForSortPart = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            int partNumber = i;
            futuresForSortPart.add(threadService.sortPart(threadService.copyPart(matrix, threadCount, partNumber)));
        }

        List<int[][]> parts = new ArrayList<>();

        try {
            for (Future<int[][]> future : futuresForSortPart) {
                parts.add(future.get());
            }

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        futuresForSortPart.clear();

        List<Future<int[][]>> futuresForMerge = new ArrayList<>();

        int countOfIterations = threadCount / 2;

        while(countOfIterations > 0) {
            for (int i = 0; i < countOfIterations; i++) {
                int[][] first = parts.get(i * 2);
                int[][] second = parts.get(i * 2 + 1);
                futuresForMerge.add(threadService.merge(first, second));
            }

            try {
                parts.clear();
                for (Future<int[][]> future : futuresForMerge) {
                    parts.add(future.get());
                }
                futuresForMerge.clear();

            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
            countOfIterations /= 2;
        }

        long time = System.currentTimeMillis() - start;

        model.put("time", time);
        return "index";
    }

    private static int[][] createArray(int height, int weight) {
        int[][] matrix = new int[height][weight];
        Random random = new Random();

        for (int row = 0; row < weight; row++) {
            for (int col = 0; col < height; col++) {
                matrix[col][row] = random.nextInt(100);
            }
        }

        return matrix;
    }
}
