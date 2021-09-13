package test.assertk.assertions

import assertk.assertThat
import assertk.assertions.isFailure
import assertk.assertions.isInstanceOf
import assertk.assertions.propsEqualTo
import com.willowtreeapps.opentest4k.AssertionFailedError
import kotlin.test.Test

class PropsTest {

    data class MyObject(
        val name: String,
        val amount: Number,
        val flag: Boolean,
        val maybe: String?
    )

    private val anObject = MyObject("a", 1, true, null)

    @Test
    fun propsEqualTo_equalValues_passes() {

        assertThat(anObject)
            .propsEqualTo(
                MyObject::name to "a",
                MyObject::amount to 1,
                MyObject::flag to true,
                MyObject::maybe to null
            )
    }

    @Test
    fun propsEqualTo_oneValueDifferent_fails() {

        assertThat {
            assertThat(anObject).propsEqualTo(
                MyObject::name to "a",
                MyObject::amount to 2,
                MyObject::flag to true
            )
        }
            .isFailure()
            .isInstanceOf(AssertionFailedError::class)
    }
}
