package test.assertk.assertions.support

import assertk.assertions.support.show
import kotlin.test.Test
import kotlin.test.assertEquals

class NativeSupportTest {

    @Test
    fun show_short() {
        assertEquals("<0x0100>", show(256.toShort()))
    }

    @Test fun show_byte_array() {
        assertEquals("<[0x0A, 0x0F]>", show(byteArrayOf(10, 15)))
    }

    @Test fun show_short_array() {
        assertEquals("<[0x0100, 0x7530]>", show(shortArrayOf(256, 30_000)))
    }
}