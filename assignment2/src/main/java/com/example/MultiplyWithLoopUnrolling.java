package com.example;

public class MultiplyWithLoopUnrolling {

    // Matrix multiplication with loop unrolling and sparsity consideration
    public static double[][] multiply(double[][] A, double[][] B, boolean sparse) {
        int n = A.length;
        double[][] result = new double[n][n];

        // Matrix multiplication with sparsity and loop unrolling
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double sum = 0;
                int k = 0;

                // Loop unrolling, processing 4 elements at a time
                for (; k <= n - 4; k += 4) {
                    if (sparse) {
                        // For sparse matrices, only multiply if neither element is zero
                        if (A[i][k] != 0 && B[k][j] != 0) sum += A[i][k] * B[k][j];
                        if (A[i][k + 1] != 0 && B[k + 1][j] != 0) sum += A[i][k + 1] * B[k + 1][j];
                        if (A[i][k + 2] != 0 && B[k + 2][j] != 0) sum += A[i][k + 2] * B[k + 2][j];
                        if (A[i][k + 3] != 0 && B[k + 3][j] != 0) sum += A[i][k + 3] * B[k + 3][j];
                    } else {
                        // For dense matrices, multiply directly
                        sum += A[i][k] * B[k][j];
                        sum += A[i][k + 1] * B[k + 1][j];
                        sum += A[i][k + 2] * B[k + 2][j];
                        sum += A[i][k + 3] * B[k + 3][j];
                    }
                }

                // Process remaining elements if n is not a multiple of 4
                for (; k < n; k++) {
                    if (sparse) {
                        if (A[i][k] != 0 && B[k][j] != 0) {
                            sum += A[i][k] * B[k][j];
                        }
                    } else {
                        sum += A[i][k] * B[k][j];
                    }
                }

                result[i][j] = sum;
            }
        }

        return result;
    }
}
