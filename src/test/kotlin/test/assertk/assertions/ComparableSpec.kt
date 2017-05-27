package test.assertk.assertions

import assertk.assert
import assertk.assertions.*
import org.assertj.core.api.Assertions
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

class AssertSpecComparable : Spek({

    given("a comparable") {

        on("isGreaterThan()") {

            // Int tests
            val lowInt: Int = 0
            val highInt: Int = 1

            it("Checking if highInt is greater than lowInt should pass") {
                assert(highInt).isGreaterThan(lowInt)
            }

            it("Checking the same Type should fail") {
                Assertions.assertThatThrownBy {
                    assert(lowInt).isGreaterThan(lowInt)
                }.hasMessage("expected to be greater than:<$lowInt> but was:<$lowInt>")
            }

            it("Checking if lowInt is greater than highInt should fail") {
                Assertions.assertThatThrownBy {
                    assert(lowInt).isGreaterThan(highInt)
                }.hasMessage("expected to be greater than:<$highInt> but was:<$lowInt>")
            }

            // Custom Comparable tests

            val lowCustom = TestClassComparable(Int.MIN_VALUE)
            val highCustom = TestClassComparable(Int.MAX_VALUE)

            it("Checking if highCustom is greater than lowCustom should pass") {
                assert(highCustom).isGreaterThan(lowCustom)
            }

            it("Checking the same TestClassComparable should fail") {
                Assertions.assertThatThrownBy {
                    assert(lowCustom.v1).isGreaterThan(lowCustom.v1)
                }.hasMessage("expected to be greater than:<${lowCustom.v1}> but was:<${lowCustom.v1}>")
            }

            it("Checking if lowCustom is greater than highCustom should fail") {
                Assertions.assertThatThrownBy {
                    assert(lowCustom.v1).isGreaterThan(highCustom.v1)
                }.hasMessage("expected to be greater than:<${highCustom.v1}> but was:<${lowCustom.v1}>")
            }
        }
    }
})

class TestClassComparable(val v1: Int) : Comparable<TestClassComparable> {

    override fun compareTo(other: TestClassComparable): Int = when {
        v1 > other.v1 -> 1
        v1 < other.v1 -> -1
        else -> 0
    }
}
