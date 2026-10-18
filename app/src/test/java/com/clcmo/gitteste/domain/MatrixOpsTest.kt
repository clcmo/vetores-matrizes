package com.clcmo.gitteste.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class MatrixOpsTest {
    private val a = Matrix.of(listOf(1.0, 2.0), listOf(3.0, 4.0))
    private val b = Matrix.of(listOf(5.0, 6.0), listOf(7.0, 8.0))

    @Test fun soma() = assertEquals(Matrix.of(listOf(6.0, 8.0), listOf(10.0, 12.0)), MatrixOps.add(a, b).value)

    @Test fun subtracao() = assertEquals(Matrix.of(listOf(-4.0, -4.0), listOf(-4.0, -4.0)), MatrixOps.subtract(a, b).value)

    @Test fun multiplicacao() =
        assertEquals(Matrix.of(listOf(19.0, 22.0), listOf(43.0, 50.0)), MatrixOps.multiply(a, b).value)

    @Test fun identidadeNaoAlteraProduto() = assertEquals(a, MatrixOps.multiply(a, Matrix.identity(2)).value)

    @Test fun transposta() {
        val m = Matrix.of(listOf(1.0, 2.0, 3.0), listOf(4.0, 5.0, 6.0))
        val t = MatrixOps.transpose(m).value
        assertEquals(3, t.rows); assertEquals(2, t.cols); assertEquals(4.0, t[0, 1], 0.0)
    }

    @Test fun escalar() = assertEquals(Matrix.of(listOf(2.0, 4.0), listOf(6.0, 8.0)), MatrixOps.scale(2.0, a).value)

    @Test fun tamanhosIncompativeisNaSoma() {
        val c = Matrix.of(listOf(1.0, 2.0, 3.0))
        assertThrows(IllegalArgumentException::class.java) { MatrixOps.add(a, c) }
    }

    @Test fun tamanhosIncompativeisNoProduto() {
        val c = Matrix.of(listOf(1.0, 2.0, 3.0))
        assertThrows(IllegalArgumentException::class.java) { MatrixOps.multiply(a, c) }
    }
}
