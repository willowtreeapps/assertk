package test.me.tatarka.assertk

import me.tatarka.assertk.assertions.isEqualTo
import me.tatarka.assertk.assertions.isPositive
import me.tatarka.assertk.tableOf
import org.assertj.core.api.Assertions
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

class TableSpec : Spek({
    given("a table") {

        on("one column with one row") {
            it("should run once and pass a successful test") {
                var invokeCount = 0
                tableOf("a")
                        .row(1)
                        .forAll { a ->
                            assert(a).isEqualTo(1)
                            invokeCount += 1
                        }

                Assertions.assertThat(invokeCount).isEqualTo(1)
            }

            it("should fail showing the failing assertion") {
                Assertions.assertThatThrownBy {
                    tableOf("a")
                            .row(1)
                            .forAll { a ->
                                assert(a).isEqualTo(2)
                            }
                }.hasMessage("The following assertion failed:\non row:(a=<1>)\n- expected:<[2]> but was:<[1]>")
            }
        }

        on("one column with two rows") {
            it("should run twice and pass a successful test") {
                var invokeCount = 0
                tableOf("a")
                        .row(1)
                        .row(2)
                        .forAll { a ->
                    assert(a).isPositive()
                    invokeCount += 1
                }

                Assertions.assertThat(invokeCount).isEqualTo(2)
            }

            it("should fail showing the failing assertions") {
                Assertions.assertThatThrownBy {
                    tableOf("a")
                            .row(1)
                            .row(2)
                            .forAll { a ->
                                assert(a).isEqualTo(3)
                            }
                }.hasMessage("The following 2 assertions failed:\non row:(a=<1>)\n- expected:<[3]> but was:<[1]>\n\non row:(a=<2>)\n- expected:<[3]> but was:<[2]>")
            }
        }

        on("two columns with one row") {
            it("should run once and pass a successful test") {
                var invokeCount = 0
                tableOf("a", "b")
                        .row(1, 1)
                        .forAll { a, b ->
                            assert(a).isEqualTo(b)
                            invokeCount += 1
                        }

                Assertions.assertThat(invokeCount).isEqualTo(1)
            }

            it("should fail showing the failing assertion") {
                Assertions.assertThatThrownBy {
                    tableOf("a", "b")
                            .row(1, 2)
                            .forAll { a, b ->
                                assert(a + b).isEqualTo(2)
                            }
                }.hasMessage("The following assertion failed:\non row:(a=<1>,b=<2>)\n- expected:<[2]> but was:<[3]>")
            }
        }
    }
})


