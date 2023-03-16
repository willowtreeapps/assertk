package test.assertk.assertions

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEqualTo
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class JavaNullableStringTest {

    //region isEqualTo
    @Test fun isEqualTo_same_nullable_string_passes() {
        assertThat(JavaNullableString.string()).isEqualTo(JavaNullableString.string())
    }

    @Test fun isEqualTo_different_string_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(JavaNullableString.string()).isEqualTo("wrong")
        }
        assertEquals("expected:<\"wrong\"> but was:<null>", error.message)
    }
    //endregion

    //region isNotEqualTo
    @Test fun isNotEqualTo_different_string_passes() {
        assertThat(JavaNullableString.string()).isNotEqualTo("wrong")
    }

    @Test fun isNotEqualTo_same_nullable_string_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(JavaNullableString.string()).isNotEqualTo(JavaNullableString.string())
        }
        assertEquals("expected to not be equal to:<null>", error.message)
    }
    //region endregion
}
