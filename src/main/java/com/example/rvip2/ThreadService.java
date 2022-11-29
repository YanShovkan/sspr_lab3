package com.example.rvip2;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import java.util.concurrent.Future;

@Service
public class ThreadService {
    @Async("threadPoolExecutor")
    public Future<int[][]> sortPart(int[][] matrix) {
        int[] columnSumMatrix = getColumnSumMatrix(matrix);

        sort(matrix, columnSumMatrix);

        return new AsyncResult<>(matrix);
    }

    @Async("threadPoolExecutor")
    public Future<int[][]> merge(int[][] leftMatrix, int[][] rightMatrix) {
        int cursorLeft = 0;
        int cursorRight = 0;
        int counter = 0;

        int[] leftColumnSumMatrix = getColumnSumMatrix(leftMatrix);
        int[] rightColumnSumMatrix = getColumnSumMatrix(rightMatrix);

        int[][] mergedMatrix = new int[leftMatrix.length][leftColumnSumMatrix.length + rightColumnSumMatrix.length];

        while (cursorLeft < leftColumnSumMatrix.length && cursorRight < rightColumnSumMatrix.length) {
            if (leftColumnSumMatrix[cursorLeft] < rightColumnSumMatrix[cursorRight]) {
                for (int k = 0; k < mergedMatrix.length; k++) {
                    mergedMatrix[k][counter] = leftMatrix[k][cursorLeft];
                }
                cursorLeft++;
            } else {
                for (int k = 0; k < mergedMatrix.length; k++) {
                    mergedMatrix[k][counter] = rightMatrix[k][cursorRight];
                }
                cursorRight++;
            }
            counter++;
        }
        if (cursorLeft < leftColumnSumMatrix.length) {
            for (int i = counter; i < mergedMatrix[0].length; i++) {
                for (int k = 0; k < mergedMatrix.length; k++) {
                    mergedMatrix[k][counter] = leftMatrix[k][cursorLeft];
                }
                counter++;
                cursorLeft++;
            }
        }
        if (cursorRight < rightColumnSumMatrix.length) {
            for (int i = counter; i < mergedMatrix[0].length; i++) {
                for (int k = 0; k < mergedMatrix.length; k++) {
                    mergedMatrix[k][counter] = rightMatrix[k][cursorRight];
                }
                counter++;
                cursorRight++;
            }
        }
        return new AsyncResult<>(mergedMatrix);
    }

    private static int[] getColumnSumMatrix(int[][] matrix){
        int[] columnSumMatrix = new int[matrix[0].length];

        for (int j = 0; j < matrix[0].length; j++) {
            int sum = 0;
            for (int i = 0; i < matrix.length; i++) {
                sum += matrix[i][j];
            }
            columnSumMatrix[j] = sum;
        }
        return columnSumMatrix;
    }

    private static void sort(int[][] matrix, int[] minimalElementsMatrix){
        for (int i = 0; i < minimalElementsMatrix.length - 1; i++) {
            for (int j = minimalElementsMatrix.length - 1; j > i; j--) {
                if (minimalElementsMatrix[j - 1] > minimalElementsMatrix[j]) {
                    int tmp = minimalElementsMatrix[j - 1];
                    minimalElementsMatrix[j - 1] = minimalElementsMatrix[j];
                    minimalElementsMatrix[j] = tmp;
                    for(int k =0; k < matrix.length;k++){
                        tmp = matrix[k][j - 1];
                        matrix[k][j - 1] = matrix[k][j];
                        matrix[k][j] = tmp;
                    }
                }
            }
        }
    }

    public static int[][] copyPart(int[][] matrix, int countOfParts, int numberOfPart) {
        int size = matrix[0].length / countOfParts;
        int[][] copedMatrix = new int[matrix.length][size];

        int row = 0;
        int column = 0;
        for (int j = size * numberOfPart; j < size * (numberOfPart + 1); j++) {
            for (int i = 0; i < matrix.length; i++) {
                copedMatrix[row][column] = matrix[i][j];
                row++;
            }
            row = 0;
            column++;
        }

        return copedMatrix;
    }

}
