package com.clcmo.vetoresmatrizes.ui

import androidx.lifecycle.ViewModel
import com.clcmo.vetoresmatrizes.domain.Matrix
import com.clcmo.vetoresmatrizes.domain.MatrixOps
import com.clcmo.vetoresmatrizes.domain.Step
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class MatrixOperation(val label: String, val hint: String, val needsB: Boolean = false, val needsScalar: Boolean = false) {
    SOMA("A + B", "Some elemento a elemento. As matrizes precisam ter o mesmo tamanho.", needsB = true),
    SUBTRACAO("A − B", "Subtraia elemento a elemento. A ordem importa: A − B ≠ B − A.", needsB = true),
    ESCALAR("k · A", "Multiplique cada elemento da matriz pelo número k.", needsScalar = true),
    TRANSPOSTA("Aᵀ", "Troque linhas por colunas: o elemento (i, j) vira o elemento (j, i)."),
    MULTIPLICACAO("A × B", "Cada elemento é (linha de A) · (coluna de B). Colunas de A = linhas de B.", needsB = true),
}

/** Células digitadas pelo aluno, guardadas como texto até o momento de calcular. */
data class GridState(val rows: Int, val cols: Int, val cells: List<String>) {
    fun cell(i: Int, j: Int) = cells[i * cols + j]

    fun withCell(i: Int, j: Int, text: String) =
        copy(cells = cells.toMutableList().also { it[i * cols + j] = text })

    fun resized(newRows: Int, newCols: Int) = GridState(
        newRows, newCols,
        List(newRows * newCols) { idx ->
            val i = idx / newCols
            val j = idx % newCols
            if (i < rows && j < cols) cell(i, j) else "0"
        }
    )

    fun toMatrix(): Matrix = Matrix(rows, cols, cells.map { parseNumber(it) })

    companion object {
        fun of(rows: Int, cols: Int, vararg values: Int) = GridState(rows, cols, values.map { it.toString() })
    }
}

fun parseNumber(text: String): Double =
    text.trim().replace(',', '.').toDoubleOrNull()
        ?: throw IllegalArgumentException("Preencha todas as células com números.")

data class MatrixUiState(
    val operation: MatrixOperation = MatrixOperation.SOMA,
    val a: GridState = GridState.of(2, 2, 1, 2, 3, 4),
    val b: GridState = GridState.of(2, 2, 5, 6, 7, 8),
    val scalar: String = "2",
    val result: Matrix? = null,
    val steps: List<Step> = emptyList(),
    val error: String? = null,
)

class MatrixViewModel : ViewModel() {
    private val _state = MutableStateFlow(MatrixUiState())
    val state: StateFlow<MatrixUiState> = _state.asStateFlow()

    fun selectOperation(op: MatrixOperation) =
        _state.update { it.copy(operation = op, result = null, steps = emptyList(), error = null) }

    fun editA(i: Int, j: Int, text: String) = _state.update { it.copy(a = it.a.withCell(i, j, text)) }
    fun editB(i: Int, j: Int, text: String) = _state.update { it.copy(b = it.b.withCell(i, j, text)) }
    fun resizeA(rows: Int, cols: Int) = _state.update { it.copy(a = it.a.resized(rows, cols)) }
    fun resizeB(rows: Int, cols: Int) = _state.update { it.copy(b = it.b.resized(rows, cols)) }
    fun setScalar(text: String) = _state.update { it.copy(scalar = text) }

    fun calculate() {
        val s = _state.value
        val outcome = runCatching {
            val a = s.a.toMatrix()
            when (s.operation) {
                MatrixOperation.SOMA -> MatrixOps.add(a, s.b.toMatrix())
                MatrixOperation.SUBTRACAO -> MatrixOps.subtract(a, s.b.toMatrix())
                MatrixOperation.ESCALAR -> MatrixOps.scale(parseNumber(s.scalar), a)
                MatrixOperation.TRANSPOSTA -> MatrixOps.transpose(a)
                MatrixOperation.MULTIPLICACAO -> MatrixOps.multiply(a, s.b.toMatrix())
            }
        }
        _state.update { cur ->
            outcome.fold(
                onSuccess = { r -> cur.copy(result = r.value, steps = r.steps, error = null) },
                onFailure = { e -> cur.copy(result = null, steps = emptyList(), error = e.message) },
            )
        }
    }
}
