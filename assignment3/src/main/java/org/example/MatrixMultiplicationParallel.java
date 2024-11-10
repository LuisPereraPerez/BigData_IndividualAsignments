package org.example;

public class MatrixMultiplicationParallel {

    // Parallel matrix multiplication method using multiple threads
    public static double[][] multiplyMatricesParallel(double[][] matrixA, double[][] matrixB, int numThreads) {
        int rows = matrixA.length;
        int columns = matrixB[0].length;
        double[][] resultMatrix = new double[rows][columns];

        // Create an array of threads
        Thread[] threads = new Thread[numThreads];
        int rowsPerThread = rows / numThreads;

        // Assign work to each thread
        for (int i = 0; i < numThreads; i++) {
            int startRow = i * rowsPerThread;
            int endRow = (i == numThreads - 1) ? rows : startRow + rowsPerThread;

            // Initialize each thread with the work it should perform
            threads[i] = new Thread(new MatrixMultiplier(matrixA, matrixB, resultMatrix, startRow, endRow));
            threads[i].start(); // Start each thread
        }

        // Wait for all threads to finish
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return resultMatrix;
    }

    // Sequential matrix multiplication method
    public static double[][] multiplyMatricesSequential(double[][] matrixA, double[][] matrixB) {
        int rows = matrixA.length;
        int columns = matrixB[0].length;
        double[][] resultMatrix = new double[rows][columns];
        int columnsA = matrixA[0].length;

        // Perform multiplication row by row
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                double sum = 0;
                for (int k = 0; k < columnsA; k++) {
                    sum += matrixA[i][k] * matrixB[k][j];
                }
                resultMatrix[i][j] = sum;
            }
        }
        return resultMatrix;
    }

    // Inner class for running the multiplication task in parallel
    static class MatrixMultiplier implements Runnable {
        private final double[][] matrixA;
        private final double[][] matrixB;
        private final double[][] resultMatrix;
        private final int startRow;
        private final int endRow;

        // Constructor to initialize the matrices, result matrix and the range of rows
        public MatrixMultiplier(double[][] matrixA, double[][] matrixB, double[][] resultMatrix, int startRow, int endRow) {
            this.matrixA = matrixA;
            this.matrixB = matrixB;
            this.resultMatrix = resultMatrix;
            this.startRow = startRow;
            this.endRow = endRow;
        }

        // Run method for the thread that does matrix multiplication for a given row range
        @Override
        public void run() {
            int columnsB = matrixB[0].length;
            int columnsA = matrixA[0].length;
            for (int i = startRow; i < endRow; i++) {
                for (int j = 0; j < columnsB; j++) {
                    double sum = 0;
                    for (int k = 0; k < columnsA; k++) {
                        sum += matrixA[i][k] * matrixB[k][j];
                    }
                    resultMatrix[i][j] = sum;
                }
            }
        }
    }
}
