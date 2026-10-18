package com.clcmo.vetoresmatrizes.ui

import androidx.lifecycle.ViewModel
import com.clcmo.vetoresmatrizes.domain.Explained
import com.clcmo.vetoresmatrizes.domain.Step
import com.clcmo.vetoresmatrizes.domain.Vec
import com.clcmo.vetoresmatrizes.domain.VectorOps
import com.clcmo.vetoresmatrizes.domain.fmt
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class VectorOperation(val label: String, val hint: String, val needsB: Boolean = false, val needsK: Boolean = false) {
    SOMA("a + b", "Regra do paralelogramo: coloque b na ponta de a. O resultado liga a origem à nova ponta.", needsB = true),
    SUBTRACAO("a − b", "a − b é a seta que vai da ponta de b até a ponta de a.", needsB = true),
    ESCALAR("k · a", "Multiplicar por k estica (|k|>1), encolhe (|k|<1) ou inverte (k<0) o vetor.", needsK = true),
    PRODUTO_ESCALAR("a · b", "Soma dos produtos componente a componente. Dá 0 quando os vetores são perpendiculares.", needsB = true),
    MODULO("|a|", "O comprimento da seta, pelo teorema de Pitágoras."),
    ANGULO("ângulo", "O ângulo entre a e b sai do produto escalar e dos módulos.", needsB = true),
}

data class VectorUiState(
    val a: Vec = Vec(3.0, 2.0),
    val b: Vec = Vec(1.0, 3.0),
    val k: Double = 2.0,
    val operation: VectorOperation = VectorOperation.SOMA,
    val resultVec: Vec? = null,
    val resultText: String = "",
    val steps: List<Step> = emptyList(),
    val error: String? = null,
)

private class Outcome(val vec: Vec?, val text: String, val steps: List<Step>)

private fun Explained<Vec>.asVec() = Outcome(value, value.toString(), steps)
private fun Explained<Double>.asScalar(label: String, unit: String = "") =
    Outcome(null, "$label = ${value.fmt()}$unit", steps)

/** Recalcula a cada mudança, para o aluno ver a seta se mexendo enquanto arrasta o controle. */
class VectorViewModel : ViewModel() {
    private val _state = MutableStateFlow(compute(VectorUiState()))
    val state: StateFlow<VectorUiState> = _state.asStateFlow()

    fun select(op: VectorOperation) = change { it.copy(operation = op) }
    fun setA(index: Int, value: Double) = change { it.copy(a = it.a.copyWith(index, value)) }
    fun setB(index: Int, value: Double) = change { it.copy(b = it.b.copyWith(index, value)) }
    fun setK(value: Double) = change { it.copy(k = value) }

    private fun change(f: (VectorUiState) -> VectorUiState) = _state.update { compute(f(it)) }

    private fun compute(s: VectorUiState): VectorUiState {
        val outcome = runCatching {
            when (s.operation) {
                VectorOperation.SOMA -> VectorOps.add(s.a, s.b).asVec()
                VectorOperation.SUBTRACAO -> VectorOps.subtract(s.a, s.b).asVec()
                VectorOperation.ESCALAR -> VectorOps.scale(s.k, s.a).asVec()
                VectorOperation.PRODUTO_ESCALAR -> VectorOps.dot(s.a, s.b).asScalar("a · b")
                VectorOperation.MODULO -> VectorOps.norm(s.a).asScalar("|a|")
                VectorOperation.ANGULO -> VectorOps.angleDegrees(s.a, s.b).asScalar("θ", "°")
            }
        }
        return outcome.fold(
            onSuccess = { o -> s.copy(resultVec = o.vec, resultText = o.text, steps = o.steps, error = null) },
            onFailure = { e -> s.copy(resultVec = null, resultText = "", steps = emptyList(), error = e.message) },
        )
    }
}
