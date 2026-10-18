package com.clcmo.gitteste.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class VectorOpsTest {
    @Test fun soma() = assertEquals(Vec(4.0, 6.0), VectorOps.add(Vec(1.0, 2.0), Vec(3.0, 4.0)).value)

    @Test fun produtoEscalar() =
        assertEquals(32.0, VectorOps.dot(Vec(1.0, 2.0, 3.0), Vec(4.0, 5.0, 6.0)).value, 1e-9)

    @Test fun modulo() = assertEquals(5.0, VectorOps.norm(Vec(3.0, 4.0)).value, 1e-9)

    @Test fun anguloReto() = assertEquals(90.0, VectorOps.angleDegrees(Vec(1.0, 0.0), Vec(0.0, 1.0)).value, 1e-9)

    @Test fun escalar() = assertEquals(Vec(-2.0, 6.0), VectorOps.scale(-2.0, Vec(1.0, -3.0)).value)
}
