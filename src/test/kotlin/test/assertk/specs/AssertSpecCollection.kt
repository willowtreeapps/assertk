package test.assertk.specs

import assertk.assertAll
import assertk.assertions.*
import org.assertj.core.api.Assertions
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

class AssertSpecCollection : Spek({

    given("a collection") {

        on("hasSameSizeAs()") {
            it("should pass all successful tests") {
                assertk.assert(listOf(1, 2, 3, 4)).hasSameSizeAs(listOf(43, 2, 3, 3))
                assertk.assert(emptyList<Any?>()).hasSameSizeAs(emptyList<Any?>())
                assertk.assert(listOf(null)).hasSameSizeAs(listOf(null))
                assertk.assert(listOf(1, 1.09, "awesome!", true)).hasSameSizeAs(listOf(1.09, "whoa!", false, true))
            }

            it("should fail all unsuccessful tests") {
                Assertions.assertThatThrownBy {
                    assertk.assert(listOf(1, 2, 3)).hasSameSizeAs(listOf(43))
                }.hasMessage("expected to have same size as:<[43]> (1) but was size:(3)")

                Assertions.assertThatThrownBy {
                    assertk.assert(emptyList<Any?>()).hasSameSizeAs(listOf(43))
                }.hasMessage("expected to have same size as:<[43]> (1) but was size:(0)")

                Assertions.assertThatThrownBy {
                    assertk.assert(listOf(null)).hasSameSizeAs(listOf(43, "whoa!"))
                }.hasMessage("expected to have same size as:<[43, whoa!]> (2) but was size:(1)")
            }

            it("should fail an unsuccessful test with only one error message per assertion") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assertk.assert(listOf(1, 2, 3)).hasSameSizeAs(listOf(1, 2, 3, 4))
                        assertk.assert(listOf(43, true, "awesome!")).hasSameSizeAs(listOf(true, true))
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                        + "- expected to have same size as:<[1, 2, 3, 4]> (4) but was size:(3)\n"
                        + "- expected to have same size as:<[true, true]> (2) but was size:(3)")
            }
        }

        on("contains()") {
            it("should pass all successful tests") {
                assertk.assert(listOf(1, 2, 3, 4)).contains(3)
                assertk.assert(listOf(null)).contains(null)
                assertk.assert(listOf(1, 1.09, "awesome!", true)).contains(1.09)
            }

            it("should fail all unsuccessful tests") {
                Assertions.assertThatThrownBy {
                    assertk.assert(listOf(1, 2, 3)).contains(43)
                }.hasMessage("expected to contain:<43> but was:<[1, 2, 3]>")

                Assertions.assertThatThrownBy {
                    assertk.assert(emptyList<Any?>()).contains(null)
                }.hasMessage("expected to contain:<null> but was:<[]>")

                Assertions.assertThatThrownBy {
                    assertk.assert(listOf(1, 1.09, "awesome!", true)).contains(43)
                }.hasMessage("expected to contain:<43> but was:<[1, 1.09, awesome!, true]>")
            }

            it("should fail an unsuccessful test with only one error message per assertion") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assertk.assert(listOf(1, 2, 3)).contains(43)
                        assertk.assert(listOf(43, true, "awesome!")).contains(false)
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                        + "- expected to contain:<43> but was:<[1, 2, 3]>\n"
                        + "- expected to contain:<false> but was:<[43, true, awesome!]>")
            }
        }

        on("doesNotContain()") {
            it("should pass all successful tests") {
                assertk.assert(listOf(1, 2, 3, 4)).doesNotContain(43)
                assertk.assert(emptyList<Any?>()).doesNotContain(4)
                assertk.assert(listOf(1, 1.09, "awesome!", true)).doesNotContain(43)
            }

            it("should fail all unsuccessful tests") {
                Assertions.assertThatThrownBy {
                    assertk.assert(listOf(1, 2, 3)).doesNotContain(2)
                }.hasMessage("expected to not contain:<2> but was:<[1, 2, 3]>")

                Assertions.assertThatThrownBy {
                    assertk.assert(listOf(1, 1.09, "awesome!", true)).doesNotContain(1.09)
                }.hasMessage("expected to not contain:<1.09> but was:<[1, 1.09, awesome!, true]>")
            }

            it("should fail an unsuccessful test with only one error message per assertion") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assertk.assert(listOf(1, 2, 3)).doesNotContain(3)
                        assertk.assert(listOf(43, true, "awesome!")).doesNotContain(true)
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                        + "- expected to not contain:<3> but was:<[1, 2, 3]>\n"
                        + "- expected to not contain:<true> but was:<[43, true, awesome!]>")
            }
        }

        on("containsNone()") {
            it("should pass all successful tests") {
                assertk.assert(listOf(1, 2, 3, 4)).containsNone(5, 6, 7)
                assertk.assert(listOf(1, 2, 3)).containsNone(4, 5, 6, 7)
                assertk.assert(emptyList<Any?>()).containsNone(4)
                assertk.assert(listOf(3, 4)).containsNone()
                assertk.assert(listOf(1, 1.09, "awesome!", true)).containsNone(43, 1.43, "awesome")
            }

            it("should fail all unsuccessful tests") {
                Assertions.assertThatThrownBy {
                    assertk.assert(listOf(1, 2, 3)).containsNone(4, 5, 6, 1)
                }.hasMessage("expected to contain none of:<[4, 5, 6, 1]> but was:<[1, 2, 3]>")

                Assertions.assertThatThrownBy {
                    assertk.assert(listOf(1, 2, 3, 4)).containsNone(8, 0, 4)
                }.hasMessage("expected to contain none of:<[8, 0, 4]> but was:<[1, 2, 3, 4]>")

                Assertions.assertThatThrownBy {
                    assertk.assert(listOf(1, 1.09, "awesome!", true)).containsNone(true, 43, "potato")
                }.hasMessage("expected to contain none of:<[true, 43, \"potato\"]> but was:<[1, 1.09, awesome!, true]>")
            }

            it("should fail an unsuccessful test with only one error message per assertion") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assertk.assert(listOf(1, 2, 3)).containsNone(5, 6, 7, 1)
                        assertk.assert(listOf("this", "is", "awesome!")).containsNone(true, 4, "awesome!")
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                        + "- expected to contain none of:<[5, 6, 7, 1]> but was:<[1, 2, 3]>\n"
                        + "- expected to contain none of:<[true, 4, \"awesome!\"]> but was:<[this, is, awesome!]>")
            }
        }

        on("containsAll()") {
            it("should pass all successful tests") {
                assertk.assert(listOf(1, 2, 3)).containsAll(3, 2, 1)
                assertk.assert(emptyList<Any?>()).containsAll()
                assertk.assert(listOf(1, 1.09, "awesome!", true)).containsAll(1, 1.09, "awesome!", true)
            }

            it("should fail all unsuccessful tests") {
                Assertions.assertThatThrownBy {
                    assertk.assert(listOf(1, 2, 3)).containsAll(4, 3, 1, 2)
                }.hasMessage("expected to contain all:<[4, 3, 1, 2]> but was:<[1, 2, 3]>")

                Assertions.assertThatThrownBy {
                    assertk.assert(listOf(1, 2, 4, 5)).containsAll(2, 1, 3)
                }.hasMessage("expected to contain all:<[2, 1, 3]> but was:<[1, 2, 4, 5]>")

                Assertions.assertThatThrownBy {
                    assertk.assert(emptyList<Any?>()).containsAll(1, 2, 3)
                }.hasMessage("expected to contain all:<[1, 2, 3]> but was:<[]>")

                Assertions.assertThatThrownBy {
                    assertk.assert(listOf("this", "is", "awesome!")).containsAll("this", 4, "awesome!")
                }.hasMessage("expected to contain all:<[\"this\", 4, \"awesome!\"]> but was:<[this, is, awesome!]>")
            }

            it("should fail an unsuccessful test with only one error message per assertion") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assertk.assert(listOf(1, 2, 3)).containsAll(5, 6, 7)
                        assertk.assert(listOf("this", "is", "awesome!")).containsAll("this", 4, "awesome!")
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                        + "- expected to contain all:<[5, 6, 7]> but was:<[1, 2, 3]>\n"
                        + "- expected to contain all:<[\"this\", 4, \"awesome!\"]> but was:<[this, is, awesome!]>")
            }
        }

        on("containsExactly()") {
            it("should pass all successful tests") {
                assertk.assert(listOf(1, 2, 3)).containsExactly(1, 2, 3)
                assertk.assert(emptyList<Any?>()).containsExactly()
                assertk.assert(listOf(1, 1.09, "awesome!", true)).containsExactly(1, 1.09, "awesome!", true)
            }

            it("should fail all unsuccessful tests") {
                Assertions.assertThatThrownBy {
                    assertk.assert(listOf(1, 2, 3)).containsExactly(1, 2, 3, 4)
                }.hasMessage("expected to contain exactly:<[1, 2, 3, 4]> but was:<[1, 2, 3]>")

                Assertions.assertThatThrownBy {
                    assertk.assert(listOf(1, 2, 3, 4)).containsExactly(1, 2, 3)
                }.hasMessage("expected to contain exactly:<[1, 2, 3]> but was:<[1, 2, 3, 4]>")

                Assertions.assertThatThrownBy {
                    assertk.assert(emptyList<Any?>()).containsExactly(1, 2, 3)
                }.hasMessage("expected to contain exactly:<[1, 2, 3]> but was:<[]>")

                Assertions.assertThatThrownBy {
                    assertk.assert(listOf("this", "is", "awesome!")).containsExactly("this", 4, "awesome!")
                }.hasMessage("expected to contain exactly:<[\"this\", 4, \"awesome!\"]> but was:<[this, is, awesome!]>")
            }

            it("should fail an unsuccessful test with only one error message per assertion") {
                Assertions.assertThatThrownBy {
                    assertAll {
                        assertk.assert(listOf(1, 2, 3)).containsExactly(5, 6, 7)
                        assertk.assert(listOf("this", "is", "awesome!")).containsExactly("this", 4, "awesome!")
                    }
                }.hasMessage("The following 2 assertions failed:\n"
                        + "- expected to contain exactly:<[5, 6, 7]> but was:<[1, 2, 3]>\n"
                        + "- expected to contain exactly:<[\"this\", 4, \"awesome!\"]> but was:<[this, is, awesome!]>")
            }
        }
    }
})
