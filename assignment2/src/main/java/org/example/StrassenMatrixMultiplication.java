package org.example;

public class StrassenMatrixMultiplication {
    public static double[][] multiply(double[][] A, double[][] B) {
        int n = A.length;
        if (n == 1) {
            double[][] C = {{A[0][0] * B[0][0]}};
            return C;
        }

        int newSize = n / 2;
        double[][] A11 = new double[newSize][newSize];
        double[][] A12 = new double[newSize][newSize];
        double[][] A21 = new double[newSize][newSize];
        double[][] A22 = new double[newSize][newSize];
        double[][] B11 = new double[newSize][newSize];
        double[][] B12 = new double[newSize][newSize];
        double[][] B21 = new double[newSize][newSize];
        double[][] B22 = new double[newSize][newSize];

        split(A, A11, 0, 0);
        split(A, A12, 0, newSize);
        split(A, A21, newSize, 0);
        split(A, A22, newSize, newSize);
        split(B, B11, 0, 0);
        split(B, B12, 0, newSize);
        split(B, B21, newSize, 0);
        split(B, B22, newSize, newSize);

        double[][] M1 = multiply(add(A11, A22), add(B11, B22));
        double[][] M2 = multiply(add(A21, A22), B11);
        double[][] M3 = multiply(A11, subtract(B12, B22));
        double[][] M4 = multiply(A22, subtract(B21, B11));
        double[][] M5 = multiply(add(A11, A12), B22);
        double[][] M6 = multiply(subtract(A21, A11), add(B11, B12));
        double[][] M7 = multiply(subtract(A12, A22), add(B21, B22));

        double[][] C11 = add(subtract(add(M1, M4), M5), M7);
        double[][] C12 = add(M3, M5);
        double[][] C21 = add(M2, M4);
        double[][] C22 = add(subtract(add(M1, M3), M2), M6);

        double[][] C = new double[n][n];
        join(C11, C, 0, 0);
        join(C12, C, 0, newSize);
        join(C21, C, newSize, 0);
        join(C22, C, newSize, newSize);

        return C;
    }

    private static void split(double[][] P, double[][] C, int iB, int jB) {
        for (int i = 0, i2 = iB; i < C.length; i++, i2++)
            for (int j = 0, j2 = jB; j < C.length; j++, j2++)
                C[i][j] = P[i2][j2];
    }

    private static void join(double[][] C, double[][] P, int iB, int jB) {
        for (int i = 0, i2 = iB; i < C.length; i++, i2++)
            for (int j = 0, j2 = jB; j < C.length; j++, j2++)
                P[i2][j2] = C[i][j];
    }

    private static double[][] add(double[][] A, double[][] B) {
        int n = A.length;
        double[][] C = new double[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                C[i][j] = A[i][j] + B[i][j];
        return C;
    }

    private static double[][] subtract(double[][] A, double[][] B) {
        int n = A.length;
        double[][] C = new double[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                C[i][j] = A[i][j] - B[i][j];
        return C;
    }
}
