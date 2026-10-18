package com.clcmo.vetoresmatrizes.ui

import androidx.lifecycle.ViewModel
import com.clcmo.vetoresmatrizes.domain.Explained
import com.clcmo.vetoresmatrizes.domain.Matrix
import com.clcmo.vetoresmatrizes.domain.MatrixOps
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

const val EXERCISE_SIZE = 2

data class ExerciseState(
    val subtract: Boolean,
    val a: Matrix,
    val b: Matrix,
    val answer: List<String>,
    val wrongCells: Set<Int> = emptySet(),
    val correct: Boolean = false,
    val showSteps: Boolean = false,
    val message: String? = null,
) {
    val expected: Explained<Matrix>
        get() = if (subtract) MatrixOps.subtract(a, b) else MatrixOps.add(a, b)
}

class ExerciseViewModel : ViewModel() {
    private val _state = MutableStateFlow(newExercise())
    val state: StateFlow<ExerciseState> = _state.asStateFlow()

    fun edit(index: Int, text: String) = _state.update {
        it.copy(
            answer = it.answer.toMutableList().also { l -> l[index] = text },
            wrongCells = it.wrongCells - index,
            message = null,
        )
    }

    fun check() {
        val s = _state.value
        val parsed = s.answer.map { it.trim().replace(',', '.').toDoubleOrNull() }
        if (parsed.any { it == null }) {
            _state.update { it.copy(message = "Preencha todas as células com números.", correct = false) }
            return
        }
        val expected = s.expected.value
        val wrong = parsed.indices
            .filter { idx -> parsed[idx] != expected[idx / EXERCISE_SIZE, idx % EXERCISE_SIZE] }
            .toSet()
        _state.update {
            it.copy(
                wrongCells = wrong,
                correct = wrong.isEmpty(),
                message = if (wrong.isEmpty()) "Correto! 🎉"
                else "Quase lá: ${wrong.size} célula(s) para revisar. Confira o sinal e a posição de cada elemento.",
            )
        }
    }

    fun next() { _state.value = newExercise() }

    fun toggleSteps() = _state.update { it.copy(showSteps = !it.showSteps) }

    private fun newExercise(): ExerciseState {
        fun random() = Matrix.fromFunction(EXERCISE_SIZE, EXERCISE_SIZE) { _, _ -> Random.nextInt(1, 10).toDouble() }
        val a = random()
        var b = random()
        // O exercício original do repositório usava duas matrizes iguais (A − A = 0), o que não ensina nada.
        while (a == b) b = random()
        return ExerciseState(
            subtract = Random.nextBoolean(),
            a = a,
            b = b,
            answer = List(EXERCISE_SIZE * EXERCISE_SIZE) { "" },
        )
    }
}
