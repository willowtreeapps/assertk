package test.assertk.assertions.support

import assertk.assertions.support.show
import test.assertk.Assertions.Companion.assertThat
import kotlin.test.Test


class SupportSpec {
    class Dummy(private val i: Int) {
        override fun toString(): String = "Dummy=$i"
    }

    val anonymous = object : Any() {}

    @Test
    fun show_null() {
        assertThat(show(null)).isEqualTo("<null>")
    }

    @Test
    fun show_boolean() {
        assertThat(show(true)).isEqualTo("<true>")
    }

    @Test
    fun show_char() {
        assertThat(show('c')).isEqualTo("<'c'>")
    }

    @Test
    fun show_long() {
        assertThat(show(42L)).isEqualTo("<42L>")
    }

    @Test
    fun show_string() {
        assertThat(show("value")).isEqualTo("<\"value\">")
    }

    @Test
    fun show_class_predefined() {
        assertThat(show(Dummy::class)).isEqualTo("<${Dummy::class}>")
    }

    @Test
    fun show_class_anonymous() {
        assertThat(show(anonymous::class)).isEqualTo("<${anonymous::class}>")
    }

    @Test
    fun show_array_generic_array() {
        val array = arrayOf(Dummy(0), Dummy(1))
        assertThat(show(array)).isEqualTo("<[Dummy=0, Dummy=1]>")
    }

    @Test
    fun show_array_boolean_array() {
        assertThat(show(booleanArrayOf(false, true))).isEqualTo("<[false, true]>")
    }

    @Test
    fun show_array_char_array() {
        assertThat(show(charArrayOf('a', 'b'))).isEqualTo("<['a', 'b']>")
    }

    @Test
    fun show_array_long_array() {
        assertThat(show(longArrayOf(42L, 8L))).isEqualTo("<[42L, 8L]>")
    }

    @Test
    fun show_collection_list() {
        assertThat(show(listOf("1", "2", "3"))).isEqualTo("""<["1", "2", "3"]>""")
    }

    @Test
    fun show_collection_nested_list() {
        assertThat(show(listOf(
                listOf("1", "2", "3"),
                listOf("4", "5", "6")
        ))).isEqualTo("""<[["1", "2", "3"], ["4", "5", "6"]]>""")
    }

    @Test
    fun show_collection_set() {
        assertThat(show(setOf("1", "2", "3"))).isEqualTo("""<["1", "2", "3"]>""")
    }

    @Test
    fun show_map() {
        assertThat(show(mapOf("1" to "5", "2" to "6"))).isEqualTo("""<{"1"="5", "2"="6"}>""")
    }

    @Test
    fun show_regex() {
        val regex = Regex("^abcd$")
        assertThat(show(regex)).isEqualTo("</$regex/>")
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
        assertThat(show("42", "##")).isEqualTo("""#"42"#""")
    }
}
