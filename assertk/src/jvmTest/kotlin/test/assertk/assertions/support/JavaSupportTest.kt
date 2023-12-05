package test.assertk.assertions.support

import assertk.assertions.support.show
import kotlin.test.Test
import kotlin.test.assertEquals

class JavaSupportTest {

    @Test
    fun show_float() {
        assertEquals("<1.2345f>", show(1.2345f))
    }

    @Test
    fun show_float_array() {
        assertEquals("<[1.2345f, 6.789f]>", show(floatArrayOf(1.2345f, 6.789f)))
    }

    @Test
    fun show_byte() {
        assertEquals("<0x0F>", show(15.toByte()))
    }

    @Test
    fun show_byte_array() {
        assertEquals("<[0x0A, 0x0F]>", show(byteArrayOf(10, 15)))
    }

    @Test
    fun show_regex() {
        assertEquals("</^abcd$/>", show(Regex("^abcd$")))
    }
}