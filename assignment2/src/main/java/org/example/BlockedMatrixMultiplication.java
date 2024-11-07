package org.example;

public class BlockedMatrixMultiplication {

    // Blocked matrix multiplication method
    public static double[][] multiply(double[][] A, double[][] B, boolean sparse) {
        int n = A.length;

        // Calculate block size as 10% of n
        int blockSize = Math.max(1, n / 10);

        // Result matrix C
        double[][] C = new double[n][n];

        // Blocked matrix multiplication
        for (int ii = 0; ii < n; ii += blockSize) {
            for (int jj = 0; jj < n; jj += blockSize) {
                for (int kk = 0; kk < n; kk += blockSize) {
                    // Iterate over the submatrices (blocks)
                    for (int i = ii; i < Math.min(ii + blockSize, n); i++) {
                        for (int j = jj; j < Math.min(jj + blockSize, n); j++) {
                            double sum = 0;

                            // If the matrix is sparse, do not multiply zero elements
                            if (sparse) {
                                for (int k = kk; k < Math.min(kk + blockSize, n); k++) {
                                    if (A[i][k] != 0 && B[k][j] != 0) {
                                        sum += A[i][k] * B[k][j];
                                    }
                                }
                            } else {
                                // For dense matrices, multiply all elements
                                for (int k = kk; k < Math.min(kk + blockSize, n); k++) {
                                    sum += A[i][k] * B[k][j];
                                }
                            }

                            // If the sum is not zero, add it to the result matrix
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
