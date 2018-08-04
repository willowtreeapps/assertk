package test.assertk.assertions.support

import assertk.assertions.support.show
import test.assertk.Platform
import test.assertk.assertions.AnyTest
import test.assertk.exceptionPackageName
import test.assertk.platform
import kotlin.test.Test
import kotlin.test.assertEquals

class SupportTest {
    //region show
    @Test fun show_null() {
        assertEquals("<null>", show(null))
    }

    @Test fun show_boolean() {
        assertEquals("<true>", show(true))
    }

    @Test fun show_char() {
        assertEquals("<'c'>", show('c'))
    }

    @Test fun show_double() {
        assertEquals("<1.234567890123>", show(1.234567890123))
    }

    @Test fun show_int() {
        assertEquals("<42>", show(42))
    }

    @Test fun show_long() {
        assertEquals("<42L>", show(42L))
    }

    @Test fun show_short() {
        assertEquals("<42>", show(42.toShort()))
    }

    @Test fun show_string() {
        assertEquals("<\"value\">", show("value"))
    }

    @Test fun show_generic_array() {
        assertEquals("<[\"one\", \"two\"]>", show(arrayOf("one", "two")))
    }

    @Test fun show_boolean_array() {
        assertEquals("<[true, false]>", show(booleanArrayOf(true, false)))
    }

    @Test fun show_char_array() {
        assertEquals("<['a', 'b']>", show(charArrayOf('a', 'b')))
    }

    @Test fun show_double_array() {
        assertEquals("<[1.2345, 6.789]>", show(doubleArrayOf(1.2345, 6.789)))
    }

    @Test fun show_int_array() {
        assertEquals("<[42, 8]>", show(intArrayOf(42, 8)))
    }

    @Test fun show_long_array() {
        assertEquals("<[42L, 8L]>", show(longArrayOf(42L, 8L)))
    }

    @Test fun show_short_array() {
        assertEquals("<[42, -1]>", show(shortArrayOf(42, -1)))
    }

    @Test fun show_list() {
        assertEquals("<[1, 2, 3]>", show(listOf(1, 2, 3)))
    }

    @Test fun show_map() {
        assertEquals("<{1=5, 2=6}>", show(mapOf(1 to 5, 2 to 6)))
    }

    @Test fun show_custom_type() {
        val other = object : Any() {
            override fun toString(): String = "different"
        }
        assertEquals("<different>", show(other))
    }

    @Test fun show_different_wrapper() {
        assertEquals("{42}", show(42, "{}"))
    }

    @Test fun show_platform_kclass() {
        assertEquals("<class ${exceptionPackageName}Exception>", show(Exception::class))
    }

    @Test fun show_lib_kclass() {
        //js backend currently doesn't support class.qualifiedName
        if (platform == Platform.JS) {
            assertEquals("<class TestObject>", show(AnyTest.Companion.TestObject::class))
        } else {
            assertEquals("<class test.assertk.assertions.AnyTest.Companion.TestObject>", show(AnyTest.Companion.TestObject::class))
        }
    }
    //endregion
}
