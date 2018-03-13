package test.assertk.assertions.support

import assertk.assertions.support.show
import test.assertk.Assertions.Companion.assertThat
import test.assertk.assertions.support.SupportSpec.Dummy
import kotlin.test.Test

class JVMSupportSpec {

    val anonymous = object : Any() {}

    @Test
    fun show_byte() {
        assertThat(show(15.toByte())).isEqualTo("<0x0F>")
    }

    @Test
    fun show_double() {
        assertThat(show(1.234567890123)).isEqualTo("<1.234567890123>")
    }

    @Test
    fun show_float() {
        assertThat(show(1.2345f)).isEqualTo("<1.2345f>")
    }

    @Test
    fun show_int() {
        assertThat(show(42)).isEqualTo("<42>")
    }

    @Test
    fun show_short() {
        assertThat(show(42.toShort())).isEqualTo("<42>")
    }

    @Test
    fun show_string() {
        assertThat(show("value")).isEqualTo("<\"value\">")
    }

    // TODO: mkobit: figure if these are needed for multi-platform support
    @Test
    fun show_java_class_predefined() {
        assertThat(show(Dummy::class.java)).isEqualTo("<test.assertk.assertions.support.SupportSpec\$Dummy>")
    }

    @Test
    fun show_java_class_anonymous() {
        assertThat(show(anonymous::class.java)).isEqualTo("<test.assertk.assertions.support.JVMSupportSpec\$anonymous\$1>")
    }

    @Test
    fun show_array_byte_array() {
        assertThat(show(byteArrayOf(10, 15))).isEqualTo("<[0x0A, 0x0F]>")
    }

    @Test
    fun show_array_double_array() {
        assertThat(show(doubleArrayOf(1.2345, 6.789))).isEqualTo("<[1.2345, 6.789]>")
    }

    @Test
    fun show_array_float_array() {
        assertThat(show(floatArrayOf(1.2345f, 6.789f))).isEqualTo("<[1.2345f, 6.789f]>")
    }

    @Test
    fun show_array_int_array() {
        assertThat(show(intArrayOf(42, 8))).isEqualTo("<[42, 8]>")
    }

    @Test
    fun show_array_short_array() {
        assertThat(show(shortArrayOf(42, -1))).isEqualTo("<[42, -1]>")
    }

    @Test
    fun show_other_type() {
        val other = object : Any() {
            override fun toString(): String = "different"
        }
        assertThat(show(other)).isEqualTo("<different>")
    }

    @Test
    fun show_different_wrapper() {
        assertThat(show(42, "##")).isEqualTo("#42#")
    }
}
