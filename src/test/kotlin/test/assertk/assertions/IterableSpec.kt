package test.assertk.assertions

import assertk.assert
import assertk.assertAll
import assertk.assertions.*
import org.assertj.core.api.Assertions
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

class IterableSpec : Spek({

    given("an iterable") {

        on("contains()") {
            it("Given a list that contains multiple Ints, " +
                    "test should pass when expecting an Int that is actually contained in the list") {
                assert(listOf(1, 2, 3, 4) as Iterable<Int>).contains(3)
            }

            it("Given a list which contains a null, " +
                    "test should pass when expecting the list to contain a null") {
                assert(listOf(null) as Iterable<Int?>).contains(null)
            }

            it("Given a list that contains multiple types, " +
                    "test should pass when expecting an element that is actually contained in the list") {
                assert(listOf(1, 1.09, "awesome!", true) as Iterable<Any>).contains(1.09)
            }

            it("Given a list that contains multiple Ints, " +
                    "test should fail when expecting an Int that is not contained in the list") {
                Assertions.assertThatThrownBy {
                    assert(listOf(1, 2, 3) as Iterable<Int>).contains(43)
                }.hasMessage("expected to contain:<43> but was:<[1, 2, 3]>")
            }

            it("Given an empty list, " +
                    "test should fail when expecting anything to be contained within the list") {
                Assertions.assertThatThrownBy {
                    assert(emptyList<Any?>() as Iterable<Any?>).contains(null)
                }.hasMessage("expected to contain:<null> but was:<[]>")
            }

            it("Given a list that contains multiple types, " +
                    "test should fail when expecting anything that is not contained in the list") {
                Assertions.assertThatThrownBy {
                    assert(listOf(1, 1.09, "awesome!", true) as Iterable<Any>).contains(43)
                }.hasMessage("expected to contain:<43> but was:<[1, 1.09, \"awesome!\", true]>")
            }

            it("Given multiple assertions which should fail, " +
                    "test should fail with only one error message per failed assertion") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assert(listOf(1, 2, 3) as Iterable<Int>).contains(43)
                        assert(listOf(43, true, "awesome!") as Iterable<Any>).contains(false)
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                        + "- expected to contain:<43> but was:<[1, 2, 3]>\n"
                        + "- expected to contain:<false> but was:<[43, true, \"awesome!\"]>")
            }

            it("Given multiple assertions which should fail and some that should pass, " +
                    "test should fail with only one error message per failed assertion") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assert(listOf(1, "test", true) as Iterable<Any>).contains(true)
                        assert(listOf(1, 2, 3) as Iterable<Int>).contains(43)
                        assert(listOf(43, true, "awesome!") as Iterable<Any>).contains(false)
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                        + "- expected to contain:<43> but was:<[1, 2, 3]>\n"
                        + "- expected to contain:<false> but was:<[43, true, \"awesome!\"]>")
            }
        }

        on("doesNotContain()") {
            it("Given a list of Ints, " +
                    "test should pass when expecting an Int that is not in the list") {
                assert(listOf(1, 2, 3, 4) as Iterable<Int>).doesNotContain(43)
            }

            it("Given an empty list, " +
                    "test should pass when expecting anything to be contained within the list") {
                assert(emptyList<Any?>() as Iterable<Any?>).doesNotContain(4)
            }

            it("Given a list with elements of multiple types, " +
                    "test should pass when expecting anything which is not contained within the list") {
                assert(listOf(1, 1.09, "awesome!", true) as Iterable<Any>).doesNotContain(43)
            }

            it("Given a list with multiple elements of the same type, " +
                    "test should fail when expecting an element which is contained in the list") {
                Assertions.assertThatThrownBy {
                    assert(listOf(1, 2, 3) as Iterable<Int>).doesNotContain(2)
                }.hasMessage("expected to not contain:<2> but was:<[1, 2, 3]>")
            }

            it("Given a list of multiple types, " +
                    "test should fail when expecting an element which is contained in the list") {
                Assertions.assertThatThrownBy {
                    assert(listOf(1, 1.09, "awesome!", true) as Iterable<Any?>).doesNotContain(1.09)
                }.hasMessage("expected to not contain:<1.09> but was:<[1, 1.09, \"awesome!\", true]>")
            }

            it("Given multiple assertions which should fail, " +
                    "test should fail with only one error message per failed assertion") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assert(listOf(1, 2, 3) as Iterable<Int>).doesNotContain(3)
                        assert(listOf(43, true, "awesome!") as Iterable<Any>).doesNotContain(true)
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                        + "- expected to not contain:<3> but was:<[1, 2, 3]>\n"
                        + "- expected to not contain:<true> but was:<[43, true, \"awesome!\"]>")
            }

            it("Given multiple assertions which should fail and some that should pass, " +
                    "test should fail with only one error message per failed assertion") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assert(listOf(1, 2, 3) as Iterable<Int>).doesNotContain(3)
                        assert(listOf("this", "passes", true) as Iterable<Any>).doesNotContain(false)
                        assert(listOf(43, true, "awesome!") as Iterable<Any>).doesNotContain(true)
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                        + "- expected to not contain:<3> but was:<[1, 2, 3]>\n"
                        + "- expected to not contain:<true> but was:<[43, true, \"awesome!\"]>")
            }
        }

        on("each()") {
            it("Given an empty list, test should pass") {
                assert(emptyList<String>() as Iterable<String>).each {
                    it.isEqualTo("test")
                }
            }

            it("Given a list with one item, test should pass for that item") {
                assert(listOf("test") as Iterable<String>).each {
                    it.isEqualTo("test")
                }
            }

            it("Given a list with one item, test should fail for that item") {
                Assertions.assertThatThrownBy {
                    assert(listOf("test") as Iterable<String>).each {
                        it.isEqualTo("wrong")
                    }
                }.hasMessage("expected [[0]]:<\"[wrong]\"> but was:<\"[test]\"> ([\"test\"])")
            }

            it("Give a list with many items, test should pass for all items") {
                assert(listOf("one", "two", "six") as Iterable<String>).each {
                    it.length().isEqualTo(3)
                }
            }

            it("Give a list with many items, test should fail for one of the items") {
                Assertions.assertThatThrownBy {
                    assert(listOf("one", "two", "three") as Iterable<String>).each {
                        it.length().isEqualTo(3)
                    }
                }.hasMessage("expected [[2].length]:<[3]> but was:<[5]> ([\"one\", \"two\", \"three\"])")
            }
        }
    }
})
