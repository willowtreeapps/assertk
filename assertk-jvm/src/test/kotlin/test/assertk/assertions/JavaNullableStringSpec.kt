package test.assertk.assertions

import assertk.assert
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEqualTo
import kotlin.test.Test

class JavaNullableStringSpec_a_String_On_isEqualTo() {

    @Test
    fun it_Given_a_java_nullable_string_picks_the_objects_isEqualTo_over_the_string_one() {
        assert(JavaNullableString.string()).isEqualTo(JavaNullableString.string())
    }
}

class JavaNullableStringSpec_a_String_On_isNotEqualTo() {

    @Test
    fun it_Given_a_java_nullable_string_picks_the_objects_isNotEqualTo_over_the_string_one() {
        assert(JavaNullableString.string()).isNotEqualTo("wrong")
    }
}
