package test.assertk.assertions

import assertk.all
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.*
import com.willowtreeapps.opentest4k.AssertionFailedError
import org.junit.Test
import java.time.LocalDate
import java.time.Month
import java.util.*
import kotlin.random.Random

internal class OptionalTest {

    @Test
    fun hasValue_assertion_should_succeed_on_non_empty_optional() {
        val nonEmpty = Optional.of(UUID.randomUUID().toString())
        assertThat(nonEmpty).isPresent()
    }

    @Test
    fun hasValue_assertion_should_fail_on_empty_optional() {
        val empty = Optional.empty<String>()
        assertThat { assertThat(empty).isPresent() }
            .isFailure()
            .messageContains("optional to not be empty")
    }

    @Test
    fun test_extracted_value_isEqual_to_expected_succeeds() {
        val expected = Random.nextBytes(12)
        val optional = Optional.of(expected)
        assertThat(optional).hasValue(expected)
    }

    @Test
    fun test_extracted_value_isEqual_to_unexpected_fails() {
        val expected = Random.nextInt()
        val unexpected = expected + 1
        val optional = Optional.of(expected)
        assertThat { assertThat(optional).isPresent().isEqualTo(unexpected) }.isFailure()
    }

    @Test
    fun test_isEmpty_assertion_on_empty_succeeds() {
        val empty = Optional.empty<Int>()
        assertThat(empty).isNotPresent()
    }

    @Test
    fun test_isEmpty_assertion_on_non_empty_fails() {
        val nonEmpty = Optional.of(Random.nextLong())
        assertThat { assertThat(nonEmpty).isNotPresent() }
            .isFailure()
            .isInstanceOf(AssertionFailedError::class)
    }

    @Test
    fun test_using_value_extractor() {
        val optional = Optional.of(12)
        assertThat(optional).hasValue(12)
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
            assertThat(findPersonById(0)).isNotPresent()
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