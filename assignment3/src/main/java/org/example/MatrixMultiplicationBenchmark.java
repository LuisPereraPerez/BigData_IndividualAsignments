package org.example;

import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;
import java.util.Random;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class MatrixMultiplicationBenchmark {

    private double[][] matrixA;
    private double[][] matrixB;
    private int numThreads = 4;  // Number of threads to use in the parallel method
    private int n = 1000;         // Size of the matrices (n x n)

    @Setup(Level.Trial)
    public void setUp() {
        // Generate random matrices
        matrixA = generateRandomMatrix(n);
        matrixB = generateRandomMatrix(n);
    }

    // Benchmark for parallel matrix multiplication
    @Benchmark
    public double[][] testParallelMatrixMultiply() {
        return MatrixMultiplicationParallel.multiplyMatricesParallel(matrixA, matrixB, numThreads);
    }

    // Benchmark for sequential matrix multiplication
    @Benchmark
    public double[][] testSequentialMatrixMultiply() {
        return MatrixMultiplicationParallel.multiplyMatricesSequential(matrixA, matrixB);
    }

    // Method to generate a random matrix of size n x n
    private double[][] generateRandomMatrix(int n) {
        Random rand = new Random();
        double[][] matrix = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = rand.nextDouble() * 10;  // Random numbers between 0 and 10
            }
        }
        return matrix;
    }
}
