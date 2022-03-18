package test.assertk.assertions

import assertk.assertThat
import assertk.assertions.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class MapTest {
    //region contains
    @Test fun contains_element_present_passes() {
        assertThat(mapOf("one" to 1, "two" to 2)).contains("two" to 2)
    }

    @Test fun contains_element_missing_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(emptyMap<String, Int>()).contains("one" to 1)
        }
        assertEquals("expected to contain:<{\"one\"=1}> but was:<{}>", error.message)
    }

    @Test fun contains_element_with_nullable_value_missing_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(emptyMap<String, Int?>()).contains("one" to null)
        }
        assertEquals("expected to contain:<{\"one\"=null}> but was:<{}>", error.message)
    }
    //endregion

    //region doesNotContain
    @Test fun doesNotContain_element_missing_passes() {
        assertThat(emptyMap<String, Int>()).doesNotContain("one" to 1)
    }

    @Test fun doesNotContain_element_with_missing_nullable_value_passes() {
        assertThat(emptyMap<String, Int?>()).doesNotContain("one" to null)
    }

    @Test fun doesNotContain_element_present_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(mapOf("one" to 1, "two" to 2)).doesNotContain("two" to 2)
        }
        assertEquals("expected to not contain:<{\"two\"=2}> but was:<{\"one\"=1, \"two\"=2}>", error.message)
    }
    //endregion

    //region containsNone
    @Test fun containsNone_missing_elements_passes() {
        assertThat(emptyMap<String, Int>()).containsNone("one" to 1)
    }

    @Test fun containsNone_missing_elements_with_nullable_value_passes() {
        assertThat(emptyMap<String, Int?>()).containsNone("one" to null)
    }

    @Test fun containsNone_present_element_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(mapOf("one" to 1, "two" to 2)).containsNone("two" to 2, "three" to 3)
        }
        assertEquals(
            """expected to contain none of:<{"two"=2, "three"=3}> but was:<{"one"=1, "two"=2}>
                | elements not expected:<{"two"=2}>
            """.trimMargin(),
            error.message
        )
    }
    //region

    //region containsAll
    @Test fun containsAll_all_elements_passes() {
        assertThat(mapOf("one" to 1, "two" to 2)).containsAll("two" to 2, "one" to 1)
    }

    @Test fun containsAll_extra_elements_passes() {
        assertThat(mapOf("one" to 1, "two" to 2, "three" to 3)).containsAll("one" to 1, "two" to 2)
    }

    @Test fun containsAll_swapped_keys_and_values_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(mapOf("one" to 2, "two" to 1)).containsAll("two" to 2, "one" to 1)
        }

        assertEquals(
                """expected to contain all:<{"two"=2, "one"=1}> but was:<{"one"=2, "two"=1}>
                | elements not found:<{"two"=2, "one"=1}>
            """.trimMargin(), error.message
        )
    }

    @Test fun containsAll_nullable_values_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(mapOf<String, Any?>()).containsAll("key" to null)
        }
        assertEquals(
                """expected to contain all:<{"key"=null}> but was:<{}>
                | elements not found:<{"key"=null}>
            """.trimMargin(), error.message
        )
    }

    @Test fun containsAll_some_elements_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(mapOf("one" to 1)).containsAll("one" to 1, "two" to 2)
        }
        assertEquals(
            """expected to contain all:<{"one"=1, "two"=2}> but was:<{"one"=1}>
                | elements not found:<{"two"=2}>
            """.trimMargin(),
            error.message
        )
    }
    //endregion

    //region containsOnly
    @Test fun containsOnly_all_elements_passes() {
        assertThat(mapOf("one" to 1, "two" to 2)).containsOnly("two" to 2, "one" to 1)
    }

    @Test fun containsOnly_swapped_keys_and_values_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(mapOf("one" to 2, "two" to 1)).containsOnly("two" to 2, "one" to 1)
        }

        assertEquals(
                """expected to contain only:<{"two"=2, "one"=1}> but was:<{"one"=2, "two"=1}>
                | elements not found:<{"two"=2, "one"=1}>
                | extra elements found:<{"one"=2, "two"=1}>
            """.trimMargin(), error.message
        )
    }

    @Test fun containsOnly_missing_element_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(mapOf("one" to 1, "two" to 2)).containsOnly("three" to 3)
        }
        assertEquals(
            """expected to contain only:<{"three"=3}> but was:<{"one"=1, "two"=2}>
                | elements not found:<{"three"=3}>
                | extra elements found:<{"one"=1, "two"=2}>
            """.trimMargin(), error.message
        )
    }

    @Test fun containsOnly_extra_element_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(mapOf("one" to 1, "two" to 2)).containsOnly("one" to 1)
        }
        assertEquals(
            """expected to contain only:<{"one"=1}> but was:<{"one"=1, "two"=2}>
                | extra elements found:<{"two"=2}>
            """.trimMargin(), error.message
        )
    }
    //endregion

    //region isEmpty
    @Test fun isEmpty_empty_passes() {
        assertThat(emptyMap<Any?, Any?>()).isEmpty()
    }

    @Test fun isEmpty_non_empty_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(mapOf<Any?, Any?>(null to null)).isEmpty()
        }
        assertEquals("expected to be empty but was:<{null=null}>", error.message)
    }
    //endregion

    //region isNotEmpty
    @Test fun isNotEmpty_non_empty_passes() {
        assertThat(mapOf<Any?, Any?>(null to null)).isNotEmpty()
    }

    @Test fun isNotEmpty_empty_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(mapOf<Any?, Any?>()).isNotEmpty()
        }
        assertEquals("expected to not be empty", error.message)
    }
    //endregion

    //region isNullOrEmpty
    @Test fun isNullOrEmpty_null_passes() {
        assertThat(null as Map<Any?, Any?>?).isNullOrEmpty()
    }

    @Test fun isNullOrEmpty_empty_passes() {
        assertThat(emptyMap<Any?, Any?>()).isNullOrEmpty()
    }

    @Test fun isNullOrEmpty_non_empty_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(mapOf<Any?, Any?>(null to null)).isNullOrEmpty()
        }
        assertEquals("expected to be null or empty but was:<{null=null}>", error.message)
    }
    //endregion

    //region hasSize
    @Test fun hasSize_correct_size_passes() {
        assertThat(emptyMap<String, Int>()).hasSize(0)
    }

    @Test fun hasSize_wrong_size_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(emptyMap<String, Int>()).hasSize(1)
        }
        assertEquals("expected [size]:<[1]> but was:<[0]> ({})", error.message)
    }
    //endregion

    //region hasSameSizeAs
    @Test fun hasSameSizeAs_equal_sizes_passes() {
        assertThat(emptyMap<String, Int>()).hasSameSizeAs(emptyMap<String, Int>())
    }

    @Test fun hasSameSizeAs_non_equal_sizes_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(emptyMap<String, Int>()).hasSameSizeAs(mapOf("one" to 1))
        }
        assertEquals("expected to have same size as:<{\"one\"=1}> (1) but was size:(0)", error.message)
    }
    //endregion

    //region key
    @Test fun index_successful_assertion_passes() {
        assertThat(mapOf("one" to 1, "two" to 2), name = "subject").key("one").isEqualTo(1)
    }

    @Test fun index_unsuccessful_assertion_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(mapOf("one" to 1, "two" to 2), name = "subject").key("one").isEqualTo(2)
        }
        assertEquals("expected [subject[\"one\"]]:<[2]> but was:<[1]> ({\"one\"=1, \"two\"=2})", error.message)
    }

    @Test fun index_missing_key_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(mapOf("one" to 1, "two" to 2), name = "subject").key("wrong").isEqualTo(1)
        }
        assertEquals("expected [subject] to have key:<\"wrong\">", error.message)
    }
    //endregion
}
