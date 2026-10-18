package com.clcmo.vetores-matrizes.domain

import kotlin.math.abs
import kotlin.math.floor

/** Um passo de resolução, exibido ao aluno. */
data class Step(val title: String, val detail: String)

/** Resultado de uma operação + o passo a passo que levou até ele. */
data class Explained<T>(val value: T, val steps: List<Step>)

/** Formata 3.0 como "3" e 2.5 como "2,50" (conforme o locale). */
fun Double.fmt(): String =
    if (this == floor(this) && abs(this) < 1e12) toLong().toString() else "%.2f".format(this)
