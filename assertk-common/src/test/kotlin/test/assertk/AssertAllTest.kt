package test.assertk

import assertk.all
import assertk.assert
import assertk.assertAll
import assertk.assertions.endsWith
import assertk.assertions.isEqualTo
import assertk.assertions.startsWith
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class AssertAllTest {
    //region all
    @Test fun all_multiple_successful_passes() {
        assert("test").all({
            startsWith("t")
        }, {
            endsWith("t")
        })
    }

    @Test fun all_one_failure_fails() {
        val error = assertFails {
            assert("test", name = "test").all({
                startsWith("t")
            }, {
                endsWith("g")
            })
        }
        assertEquals(
            "expected [test] to end with:<\"g\"> but was:<\"test\">",
            error.message
        )
    }

    @Test fun all_both_failures_fails_with_both() {
        val error = assertFails {
            assert("test", name = "test").all({
                startsWith("w")
            }, {
                endsWith("g")
            })
        }
        assertEquals(
            """The following assertions failed (2 failures)
              |${"\t"}expected [test] to start with:<"w"> but was:<"test">
              |${"\t"}expected [test] to end with:<"g"> but was:<"test">
            """.trimMargin(),
            error.message
        )
    }
    //endregion

    //region assertAll
    @Test fun assertAll_multiple_successful_passes() {
        assertAll({
            assert("test1", name = "test1").isEqualTo("test1")
        }, {
            assert("test2", name = "test2").isEqualTo("test2")
        })
    }

    @Test fun assertAll_one_failure_fails() {
        val error = assertFails {
            assertAll({
                assert("test1", name = "test1").isEqualTo("wrong1")
            }, {
                assert("test2", name = "test2").isEqualTo("test2")
            })
        }
        assertEquals(
            "expected [test1]:<\"[wrong]1\"> but was:<\"[test]1\">",
            error.message
        )
    }

    @Test fun assertAll_both_failures_fails_with_both() {
        val error = assertFails {
            assertAll({
                assert("test1", name = "test1").isEqualTo("wrong1")
            }, {
                assert("test2", name = "test2").isEqualTo("wrong2")
            })
        }
        assertEquals(
            """The following assertions failed (2 failures)
              |${"\t"}expected [test1]:<"[wrong]1"> but was:<"[test]1">
              |${"\t"}expected [test2]:<"[wrong]2"> but was:<"[test]2">
            """.trimMargin(),
            error.message
        )
    }

    @Test fun assertAll_nested_failures_are_merged() {
        val error = assertFails {
            assertAll({
                assert("test1", name = "test1").isEqualTo("wrong1")
            }, {
                assertAll({
                    assert("test2", name = "test2").isEqualTo("wrong2")
                }, {
                    assert("test3", name = "test3").isEqualTo("wrong3")
                })
            })
        }
        assertEquals(
            """The following assertions failed (3 failures)
              |${"\t"}expected [test1]:<"[wrong]1"> but was:<"[test]1">
              |${"\t"}expected [test2]:<"[wrong]2"> but was:<"[test]2">
              |${"\t"}expected [test3]:<"[wrong]3"> but was:<"[test]3">
            """.trimMargin(),
            error.message
        )
    }
    //endregion
}