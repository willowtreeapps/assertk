package assertk.assertions

import assertk.Assert

/**
 * Asserts equality of the object's specified props.
 *
 * Use [Pair.to] infix constructor to achieve better readability:
 *
 * ```
 * assertThat(MyObject("a", 1, true))
 *   .propsEqualTo(
 *     MyObject::name to "a",
 *     MyObject::amount to 1,
 *     MyObject::flag to true
 *   )
 * ```
 *
 * @see[Pair.to]
 */
fun <T> Assert<T>.propsEqualTo(vararg propEqualities: Pair<(T) -> Any?, Any?>) =
    given { actual ->
        assertThat(propEqualities.map { it.first(actual) })
            .isEqualTo(
                propEqualities.map { it.second }
            )
    }
