package com.clcmo.vetores-matrizes.domain

/** Operações com matrizes. Cada uma devolve o resultado e o passo a passo. */
object MatrixOps {

    fun add(a: Matrix, b: Matrix) = elementWise(a, b, "+") { x, y -> x + y }

    fun subtract(a: Matrix, b: Matrix) = elementWise(a, b, "−") { x, y -> x - y }

    private fun elementWise(
        a: Matrix, b: Matrix, symbol: String, op: (Double, Double) -> Double,
    ): Explained<Matrix> {
        require(a.rows == b.rows && a.cols == b.cols) {
            "Para somar ou subtrair, as matrizes precisam ter o mesmo tamanho " +
                "(A é ${a.rows}×${a.cols} e B é ${b.rows}×${b.cols})."
        }
        val steps = mutableListOf(
            Step("Confira os tamanhos", "As duas são ${a.rows}×${a.cols}: a operação é feita elemento a elemento.")
        )
        val result = Matrix.fromFunction(a.rows, a.cols) { i, j ->
            val v = op(a[i, j], b[i, j])
            steps += Step("Posição (${i + 1}, ${j + 1})", "${a[i, j].fmt()} $symbol ${b[i, j].fmt()} = ${v.fmt()}")
            v
        }
        return Explained(result, steps)
    }

    fun scale(k: Double, a: Matrix): Explained<Matrix> {
        val steps = mutableListOf(Step("Ideia", "Multiplique cada elemento de A por k = ${k.fmt()}."))
        val result = Matrix.fromFunction(a.rows, a.cols) { i, j ->
            val v = k * a[i, j]
            steps += Step("Posição (${i + 1}, ${j + 1})", "${k.fmt()} · ${a[i, j].fmt()} = ${v.fmt()}")
            v
        }
        return Explained(result, steps)
    }

    fun transpose(a: Matrix): Explained<Matrix> {
        val steps = List(a.rows) { i ->
            Step("Linha ${i + 1} de A vira coluna ${i + 1} de Aᵀ", a.row(i).joinToString("  ") { it.fmt() })
        }
        return Explained(Matrix.fromFunction(a.cols, a.rows) { i, j -> a[j, i] }, steps)
    }

    fun multiply(a: Matrix, b: Matrix): Explained<Matrix> {
        require(a.cols == b.rows) {
            "Para calcular A×B, o número de colunas de A (${a.cols}) precisa ser igual ao " +
                "número de linhas de B (${b.rows})."
        }
        val steps = mutableListOf(
            Step(
                "Confira os tamanhos",
                "A é ${a.rows}×${a.cols} e B é ${b.rows}×${b.cols}: o produto existe e será ${a.rows}×${b.cols}."
            )
        )
        val result = Matrix.fromFunction(a.rows, b.cols) { i, j ->
            val v = (0 until a.cols).sumOf { k -> a[i, k] * b[k, j] }
            val expr = (0 until a.cols).joinToString(" + ") { k -> "${a[i, k].fmt()}·${b[k, j].fmt()}" }
            steps += Step("Linha ${i + 1} de A × coluna ${j + 1} de B", "$expr = ${v.fmt()}")
            v
        }
        return Explained(result, steps)
    }
}
