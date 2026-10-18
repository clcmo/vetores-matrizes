package com.clcmo.vetoresmatrizes.domain

import kotlin.math.acos
import kotlin.math.sqrt

object VectorOps {

    fun add(a: Vec, b: Vec) = combine(a, b, "+") { x, y -> x + y }

    fun subtract(a: Vec, b: Vec) = combine(a, b, "−") { x, y -> x - y }

    private fun combine(a: Vec, b: Vec, symbol: String, op: (Double, Double) -> Double): Explained<Vec> {
        requireSameDimension(a, b)
        val values = a.components.indices.map { op(a[it], b[it]) }
        val steps = a.components.indices.map { i ->
            Step("Componente ${i + 1}", "${a[i].fmt()} $symbol ${b[i].fmt()} = ${values[i].fmt()}")
        }
        return Explained(Vec(values), steps)
    }

    fun scale(k: Double, a: Vec): Explained<Vec> {
        val values = a.components.map { k * it }
        val steps = a.components.indices.map { i ->
            Step("Componente ${i + 1}", "${k.fmt()} · ${a[i].fmt()} = ${values[i].fmt()}")
        }
        return Explained(Vec(values), steps)
    }

    fun dot(a: Vec, b: Vec): Explained<Double> {
        requireSameDimension(a, b)
        val v = a.components.indices.sumOf { a[it] * b[it] }
        val expr = a.components.indices.joinToString(" + ") { "${a[it].fmt()}·${b[it].fmt()}" }
        return Explained(v, listOf(Step("Multiplique componente a componente e some", "$expr = ${v.fmt()}")))
    }

    fun norm(a: Vec): Explained<Double> {
        val squares = a.components.sumOf { it * it }
        val v = sqrt(squares)
        val expr = a.components.joinToString(" + ") { "${it.fmt()}²" }
        return Explained(v, listOf(Step("Teorema de Pitágoras", "√($expr) = √${squares.fmt()} = ${v.fmt()}")))
    }

    fun angleDegrees(a: Vec, b: Vec): Explained<Double> {
        requireSameDimension(a, b)
        val na = norm(a).value
        val nb = norm(b).value
        require(na > 0 && nb > 0) { "O ângulo não está definido para o vetor nulo." }
        val d = dot(a, b).value
        val cos = (d / (na * nb)).coerceIn(-1.0, 1.0)
        val deg = Math.toDegrees(acos(cos))
        return Explained(
            deg,
            listOf(
                Step("Produto escalar", "a · b = ${d.fmt()}"),
                Step("Módulos", "|a| = ${na.fmt()} e |b| = ${nb.fmt()}"),
                Step("cos θ = (a · b) / (|a|·|b|)", "cos θ = ${d.fmt()} / (${na.fmt()}·${nb.fmt()}) = ${cos.fmt()}"),
                Step("θ = arccos(cos θ)", "θ = ${deg.fmt()}°"),
            )
        )
    }

    private fun requireSameDimension(a: Vec, b: Vec) =
        require(a.dimension == b.dimension) { "Os vetores precisam ter o mesmo número de componentes." }
}
