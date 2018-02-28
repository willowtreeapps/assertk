package test.assertk.assertions

import assertk.assert
import assertk.assertAll
import assertk.assertions.*
import org.assertj.core.api.Assertions
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

class ArraySpec : Spek({

    given("an array") {

        on("isEmpty()") {
            it("Given an empty array, " +
                    "test should pass") {
                assert(emptyArray<Any?>()).isEmpty()
            }

            it("Given a non-empty array of Ints, " +
                    "test should fail") {
                Assertions.assertThatThrownBy {
                    assert(arrayOf(1, 2, 3, 4)).isEmpty()
                }.hasMessage("expected to be empty but was:<[1, 2, 3, 4]>")
            }

            it("Given a non-empty array of Nulls, " +
                    "test should fail") {
                Assertions.assertThatThrownBy {
                    assert(arrayOf<Any?>(null)).isEmpty()
                }.hasMessage("expected to be empty but was:<[null]>")
            }

            it("Given a non-empty array of mixed types, " +
                    "test should fail") {
                Assertions.assertThatThrownBy {
                    assert(arrayOf(1, 1.09, "awesome!", true)).isEmpty()
                }.hasMessage("expected to be empty but was:<[1, 1.09, \"awesome!\", true]>")
            }

            it("Given two non-empty arrays, " +
                    "test should fail with only one error message per failed assertion") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assert(arrayOf(1, 2, 3, 4)).isEmpty()
                        assert(arrayOf(1, 1.09, "awesome!", true)).isEmpty()
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                        + "- expected to be empty but was:<[1, 2, 3, 4]>\n"
                        + "- expected to be empty but was:<[1, 1.09, \"awesome!\", true]>")
            }

            it("Given one empty array and two non-empty arrays, " +
                    "test should fail for only the non-empty array with only one error message per failed assertion") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assert(emptyArray<Any?>()).isEmpty()
                        assert(arrayOf(1, 2, 3, 4)).isEmpty()
                        assert(arrayOf(1, 1.09, "awesome!", true)).isEmpty()
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                        + "- expected to be empty but was:<[1, 2, 3, 4]>\n"
                        + "- expected to be empty but was:<[1, 1.09, \"awesome!\", true]>")
            }
        }

        on("isNotEmpty()") {
            it("Given a array of Ints, " +
                    "test should pass") {
                assert(arrayOf(1, 2, 3, 4)).isNotEmpty()
            }

            it("Given a array filled with a null, " +
                    "test should pass") {
                assert(arrayOf<Any?>(null)).isNotEmpty()
            }

            it("Given a array of mixed types, " +
                    "test should pass") {
                assert(arrayOf(1, 1.09, "awesome!", true)).isNotEmpty()
            }

            it("Given an empty array passed directly in, " +
                    "test should fail") {
                Assertions.assertThatThrownBy {
                    assert(emptyArray<Any?>()).isNotEmpty()
                }.hasMessage("expected to not be empty")
            }

            it("Given a array that hasn't been populated, " +
                    "test should fail") {
                Assertions.assertThatThrownBy {
                    val anEmptyList: Array<Any?> = arrayOf()
                    assert(anEmptyList).isNotEmpty()
                }.hasMessage("expected to not be empty")
            }

            it("Given two empty arrays, " +
                    "test should fail with only one error message per failed assertion") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assert(emptyArray<Any?>()).isNotEmpty()
                        val anEmptyList: Array<Any?> = arrayOf()
                        assert(anEmptyList).isNotEmpty()
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                        + "- expected to not be empty\n"
                        + "- expected to not be empty")
            }

            it("Given one empty array and two empty arrays, " +
                    "test should fail for only for the empty arrays with only one error message per failed assertion") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assert(emptyArray<Any?>()).isNotEmpty()
                        val anEmptyList: Array<Any?> = arrayOf()
                        assert(arrayOf(1, 2, 3)).isNotEmpty()
                        assert(anEmptyList).isNotEmpty()
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                        + "- expected to not be empty\n"
                        + "- expected to not be empty")
            }
        }

        on("isNullOrEmpty()") {
            it("Given a forced null arrays, " +
                    "test should pass") {
                // Need to force a null here
                val nullList: Array<Any?>? = null
                assert(nullList).isNullOrEmpty()
            }

            it("Given an empty arrays, " +
                    "test should pass") {
                assert(emptyArray<Any?>()).isEmpty()
            }

            it("Given a non-empty array of Ints, " +
                    "test should fail") {
                Assertions.assertThatThrownBy {
                    assert(arrayOf(1, 2, 3)).isEmpty()
                }.hasMessage("expected to be empty but was:<[1, 2, 3]>")
            }

            it("Given a non-empty array with a null, " +
                    "test should fail") {
                Assertions.assertThatThrownBy {
                    assert(arrayOf<Any?>(null)).isEmpty()
                }.hasMessage("expected to be empty but was:<[null]>")
            }

            it("Given two non-empty non-null arrays, " +
                    "test should fail with only one error message per failed assertion") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assert(arrayOf(1, 2, 3)).isEmpty()
                        assert(arrayOf(43, true, "awesome!")).isEmpty()
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                        + "- expected to be empty but was:<[1, 2, 3]>\n"
                        + "- expected to be empty but was:<[43, true, \"awesome!\"]>")
            }

            it("Given one empty array and two non-empty non-null arrays, " +
                    "test should fail only for the non-empty non-null arrays") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assert(arrayOf(1, 2, 3)).isEmpty()
                        assert(arrayOf(43, true, "awesome!")).isEmpty()
                        assert(emptyArray<Any?>()).isEmpty()
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                        + "- expected to be empty but was:<[1, 2, 3]>\n"
                        + "- expected to be empty but was:<[43, true, \"awesome!\"]>")
            }
        }

        on("hasSize()") {
            it("Given a array that has 4 Ints, " +
                    "test should pass") {
                assert(arrayOf(1, 2, 3, 4)).hasSize(4)
            }

            it("Given an empty array, " +
                    "test should pass") {
                assert(emptyArray<Any?>()).hasSize(0)
            }

            it("Given a array with one Null object, " +
                    "test should pass") {
                assert(arrayOf<Any?>(null)).hasSize(1)
            }

            it("Given a array with 4 mixed type elements, " +
                    "test should pass") {
                assert(arrayOf(1, 1.09, "awesome!", true)).hasSize(4)
            }

            it("Given a array of 3 Ints, " +
                    "test should fail when expecting 4 elements") {
                Assertions.assertThatThrownBy {
                    assert(arrayOf(1, 2, 3)).hasSize(4)
                }.hasMessage("expected [size]:<[4]> but was:<[3]> ([1, 2, 3])")
            }

            it("Given an empty array, " +
                    "test should fail when expecting more than 0 elements") {
                Assertions.assertThatThrownBy {
                    assert(emptyArray<Any?>()).hasSize(1)
                }.hasMessage("expected [size]:<[1]> but was:<[0]> ([])")
            }

            it("Given a array of 1 null, " +
                    "test should fail when expecting 0 elements") {
                Assertions.assertThatThrownBy {
                    assert(arrayOf<Any?>(null)).hasSize(0)
                }.hasMessage("expected [size]:<[0]> but was:<[1]> ([null])")
            }

            it("Given two arrays expecting incorrect size for each, " +
                    "test should fail with only one error message per failed assertion") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assert(arrayOf(1, 2, 3)).hasSize(4)
                        assert(arrayOf(43, true, "awesome!")).hasSize(1)
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                        + "- expected [size]:<[4]> but was:<[3]> ([1, 2, 3])\n"
                        + "- expected [size]:<[1]> but was:<[3]> ([43, true, \"awesome!\"])")
            }

            it("Given one array expecting the correct size and two arrays expecting incorrect size for each, " +
                    "test should fail with only one error message per failed assertion") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assert(arrayOf(1, 2, 3)).hasSize(4)
                        assert(arrayOf("this", "test")).hasSize(2)
                        assert(arrayOf(43, true, "awesome!")).hasSize(1)
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                        + "- expected [size]:<[4]> but was:<[3]> ([1, 2, 3])\n"
                        + "- expected [size]:<[1]> but was:<[3]> ([43, true, \"awesome!\"])")
            }
        }

        on("contains()") {
            it("Given a array that contains multiple Ints, " +
                    "test should pass when expecting an Int that is actually contained in the array") {
                assert(arrayOf(1, 2, 3, 4)).contains(3)
            }

            it("Given a array which contains a null, " +
                    "test should pass when expecting the array to contain a null") {
                assert(arrayOf<Any?>(null)).contains(null)
            }

            it("Given a array that contains multiple types, " +
                    "test should pass when expecting an element that is actually contained in the array") {
                assert(arrayOf(1, 1.09, "awesome!", true)).contains(1.09)
            }

            it("Given a array that contains multiple Ints, " +
                    "test should fail when expecting an Int that is not contained in the array") {
                Assertions.assertThatThrownBy {
                    assert(arrayOf(1, 2, 3)).contains(43)
                }.hasMessage("expected to contain:<43> but was:<[1, 2, 3]>")
            }

            it("Given an empty array, " +
                    "test should fail when expecting anything to be contained within the array") {
                Assertions.assertThatThrownBy {
                    assert(emptyArray<Any?>()).contains(null)
                }.hasMessage("expected to contain:<null> but was:<[]>")
            }

            it("Given a array that contains multiple types, " +
                    "test should fail when expecting anything that is not contained in the array") {
                Assertions.assertThatThrownBy {
                    assert(arrayOf(1, 1.09, "awesome!", true)).contains(43)
                }.hasMessage("expected to contain:<43> but was:<[1, 1.09, \"awesome!\", true]>")
            }

            it("Given multiple assertions which should fail, " +
                    "test should fail with only one error message per failed assertion") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assert(arrayOf(1, 2, 3)).contains(43)
                        assert(arrayOf(43, true, "awesome!")).contains(false)
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                        + "- expected to contain:<43> but was:<[1, 2, 3]>\n"
                        + "- expected to contain:<false> but was:<[43, true, \"awesome!\"]>")
            }

            it("Given multiple assertions which should fail and some that should pass, " +
                    "test should fail with only one error message per failed assertion") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assert(arrayOf(1, "test", true)).contains(true)
                        assert(arrayOf(1, 2, 3)).contains(43)
                        assert(arrayOf(43, true, "awesome!")).contains(false)
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                        + "- expected to contain:<43> but was:<[1, 2, 3]>\n"
                        + "- expected to contain:<false> but was:<[43, true, \"awesome!\"]>")
            }
        }

        on("doesNotContain()") {
            it("Given a array of Ints, " +
                    "test should pass when expecting an Int that is not in the array") {
                assert(arrayOf(1, 2, 3, 4)).doesNotContain(43)
            }

            it("Given an empty array, " +
                    "test should pass when expecting anything to be contained within the array") {
                assert(emptyArray<Any?>()).doesNotContain(4)
            }

            it("Given a array with elements of multiple types, " +
                    "test should pass when expecting anything which is not contained within the array") {
                assert(arrayOf(1, 1.09, "awesome!", true)).doesNotContain(43)
            }

            it("Given a array with multiple elements of the same type, " +
                    "test should fail when expecting an element which is contained in the array") {
                Assertions.assertThatThrownBy {
                    assert(arrayOf(1, 2, 3)).doesNotContain(2)
                }.hasMessage("expected to not contain:<2> but was:<[1, 2, 3]>")
            }

            it("Given a array of multiple types, " +
                    "test should fail when expecting an element which is contained in the array") {
                Assertions.assertThatThrownBy {
                    assert(arrayOf(1, 1.09, "awesome!", true)).doesNotContain(1.09)
                }.hasMessage("expected to not contain:<1.09> but was:<[1, 1.09, \"awesome!\", true]>")
            }

            it("Given multiple assertions which should fail, " +
                    "test should fail with only one error message per failed assertion") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assert(arrayOf(1, 2, 3)).doesNotContain(3)
                        assert(arrayOf(43, true, "awesome!")).doesNotContain(true)
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                        + "- expected to not contain:<3> but was:<[1, 2, 3]>\n"
                        + "- expected to not contain:<true> but was:<[43, true, \"awesome!\"]>")
            }

            it("Given multiple assertions which should fail and some that should pass, " +
                    "test should fail with only one error message per failed assertion") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assert(arrayOf(1, 2, 3)).doesNotContain(3)
                        assert(arrayOf("this", "passes", true)).doesNotContain(false)
                        assert(arrayOf(43, true, "awesome!")).doesNotContain(true)
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                        + "- expected to not contain:<3> but was:<[1, 2, 3]>\n"
                        + "- expected to not contain:<true> but was:<[43, true, \"awesome!\"]>")
            }
        }

        on("containsAll()") {
            it("Given a array of multiple elements, " +
                    "test should pass when containing all elements expected regardless of order") {
                assert(arrayOf(1, 2, 3)).containsAll(3, 2, 1)
            }

            it("Given an empty array, " +
                    "test should pass when expecting nothing") {
                assert(emptyArray<Any?>()).containsAll()
            }

            it("Given a array of multiple types of elements, " +
                    "test should pass when containing all elements expected regardless of order") {
                assert(arrayOf(1, 1.09, "awesome!", true)).containsAll(1, 1.09, "awesome!", true)
            }

            it("Given a array of multiple elements with more elements given than expected, " +
                    "test should fail when not containing all elements expected regardless of order") {
                Assertions.assertThatThrownBy {
                    assert(arrayOf(1, 2, 3)).containsAll(4, 3, 1, 2)
                }.hasMessage("expected to contain all:<[4, 3, 1, 2]> but was:<[1, 2, 3]>")
            }

            it("Given a array of multiple elements with less elements given than expected, " +
                    "test should fail when not containing all elements expected regardless of order") {
                Assertions.assertThatThrownBy {
                    assert(arrayOf(1, 2, 4, 5)).containsAll(2, 1, 3)
                }.hasMessage("expected to contain all:<[2, 1, 3]> but was:<[1, 2, 4, 5]>")
            }

            it("Given an empty array, " +
                    "test should fail when expecting anything") {
                Assertions.assertThatThrownBy {
                    assert(emptyArray<Any?>()).containsAll(1, 2, 3)
                }.hasMessage("expected to contain all:<[1, 2, 3]> but was:<[]>")
            }

            it("Given a array of multiple types, " +
                    "test should fail when not containing all elements expected regardless of order") {
                Assertions.assertThatThrownBy {
                    assert(arrayOf(1, "is", "awesome!")).containsAll("this", 4, "awesome!")
                }.hasMessage("expected to contain all:<[\"this\", 4, \"awesome!\"]> but was:<[1, \"is\", \"awesome!\"]>")
            }

            it("Given multiple assertions which should fail, " +
                    "test should fail with only one error message per failed assertion") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assert(arrayOf(1, 2, 3)).containsAll(5, 6, 7)
                        assert(arrayOf(1, "is", "awesome!")).containsAll("this", 4, "awesome!")
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                        + "- expected to contain all:<[5, 6, 7]> but was:<[1, 2, 3]>\n"
                        + "- expected to contain all:<[\"this\", 4, \"awesome!\"]> but was:<[1, \"is\", \"awesome!\"]>")
            }

            it("Given multiple assertions which should fail and some that should pass, " +
                    "test should fail with only one error message per failed assertion") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assert(arrayOf(1, 2, 3)).containsAll(3, 2, 1) // should pass
                        assert(arrayOf(1, 2, 3)).containsAll(5, 6, 7) // should fail
                        assert(arrayOf("this", "is", "awesome!")).containsAll("this", 4, "awesome!") // should fail
                        assert(arrayOf(1, 1.09, "awesome!", true)).containsAll(1, 1.09, "awesome!", true) // should pass
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                        + "- expected to contain all:<[5, 6, 7]> but was:<[1, 2, 3]>\n"
                        + "- expected to contain all:<[\"this\", 4, \"awesome!\"]> but was:<[\"this\", \"is\", \"awesome!\"]>")
            }
        }

        on("containsExactly()") {
            it("Given a array of multiple elements, " +
                    "test should pass when containing all elements expected exactly in order") {
                assert(arrayOf(1, 2, 3)).containsExactly(1, 2, 3)
            }

            it("Given an empty array, " +
                    "test should pass when expecting nothing") {
                assert(emptyArray<Any?>()).containsExactly()
            }

            it("Given a array of multiple types of elements, " +
                    "test should pass when containing all elements expected exactly in order") {
                assert(arrayOf(1, 1.09, "awesome!", true)).containsExactly(1, 1.09, "awesome!", true)
            }

            it("Given a array of multiple elements with less elements given than expected, " +
                    "test should fail when not containing all elements expected exactly in order") {
                Assertions.assertThatThrownBy {
                    assert(arrayOf(1, 2, 3)).containsExactly(1, 2, 3, 4)
                }.hasMessage("expected to contain exactly:<[1, 2, 3, 4]> but was:<[1, 2, 3]>")
            }

            it("Given a array of multiple elements with more elements given than expected, " +
                    "test should fail when not containing all elements expected exactly in order") {
                Assertions.assertThatThrownBy {
                    assert(arrayOf(1, 2, 3, 4)).containsExactly(1, 2, 3)
                }.hasMessage("expected to contain exactly:<[1, 2, 3]> but was:<[1, 2, 3, 4]>")
            }

            it("Given an empty array, " +
                    "test should fail when expecting anything") {
                Assertions.assertThatThrownBy {
                    assert(emptyArray<Any?>()).containsExactly(1, 2, 3)
                }.hasMessage("expected to contain exactly:<[1, 2, 3]> but was:<[]>")
            }

            it("Given a array of multiple types, " +
                    "test should fail when not containing all elements expected exactly in order") {
                Assertions.assertThatThrownBy {
                    assert(arrayOf(1, "is", "awesome!")).containsExactly("this", 4, "awesome!")
                }.hasMessage("expected to contain exactly:<[\"this\", 4, \"awesome!\"]> but was:<[1, \"is\", \"awesome!\"]>")
            }

            it("Given multiple assertions which should fail, " +
                    "test should fail with only one error message per failed assertion") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assert(arrayOf(1, 2, 3)).containsExactly(5, 6, 7) // should fail
                        assert(arrayOf(1, "is", "awesome!")).containsExactly("this", 4, "awesome!") // should fail
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                        + "- expected to contain exactly:<[5, 6, 7]> but was:<[1, 2, 3]>\n"
                        + "- expected to contain exactly:<[\"this\", 4, \"awesome!\"]> but was:<[1, \"is\", \"awesome!\"]>")
            }

            it("Given multiple assertions which should fail and some that should pass, " +
                    "test should fail with only one error message per failed assertion") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assert(arrayOf(1, 2, 3)).containsExactly(1, 2, 3) // should pass
                        assert(arrayOf(1, 2, 3)).containsExactly(5, 6, 7) // should fail
                        assert(emptyArray<Any?>()).containsExactly() // should pass
                        assert(arrayOf(1, "is", "awesome!")).containsExactly("this", 4, "awesome!") // should fail
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                        + "- expected to contain exactly:<[5, 6, 7]> but was:<[1, 2, 3]>\n"
                        + "- expected to contain exactly:<[\"this\", 4, \"awesome!\"]> but was:<[1, \"is\", \"awesome!\"]>")
            }
        }

        on("each()") {
            it("Given an empty array, test should pass when expecting nothing") {
                assert(emptyArray<Any>()).each { }
            }

            it("Given a non empty array, test should pass when expecting nothing") {
                assert(arrayOf("one", "two")).each { }
            }

            it("Given an array of Strings, valid assertion should pass for each item") {
                assert(arrayOf("one", "two")).each {
                    it.length().isEqualTo(3)
                }
            }

            it("Given an array, invalid assertion should fail") {
                Assertions.assertThatThrownBy {
                    assert(arrayOf(1, 2, 3, 4)).each {
                        it.isLessThan(3)
                    }
                }.hasMessage("expected [[2]] to be less than:<3> but was:<3> ([1, 2, 3, 4])")
            }

            it("Given an array of multiple types, valid assertion should pass for each item") {
                assert(arrayOf("one", 2)).each {
                    it.kClass().isNotEqualTo(Boolean::class)
                }
            }
        }
    }
})