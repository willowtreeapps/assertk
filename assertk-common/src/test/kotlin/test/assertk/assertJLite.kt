package test.assertk

import assertk.catch
import kotlin.reflect.KClass
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Assertions {
    companion object {
        fun assertThatThrownBy(function: () -> Unit): ExceptionAssert = catch {
            function()
        }?.let { ExceptionAssert(it) } ?: throw AssertionError("Expect exception to be thrown but got nothing")

        fun assertThat(count: Int) = IntAssertion(count)
    }
}

class IntAssertion(private val n: Int) {
    fun isEqualTo(expected: Int) {
        assertEquals(expected, n)
    }
}

class ExceptionAssert(val e: Throwable) {

    fun hasMessage(s: String): ExceptionAssert {
        assertEquals(s, e.message,"message <${e.message}> should be <$s>")
        return this
    }

    fun hasMessageStartingWith(s: String): ExceptionAssert {
        assertTrue(e.message?.startsWith(s) ?: false, "message <${e.message}> should start with <$s>")
        return this
    }

    fun hasMessageContaining(s: String): ExceptionAssert {
        assertTrue(e.message?.contains(s) ?: true,"message <${e.message}> should contain <$s>")
        return this
    }

    fun isInstanceOf(kClass: KClass<*>): ExceptionAssert {
        assertTrue(kClass.isInstance(e),"Exception type should be $kClass")
        return this
    }

}
