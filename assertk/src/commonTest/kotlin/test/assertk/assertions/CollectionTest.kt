package test.assertk.assertions

import assertk.assertThat
import assertk.assertions.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CollectionTest {
    //region isEmpty
    @Test
    fun isEmpty_empty_passes() {
        assertThat(emptyList<Any?>()).isEmpty()
    }

    @Test
    fun isEmpty_non_empty_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(listOf<Any?>(null)).isEmpty()
        }
        assertEquals("expected to be empty but was:<[null]>", error.message)
    }
    //endregion

    //region isNotEmpty
    @Test
    fun isNotEmpty_non_empty_passes() {
        assertThat(listOf<Any?>(null)).isNotEmpty()
    }

    @Test
    fun isNotEmpty_empty_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(emptyList<Any?>()).isNotEmpty()
        }
        assertEquals("expected to not be empty", error.message)
    }
    //endregion

    //region isNullOrEmpty
    @Test
    fun isNullOrEmpty_null_passes() {
        assertThat(null as List<Any?>?).isNullOrEmpty()
    }

    @Test
    fun isNullOrEmpty_empty_passes() {
        assertThat(emptyList<Any?>()).isNullOrEmpty()
    }

    @Test
    fun isNullOrEmpty_non_empty_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(listOf<Any?>(null)).isNullOrEmpty()
        }
        assertEquals("expected to be null or empty but was:<[null]>", error.message)
    }
    //endregion

    //region isNotNullOrEmpty
    @Test
    fun isNotNullOrEmpty_non_empty_passes() {
        assertThat(listOf(1, 2, 3)).isNotNullOrEmpty()
    }

    @Test
    fun isNotNullOrEmpty_null_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(null as List<Any?>?).isNotNullOrEmpty()
        }
        assertEquals("expected to not be null or empty but was:<null>", error.message)
    }

    @Test
    fun isNotNullOrEmpty_empty_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(emptyList<Any?>()).isNotNullOrEmpty()
        }
        assertEquals("expected to not be null or empty but was:<[]>", error.message)
    }
    //endregion

    //region hasSize
    @Test
    fun hasSize_correct_size_passes() {
        assertThat(emptyList<Any?>()).hasSize(0)
    }

    @Test
    fun hasSize_wrong_size_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(emptyList<Any?>()).hasSize(1)
        }
        assertEquals("expected [size]:<[1]> but was:<[0]> ([])", error.message)
    }
    //endregion

    //region hasSameSizeAs
    @Test
    fun hasSameSizeAs_equal_sizes_passes() {
        assertThat(emptyList<Any?>()).hasSameSizeAs(emptyList<Any?>())
    }

    @Test
    fun hasSameSizeAs_non_equal_sizes_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(emptyList<Any?>()).hasSameSizeAs(listOf<Any?>(null))
        }
        assertEquals("expected to have same size as:<[null]> (1) but was size:(0)", error.message)
    }
    //endregion
}
