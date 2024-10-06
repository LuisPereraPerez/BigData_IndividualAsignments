package org.example;

import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class MyBenchmark {

    @Benchmark
    public double[][] testMatrixMultiply() {
        int n = 10; // Puedes parametrizar n si lo deseas
        return MatrixMultiplier.matrixMultiply(n);
    }
}
