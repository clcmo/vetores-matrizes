package com.clcmo.vetores-matrizes.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ExerciseScreen(vm: ExerciseViewModel = viewModel()) {
    val s by vm.state.collectAsStateWithLifecycle()
    Column(
        Modifier.verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Exercício", style = MaterialTheme.typography.headlineSmall)
        Text(if (s.subtract) "Calcule A − B." else "Calcule A + B.", style = MaterialTheme.typography.titleMedium)

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Column { Text("A"); MatrixView(s.a) }
            Column { Text("B"); MatrixView(s.b) }
        }

        Text("Sua resposta")
        for (i in 0 until EXERCISE_SIZE) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                for (j in 0 until EXERCISE_SIZE) {
                    val idx = i * EXERCISE_SIZE + j
                    OutlinedTextField(
                        value = s.answer[idx],
                        onValueChange = { vm.edit(idx, it) },
                        isError = idx in s.wrongCells,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                        modifier = Modifier.width(72.dp),
                    )
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = vm::check) { Text("Verificar") }
            OutlinedButton(onClick = vm::next) { Text("Novo exercício") }
        }
        s.message?.let {
            Text(it, color = if (s.correct) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error)
        }
        TextButton(onClick = vm::toggleSteps) {
            Text(if (s.showSteps) "Esconder passo a passo" else "Ver passo a passo")
        }
        if (s.showSteps) StepsList(s.expected.steps)
    }
}
