package com.clcmo.gitteste.domain

/** Matriz imutável, armazenada em ordem de linhas (row-major). */
class Matrix(val rows: Int, val cols: Int, values: List<Double>) {
    private val data: List<Double> = values.toList()

    init {
        require(rows > 0 && cols > 0) { "A matriz precisa ter ao menos 1 linha e 1 coluna." }
        require(data.size == rows * cols) { "Quantidade de elementos incompatível com ${rows}×${cols}." }
    }

    operator fun get(i: Int, j: Int): Double = data[i * cols + j]
    fun row(i: Int): List<Double> = List(cols) { j -> this[i, j] }
    fun column(j: Int): List<Double> = List(rows) { i -> this[i, j] }
    fun toRows(): List<List<Double>> = List(rows) { row(it) }

    override fun equals(other: Any?) =
        other is Matrix && rows == other.rows && cols == other.cols && data == other.data

    override fun hashCode() = 31 * (31 * rows + cols) + data.hashCode()

    override fun toString() = toRows().joinToString("\n") { r -> r.joinToString(" ") { it.fmt() } }

    companion object {
        fun of(vararg rows: List<Double>): Matrix {
            require(rows.isNotEmpty()) { "A matriz precisa ter ao menos 1 linha." }
            val c = rows[0].size
            require(rows.all { it.size == c }) { "Todas as linhas precisam ter o mesmo tamanho." }
            return Matrix(rows.size, c, rows.flatMap { it })
        }

        fun fromFunction(rows: Int, cols: Int, f: (Int, Int) -> Double) =
            Matrix(rows, cols, List(rows * cols) { f(it / cols, it % cols) })

        fun identity(n: Int) = fromFunction(n, n) { i, j -> if (i == j) 1.0 else 0.0 }
    }
}
