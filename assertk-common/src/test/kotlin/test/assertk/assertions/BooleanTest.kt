package test.assertk.assertions

import assertk.assert
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class BooleanTest {
    //region isTrue
    @Test fun isTrue_true_value_passes() {
        assert(true).isTrue()
    }

    @Test fun isTrue_false_value_fails() {
        val error = assertFails {
            assert(false).isTrue()
        }
        assertEquals("expected to be true", error.message)
    }
    //endregion

    //region isFalse
    @Test fun isFalse_false_value_passes() {
        assert(false).isFalse()
    }

    @Test fun isFalse_true_value_fails() {
        val error = assertFails {
            assert(true).isFalse()
        }
        assertEquals("expected to be false", error.message)
    }
    //endregion
}
