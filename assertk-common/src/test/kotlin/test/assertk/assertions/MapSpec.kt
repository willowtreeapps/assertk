package test.assertk.assertions

import assertk.assert
import assertk.assertAll
import assertk.assertions.*
import test.assertk.Assertions
import kotlin.test.Test


class MapSpec_a_map_On_contains() {
    @Test
    fun it_Given_a_map_that_contains_multiple_pairs_of_Strings_to_Ints_test_should_pass_when_expecting_a_pair_that_is_actually_contained_in_the_map() {
        assert(mapOf("one" to 1, "two" to 2, "three" to 3)).contains("one" to 1)
    }

    @Test
    fun it_Given_a_map_that_contains_multiple_types_test_should_pass_when_expecting_a_pair_that_is_actually_contained_in_the_map() {
        assert(mapOf<Any?, Any?>("one" to 1, 1.09 to "Something", "awesome!" to true)).contains("awesome!" to true)
    }

    @Test
    fun it_Given_a_map_which_contains_a_null_test_should_pass_when_expecting_the_map_to_contain_a_null() {
        assert(mapOf<Any?, Any?>(null to null)).contains(null to null)
    }

    @Test
    fun it_Given_a_map_that_contains_multiple_pairs_of_Strings_to_Ints_test_should_fail_when_expecting_a_pair_that_is_not_contained_in_the_map() {
        Assertions.assertThatThrownBy {
            assert(mapOf("one" to 1, "two" to 2, "three" to 3)).contains("zero" to 0)
        }.hasMessage("expected to contain:<{\"zero\"=0}> but was:<{\"one\"=1, \"two\"=2, \"three\"=3}>")
    }
}

class MapSpec_a_map_On_doesNotContain() {
    @Test
    fun it_Given_an_empty_map_test_should_pass_when_expecting_anything_to_be_contained_within_the_map() {
        assert(emptyMap<Any?, Any?>()).doesNotContain("Something" to 99)
    }

    @Test
    fun it_Given_a_map_that_contains_multiple_pairs_of_Strings_to_Ints_test_should_pass_when_expecting_a_pair_that_is_not_contained_in_the_map() {
        assert(mapOf("one" to 1, "two" to 2, "three" to 3)).doesNotContain("zero" to 0)
    }

    @Test
    fun it_Given_a_map_that_contains_multiple_pairs_of_Strings_to_Ints_test_should_fail_when_expecting_a_pair_that_is_contained_in_the_map() {
        Assertions.assertThatThrownBy {
            assert(mapOf("one" to 1, "two" to 2, "three" to 3)).doesNotContain("one" to 1)
        }.hasMessage("expected to not contain:<{\"one\"=1}> but was:<{\"one\"=1, \"two\"=2, \"three\"=3}>")
    }
}

class MapSpec_a_map_On_containsNone() {
    @Test
    fun it_Given_an_empty_map_test_should_pass_when_expecting_multiple_elements_to_be_contained_within_the_map() {
        assert(emptyMap<Any?, Any?>()).containsNone("not" to 3, "in" to 2, "list" to 4)
    }

    @Test
    fun it_Given_a_map_that_contains_multiple_pairs_of_Strings_to_Ints_test_should_pass_when_pairs_that_arent_not_contained_in_the_map() {
        assert(mapOf("one" to 1, "two" to 2, "three" to 3)).containsNone("zero" to 0, "negative one" to -1)
    }

    @Test
    fun it_Given_a_map_that_contains_multiple_pairs_of_Strings_to_Ints_test_should_fail_when_expecting_a_pairs_that_are_contained_in_the_map() {
        Assertions.assertThatThrownBy {
            assert(mapOf("one" to 1, "two" to 2, "three" to 3)).containsNone("one" to 1, "three" to 3)
        }.hasMessage("expected to not contain:<{\"one\"=1, \"three\"=3}> but was:<{\"one\"=1, \"two\"=2, \"three\"=3}>")
    }
}

class MapSpec_a_map_On_containsExactly() {
    @Test
    fun it_Given_an_empty_map_test_should_pas_when_expecting_nothing() {
        assert(emptyMap<Any?, Any?>()).containsExactly()
    }

    @Test
    fun it_Given_a_map_of_multiple_elements_test_should_pass_when_containing_all_elements_in_the_map() {
        assert(mapOf("one" to 1, "two" to 2, "three" to 3)).containsExactly("one" to 1, "two" to 2, "three" to 3)
    }

    @Test
    fun it_Given_an_empty_map_test_should_fail_when_expecting_anything() {
        Assertions.assertThatThrownBy {
            assert(emptyMap<Any?, Any?>()).containsExactly("one" to 1, "one" to 2)
        }.hasMessage("expected to contain exactly:<{\"one\"=2}> but was:<{}>")
    }

    @Test
    fun it_Given_a_map_of_multiple_elements_test_should_fail_when_maps_are_of_different_sizes() {
        Assertions.assertThatThrownBy {
            assert(mapOf("one" to 1, "two" to 2, "three" to 3, "four" to 4))
                    .containsExactly("one" to 1, "two" to 2, "three" to 3)
        }.hasMessage("expected to contain exactly:<{\"one\"=1, \"two\"=2, \"three\"=3}> " +
                "but was:<{\"one\"=1, \"two\"=2, \"three\"=3, \"four\"=4}>")
    }

    @Test
    fun it_Given_a_map_of_multiple_elements_test_should_not_fail_when_elements_are_in_different_order() {
        assert(mapOf("one" to 1, "two" to 2, "three" to 3))
                .containsExactly("two" to 2, "three" to 3, "one" to 1)
    }
}


class MapSpec_a_map_On_containsAll() {
    @Test
    fun it_Given_a_map_of_multiple_entries_test_should_pass_when_containing_all_entries_expected_regardless_of_order() {
        assertk.assert(mapOf(1 to 'i', 2 to 'j')).containsAll(2 to 'j', 1 to 'i')
    }

    @Test
    fun it_Given_an_empty_map_test_should_pass_when_expecting_nothing() {
        assertk.assert(mapOf<Any?, Any?>()).containsAll()
    }

    @Test
    fun it_Given_a_map_of_multiple_entries_with_more_entries_given_than_expected_test_should_fail_when_not_containing_all_entries_expected_regardless_of_order() {
        Assertions.assertThatThrownBy {
            assertk.assert(mapOf(1 to 'i', 2 to 'j', 3 to 'k')).containsAll(4 to 'a', 1 to 'i')
        }.hasMessage("expected to contain:<{4=a, 1=i}> but was:<{1=i, 2=j, 3=k}>")
    }

    @Test
    fun it_Given_a_map_of_multiple_entries_with_less_entries_given_than_expected_test_should_fail_when_not_containing_all_entries_expected_regardless_of_order() {
        Assertions.assertThatThrownBy {
            assertk.assert(mapOf(1 to 'i', 3 to 'k')).containsAll(3 to 'k', 1 to 'i', 2 to 'j')
        }.hasMessage("expected to contain:<{3=k, 1=i, 2=j}> but was:<{1=i, 3=k}>")
    }

    @Test
    fun it_Given_an_empty_map_test_should_fail_when_expecting_anything() {
        Assertions.assertThatThrownBy {
            assertk.assert(emptyMap<Any?, Any?>()).containsAll(1 to 'x')
        }.hasMessage("expected to contain:<{1=x}> but was:<{}>")
    }

    @Test
    fun it_Given_two_failing_assertions_test_should_fail_with_only_one_error_message_per_failed_assertion() {
        Assertions.assertThatThrownBy {
            assertAll {
                assertk.assert(mapOf(1 to 'x')).containsAll(1 to 'y')
                assertk.assert(emptyMap<Any?, Any?>()).containsAll(1 to 0)
            }
        }.hasMessage("The following 2 assertions failed:\n"
                + "- expected to contain:<{1=y}> but was:<{1=x}>\n"
                + "- expected to contain:<{1=0}> but was:<{}>")
    }

    @Test
    fun it_Given_one_passing_and_two_failing_assertions_test_should_fail_with_only_one_error_message_per_failed_assertion() {
        Assertions.assertThatThrownBy {
            assertAll {
                assertk.assert(mapOf(1 to "x")).containsAll(1 to "x")
                assertk.assert(mapOf(1 to 'x')).containsAll(1 to 'y')
                assertk.assert(emptyMap<Any?, Any?>()).containsAll(1 to 0)
            }
        }.hasMessage("The following 2 assertions failed:\n"
                + "- expected to contain:<{1=y}> but was:<{1=x}>\n"
                + "- expected to contain:<{1=0}> but was:<{}>")
    }
}

class MapSpec_a_map_On_hasSameSizeAs() {
    @Test
    fun it_Given_two_maps_with_2_entries_each_test_should_pass() {
        assertk.assert(mapOf(1 to "x", 2 to "y")).hasSameSizeAs(mapOf(3 to "i", 4 to "j"))
    }

    @Test
    fun it_Given_two_empty_maps_test_should_pass() {
        assertk.assert(emptyMap<Any?, Any?>()).hasSameSizeAs(emptyMap<Any?, Any?>())
    }

    @Test
    fun it_Given_two_maps_both_with_2_entries_but_different_types_test_should_pass() {
        assertk.assert(mapOf(1 to "x", 2 to "y")).hasSameSizeAs(mapOf('a' to 9, 'b' to 7))
    }

    @Test
    fun it_Given_a_map_with_2_entries_and_a_map_with_1_entry_test_should_fail() {
        Assertions.assertThatThrownBy {
            assertk.assert(mapOf(1 to 'x', 2 to 'y')).hasSameSizeAs(mapOf(1 to 'x'))
        }.hasMessage("expected to have same size as:<{1=x}> (1) but was size:(2)")
    }

    @Test
    fun it_Given_an_empty_map_and_a_map_with_1_entry_test_should_fail() {
        Assertions.assertThatThrownBy {
            assertk.assert(emptyMap<Any?, Any?>()).hasSameSizeAs(mapOf(1 to 'x'))
        }.hasMessage("expected to have same size as:<{1=x}> (1) but was size:(0)")
    }

    @Test
    fun it_Given_two_failing_assertions_test_should_fail_with_only_one_error_message_per_failed_assertion() {
        Assertions.assertThatThrownBy {
            assertAll {
                assertk.assert(mapOf(1 to 'x', 2 to 'y')).hasSameSizeAs(mapOf(1 to 'x'))
                assertk.assert(emptyMap<Any?, Any?>()).hasSameSizeAs(mapOf(1 to 'x'))
            }
        }.hasMessage("The following 2 assertions failed:\n"
                + "- expected to have same size as:<{1=x}> (1) but was size:(2)\n"
                + "- expected to have same size as:<{1=x}> (1) but was size:(0)")
    }

    @Test
    fun it_Given_one_passing_and_two_failing_assertions_test_should_fail_with_only_one_error_message_per_failed_assertion() {
        Assertions.assertThatThrownBy {
            assertAll {
                assertk.assert(emptyMap<Any?, Any?>()).hasSameSizeAs(emptyMap<Any?, Any?>())
                assertk.assert(mapOf(1 to 'x', 2 to 'y')).hasSameSizeAs(mapOf(1 to 'x'))
                assertk.assert(emptyMap<Any?, Any?>()).hasSameSizeAs(mapOf(1 to 'x'))
            }
        }.hasMessage("The following 2 assertions failed:\n"
                + "- expected to have same size as:<{1=x}> (1) but was size:(2)\n"
                + "- expected to have same size as:<{1=x}> (1) but was size:(0)")
    }
}
