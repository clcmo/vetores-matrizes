package com.clcmo.gitteste.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.clcmo.gitteste.domain.fmt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VectorScreen(vm: VectorViewModel = viewModel()) {
    val s by vm.state.collectAsStateWithLifecycle()
    Column(
        Modifier.verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Vetores", style = MaterialTheme.typography.headlineSmall)
        Text(s.operation.hint, style = MaterialTheme.typography.bodyMedium)

        Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            VectorOperation.entries.forEach { op ->
                FilterChip(selected = op == s.operation, onClick = { vm.select(op) }, label = { Text(op.label) })
            }
        }

        VectorCanvas(a = s.a, b = if (s.operation.needsB) s.b else null, result = s.resultVec)

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("a = ${s.a}", color = MaterialTheme.colorScheme.primary)
            if (s.operation.needsB) Text("b = ${s.b}", color = MaterialTheme.colorScheme.tertiary)
            if (s.resultVec != null) Text("resultado = ${s.resultVec}", color = MaterialTheme.colorScheme.error)
        }
        if (s.resultText.isNotEmpty()) Text(s.resultText, style = MaterialTheme.typography.titleMedium)
        s.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

        ComponentSlider("a₁ (x)", s.a[0]) { vm.setA(0, it) }
        ComponentSlider("a₂ (y)", s.a[1]) { vm.setA(1, it) }
        if (s.operation.needsB) {
            ComponentSlider("b₁ (x)", s.b[0]) { vm.setB(0, it) }
            ComponentSlider("b₂ (y)", s.b[1]) { vm.setB(1, it) }
        }
        if (s.operation.needsK) ComponentSlider("k", s.k, -3f..3f, 5) { vm.setK(it) }

        if (s.steps.isNotEmpty()) {
            Text("Passo a passo", style = MaterialTheme.typography.titleMedium)
            StepsList(s.steps)
        }
    }
}

@Composable
private fun ComponentSlider(
    label: String,
    value: Double,
    range: ClosedFloatingPointRange<Float> = -5f..5f,
    steps: Int = 9,
    onChange: (Double) -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("$label = ${value.fmt()}", modifier = Modifier.width(96.dp))
        Slider(
            value = value.toFloat(),
            onValueChange = { onChange(it.toDouble()) },
            valueRange = range,
            steps = steps,
            modifier = Modifier.weight(1f),
        )
    }
}
