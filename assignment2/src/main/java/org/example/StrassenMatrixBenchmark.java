package org.example;

import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class StrassenMatrixBenchmark {

    private int n = 1000;  // Size of the matrices (n x n)
    private boolean sparse = true;  // Whether the matrix is sparse or dense
    private double sparsityLevel = 0.2;  // 20% non-zero elements for sparse matrix
    private double[][] matrixA;
    private double[][] matrixB;

    @Setup(Level.Trial)
    public void setUp() {
        // Generate the matrices based on sparsity settings
        matrixA = generateRandomMatrix(n, sparse, sparsityLevel);
        matrixB = generateRandomMatrix(n, sparse, sparsityLevel);
    }

    @Benchmark
    public double[][] testStrassenMatrixMultiply() {
        // Perform the matrix multiplication with the generated matrices
        return StrassenMatrixMultiplication.multiply(matrixA, matrixB);
    }

    // Method to generate a random matrix based on sparsity settings
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
