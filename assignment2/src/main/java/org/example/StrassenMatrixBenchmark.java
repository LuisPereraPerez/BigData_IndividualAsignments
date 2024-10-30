package org.example;

import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class StrassenMatrixBenchmark {

    private double[][] matrixA;
    private double[][] matrixB;

    @Setup(Level.Trial)
    public void setUp() {
        int n = 1000;  // Size of the matrices
        matrixA = generateRandomMatrix(n, n);
        matrixB = generateRandomMatrix(n, n);
    }

    @Benchmark
    public double[][] testStrassenMatrixMultiply() {
        return StrassenMatrixMultiplication.multiply(matrixA, matrixB);
    }

    private double[][] generateRandomMatrix(int rows, int cols) {
        double[][] matrix = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = Math.random();
            }
        }
        return matrix;
    }
}
