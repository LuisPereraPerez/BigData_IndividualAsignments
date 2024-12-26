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
        // Crear o unirse a un clúster Hazelcast
        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();

        waitForCluster(hazelcastInstance, 3); // Esperar hasta que el clúster tenga 3 nodos

        // Solo el primer nodo genera y distribuye las matrices
        if (hazelcastInstance.getCluster().getLocalMember().getUuid().equals(hazelcastInstance.getCluster().getMembers().iterator().next().getUuid())) {
            // Generamos las matrices solo en el nodo principal
            int[][] matrixA = generateMatrix(3);
            int[][] matrixB = generateMatrix(3);

            System.out.println("Matrix A:");
            printMatrix(matrixA);

            System.out.println("Matrix B:");
            printMatrix(matrixB);

            // Almacena las matrices en el mapa distribuido
            storeMatricesInHazelcast(hazelcastInstance, matrixA, matrixB);
        }

        // Realizar la multiplicación distribuida
        int[][] result = multiplyMatricesDistributed(hazelcastInstance);

        System.out.println("Resultant Matrix:");
        printMatrix(result);

        // Asegúrate de que todas las tareas han terminado antes de apagar Hazelcast
        hazelcastInstance.getLifecycleService().terminate(); // Terminar las operaciones de Hazelcast adecuadamente
    }

    private static void storeMatricesInHazelcast(HazelcastInstance hazelcastInstance, int[][] matrixA, int[][] matrixB) {
        // Obtiene el mapa distribuido para las matrices
        IMap<String, int[][]> map = hazelcastInstance.getMap("matrices");

        // Almacena las matrices A y B en el mapa
        map.put("matrixA", matrixA);
        map.put("matrixB", matrixB);
    }

    private static int[][] multiplyMatricesDistributed(HazelcastInstance hazelcastInstance) throws Exception {
        IMap<String, int[][]> map = hazelcastInstance.getMap("matrices");

        // Obtener las matrices A y B desde el mapa distribuido
        int[][] matrixA = map.get("matrixA");
        int[][] matrixB = map.get("matrixB");

        int size = matrixA.length;
        int[][] result = new int[size][size];

        // Transponer la matriz B para facilitar el acceso a las columnas
        int[][] transposedB = transposeMatrix(matrixB);

        // Usar Hazelcast ExecutorService para el cálculo distribuido
        IExecutorService executorService = hazelcastInstance.getExecutorService("matrixMultiplication");

        List<Future<int[]>> futures = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            // Crear una tarea para cada fila de la matriz A
            MatrixMultiplicationTask task = new MatrixMultiplicationTask(matrixA[i], transposedB);
            futures.add(executorService.submit(task));
        }

        // Esperar que todas las tareas terminen antes de continuar
        for (int i = 0; i < size; i++) {
            result[i] = futures.get(i).get();  // Esperar el resultado de cada tarea
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