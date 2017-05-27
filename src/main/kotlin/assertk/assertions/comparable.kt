package assertk.assertions

import assertk.Assert
import assertk.assertions.support.expected
import assertk.assertions.support.show

fun <A, B : Comparable<A>> Assert<B>.isGreaterThan(other: A) {
    if (actual > other) return
    expected("to be greater than:${show(other)} but was:${show(actual)}")
}

fun <A, B : Comparable<A>> Assert<B>.isLessThan(other: A) {
    if (actual < other) return
    expected("to be less than:${show(other)} but was:${show(actual)}")
}

fun <A, B : Comparable<A>> Assert<B>.isGreaterThanOrEqualTo(other: A) {
    if (actual >= other) return
    expected("to be greater than or equal to:${show(other)} but was:${show(actual)}")
}

fun <A, B : Comparable<A>> Assert<B>.isLessThanOrEqualTo(other: A) {
    if (actual <= other) return
    expected("to be less than or equal to:${show(other)} but was:${show(actual)}")
}

fun <A, B : Comparable<A>> Assert<B>.isBetween(start: A, end: A) {
    if (actual >= start && actual <= end) return
    expected("to be between:${show(start)} and ${show(end)} but was:${show(actual)}")
}

fun <A, B : Comparable<A>> Assert<B>.isStrictlyBetween(start: A, end: A) {
    if (actual > start && actual < end) return
    expected("to be strictly between:${show(start)} and ${show(end)} but was:${show(actual)}")
}
