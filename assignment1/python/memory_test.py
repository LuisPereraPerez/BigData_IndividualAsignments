from memory_profiler import memory_usage
from matrix import matrix_multiply

def memory_test_matrix_multiply(n):
    return matrix_multiply(n)

def run_memory_test():
    n = 1000
    mem_usage = memory_usage((memory_test_matrix_multiply, (n,)), interval=0.1, timeout=1)
    
    if mem_usage:
        print(f'Máximo uso de memoria para n={n}: {max(mem_usage)} MB')
    else:
        print("No se pudo obtener información sobre el uso de memoria.")

if __name__ == "__main__":
    run_memory_test()
