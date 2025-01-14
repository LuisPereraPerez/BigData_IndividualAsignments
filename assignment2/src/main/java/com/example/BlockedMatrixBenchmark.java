package com.example;

import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;
import java.util.Random;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class BlockedMatrixBenchmark {

    private double[][] matrixA;
    private double[][] matrixB;

    private int n = 500;  // Size of the matrices (n x n)
    private boolean sparse = true;  // Whether to generate sparse matrices or not
    private double sparsityLevel = 0.5;  // 20% of elements will be non-zero for sparse matrix

    @Setup(Level.Trial)
    public void setUp() {
        // Generate the matrices based on the sparsity setting
        matrixA = generateRandomMatrix(n, sparse, sparsityLevel);
        matrixB = generateRandomMatrix(n, sparse, sparsityLevel);
    }

    @Benchmark
    public double[][] testBlockedMatrixMultiply() {
        return BlockedMatrixMultiplication.multiply(matrixA, matrixB, sparse);
    }

    // Method to generate a random matrix
    private double[][] generateRandomMatrix(int n, boolean sparse, double sparsityLevel) {
        Random rand = new Random();
        double[][] matrix = new double[n][n];

        // If the matrix is sparse, populate only sparsityLevel * 100% of elements with non-zero values
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (sparse) {
                    // For sparse matrix, only some elements will be non-zero
                    if (rand.nextDouble() < sparsityLevel) {
                        matrix[i][j] = rand.nextDouble() * 10;  // Random value between 0 and 10
                    } else {
                        matrix[i][j] = 0;  // Make the element zero
                    }
                } else {
                    // For dense matrix, all elements are non-zero
                    matrix[i][j] = rand.nextDouble() * 10;  // Random value between 0 and 10
                }
            }
        }

        return matrix;
    }
}