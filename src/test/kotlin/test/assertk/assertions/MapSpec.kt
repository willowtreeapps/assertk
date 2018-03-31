package test.assertk.assertions

import assertk.assert
import assertk.assertAll
import assertk.assertions.contains
import assertk.assertions.containsAll
import assertk.assertions.containsExactly
import assertk.assertions.containsNone
import assertk.assertions.doesNotContain
import assertk.assertions.hasSameSizeAs
import org.assertj.core.api.Assertions
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

class MapSpec : Spek({

    given("a map") {

        on("contains()") {
            it("Given a map that contains multiple pairs of Strings to Ints, " +
                    "test should pass when expecting a pair that is actually contained in the map") {
                assert(mapOf("one" to 1, "two" to 2, "three" to 3)).contains("one" to 1)
            }

            it("Given a map that contains multiple types, " +
                    "test should pass when expecting a pair that is actually contained in the map") {
                assert(mapOf<Any?, Any?>("one" to 1, 1.09 to "Something", "awesome!" to true)).contains("awesome!" to true)
            }

            it("Given a map which contains a null, " +
                    "test should pass when expecting the map to contain a null") {
                assert(mapOf<Any?, Any?>(null to null)).contains(null to null)
            }

            it("Given a map that contains multiple pairs of Strings to Ints, " +
                    "test should fail when expecting a pair that is not contained in the map") {
                Assertions.assertThatThrownBy {
                    assert(mapOf("one" to 1, "two" to 2, "three" to 3)).contains("zero" to 0)
                }.hasMessage("expected to contain:<{\"zero\"=0}> but was:<{\"one\"=1, \"two\"=2, \"three\"=3}>")
            }
        }

        on("doesNotContain()") {
            it("Given an empty map, " +
                    "test should pass when expecting anything to be contained within the map") {
                assert(emptyMap<Any?, Any?>()).doesNotContain("Something" to 99)
            }

            it("Given a map that contains multiple pairs of Strings to Ints, " +
                    "test should pass when expecting a pair that is not contained in the map") {
                assert(mapOf("one" to 1, "two" to 2, "three" to 3)).doesNotContain("zero" to 0)
            }

            it("Given a map that contains multiple pairs of Strings to Ints, " +
                    "test should fail when expecting a pair that is contained in the map") {
                Assertions.assertThatThrownBy {
                    assert(mapOf("one" to 1, "two" to 2, "three" to 3)).doesNotContain("one" to 1)
                }.hasMessage("expected to not contain:<{\"one\"=1}> but was:<{\"one\"=1, \"two\"=2, \"three\"=3}>")
            }
        }

        on("containsNone()") {
            it("Given an empty map, " +
                    "test should pass when expecting multiple elements to be contained within the map") {
                assert(emptyMap<Any?, Any?>()).containsNone("not" to 3, "in" to 2, "list" to 4)
            }

            it("Given a map that contains multiple pairs of Strings to Ints, " +
                    "test should pass when pairs that aren't not contained in the map") {
                assert(mapOf("one" to 1, "two" to 2, "three" to 3)).containsNone("zero" to 0, "negative one" to -1)
            }

            it("Given a map that contains multiple pairs of Strings to Ints, " +
                    "test should fail when expecting a pairs that are contained in the map") {
                Assertions.assertThatThrownBy {
                    assert(mapOf("one" to 1, "two" to 2, "three" to 3)).containsNone("one" to 1, "three" to 3)
                }.hasMessage("expected to not contain:<{\"one\"=1, \"three\"=3}> but was:<{\"one\"=1, \"two\"=2, \"three\"=3}>")
            }
        }

        on("containsExactly()") {
            it("Given an empty map, " +
                    "test should pas when expecting nothing") {
                assert(emptyMap<Any?, Any?>()).containsExactly()
            }

            it("Given a map of multiple elements, " +
                    "test should pass when containing all elements in the map") {
                assert(mapOf("one" to 1, "two" to 2, "three" to 3)).containsExactly("one" to 1, "two" to 2, "three" to 3)
            }

            it("Given a map of multiple elements, " +
                    "test should pass when containing all elements in the map") {
                assert(mapOf("one" to 1, "two" to 2, "three" to 3)).containsExactly("one" to 1, "two" to 2, "three" to 3)
            }

            it("Given an empty map, " +
                    "test should fail when expecting anything") {
                Assertions.assertThatThrownBy {
                    assert(emptyMap<Any?, Any?>()).containsExactly("one" to 1, "one" to 2)
                }.hasMessage("expected to contain exactly:<{\"one\"=2}> but was:<{}>")
            }

            it("Given a map of multiple elements, " +
                    "test should fail when maps are of different sizes") {
                Assertions.assertThatThrownBy {
                    assert(mapOf("one" to 1, "two" to 2, "three" to 3, "four" to 4))
                            .containsExactly("one" to 1, "two" to 2, "three" to 3)
                }.hasMessage("expected to contain exactly:<{\"one\"=1, \"two\"=2, \"three\"=3}> " +
                        "but was:<{\"one\"=1, \"two\"=2, \"three\"=3, \"four\"=4}>")
            }

            it("Given a map of multiple elements, " +
                    "test should not fail when elements are in different order") {
                assert(mapOf("one" to 1, "two" to 2, "three" to 3))
                        .containsExactly("two" to 2, "three" to 3, "one" to 1)
            }
        }


        on("containsAll()") {
            it("Given a map of multiple entries, " +
                    "test should pass when containing all entries expected regardless of order") {
                assertk.assert(mapOf(1 to 'i', 2 to 'j')).containsAll(2 to 'j', 1 to 'i')
            }

            it("Given an empty map, " +
                    "test should pass when expecting nothing") {
                assertk.assert(mapOf<Any?, Any?>()).containsAll()
            }

            it("Given a map of multiple entries with more entries given than expected, " +
                    "test should fail when not containing all entries expected regardless of order") {
                Assertions.assertThatThrownBy {
                    assertk.assert(mapOf(1 to 'i', 2 to 'j', 3 to 'k')).containsAll(4 to 'a', 1 to 'i')
                }.hasMessage("expected to contain:<{4='a', 1='i'}> but was:<{1='i', 2='j', 3='k'}>")
            }

            it("Given a map of multiple entries with less entries given than expected, " +
                    "test should fail when not containing all entries expected regardless of order") {
                Assertions.assertThatThrownBy {
                    assertk.assert(mapOf(1 to 'i', 3 to 'k')).containsAll(3 to 'k', 1 to 'i', 2 to 'j')
                }.hasMessage("expected to contain:<{3='k', 1='i', 2='j'}> but was:<{1='i', 3='k'}>")
            }

            it("Given an empty map, " +
                    "test should fail when expecting anything") {
                Assertions.assertThatThrownBy {
                    assertk.assert(emptyMap<Any?, Any?>()).containsAll(1 to 'x')
                }.hasMessage("expected to contain:<{1='x'}> but was:<{}>")
            }

            it("Given two failing assertions, " +
                    "test should fail with only one error message per failed assertion") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assertk.assert(mapOf(1 to 'x')).containsAll(1 to 'y')
                        assertk.assert(emptyMap<Any?, Any?>()).containsAll(1 to 0)
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                        + "- expected to contain:<{1='y'}> but was:<{1='x'}>\n"
                        + "- expected to contain:<{1=0}> but was:<{}>")
            }

            it("Given one passing and two failing assertions, " +
                    "test should fail with only one error message per failed assertion") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assertk.assert(mapOf(1 to "x")).containsAll(1 to "x")
                        assertk.assert(mapOf(1 to 'x')).containsAll(1 to 'y')
                        assertk.assert(emptyMap<Any?, Any?>()).containsAll(1 to 0)
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                        + "- expected to contain:<{1='y'}> but was:<{1='x'}>\n"
                        + "- expected to contain:<{1=0}> but was:<{}>")
            }
        }

        on("hasSameSizeAs()") {
            it("Given two maps with 2 entries each, " +
                    "test should pass") {
                assertk.assert(mapOf(1 to "x", 2 to "y")).hasSameSizeAs(mapOf(3 to "i", 4 to "j"))
            }

            it("Given two empty maps, " +
                    "test should pass") {
                assertk.assert(emptyMap<Any?, Any?>()).hasSameSizeAs(emptyMap<Any?, Any?>())
            }

            it("Given two maps both with 2 entries but different types, " +
                    "test should pass") {
                assertk.assert(mapOf(1 to "x", 2 to "y")).hasSameSizeAs(mapOf('a' to 9, 'b' to 7))
            }

            it("Given a map with 2 entries and a map with 1 entry, " +
                    "test should fail") {
                Assertions.assertThatThrownBy {
                    assertk.assert(mapOf(1 to 'x', 2 to 'y')).hasSameSizeAs(mapOf(1 to 'x'))
                }.hasMessage("expected to have same size as:<{1='x'}> (1) but was size:(2)")
            }

            it("Given an empty map and a map with 1 entry, " +
                    "test should fail") {
                Assertions.assertThatThrownBy {
                    assertk.assert(emptyMap<Any?, Any?>()).hasSameSizeAs(mapOf(1 to 'x'))
                }.hasMessage("expected to have same size as:<{1='x'}> (1) but was size:(0)")
            }

            it("Given two failing assertions, " +
                    "test should fail with only one error message per failed assertion") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assertk.assert(mapOf(1 to 'x', 2 to 'y')).hasSameSizeAs(mapOf(1 to 'x'))
                        assertk.assert(emptyMap<Any?, Any?>()).hasSameSizeAs(mapOf(1 to 'x'))
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                        + "- expected to have same size as:<{1='x'}> (1) but was size:(2)\n"
                        + "- expected to have same size as:<{1='x'}> (1) but was size:(0)")
            }

            it("Given one passing and two failing assertions, " +
                    "test should fail with only one error message per failed assertion") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assertk.assert(emptyMap<Any?, Any?>()).hasSameSizeAs(emptyMap<Any?, Any?>())
                        assertk.assert(mapOf(1 to 'x', 2 to 'y')).hasSameSizeAs(mapOf(1 to 'x'))
                        assertk.assert(emptyMap<Any?, Any?>()).hasSameSizeAs(mapOf(1 to 'x'))
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                        + "- expected to have same size as:<{1='x'}> (1) but was size:(2)\n"
                        + "- expected to have same size as:<{1='x'}> (1) but was size:(0)")
            }
        }
    }
})
