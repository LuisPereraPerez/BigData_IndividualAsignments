package com.example;

import java.util.Random;

public class MatrixMultiplier {

    public static double[][] matrixMultiply(int n) {
        double[][] a = new double[n][n];
        double[][] b = new double[n][n];
        double[][] c = new double[n][n];

        Random random = new Random();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = random.nextDouble();
                b[i][j] = random.nextDouble();
                c[i][j] = 0;
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    c[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return c;
    }
}