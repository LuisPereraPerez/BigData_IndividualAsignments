from matrix import matrix_multiplication

def test_matrix_multiply(benchmark):
    n = 100
    benchmark(matrix_multiplication, n)