package com.example;

public class MatrixMultiplier {

    // Matrix multiplication considering sparse matrices
    public static double[][] matrixMultiply(double[][] A, double[][] B, boolean sparse) {
        int n = A.length;
        double[][] result = new double[n][n];

        // Matrix multiplication with sparsity consideration
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double sum = 0;
                for (int k = 0; k < n; k++) {
                    // For sparse matrices, skip multiplication if A[i][k] or B[k][j] is zero
                    if (sparse) {
                        if (A[i][k] != 0 && B[k][j] != 0) {
                            sum += A[i][k] * B[k][j];
                        }
                    } else {
                        // For dense matrices, just multiply normally
                        sum += A[i][k] * B[k][j];
                    }
                }
                result[i][j] = sum;
            }
        }

        return result;
    }
}