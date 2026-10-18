package com.clcmo.vetores-matrizes.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatrixScreen(vm: MatrixViewModel = viewModel()) {
    val s by vm.state.collectAsStateWithLifecycle()
    Column(
        Modifier.verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Matrizes", style = MaterialTheme.typography.headlineSmall)
        Text(s.operation.hint, style = MaterialTheme.typography.bodyMedium)

        Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            MatrixOperation.entries.forEach { op ->
                FilterChip(selected = op == s.operation, onClick = { vm.selectOperation(op) }, label = { Text(op.label) })
            }
        }

        MatrixEditor("Matriz A", s.a, vm::editA, vm::resizeA)
        if (s.operation.needsB) MatrixEditor("Matriz B", s.b, vm::editB, vm::resizeB)
        if (s.operation.needsScalar) {
            OutlinedTextField(
                value = s.scalar,
                onValueChange = vm::setScalar,
                label = { Text("k") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.width(96.dp),
            )
        }

        Button(onClick = vm::calculate) { Text("Calcular") }
        s.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        s.result?.let { result ->
            Text("Resultado", style = MaterialTheme.typography.titleMedium)
            MatrixView(result)
            Text("Passo a passo", style = MaterialTheme.typography.titleMedium)
            StepsList(s.steps)
        }
    }
}

@Composable
fun MatrixEditor(
    title: String,
    grid: GridState,
    onCell: (Int, Int, String) -> Unit,
    onResize: (Int, Int) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(title, style = MaterialTheme.typography.titleMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Stepper("Linhas", grid.rows, 1..4, Modifier.weight(1f)) { onResize(it, grid.cols) }
            Stepper("Colunas", grid.cols, 1..4, Modifier.weight(1f)) { onResize(grid.rows, it) }
        }
        for (i in 0 until grid.rows) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                for (j in 0 until grid.cols) {
                    OutlinedTextField(
                        value = grid.cell(i, j),
                        onValueChange = { onCell(i, j, it) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                        modifier = Modifier.width(72.dp),
                    )
                }
            }
        }
    }
}
