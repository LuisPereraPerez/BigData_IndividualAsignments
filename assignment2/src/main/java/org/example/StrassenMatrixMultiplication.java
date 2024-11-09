import java.util.HashMap;
import java.util.Map;

public class StrassenSparseMatrixMultiplication {

    public static Map<String, Double> multiply(Map<String, Double> A, Map<String, Double> B, int n) {
        if (n == 1) {
            String key = "0,0";  // La única posición de la matriz
            double product = A.getOrDefault(key, 0.0) * B.getOrDefault(key, 0.0);
            Map<String, Double> result = new HashMap<>();
            result.put(key, product);
            return result;
        }

        int newSize = n / 2;

        Map<String, Double> A11 = getSubmatrix(A, 0, 0, newSize);
        Map<String, Double> A12 = getSubmatrix(A, 0, newSize, newSize);
        Map<String, Double> A21 = getSubmatrix(A, newSize, 0, newSize);
        Map<String, Double> A22 = getSubmatrix(A, newSize, newSize, newSize);

        Map<String, Double> B11 = getSubmatrix(B, 0, 0, newSize);
        Map<String, Double> B12 = getSubmatrix(B, 0, newSize, newSize);
        Map<String, Double> B21 = getSubmatrix(B, newSize, 0, newSize);
        Map<String, Double> B22 = getSubmatrix(B, newSize, newSize, newSize);

        Map<String, Double> M1 = multiply(add(A11, A22, newSize), add(B11, B22, newSize), newSize);
        Map<String, Double> M2 = multiply(add(A21, A22, newSize), B11, newSize);
        Map<String, Double> M3 = multiply(A11, subtract(B12, B22, newSize), newSize);
        Map<String, Double> M4 = multiply(A22, subtract(B21, B11, newSize), newSize);
        Map<String, Double> M5 = multiply(add(A11, A12, newSize), B22, newSize);
        Map<String, Double> M6 = multiply(subtract(A21, A11, newSize), add(B11, B12, newSize), newSize);
        Map<String, Double> M7 = multiply(subtract(A12, A22, newSize), add(B21, B22, newSize), newSize);

        Map<String, Double> C = new HashMap<>();
        Map<String, Double> C11 = add(subtract(add(M1, M4, newSize), M5, newSize), M7, newSize);
        Map<String, Double> C12 = add(M3, M5, newSize);
        Map<String, Double> C21 = add(M2, M4, newSize);
        Map<String, Double> C22 = add(subtract(add(M1, M3, newSize), M2, newSize), M6, newSize);

        join(C11, C, 0, 0, newSize);
        join(C12, C, 0, newSize, newSize);
        join(C21, C, newSize, 0, newSize);
        join(C22, C, newSize, newSize, newSize);

        return C;
    }

    private static Map<String, Double> getSubmatrix(Map<String, Double> matrix, int startRow, int startCol, int size) {
        Map<String, Double> submatrix = new HashMap<>();
        for (int i = startRow; i < startRow + size; i++) {
            for (int j = startCol; j < startCol + size; j++) {
                String key = i + "," + j;
                if (matrix.containsKey(key)) {
                    submatrix.put(key, matrix.get(key));
                }
            }
        }
        return submatrix;
    }

    private static Map<String, Double> add(Map<String, Double> A, Map<String, Double> B, int size) {
        Map<String, Double> result = new HashMap<>();
        for (Map.Entry<String, Double> entry : A.entrySet()) {
            String key = entry.getKey();
            result.put(key, result.getOrDefault(key, 0.0) + entry.getValue());
        }
        for (Map.Entry<String, Double> entry : B.entrySet()) {
            String key = entry.getKey();
            result.put(key, result.getOrDefault(key, 0.0) + entry.getValue());
        }
        return result;
    }

    private static Map<String, Double> subtract(Map<String, Double> A, Map<String, Double> B, int size) {
        Map<String, Double> result = new HashMap<>();
        for (Map.Entry<String, Double> entry : A.entrySet()) {
            String key = entry.getKey();
            result.put(key, result.getOrDefault(key, 0.0) + entry.getValue());
        }
        for (Map.Entry<String, Double> entry : B.entrySet()) {
            String key = entry.getKey();
            result.put(key, result.getOrDefault(key, 0.0) - entry.getValue());
        }
        return result;
    }

    private static void join(Map<String, Double> submatrix, Map<String, Double> matrix, int startRow, int startCol, int size) {
        for (Map.Entry<String, Double> entry : submatrix.entrySet()) {
            String key = entry.getKey();
            String[] parts = key.split(",");
            int i = Integer.parseInt(parts[0]) + startRow;
            int j = Integer.parseInt(parts[1]) + startCol;
            matrix.put(i + "," + j, entry.getValue());
        }
    }
}
