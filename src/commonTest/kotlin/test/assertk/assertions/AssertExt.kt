package test.assertk.assertions

import assertk.Assert
import assertk.FailingAssert
import assertk.ValueAssert

val <T> Assert<T>.valueOrFail
    get() = when (this) {
        is ValueAssert -> value
        is FailingAssert -> throw error
    }