package com.example;

import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class MatrixMultiplierBenchmark {

    @Benchmark
    public double[][] testMatrixMultiply() {
        int n = 10;
        return MatrixMultiplier.matrixMultiply(n);
    }
}