package com.clcmo.vetoresmatrizes.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.clcmo.vetoresmatrizes.domain.Vec
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.sin

/** Plano cartesiano com as setas de a (primária), b (terciária) e do resultado (erro/vermelho). */
@Composable
fun VectorCanvas(a: Vec, b: Vec?, result: Vec?, modifier: Modifier = Modifier) {
    val colorA = MaterialTheme.colorScheme.primary
    val colorB = MaterialTheme.colorScheme.tertiary
    val colorR = MaterialTheme.colorScheme.error
    val gridColor = MaterialTheme.colorScheme.outlineVariant
    val axisColor = MaterialTheme.colorScheme.outline

    val extent = listOfNotNull(a, b, result).flatMap { it.components }.maxOf { abs(it) }
    val range = ceil(extent.coerceAtLeast(5.0)).toInt() + 1

    Canvas(modifier.fillMaxWidth().aspectRatio(1f)) {
        val half = size.width / 2f
        val unit = half / range
        fun p(x: Double, y: Double) = Offset(half + x.toFloat() * unit, half - y.toFloat() * unit)

        for (t in -range..range) {
            val c = if (t == 0) axisColor else gridColor
            val w = if (t == 0) 3f else 1f
            drawLine(c, p(t.toDouble(), -range.toDouble()), p(t.toDouble(), range.toDouble()), strokeWidth = w)
            drawLine(c, p(-range.toDouble(), t.toDouble()), p(range.toDouble(), t.toDouble()), strokeWidth = w)
        }

        fun arrow(v: Vec, color: Color) {
            val start = p(0.0, 0.0)
            val end = p(v[0], v[1])
            drawLine(color, start, end, strokeWidth = 6f)
            if (end == start) return
            val angle = atan2(end.y - start.y, end.x - start.x)
            val head = 28f
            listOf(angle + 2.6f, angle - 2.6f).forEach { ang ->
                drawLine(color, end, Offset(end.x + head * cos(ang), end.y + head * sin(ang)), strokeWidth = 6f)
            }
        }

        arrow(a, colorA)
        b?.let { arrow(it, colorB) }
        result?.let { arrow(it, colorR) }
    }
}
