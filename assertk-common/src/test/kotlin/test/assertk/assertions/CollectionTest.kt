package test.assertk.assertions

import assertk.assert
import assertk.assertions.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class CollectionTest {
    //region isEmpty
    @Test fun isEmptyPasses() {
        assert(emptyList<Any?>()).isEmpty()
    }

    @Test fun isEmptyFails() {
        val error = assertFails {
            assert(listOf<Any?>(null)).isEmpty()
        }
        assertEquals("expected to be empty but was:<[null]>", error.message)
    }
    //endregion

    //region isNotEmpty
    @Test fun isNotEmptyPasses() {
        assert(listOf<Any?>(null)).isNotEmpty()
    }

    @Test fun isNotEmptyFails() {
        val error = assertFails {
            assert(emptyList<Any?>()).isNotEmpty()
        }
        assertEquals("expected to not be empty", error.message)
    }
    //endregion

    //region isNullOrEmpty
    @Test fun isNullOrEmptyWithNullPasses() {
        assert(null as List<Any?>?).isNullOrEmpty()
    }

    @Test fun isNullOrEmptyPasses() {
        assert(emptyList<Any?>()).isNullOrEmpty()
    }

    @Test fun isNullOrEmptyFails() {
        val error = assertFails {
            assert(listOf<Any?>(null)).isNullOrEmpty()
        }
        assertEquals("expected to be null or empty but was:<[null]>", error.message)
    }
    //endregion

    //region hasSize
    @Test fun hasSizePasses() {
        assert(emptyList<Any?>()).hasSize(0)
    }

    @Test fun hasSizeFails() {
        val error = assertFails {
            assert(emptyList<Any?>()).hasSize(1)
        }
        assertEquals("expected [size]:<[1]> but was:<[0]> ([])", error.message)
    }
    //endregion

    //region hasSameSizeAs
    @Test fun hasSameSizeAsPasses() {
        assert(emptyList<Any?>()).hasSameSizeAs(emptyList<Any?>())
    }

    @Test fun hasSameSizeAsFails() {
        val error = assertFails {
            assert(emptyList<Any?>()).hasSameSizeAs(listOf<Any?>(null))
        }
        assertEquals("expected to have same size as:<[null]> (1) but was size:(0)", error.message)
    }
    //endregion

    //region containsNone
    @Test fun containsNonePasses() {
        assert(emptyList<Any?>()).containsNone(1)
    }

    @Test fun containsNoneFails() {
        val error = assertFails {
            assert(listOf(1, 2)).containsNone(2, 3)
        }
        assertEquals("expected to contain none of:<[2, 3]> some elements were not expected:<[2]>", error.message)
    }
    //region

    //region containsAll
    @Test fun containsAllPasses() {
        assert(listOf(1, 2)).containsAll(2, 1)
    }

    @Test fun containsAllFails() {
        val error = assertFails {
            assert(listOf(1)).containsAll(1, 2)
        }
        assertEquals("expected to contain all:<[1, 2]> some elements were not found:<[2]>", error.message)
    }
    //endregion

    //region containsOnly
    @Test fun containsOnlyPasses() {
        assert(listOf(1, 2)).containsOnly(2, 1)
    }

    @Test fun containsOnlyElementsMissingFails() {
        val error = assertFails {
            assert(listOf(1)).containsOnly(1, 2)
        }
        assertEquals("expected to contain all:<[1, 2]> some elements were not found:<[2]>", error.message)
    }

    @Test fun containsOnlyUnexpectedElementsFails() {
        val error = assertFails {
            assert(listOf(1, 2, 3)).containsOnly(1, 2)
        }
        assertEquals("expected to contain only:<[1, 2]> some elements were not expected:<[3]>", error.message)
    }
    //endregion
}
