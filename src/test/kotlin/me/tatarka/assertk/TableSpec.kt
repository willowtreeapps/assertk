package me.tatarka.assertk

import me.tatarka.assertk.assertions.isEqualTo
import me.tatarka.assertk.assertions.isPositive
import org.assertj.core.api.Assertions
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*

class TableSpec : Spek({
    given("a table") {
        on("empty") {
            it("should do nothing") {
                table {

                }
            }
        }

        on("one row with one value") {
            it("should run once and pass a successful test") {
                var invokeCount = 0
                table {
                    val a = row("a", 1)

                    assert {
                        that(a()).isEqualTo(1)
                        invokeCount += 1
                    }
                }

                Assertions.assertThat(invokeCount).isEqualTo(1)
            }

            it("should fail if you try to access the row value outside the run block") {
                Assertions.assertThatThrownBy {
                    table {
                        val a = row("a", 1)
                        a()
                    }
                }.hasMessage("cannot access row value outside run block")
            }

            it("should fail showing the failing assertion") {
                Assertions.assertThatThrownBy {
                    table {
                        val a = row("a", 1)

                        assert {
                            that(a()).isEqualTo(2)
                        }
                    }
                }.hasMessage("The following assertion failed:\non row:(a=<1>)\n- expected:<[2]> but was:<[1]>")
            }
        }

        on("one row with two values") {
            it("should run twice and pass a successful test") {
                var invokeCount = 0
                table {
                    val a = row("a", 1, 2)

                    assert {
                        that(a()).isPositive()
                        invokeCount += 1
                    }
                }

                Assertions.assertThat(invokeCount).isEqualTo(2)
            }

            it("should fail showing the failing assertions") {
                Assertions.assertThatThrownBy {
                    table {
                        val a = row("a", 1, 2)

                        assert {
                            that(a()).isEqualTo(3)
                        }
                    }
                }.hasMessage("The following 2 assertions failed:\non row:(a=<1>)\n- expected:<[3]> but was:<[1]>\n\non row:(a=<2>)\n- expected:<[3]> but was:<[2]>")
            }
        }
    }
})

