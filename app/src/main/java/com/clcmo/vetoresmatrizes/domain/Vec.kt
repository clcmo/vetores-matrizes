package com.clcmo.vetoresmatrizes.domain

/** Vetor com qualquer número de componentes. */
data class Vec(val components: List<Double>) {
    constructor(vararg c: Double) : this(c.toList())

    init { require(components.isNotEmpty()) { "O vetor precisa ter ao menos 1 componente." } }

    val dimension: Int get() = components.size
    operator fun get(i: Int): Double = components[i]
    fun copyWith(index: Int, value: Double) = Vec(components.toMutableList().also { it[index] = value })

    override fun toString() = components.joinToString(", ", "(", ")") { it.fmt() }
}
