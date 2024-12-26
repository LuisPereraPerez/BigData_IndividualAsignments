package com.example;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.map.IMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class DistributedMatrixMultiplication {

    public static void main(String[] args) throws Exception {
        // Create or join a Hazelcast cluster
        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();

        waitForCluster(hazelcastInstance, 3);

        int[][] matrixA = generateMatrix(3);
        int[][] matrixB = generateMatrix(3);

        System.out.println("Matrix A:");
        printMatrix(matrixA);

        System.out.println("Matrix B:");
        printMatrix(matrixB);

        int[][] result = multiplyMatricesDistributed(hazelcastInstance, matrixA, matrixB);

        System.out.println("Resultant Matrix:");
        printMatrix(result);

        hazelcastInstance.shutdown();
    }

    private static int[][] multiplyMatricesDistributed(HazelcastInstance hazelcastInstance, int[][] matrixA, int[][] matrixB) throws Exception {
        int size = matrixA.length;
        int[][] result = new int[size][size];

        // Transpose matrix B for easier column access
        int[][] transposedB = transposeMatrix(matrixB);

        // Use Hazelcast ExecutorService for distributed computation
        IExecutorService executorService = hazelcastInstance.getExecutorService("matrixMultiplication");

        List<Future<int[]>> futures = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            // Create a task for each row of matrixA
            MatrixMultiplicationTask task = new MatrixMultiplicationTask(matrixA[i], transposedB);
            futures.add(executorService.submit(task));
        }

        // Collect the results
        for (int i = 0; i < size; i++) {
            result[i] = futures.get(i).get();
        }

        return result;
    }

    private static int[][] transposeMatrix(int[][] matrix) {
        int size = matrix.length;
        int[][] transposed = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                transposed[i][j] = matrix[j][i];
            }
        }
        return transposed;
    }

    private static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int value : row) {
                System.out.printf("%4d", value);
            }
            System.out.println();
        }
    }

    private static int[][] generateMatrix(int size) {
        Random random = new Random();
        int[][] matrix = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = random.nextInt(10); // Generate random integers between 0 and 9
            }
        }
        return matrix;
    }

    private static void waitForCluster(HazelcastInstance hazelcastInstance, int clusterSize) {
        while (hazelcastInstance.getCluster().getMembers().size() < clusterSize) {
            System.out.println("Waiting for cluster to reach size " + clusterSize + "...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("Cluster size reached: " + clusterSize);
    }

    // Callable task for matrix row multiplication
    public static class MatrixMultiplicationTask implements Callable<int[]>, Serializable {
        private final int[] row;
        private final int[][] transposedMatrixB;

        public MatrixMultiplicationTask(int[] row, int[][] transposedMatrixB) {
            this.row = row;
            this.transposedMatrixB = transposedMatrixB;
        }

        @Override
        public int[] call() {
            int size = transposedMatrixB.length;
            int[] resultRow = new int[size];
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    resultRow[i] += row[j] * transposedMatrixB[i][j];
                }
            }
            return resultRow;
        }
    }
}