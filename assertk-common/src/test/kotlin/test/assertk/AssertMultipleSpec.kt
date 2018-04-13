package test.assertk

import assertk.all
import assertk.assert
import assertk.assertAll
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.toStringFun
import test.assertk.AssertMultipleSpec.BasicObject
import kotlin.test.Test

class AssertMultipleSpec_On_an_assert_block_with_a_value {
    private val subject = BasicObject("test", 1)

    @Test
    fun it_should_pass_multiple_successful_assertions() {
        assert(subject).all {
            isInstanceOf(BasicObject::class)
            toStringFun().isEqualTo("BasicObject(arg1=test, arg2=1)")
        }
    }

    @Test
    fun it_should_fail_the_first_assertion() {
        Assertions.assertThatThrownBy {
            assert(subject).all {
                isInstanceOf(String::class)
                toStringFun().isEqualTo("BasicObject(arg1=test, arg2=1)")
            }
        }.hasMessage("expected to be instance of:<${String::class}> but had class:<${BasicObject::class}>")
    }

    @Test
    fun it_should_fail_the_second_assertion() {
        Assertions.assertThatThrownBy {
            assert(subject).all {
                isInstanceOf(BasicObject::class)
                toStringFun().isEqualTo("wrong")
            }
        }.hasMessage("expected [toString]:<\"[wrong]\"> but was:<\"[BasicObject(arg1=test, arg2=1)]\"> (BasicObject(arg1=test, arg2=1))")
    }

    @Test
    fun it_should_fail_both_assertions() {
        Assertions.assertThatThrownBy {
            assert<Any>(subject).all {
                isInstanceOf(String::class)
                toStringFun().isEqualTo("wrong")
            }
        }.hasMessage("""The following 2 assertions failed:
- expected to be instance of:<${String::class}> but had class:<${BasicObject::class}>
- expected [toString]:<"[wrong]"> but was:<"[BasicObject(arg1=test, arg2=1)]"> (BasicObject(arg1=test, arg2=1))""")
    }
}

class AssertMultipleSpec_On_an_assert_all_block {
    val subject1 = BasicObject("test1", 1)
    val subject2 = BasicObject("test2", 2)

    @Test
    fun it_should_pass_multiple_successful_assertions() {
        assertAll {
            assert(subject1).toStringFun().isEqualTo("BasicObject(arg1=test1, arg2=1)")
            assert(subject2).toStringFun().isEqualTo("BasicObject(arg1=test2, arg2=2)")
        }
    }

    @Test
    fun it_should_fail_the_first_assertion() {
        Assertions.assertThatThrownBy {
            assertAll {
                assert(subject1).toStringFun().isEqualTo("wrong1")
                assert(subject2).toStringFun().isEqualTo("BasicObject(arg1=test2, arg2=2)")
            }
        }.hasMessage("expected [toString]:<\"[wrong1]\"> but was:<\"[BasicObject(arg1=test1, arg2=1)]\"> (BasicObject(arg1=test1, arg2=1))")
    }

    @Test
    fun it_should_fail_the_second_assertion() {
        Assertions.assertThatThrownBy {
            assertAll {
                assert(subject1).toStringFun().isEqualTo("BasicObject(arg1=test1, arg2=1)")
                assert(subject2).toStringFun().isEqualTo("wrong2")
            }
        }.hasMessage("expected [toString]:<\"[wrong2]\"> but was:<\"[BasicObject(arg1=test2, arg2=2)]\"> (BasicObject(arg1=test2, arg2=2))")
    }

    @Test
    fun it_should_fail_both_assertions() {
        Assertions.assertThatThrownBy {
            assertAll {
                assert(subject1).toStringFun().isEqualTo("wrong1")
                assert(subject2).toStringFun().isEqualTo("wrong2")
            }
        }.hasMessage("""The following 2 assertions failed:
- expected [toString]:<"[wrong1]"> but was:<"[BasicObject(arg1=test1, arg2=1)]"> (BasicObject(arg1=test1, arg2=1))
- expected [toString]:<"[wrong2]"> but was:<"[BasicObject(arg1=test2, arg2=2)]"> (BasicObject(arg1=test2, arg2=2))""")
    }
}

class AssertMultipleSpec {

    data class BasicObject(val arg1: String, val arg2: Int)
}

