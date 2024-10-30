package org.example;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class SparseMatrixBenchmark {

    private SparseMatrix sparseMatrix;

    @Setup(Level.Trial)
    public void setUp() {
        int size = 1000; // Size of the matrix
        sparseMatrix = new SparseMatrix(size, size);
        fillSparseMatrix();
    }

    // Method to fill the sparse matrix
    private void fillSparseMatrix() {
        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 1000; j++) {
                // Add sparse values
                if (Math.random() < 0.01) { // 1% probability to add a value
                    sparseMatrix.addValue(i, j, Math.random());
                }
            }
        }
    }

    @Benchmark
    public void testSparseMatrixMultiply() {
        SparseMatrix otherMatrix = new SparseMatrix(1000, 1000); // Create another matrix
        fillSparseMatrix(otherMatrix); // Fill the other matrix
        sparseMatrix.multiply(otherMatrix);
    }

    private void fillSparseMatrix(SparseMatrix matrix) {
        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 1000; j++) {
                // Add sparse values
                if (Math.random() < 0.01) { // 1% probability to add a value
                    matrix.addValue(i, j, Math.random());
                }
            }
        }
    }
}
