from mrjob.job import MRJob
from mrjob.step import MRStep

class MatrixMultiplication(MRJob):

    def configure_args(self):
        super(MatrixMultiplication, self).configure_args()

    def mapper(self, _, line):
        # Dividir la entrada
        parts = line.strip().split(',')
        matrix_type = parts[0]  # 'A' o 'B'
        row_index = int(parts[1])  # Índice de la fila o columna
        col_index = int(parts[2])  # Índice de la columna
        value = int(parts[3])  # Valor de la matriz

        # Emitir las filas de A y columnas de B
        if matrix_type == 'A':
            # Emitir clave (row_index) para las filas de A
            yield (row_index, ('A', col_index, value))  # (row_index, ('A', col_index, value))
        elif matrix_type == 'B':
            # Emitir clave (col_index) para las columnas de B
            yield (col_index, ('B', row_index, value))  # (col_index, ('B', row_index, value))

    def reducer(self, key, values):
        A_rows = {}  # Diccionario de filas de A
        B_columns = {}  # Diccionario de columnas de B

        # Recolectamos las filas de A y las columnas de B
        for val in values:
            if val[0] == 'A':
                col_index, value = val[1], val[2]  # val[1] = col_index, val[2] = value
                if col_index not in A_rows:
                    A_rows[col_index] = []
                A_rows[col_index].append(value)  # Guardar valor de la fila de A
            elif val[0] == 'B':
                row_index, value = val[1], val[2]  # val[1] = row_index, val[2] = value
                if row_index not in B_columns:
                    B_columns[row_index] = []
                B_columns[row_index].append(value)  # Guardar valor de la columna de B

        # Realizamos el producto de matrices
        results = {}

        # Producto punto entre las filas de A y las columnas de B
        for i in A_rows:  # Recorremos las filas de A
            for j in B_columns:  # Recorremos las columnas de B
                if len(A_rows[i]) == len(B_columns[j]):  # Aseguramos que las dimensiones coincidan
                    s = 0  # Inicializamos la variable de acumulación

                    # Realizamos el producto punto entre la fila de A y la columna de B
                    for k in range(len(A_rows[i])):
                        s += A_rows[i][k] * B_columns[j][k]  # Acumulamos el resultado

                    # Sumamos el resultado de la posición (i, j) si ya existe
                    if (i, j) not in results:
                        results[(i, j)] = 0
                    results[(i, j)] += s  # Acumulamos los resultados para cada posición

        # Emitimos los resultados acumulados
        for key, value in results.items():
            yield (f'({key[0]},{key[1]})', value)

    def steps(self):
        return [
            MRStep(mapper=self.mapper, reducer=self.reducer)
        ]

if __name__ == '__main__':
    MatrixMultiplication.run()
