package org.example;

public class BlockedMatrixMultiplication {
    public static double[][] multiply(double[][] A, double[][] B, int blockSize) {
        int n = A.length;
        double[][] C = new double[n][n];

        for (int ii = 0; ii < n; ii += blockSize) {
            for (int jj = 0; jj < n; jj += blockSize) {
                for (int kk = 0; kk < n; kk += blockSize) {
                    for (int i = ii; i < Math.min(ii + blockSize, n); i++) {
                        for (int j = jj; j < Math.min(jj + blockSize, n); j++) {
                            double sum = 0;
                            for (int k = kk; k < Math.min(kk + blockSize, n); k++) {
                                if (A[i][k] != 0 && B[k][j] != 0) {
                                    sum += A[i][k] * B[k][j];
                                }
                            }
                            if (sum != 0) {
                                C[i][j] += sum;
                            }
                        }
                    }
                }
            }
        }

        return C;
    }
}
