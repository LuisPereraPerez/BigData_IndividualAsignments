package org.example;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.cluster.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Future;
import java.io.Serializable;
import java.util.concurrent.Callable;

public class DistributedMatrixMultiplication {

    public static void main(String[] args) throws Exception {
        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();

        waitForClusterSize(hazelcastInstance, 3);


        int[][] matrixA = createRandomMatrix(10);
        int[][] matrixB = createRandomMatrix(10);


        System.out.println("Matrix A:");
        displayMatrix(matrixA);

        System.out.println("Matrix B:");
        displayMatrix(matrixB);

        int[][] result = multiplyMatricesDistributed(hazelcastInstance, matrixA, matrixB);

        System.out.println("Resultant Matrix:");
        displayMatrix(result);

        hazelcastInstance.shutdown();
    }


    private static int[][] multiplyMatricesDistributed(HazelcastInstance hazelcastInstance, int[][] matrixA, int[][] matrixB) throws Exception {
        Set<Member> members = hazelcastInstance.getCluster().getMembers();
        List<Member> memberList = new ArrayList<>(members);
        memberList.remove(hazelcastInstance.getCluster().getLocalMember());

        IExecutorService executorService = hazelcastInstance.getExecutorService("matrixExecutor");

        List<Future<int[]>> futures = new ArrayList<>();

        for (int i = 0; i < matrixA.length; i++) {
            Member targetMember = memberList.get(i % memberList.size());
            System.out.printf("Assigning row %d to node: %s%n", i, targetMember);
            Future<int[]> future = executorService.submitToMember(
                    new MatrixMultiplicationTask(matrixA[i], matrixB), targetMember);
            futures.add(future);
        }

        int[][] resultMatrix = new int[matrixA.length][matrixB[0].length];
        for (int i = 0; i < futures.size(); i++) {
            resultMatrix[i] = futures.get(i).get();
        }

        return resultMatrix;
    }

    private static void displayMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int value : row) {
                System.out.printf("%4d", value);
            }
            System.out.println();
        }
    }

    private static int[][] createRandomMatrix(int size) {
        Random random = new Random();
        int[][] matrix = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = random.nextInt(10);
            }
        }
        return matrix;
    }

    private static void waitForClusterSize(HazelcastInstance hazelcastInstance, int clusterSize) throws InterruptedException {
        while (hazelcastInstance.getCluster().getMembers().size() < clusterSize) {
            Thread.sleep(1000);
        }
    }

    public static class MatrixMultiplicationTask implements Callable<int[]>, Serializable {
        private final int[] rowA;
        private final int[][] matrixB;

        public MatrixMultiplicationTask(int[] row, int[][] matrix) {
            this.rowA = row;
            this.matrixB = matrix;
        }

        @Override
        public int[] call() {
            int size = matrixB[0].length;
            int[] resultRow = new int[size];
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < rowA.length; j++) {
                    resultRow[i] += rowA[j] * matrixB[j][i];
                }
            }
            System.out.printf("Node %s processed row with result: %s%n",
                    Thread.currentThread().getName(), java.util.Arrays.toString(resultRow));
            return resultRow;
        }
    }
}