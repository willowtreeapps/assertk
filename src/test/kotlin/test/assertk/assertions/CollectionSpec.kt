package test.assertk.assertions

import assertk.assert
import assertk.assertAll
import assertk.assertions.*
import org.assertj.core.api.Assertions
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

class CollectionSpec : Spek({

    given("a collection") {

        on("isEmpty()") {
            it("Given an empty list, " +
                    "test should pass") {
                assert(emptyList<Any?>()).isEmpty()
            }

            it("Given a non-empty list of Ints, " +
                    "test should fail") {
                Assertions.assertThatThrownBy {
                    assert(listOf(1, 2, 3, 4)).isEmpty()
                }.hasMessage("expected to be empty but was:<[1, 2, 3, 4]>")
            }

            it("Given a non-empty list of Nulls, " +
                    "test should fail") {
                Assertions.assertThatThrownBy {
                    assert(listOf(null)).isEmpty()
                }.hasMessage("expected to be empty but was:<[null]>")
            }

            it("Given a non-empty list of mixed types, " +
                    "test should fail") {
                Assertions.assertThatThrownBy {
                    assert(listOf(1, 1.09, "awesome!", true)).isEmpty()
                }.hasMessage("expected to be empty but was:<[1, 1.09, \"awesome!\", true]>")
            }

            it("Given two non-empty lists, " +
                    "test should fail with only one error message per failed assertion") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assert(listOf(1, 2, 3, 4)).isEmpty()
                        assert(listOf(1, 1.09, "awesome!", true)).isEmpty()
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                        + "- expected to be empty but was:<[1, 2, 3, 4]>\n"
                        + "- expected to be empty but was:<[1, 1.09, \"awesome!\", true]>")
            }

            it("Given one empty list and two non-empty lists, " +
                    "test should fail for only the non-empty list with only one error message per failed assertion") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assert(emptyList<Any?>()).isEmpty()
                        assert(listOf(1, 2, 3, 4)).isEmpty()
                        assert(listOf(1, 1.09, "awesome!", true)).isEmpty()
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                        + "- expected to be empty but was:<[1, 2, 3, 4]>\n"
                        + "- expected to be empty but was:<[1, 1.09, \"awesome!\", true]>")
            }
        }

        on("isNotEmpty()") {
            it("Given a list of Ints, " +
                    "test should pass") {
                assert(listOf(1, 2, 3, 4)).isNotEmpty()
            }

            it("Given a list filled with a null, " +
                    "test should pass") {
                assert(listOf(null)).isNotEmpty()
            }

            it("Given a list of mixed types, " +
                    "test should pass") {
                assert(listOf(1, 1.09, "awesome!", true)).isNotEmpty()
            }

            it("Given an empty list passed directly in, " +
                    "test should fail") {
                Assertions.assertThatThrownBy {
                    assert(emptyList<Any?>()).isNotEmpty()
                }.hasMessage("expected to not be empty")
            }

            it("Given a list that hasn't been populated, " +
                    "test should fail") {
                Assertions.assertThatThrownBy {
                    val anEmptyList: List<Any?> = listOf()
                    assert(anEmptyList).isNotEmpty()
                }.hasMessage("expected to not be empty")
            }

            it("Given two empty lists, " +
                    "test should fail with only one error message per failed assertion") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assert(emptyList<Any?>()).isNotEmpty()
                        val anEmptyList: List<Any?> = listOf()
                        assert(anEmptyList).isNotEmpty()
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                        + "- expected to not be empty\n"
                        + "- expected to not be empty")
            }

            it("Given one empty list and two empty lists, " +
                    "test should fail for only for the empty lists with only one error message per failed assertion") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assert(emptyList<Any?>()).isNotEmpty()
                        val anEmptyList: List<Any?> = listOf()
                        assert(listOf(1, 2, 3)).isNotEmpty()
                        assert(anEmptyList).isNotEmpty()
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                        + "- expected to not be empty\n"
                        + "- expected to not be empty")
            }
        }

        on("isNullOrEmpty()") {
            it("Given a forced null list, " +
                    "test should pass") {
                // Need to force a null here
                val nullList: List<Any?>? = null
                assert(nullList).isNullOrEmpty()
            }

            it("Given an empty list, " +
                    "test should pass") {
                assert(emptyList<Any?>()).isNullOrEmpty()
            }

            it("Given a non-empty list of Ints, " +
                    "test should fail") {
                Assertions.assertThatThrownBy {
                    assert(listOf(1, 2, 3)).isNullOrEmpty()
                }.hasMessage("expected to be null or empty but was:<[1, 2, 3]>")
            }

            it("Given a non-empty list with a null, " +
                    "test should fail") {
                Assertions.assertThatThrownBy {
                    assert(listOf(null)).isNullOrEmpty()
                }.hasMessage("expected to be null or empty but was:<[null]>")
            }

            it("Given two non-empty non-null lists, " +
                    "test should fail with only one error message per failed assertion") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assert(listOf(1, 2, 3)).isNullOrEmpty()
                        assert(listOf(43, true, "awesome!")).isNullOrEmpty()
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                        + "- expected to be null or empty but was:<[1, 2, 3]>\n"
                        + "- expected to be null or empty but was:<[43, true, \"awesome!\"]>")
            }

            it("Given one empty list and two non-empty non-null lists, " +
                    "test should fail only for the non-empty non-null lists") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assert(listOf(1, 2, 3)).isNullOrEmpty()
                        assert(listOf(43, true, "awesome!")).isNullOrEmpty()
                        assert(emptyList<Any?>()).isNullOrEmpty()
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                        + "- expected to be null or empty but was:<[1, 2, 3]>\n"
                        + "- expected to be null or empty but was:<[43, true, \"awesome!\"]>")
            }
        }

        on("hasSize()") {
            it("Given a list that has 4 Ints, " +
                    "test should pass") {
                assert(listOf(1, 2, 3, 4)).hasSize(4)
            }

            it("Given an empty list, " +
                    "test should pass") {
                assert(emptyList<Any?>()).hasSize(0)
            }

            it("Given a list with one Null object, " +
                    "test should pass") {
                assert(listOf(null)).hasSize(1)
            }

            it("Given a list with 4 mixed type elements, " +
                    "test should pass") {
                assert(listOf(1, 1.09, "awesome!", true)).hasSize(4)
            }

            it("Given a list of 3 Ints, " +
                    "test should fail when expecting 4 elements") {
                Assertions.assertThatThrownBy {
                    assert(listOf(1, 2, 3)).hasSize(4)
                }.hasMessage("expected [size]:<[4]> but was:<[3]> ([1, 2, 3])")
            }

            it("Given an empty list, " +
                    "test should fail when expecting more than 0 elements") {
                Assertions.assertThatThrownBy {
                    assert(emptyList<Any?>()).hasSize(1)
                }.hasMessage("expected [size]:<[1]> but was:<[0]> ([])")
            }

            it("Given a list of 1 null, " +
                    "test should fail when expecting 0 elements") {
                Assertions.assertThatThrownBy {
                    assert(listOf(null)).hasSize(0)
                }.hasMessage("expected [size]:<[0]> but was:<[1]> ([null])")
            }

            it("Given two lists expecting incorrect size for each, " +
                    "test should fail with only one error message per failed assertion") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assert(listOf(1, 2, 3)).hasSize(4)
                        assert(listOf(43, true, "awesome!")).hasSize(1)
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                        + "- expected [size]:<[4]> but was:<[3]> ([1, 2, 3])\n"
                        + "- expected [size]:<[1]> but was:<[3]> ([43, true, \"awesome!\"])")
            }

            it("Given one list expecting the correct size and two lists expecting incorrect size for each, " +
                    "test should fail with only one error message per failed assertion") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assert(listOf(1, 2, 3)).hasSize(4)
                        assert(listOf("this", "test")).hasSize(2)
                        assert(listOf(43, true, "awesome!")).hasSize(1)
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                        + "- expected [size]:<[4]> but was:<[3]> ([1, 2, 3])\n"
                        + "- expected [size]:<[1]> but was:<[3]> ([43, true, \"awesome!\"])")
            }
        }

        on("hasSameSizeAs()") {
            it("Given two lists with 4 Ints each, " +
                    "test should pass") {
                assert(listOf(1, 2, 3, 4)).hasSameSizeAs(listOf(43, 2, 3, 3))
            }

            it("Given two empty lists, " +
                    "test should pass") {
                assert(emptyList<Any?>()).hasSameSizeAs(emptyList<Any?>())
            }

            it("Given two lists each with one Null, " +
                    "test should pass") {
                assert(listOf(null)).hasSameSizeAs(listOf(null))
            }

            it("Given two lists each with 4 elements of mixed types, " +
                    "test should pass") {
                assert(listOf(1, 1.09, "awesome!", true)).hasSameSizeAs(listOf(1.09, "whoa!", false, true))
            }

            it("Given a list with 3 elements and a list with 1 element, " +
                    "test should fail") {
                Assertions.assertThatThrownBy {
                    assert(listOf(1, 2, 3)).hasSameSizeAs(listOf(43))
                }.hasMessage("expected to have same size as:<[43]> (1) but was size:(3)")
            }

            it("Given an empty list and a list with 1 element, " +
                    "test should fail") {
                Assertions.assertThatThrownBy {
                    assert(emptyList<Any?>()).hasSameSizeAs(listOf(43))
                }.hasMessage("expected to have same size as:<[43]> (1) but was size:(0)")
            }

            it("Given a list with 1 null element and a list with 2 mixed type elements, " +
                    "test should fail") {
                Assertions.assertThatThrownBy {
                    assert(listOf(null)).hasSameSizeAs(listOf(43, "whoa!"))
                }.hasMessage("expected to have same size as:<[43, \"whoa!\"]> (2) but was size:(1)")
            }

            it("Given two failing assertions, " +
                    "test should fail with only one error message per failed assertion") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assert(listOf(1, 2, 3)).hasSameSizeAs(listOf(1, 2, 3, 4))
                        assert(listOf(43, true, "awesome!")).hasSameSizeAs(listOf(true, true))
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                        + "- expected to have same size as:<[1, 2, 3, 4]> (4) but was size:(3)\n"
                        + "- expected to have same size as:<[true, true]> (2) but was size:(3)")
            }

            it("Given one passing and two failing assertions, " +
                    "test should fail with only one error message per failed assertion") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assert(listOf(1, 2, 3)).hasSameSizeAs(listOf(1, 2, 3, 4))
                        assert(listOf(1, 2, 3, 4)).hasSameSizeAs(listOf("this", "test", "passes", true))
                        assert(listOf(43, true, "awesome!")).hasSameSizeAs(listOf(true, true))
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                        + "- expected to have same size as:<[1, 2, 3, 4]> (4) but was size:(3)\n"
                        + "- expected to have same size as:<[true, true]> (2) but was size:(3)")
            }
        }

        on("containsNone()") {
            it("Given a list with more elements than the passed in number of elements expected, " +
                    "test should pass if the list does not contain any of the elements that are expected") {
                assert(listOf(1, 2, 3, 4)).containsNone(5, 6, 7)
            }

            it("Given a list with less elements than the passed in number of elements expected, " +
                    "test should pass if the list does not contain any of the elements that are expected") {
                assert(listOf(1, 2, 3)).containsNone(4, 5, 6, 7)
            }

            it("Given an empty list, " +
                    "test should pass when expecting it to contain anything") {
                assert(emptyList<Any?>()).containsNone(4)
            }

            it("Given a list with some number of elements, " +
                    "test should pass when expecting nothing") {
                assert(listOf(3, 4)).containsNone()
            }

            it("Given a list with multiple types, " +
                    "test should pass when expecting none of the elements that are being checked for") {
                assert(listOf(1, 1.09, "awesome!", true)).containsNone(43, 1.43, "awesome")
            }

            it("Given a list with more elements passed in than the number of elements expected, " +
                    "test should fail if the list contains any of the elements that are expected") {
                Assertions.assertThatThrownBy {
                    assert(listOf(1, 2, 3)).containsNone(4, 5, 6, 1)
                }.hasMessage("expected to contain none of:<[4, 5, 6, 1]> but was:<[1, 2, 3]>")
            }

            it("Given a list with less elements passed in than the number of elements expected, " +
                    "test should fail if the list contains any of the elements that are expected") {
                Assertions.assertThatThrownBy {
                    assert(listOf(1, 2, 3, 4)).containsNone(8, 0, 4)
                }.hasMessage("expected to contain none of:<[8, 0, 4]> but was:<[1, 2, 3, 4]>")
            }

            it("Given a list of multiple types containing more elements passed in than the number of elements expected, " +
                    "test should fail if the list contains any of the elements that are expected") {
                Assertions.assertThatThrownBy {
                    assert(listOf(1, 1.09, "awesome!", true)).containsNone(true, 43, "potato")
                }.hasMessage("expected to contain none of:<[true, 43, \"potato\"]> but was:<[1, 1.09, \"awesome!\", true]>")
            }

            it("Given multiple assertions which should fail, " +
                    "test should fail with only one error message per failed assertion") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assert(listOf(1, 2, 3)).containsNone(5, 6, 7, 1)
                        assert(listOf("this", "is", "awesome!")).containsNone(true, 4, "awesome!")
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                        + "- expected to contain none of:<[5, 6, 7, 1]> but was:<[1, 2, 3]>\n"
                        + "- expected to contain none of:<[true, 4, \"awesome!\"]> but was:<[\"this\", \"is\", \"awesome!\"]>")
            }

            it("Given multiple assertions which should fail and some that should pass, " +
                    "test should fail with only one error message per failed assertion") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assert(listOf(1, 2, 3)).containsNone(5, 6, 7, 1) // Should fail
                        assert(listOf(1, 2, 3)).containsNone(4, 5, 6, 7) // Should pass
                        assert(listOf("this", "is", "awesome!")).containsNone(true, 4, "awesome!") // Should fail
                        assert(listOf(1, 2, 3, 4)).containsNone(5, 6, 7) // Should pass
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                        + "- expected to contain none of:<[5, 6, 7, 1]> but was:<[1, 2, 3]>\n"
                        + "- expected to contain none of:<[true, 4, \"awesome!\"]> but was:<[\"this\", \"is\", \"awesome!\"]>")
            }
        }

        on("containsAll()") {
            it("Given a list of multiple elements, " +
                    "test should pass when containing all elements expected regardless of order") {
                assert(listOf(1, 2, 3)).containsAll(3, 2, 1)
            }

            it("Given an empty list, " +
                    "test should pass when expecting nothing") {
                assert(emptyList<Any?>()).containsAll()
            }

            it("Given a list of multiple types of elements, " +
                    "test should pass when containing all elements expected regardless of order") {
                assert(listOf(1, 1.09, "awesome!", true)).containsAll(1, 1.09, "awesome!", true)
            }

            it("Given a list of multiple elements with more elements given than expected, " +
                    "test should fail when not containing all elements expected regardless of order") {
                Assertions.assertThatThrownBy {
                    assert(listOf(1, 2, 3)).containsAll(4, 3, 1, 2)
                }.hasMessage("expected to contain all:<[4, 3, 1, 2]> but was:<[1, 2, 3]>")
            }

            it("Given a list of multiple elements with less elements given than expected, " +
                    "test should fail when not containing all elements expected regardless of order") {
                Assertions.assertThatThrownBy {
                    assert(listOf(1, 2, 4, 5)).containsAll(2, 1, 3)
                }.hasMessage("expected to contain all:<[2, 1, 3]> but was:<[1, 2, 4, 5]>")
            }

            it("Given an empty list, " +
                    "test should fail when expecting anything") {
                Assertions.assertThatThrownBy {
                    assert(emptyList<Any?>()).containsAll(1, 2, 3)
                }.hasMessage("expected to contain all:<[1, 2, 3]> but was:<[]>")
            }

            it("Given a list of multiple types, " +
                    "test should fail when not containing all elements expected regardless of order") {
                Assertions.assertThatThrownBy {
                    assert(listOf(1, "is", "awesome!")).containsAll("this", 4, "awesome!")
                }.hasMessage("expected to contain all:<[\"this\", 4, \"awesome!\"]> but was:<[1, \"is\", \"awesome!\"]>")
            }

            it("Given multiple assertions which should fail, " +
                    "test should fail with only one error message per failed assertion") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assert(listOf(1, 2, 3)).containsAll(5, 6, 7)
                        assert(listOf(1, "is", "awesome!")).containsAll("this", 4, "awesome!")
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                        + "- expected to contain all:<[5, 6, 7]> but was:<[1, 2, 3]>\n"
                        + "- expected to contain all:<[\"this\", 4, \"awesome!\"]> but was:<[1, \"is\", \"awesome!\"]>")
            }

            it("Given multiple assertions which should fail and some that should pass, " +
                    "test should fail with only one error message per failed assertion") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assert(listOf(1, 2, 3)).containsAll(3, 2, 1) // should pass
                        assert(listOf(1, 2, 3)).containsAll(5, 6, 7) // should fail
                        assert(listOf("this", "is", "awesome!")).containsAll("this", 4, "awesome!") // should fail
                        assert(listOf(1, 1.09, "awesome!", true)).containsAll(1, 1.09, "awesome!", true) // should pass
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                        + "- expected to contain all:<[5, 6, 7]> but was:<[1, 2, 3]>\n"
                        + "- expected to contain all:<[\"this\", 4, \"awesome!\"]> but was:<[\"this\", \"is\", \"awesome!\"]>")
            }
        }

        on("containsExactly()") {
            it("Given a list of multiple elements, " +
                    "test should pass when containing all elements expected exactly in order") {
                assert(listOf(1, 2, 3)).containsExactly(1, 2, 3)
            }

            it("Given an empty list, " +
                    "test should pass when expecting nothing") {
                assert(emptyList<Any?>()).containsExactly()
            }

            it("Given a list of multiple types of elements, " +
                    "test should pass when containing all elements expected exactly in order") {
                assert(listOf(1, 1.09, "awesome!", true)).containsExactly(1, 1.09, "awesome!", true)
            }

            it("Given a list of multiple elements with less elements given than expected, " +
                    "test should fail when not containing all elements expected exactly in order") {
                Assertions.assertThatThrownBy {
                    assert(listOf(1, 2, 3)).containsExactly(1, 2, 3, 4)
                }.hasMessage("expected to contain exactly:<[1, 2, 3, 4]> but was:<[1, 2, 3]>" +
                                " some elements were not found:<[4]>")
            }

            it("Given a list of multiple elements with more elements given than expected, " +
                    "test should fail when not containing all elements expected exactly in order") {
                Assertions.assertThatThrownBy {
                    assert(listOf(1, 2, 3, 4)).containsExactly(1, 2, 3)
                }.hasMessage("expected to contain exactly:<[1, 2, 3]> but was:<[1, 2, 3, 4]>" +
                                " some elements were not expected:<[4]>")
            }

            it("Given an empty list, " +
                    "test should fail when expecting anything") {
                Assertions.assertThatThrownBy {
                    assert(emptyList<Any?>()).containsExactly(1, 2, 3)
                }.hasMessage("expected to contain exactly:<[1, 2, 3]> but was:<[]>" +
                                " some elements were not found:<[1, 2, 3]>")
            }

            it("Given a list of multiple types, " +
                    "test should fail when not containing all elements expected exactly in order") {
                Assertions.assertThatThrownBy {
                    assert(listOf(1, "is", "awesome!")).containsExactly("this", 4, "awesome!")
                }.hasMessage("expected to contain exactly:<[\"this\", 4, \"awesome!\"]> but was:<[1, \"is\", \"awesome!\"]>" +
                                " some elements were not found:<[\"this\", 4]>" +
                                " some elements were not expected:<[1, \"is\"]>")
            }

            it("Given multiple assertions which should fail, " +
                    "test should fail with only one error message per failed assertion") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assert(listOf(1, 2, 3)).containsExactly(5, 6, 7) // should fail
                        assert(listOf(1, "is", "awesome!")).containsExactly("this", 4, "awesome!") // should fail
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                        + "- expected to contain exactly:<[5, 6, 7]> but was:<[1, 2, 3]>"
                        + " some elements were not found:<[5, 6, 7]>"
                        + " some elements were not expected:<[1, 2, 3]>\n"
                        + "- expected to contain exactly:<[\"this\", 4, \"awesome!\"]> but was:<[1, \"is\", \"awesome!\"]>"
                        + " some elements were not found:<[\"this\", 4]>"
                        + " some elements were not expected:<[1, \"is\"]>")
            }

            it("Given multiple assertions which should fail and some that should pass, " +
                    "test should fail with only one error message per failed assertion") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assert(listOf(1, 2, 3)).containsExactly(1, 2, 3) // should pass
                        assert(listOf(1, 2, 3)).containsExactly(5, 6, 7) // should fail
                        assert(emptyList<Any?>()).containsExactly() // should pass
                        assert(listOf(1, "is", "awesome!")).containsExactly("this", 4, "awesome!") // should fail
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                                + "- expected to contain exactly:<[5, 6, 7]> but was:<[1, 2, 3]>"
                                + " some elements were not found:<[5, 6, 7]>"
                                + " some elements were not expected:<[1, 2, 3]>\n"
                                + "- expected to contain exactly:<[\"this\", 4, \"awesome!\"]> but was:<[1, \"is\", \"awesome!\"]>"
                                + " some elements were not found:<[\"this\", 4]>"
                                + " some elements were not expected:<[1, \"is\"]>")
            }

            it("Given the same element multiple times, difference reporting should still work") {
                Assertions.assertThatThrownBy {
                    assert(listOf(1, 1, 3)).containsExactly(1, 3, 3)
                }.hasMessage("expected to contain exactly:<[1, 3, 3]> but was:<[1, 1, 3]>"
                                + " some elements were not found:<[3]>"
                                + " some elements were not expected:<[1]>")
            }


            it("Given the same elements in a different order, difference reporting should still work") {
                Assertions.assertThatThrownBy {
                    assert(listOf(1, 2, 1, 3)).containsExactly(1, 1, 2, 3)
                }.hasMessage("expected to contain exactly:<[1, 1, 2, 3]> but was:<[1, 2, 1, 3]>"
                                + " first difference at index 1"
                                + " expected:<1> but was:<2>")
            }

        }
    }
})
