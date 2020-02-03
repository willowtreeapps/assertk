package test.assertk.assertions

import assertk.Assert
import assertk.FailingAssert
import assertk.ValueAssert

val Assert<*>.valueOrFail
    get() = when (this) {
        is ValueAssert -> value
        is FailingAssert -> throw error
    }