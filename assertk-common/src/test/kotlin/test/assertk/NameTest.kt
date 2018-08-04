package test.assertk

import assertk.assert
import assertk.assertions.isEqualTo
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class NameTest {
    @Test fun with_no_name_fails_with_default_error_message() {
        val error = assertFails {
            assert("yes").isEqualTo("no")
        }
        assertEquals("expected:<\"[no]\"> but was:<\"[yes]\">", error.message)
    }

    @Test fun with_name_fails_with_name_prefixing_message() {
        val error = assertFails {
            assert("yes", name = "test").isEqualTo("no")
        }
        assertEquals("expected [test]:<\"[no]\"> but was:<\"[yes]\">", error.message)
    }
}
