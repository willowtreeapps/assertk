package test.assertk.assertions.support

import assertk.assertThat
import assertk.assertions.support.expected
import assertk.assertions.support.fail
import assertk.assertions.support.show
import com.willowtreeapps.opentest4k.*
import kotlin.test.*

class SupportTest {
    //region show
    @Test fun show_null() {
        assertEquals("<null>", show(null))
    }

    @Test fun show_boolean() {
        assertEquals("<true>", show(true))
    }

    @Test fun show_char() {
        assertEquals("<'c'>", show('c'))
    }

    @Test fun show_double() {
        assertEquals("<1.234567890123>", show(1.234567890123))
    }

    @Test fun show_int() {
        assertEquals("<42>", show(42))
    }

    @Test fun show_long() {
        assertEquals("<42L>", show(42L))
    }

    @Test fun show_short() {
        assertEquals("<42>", show(42.toShort()))
    }

    @Test fun show_string() {
        assertEquals("<\"value\">", show("value"))
    }

    @Test fun show_generic_array() {
        assertEquals("<[\"one\", \"two\"]>", show(arrayOf("one", "two")))
    }

    @Test fun show_boolean_array() {
        assertEquals("<[true, false]>", show(booleanArrayOf(true, false)))
    }

    @Test fun show_char_array() {
        assertEquals("<['a', 'b']>", show(charArrayOf('a', 'b')))
    }

    @Test fun show_double_array() {
        assertEquals("<[1.2345, 6.789]>", show(doubleArrayOf(1.2345, 6.789)))
    }

    @Test fun show_int_array() {
        assertEquals("<[42, 8]>", show(intArrayOf(42, 8)))
    }

    @Test fun show_long_array() {
        assertEquals("<[42L, 8L]>", show(longArrayOf(42L, 8L)))
    }

    @Test fun show_short_array() {
        assertEquals("<[42, -1]>", show(shortArrayOf(42, -1)))
    }

    @Test fun show_list() {
        assertEquals("<[1, 2, 3]>", show(listOf(1, 2, 3)))
    }

    @Test fun show_map() {
        assertEquals("<{1=5, 2=6}>", show(mapOf(1 to 5, 2 to 6)))
    }

    @Test fun show_pair() {
        assertEquals("<(\"one\", '2')>", show("one" to '2'))
    }

    @Test fun show_triple() {
        assertEquals("<(\"one\", '2', 3)>", show(Triple("one", '2', 3)))
    }

    @Test fun show_custom_type() {
        val other = object : Any() {
            override fun toString(): String = "different"
        }
        assertEquals("<different>", show(other))
    }

    @Test fun show_different_wrapper() {
        assertEquals("{42}", show(42, "{}"))
    }
    //endregion

    //region fail
    @Test fun fail_expected_and_actual_the_same_shows_simple_message_without_diff() {
        val error = assertFails {
            assertThat(0).fail(1, 1)
        }

        assertEquals("expected:<1> but was:<1>", error.message)
    }

    @Test fun fail_expected_null_shows_simple_message_without_diff() {
        val error = assertFails {
            assertThat(0).fail(null, 1)
        }

        assertEquals("expected:<null> but was:<1>", error.message)
    }

    @Test fun fail_actual_null_shows_simple_message_without_diff() {
        val error = assertFails {
            assertThat(0).fail(1, null)
        }

        assertEquals("expected:<1> but was:<null>", error.message)
    }

    @Test fun fail_short_expected_and_actual_different_shows_simple_diff() {
        val error = assertFails {
            assertThat(0).fail("test1", "test2")
        }

        assertEquals("expected:<\"test[1]\"> but was:<\"test[2]\">", error.message)
    }

    @Test fun fail_long_expected_and_actual_different_shows_compact_diff() {
        val error = assertFails {
            assertThat(0).fail(
                "this is a long prefix 1 this is a long suffix",
                "this is a long prefix 2 this is a long suffix"
            )
        }

        assertEquals(
            "expected:<...is is a long prefix [1] this is a long suff...> but was:<...is is a long prefix [2] this is a long suff...>",
            error.message
        )
    }
    //endregion

    //region expected
    @Test fun expected_throws_assertion_failed_error_with_actual_and_expected_present_and_defined() {
        val error = assertFails {
            assertThat(0).expected("message", "expected", "actual")
        }

        assertEquals(AssertionFailedError::class, error::class)
        error as AssertionFailedError
        assertEquals("expected" as Any?, error.expected?.value)
        assertEquals("actual" as Any?, error.actual?.value)
        assertTrue(error.isExpectedDefined)
        assertTrue(error.isActualDefined)
    }

    @Test fun expected_throws_assertion_failed_error_with_actual_and_expected_not_defined() {
        val error = assertFails {
            assertThat(0).expected("message")
        }

        assertEquals(AssertionFailedError::class, error::class)
        error as AssertionFailedError
        assertNull(error.expected)
        assertNull(error.actual)
        assertFalse(error.isExpectedDefined)
        assertFalse(error.isActualDefined)
    }
    //endregion
}
