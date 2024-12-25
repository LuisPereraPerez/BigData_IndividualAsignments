import numpy as np

# Tamaño de las matrices
n = 2

# Generamos matrices A y B
A = np.random.randint(0, 9, size=(n, n))
B = np.random.randint(0, 9, size=(n, n))

# Calculamos el producto de las matrices A y B
C = np.dot(A, B)

# Guardamos A, B y C en un solo archivo
with open('matrices.txt', 'w') as f:
    # Guardar la matriz A
    for i in range(n):
        for j in range(n):
            f.write(f"A,{i},{j},{A[i, j]}\n")

    # Guardar la matriz B
    for i in range(n):
        for j in range(n):
            f.write(f"B,{i},{j},{B[i, j]}\n")

np.savetxt('matriz_resultado.txt', C, fmt='%d')
