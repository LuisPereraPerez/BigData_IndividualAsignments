package org.example;

import java.util.ArrayList;
import java.util.List;

public class SparseMatrix {
    private final int rows;
    private final int cols;
    private final List<List<Double>> values;

    // Constructor
    public SparseMatrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.values = new ArrayList<>(rows);
        for (int i = 0; i < rows; i++) {
            values.add(new ArrayList<>());
        }
    }

    // Method to add a value in the matrix
    public void addValue(int row, int col, double value) {
        // Check that the position is valid
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }

        // Store the value at the specific position
        if (col >= values.get(row).size()) {
            for (int i = values.get(row).size(); i <= col; i++) {
                values.get(row).add(0.0); // Add zeros until the required index
            }
        }
        values.get(row).set(col, value); // Assign the value
    }

    // Method to get a value in the matrix
    public double getValue(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        return col < values.get(row).size() ? values.get(row).get(col) : 0.0;
    }

    // Method to multiply matrices (only if needed)
    public SparseMatrix multiply(SparseMatrix other) {
        if (this.cols != other.rows) {
            throw new IllegalArgumentException("Incompatible dimensions for multiplication");
        }
        SparseMatrix result = new SparseMatrix(this.rows, other.cols);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < other.cols; j++) {
                double sum = 0;
                for (int k = 0; k < this.cols; k++) {
                    sum += this.getValue(i, k) * other.getValue(k, j);
                }
                if (sum != 0) {
                    result.addValue(i, j, sum);
                }
            }
        }
        return result;
    }
}
