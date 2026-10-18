package com.clcmo.gitteste.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.clcmo.gitteste.domain.Matrix
import com.clcmo.gitteste.domain.Step
import com.clcmo.gitteste.domain.fmt

@Composable
fun Stepper(label: String, value: Int, range: IntRange, modifier: Modifier = Modifier, onChange: (Int) -> Unit) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(label, modifier = Modifier.width(56.dp), style = MaterialTheme.typography.bodySmall)
        OutlinedButton(
            onClick = { onChange(value - 1) },
            enabled = value > range.first,
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.size(36.dp),
        ) { Text("−") }
        Text("$value", modifier = Modifier.width(28.dp), textAlign = TextAlign.Center)
        OutlinedButton(
            onClick = { onChange(value + 1) },
            enabled = value < range.last,
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.size(36.dp),
        ) { Text("+") }
    }
}

@Composable
fun MatrixView(matrix: Matrix, modifier: Modifier = Modifier) {
    Column(
        modifier
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        for (i in 0 until matrix.rows) {
            Row {
                for (j in 0 until matrix.cols) {
                    Text(
                        matrix[i, j].fmt(),
                        modifier = Modifier.width(56.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
        }
    }
}

@Composable
fun StepsList(steps: List<Step>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        steps.forEach { step ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp)) {
                    Text(step.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                    Text(step.detail, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
