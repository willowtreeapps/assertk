package test.assertk.assertions

import assertk.all
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.*
import java.time.LocalDate
import java.time.Month
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class OptionalTest {

    @Test
    fun isPresent_passes() {
        assertThat(Optional.of("test")).isPresent()
    }

    @Test
    fun isPresent_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(Optional.empty<Any>()).isPresent()
        }
        assertEquals(
            "expected optional to not be empty",
            error.message
        )
    }

    @Test
    fun isEmpty_passes() {
        assertThat(Optional.empty<Any>()).isEmpty()
    }

    @Test
    fun isEmpty_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(Optional.of("test")).isEmpty()
        }
        assertEquals(
            "expected optional to be empty but was:<\"test\">",
            error.message
        )
    }

    @Test
    fun hasValue_passes() {
        assertThat(Optional.of("test")).hasValue("test")
    }

    @Test
    fun hasValue_empty_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(Optional.empty<String>()).hasValue("test")
        }
        assertEquals(
            "expected optional to not be empty",
            error.message
        )
    }

    @Test
    fun hasValue_wrong_value_fails() {
        val error = assertFailsWith<AssertionError> {
            assertThat(Optional.of("test")).hasValue("wrong")
        }
        assertEquals(
            "expected:<\"[wrong]\"> but was:<\"[test]\"> (Optional[test])",
            error.message
        )
    }

    @Test
    fun test_envisioned_usage() {

        // Data Access Logic - eg. Repository, DAO etc..
        val personId = 1L
        val personName = "person name"
        val personDateCreated = LocalDate.of(2021, Month.JANUARY, 16)

        val personStore = mapOf(
            personId to Person(
                id = personId,
                name = personName,
                dateCreated = personDateCreated
            )
        )

        fun findPersonById(id: Long): Optional<Person> {
            return Optional.ofNullable(personStore[id])
        }

        // Asserting against findPersonById in data access logic:
        assertAll {
            assertThat(findPersonById(0)).isEmpty()
            assertThat(findPersonById(personId)).isPresent().all {
                prop(Person::id).isEqualTo(personId)
                prop(Person::name).isEqualTo(personName)
                prop(Person::dateCreated).all {
                    isEqualTo(personDateCreated)
                    transform("dateCreated.year") { it.year }.isGreaterThan(2020)
                }
            }
        }
    }

    private data class Person(val id: Long, val name: String, val dateCreated: LocalDate)
}
