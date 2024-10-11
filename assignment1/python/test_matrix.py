from matrix import matrix_multiply

def test_matrix_multiply(benchmark):
    n = 100
    benchmark(matrix_multiply, n)
