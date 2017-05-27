package assertk.assertions

import assertk.Assert
import assertk.assertions.support.expected
import assertk.assertions.support.show

@JvmName("isCharSequenceEmpty")
fun <T : CharSequence> Assert<T>.isEmpty() {
    if (actual.isEmpty()) return
    expected("to be empty but was:${show(actual)}")
}

@JvmName("isCharSequenceNotEmpty")
fun <T : CharSequence> Assert<T>.isNotEmpty() {
    if (actual.isNotEmpty()) return
    expected("to to not be empty")
}

@JvmName("isCharSequenceNullOrEmpty")
fun <T : CharSequence?> Assert<T>.isNullOrEmpty() {
    if (actual.isNullOrEmpty()) return
    expected("to be null or empty but was:${show(actual)}")
}

@JvmName("charSequenceHasLength")
fun <T : CharSequence> Assert<T>.hasLength(length: Int) {
    if (actual.length == length) return
    expected("to have length:${show(length)} but was:${show(actual)} (${actual.length}})")
}

fun <T : CharSequence> Assert<T>.hasSameLengthAs(other: CharSequence) {
    val actualLength = actual.length
    val otherLength = other.length
    if (actualLength == otherLength) return
    expected("to have same length as:${show(other)} ($otherLength) but was:${show(actual)} ($actualLength)")
}
