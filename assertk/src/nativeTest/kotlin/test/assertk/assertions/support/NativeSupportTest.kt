package test.assertk.assertions.support

import assertk.assertions.support.show
import kotlin.test.Test
import kotlin.test.assertEquals

class NativeSupportTest {

    @Test
    fun show_byte_array() {
        assertEquals("<[0x0A, 0x0F]>", show(byteArrayOf(10, 15)))
    }

}