package test.assertk.assertions

import assertk.assert
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import kotlin.test.Test
import test.assertk.Assertions


class BooleanSpec_a_boolean_On_isTrue() {
    @Test
    fun it_Given_a_truevalue_boolean_it_should_pass() {
        assert(true).isTrue()
    }

    @Test
    fun it_Given_a_falsevalue_boolean_it_should_fail() {
        Assertions.assertThatThrownBy {
            assert(false).isTrue()
        }.hasMessage("expected to be true")
    }
}

class BooleanSpec_a_boolean_On_isFalse() {
    @Test
    fun it_Given_a_falsevalue_boolean_it_should_pass() {
        assert(false).isFalse()
    }

    @Test
    fun it_Given_a_truevalue_boolean_it_should_fail() {
        Assertions.assertThatThrownBy {
            assert(true).isFalse()
        }.hasMessage("expected to be false")
    }
}
