package test.assertk.assertions.support

import assertk.assertions.support.show
import kotlin.test.Test
import kotlin.test.assertEquals

class JsSupportTest {

    @Test
    fun show_regex() {
        assertEquals("</^abcd$/gu>", show(Regex("^abcd$")))
    }
}