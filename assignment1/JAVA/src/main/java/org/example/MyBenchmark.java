package org.example;

import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class MyBenchmark {

    @Benchmark
    public double[][] testMatrixMultiply() {
        int n = 1000;
        return MatrixMultiplier.matrixMultiply(n);
    }
}
