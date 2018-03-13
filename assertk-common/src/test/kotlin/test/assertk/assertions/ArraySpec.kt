package test.assertk.assertions

import assertk.assert
import assertk.assertAll
import assertk.assertions.*
import test.assertk.Assertions
import kotlin.test.Test

class ArraySpec_an_array_On_isEmpty() {
    @Test
    fun it_Given_an_empty_array_test_should_pass() {
        assert(emptyArray<Any?>()).isEmpty()
    }

    @Test
    fun it_Given_a_nonempty_array_of_Ints_test_should_fail() {
        Assertions.assertThatThrownBy {
            assert(arrayOf(1, 2, 3, 4)).isEmpty()
        }.hasMessage("expected to be empty but was:<[1, 2, 3, 4]>")
    }

    @Test
    fun it_Given_a_nonempty_array_of_Nulls_test_should_fail() {
        Assertions.assertThatThrownBy {
            assert(arrayOf<Any?>(null)).isEmpty()
        }.hasMessage("expected to be empty but was:<[null]>")
    }

    @Test
    fun it_Given_a_nonempty_array_of_mixed_types_test_should_fail() {
        Assertions.assertThatThrownBy {
            assert(arrayOf(1, 1.09, "awesome!", true)).isEmpty()
        }.hasMessage("expected to be empty but was:<[1, 1.09, \"awesome!\", true]>")
    }

    @Test
    fun it_Given_two_nonempty_arrays_test_should_fail_with_only_one_error_message_per_failed_assertion() {
        Assertions.assertThatThrownBy {
            assertAll {
                assert(arrayOf(1, 2, 3, 4)).isEmpty()
                assert(arrayOf(1, 1.09, "awesome!", true)).isEmpty()
            }
        }.hasMessage("The following 2 assertions failed:\n"
                + "- expected to be empty but was:<[1, 2, 3, 4]>\n"
                + "- expected to be empty but was:<[1, 1.09, \"awesome!\", true]>")
    }

    @Test
    fun it_Given_one_empty_array_and_two_nonempty_arrays_test_should_fail_for_only_the_nonempty_array_with_only_one_error_message_per_failed_assertion() {
        Assertions.assertThatThrownBy {
            assertAll {
                assert(emptyArray<Any?>()).isEmpty()
                assert(arrayOf(1, 2, 3, 4)).isEmpty()
                assert(arrayOf(1, 1.09, "awesome!", true)).isEmpty()
            }
        }.hasMessage("The following 2 assertions failed:\n"
                + "- expected to be empty but was:<[1, 2, 3, 4]>\n"
                + "- expected to be empty but was:<[1, 1.09, \"awesome!\", true]>")
    }
}

class ArraySpec_an_array_On_isNotEmpty() {
    @Test
    fun it_Given_a_array_of_Ints_test_should_pass() {
        assert(arrayOf(1, 2, 3, 4)).isNotEmpty()
    }

    @Test
    fun it_Given_a_array_filled_with_a_null_test_should_pass() {
        assert(arrayOf<Any?>(null)).isNotEmpty()
    }

    @Test
    fun it_Given_a_array_of_mixed_types_test_should_pass() {
        assert(arrayOf(1, 1.09, "awesome!", true)).isNotEmpty()
    }

    @Test
    fun it_Given_an_empty_array_passed_directly_in_test_should_fail() {
        Assertions.assertThatThrownBy {
            assert(emptyArray<Any?>()).isNotEmpty()
        }.hasMessage("expected to not be empty")
    }

    @Test
    fun it_Given_a_array_that_hasnt_been_populated_test_should_fail() {
        Assertions.assertThatThrownBy {
            val anEmptyList: Array<Any?> = arrayOf()
            assert(anEmptyList).isNotEmpty()
        }.hasMessage("expected to not be empty")
    }

    @Test
    fun it_Given_two_empty_arrays_test_should_fail_with_only_one_error_message_per_failed_assertion() {
        Assertions.assertThatThrownBy {
            assertAll {
                assert(emptyArray<Any?>()).isNotEmpty()
                val anEmptyList: Array<Any?> = arrayOf()
                assert(anEmptyList).isNotEmpty()
            }
        }.hasMessage("The following 2 assertions failed:\n"
                + "- expected to not be empty\n"
                + "- expected to not be empty")
    }

    @Test
    fun it_Given_one_empty_array_and_two_empty_arrays_test_should_fail_for_only_for_the_empty_arrays_with_only_one_error_message_per_failed_assertion() {
        Assertions.assertThatThrownBy {
            assertAll {
                assert(emptyArray<Any?>()).isNotEmpty()
                val anEmptyList: Array<Any?> = arrayOf()
                assert(arrayOf(1, 2, 3)).isNotEmpty()
                assert(anEmptyList).isNotEmpty()
            }
        }.hasMessage("The following 2 assertions failed:\n"
                + "- expected to not be empty\n"
                + "- expected to not be empty")
    }
}

class ArraySpec_an_array_On_isNullOrEmpty() {
    @Test
    fun it_Given_a_forced_null_arrays_test_should_pass() {
        // Need to force a null here
        val nullList: Array<Any?>? = null
        assert(nullList).isNullOrEmpty()
    }

    @Test
    fun it_Given_an_empty_arrays_test_should_pass() {
        assert(emptyArray<Any?>()).isNullOrEmpty()
    }

    @Test
    fun it_Given_a_nonempty_array_of_Ints_test_should_fail() {
        Assertions.assertThatThrownBy {
            assert(arrayOf(1, 2, 3)).isNullOrEmpty()
        }.hasMessage("expected to be null or empty but was:<[1, 2, 3]>")
    }

    @Test
    fun it_Given_a_nonempty_array_with_a_null_test_should_fail() {
        Assertions.assertThatThrownBy {
            assert(arrayOf<Any?>(null)).isNullOrEmpty()
        }.hasMessage("expected to be null or empty but was:<[null]>")
    }

    @Test
    fun it_Given_two_nonempty_nonnull_arrays_test_should_fail_with_only_one_error_message_per_failed_assertion() {
        Assertions.assertThatThrownBy {
            assertAll {
                assert(arrayOf(1, 2, 3)).isNullOrEmpty()
                assert(arrayOf(43, true, "awesome!")).isNullOrEmpty()
            }
        }.hasMessage("The following 2 assertions failed:\n"
                + "- expected to be null or empty but was:<[1, 2, 3]>\n"
                + "- expected to be null or empty but was:<[43, true, \"awesome!\"]>")
    }

    @Test
    fun it_Given_one_empty_array_and_two_nonempty_nonnull_arrays_test_should_fail_only_for_the_nonempty_nonnull_arrays() {
        Assertions.assertThatThrownBy {
            assertAll {
                assert(arrayOf(1, 2, 3)).isNullOrEmpty()
                assert(arrayOf(43, true, "awesome!")).isNullOrEmpty()
                assert(emptyArray<Any?>()).isNullOrEmpty()
            }
        }.hasMessage("The following 2 assertions failed:\n"
                + "- expected to be null or empty but was:<[1, 2, 3]>\n"
                + "- expected to be null or empty but was:<[43, true, \"awesome!\"]>")
    }
}

class ArraySpec_an_array_On_hasSize() {
    @Test
    fun it_Given_a_array_that_has_4_Ints_test_should_pass() {
        assert(arrayOf(1, 2, 3, 4)).hasSize(4)
    }

    @Test
    fun it_Given_an_empty_array_test_should_pass() {
        assert(emptyArray<Any?>()).hasSize(0)
    }

    @Test
    fun it_Given_a_array_with_one_Null_object_test_should_pass() {
        assert(arrayOf<Any?>(null)).hasSize(1)
    }

    @Test
    fun it_Given_a_array_with_4_mixed_type_elements_test_should_pass() {
        assert(arrayOf(1, 1.09, "awesome!", true)).hasSize(4)
    }

    @Test
    fun it_Given_a_array_of_3_Ints_test_should_fail_when_expecting_4_elements() {
        Assertions.assertThatThrownBy {
            assert(arrayOf(1, 2, 3)).hasSize(4)
        }.hasMessage("expected [size]:<[4]> but was:<[3]> ([1, 2, 3])")
    }

    @Test
    fun it_Given_an_empty_array_test_should_fail_when_expecting_more_than_0_elements() {
        Assertions.assertThatThrownBy {
            assert(emptyArray<Any?>()).hasSize(1)
        }.hasMessage("expected [size]:<[1]> but was:<[0]> ([])")
    }

    @Test
    fun it_Given_a_array_of_1_null_test_should_fail_when_expecting_0_elements() {
        Assertions.assertThatThrownBy {
            assert(arrayOf<Any?>(null)).hasSize(0)
        }.hasMessage("expected [size]:<[0]> but was:<[1]> ([null])")
    }

    @Test
    fun it_Given_two_arrays_expecting_incorrect_size_for_each_test_should_fail_with_only_one_error_message_per_failed_assertion() {
        Assertions.assertThatThrownBy {
            assertAll {
                assert(arrayOf(1, 2, 3)).hasSize(4)
                assert(arrayOf(43, true, "awesome!")).hasSize(1)
            }
        }.hasMessage("The following 2 assertions failed:\n"
                + "- expected [size]:<[4]> but was:<[3]> ([1, 2, 3])\n"
                + "- expected [size]:<[1]> but was:<[3]> ([43, true, \"awesome!\"])")
    }

    @Test
    fun it_Given_one_array_expecting_the_correct_size_and_two_arrays_expecting_incorrect_size_for_each_test_should_fail_with_only_one_error_message_per_failed_assertion() {
        Assertions.assertThatThrownBy {
            assertAll {
                assert(arrayOf(1, 2, 3)).hasSize(4)
                assert(arrayOf("this", "test")).hasSize(2)
                assert(arrayOf(43, true, "awesome!")).hasSize(1)
            }
        }.hasMessage("The following 2 assertions failed:\n"
                + "- expected [size]:<[4]> but was:<[3]> ([1, 2, 3])\n"
                + "- expected [size]:<[1]> but was:<[3]> ([43, true, \"awesome!\"])")
    }
}

class ArraySpec_an_array_On_contains() {
    @Test
    fun it_Given_a_array_that_contains_multiple_Ints_test_should_pass_when_expecting_an_Int_that_is_actually_contained_in_the_array() {
        assert(arrayOf(1, 2, 3, 4)).contains(3)
    }

    @Test
    fun it_Given_a_array_which_contains_a_null_test_should_pass_when_expecting_the_array_to_contain_a_null() {
        assert(arrayOf<Any?>(null)).contains(null)
    }

    @Test
    fun it_Given_a_array_that_contains_multiple_types_test_should_pass_when_expecting_an_element_that_is_actually_contained_in_the_array() {
        assert(arrayOf(1, 1.09, "awesome!", true)).contains(1.09)
    }

    @Test
    fun it_Given_a_array_that_contains_multiple_Ints_test_should_fail_when_expecting_an_Int_that_is_not_contained_in_the_array() {
        Assertions.assertThatThrownBy {
            assert(arrayOf(1, 2, 3)).contains(43)
        }.hasMessage("expected to contain:<43> but was:<[1, 2, 3]>")
    }

    @Test
    fun it_Given_an_empty_array_test_should_fail_when_expecting_anything_to_be_contained_within_the_array() {
        Assertions.assertThatThrownBy {
            assert(emptyArray<Any?>()).contains(null)
        }.hasMessage("expected to contain:<null> but was:<[]>")
    }

    @Test
    fun it_Given_a_array_that_contains_multiple_types_test_should_fail_when_expecting_anything_that_is_not_contained_in_the_array() {
        Assertions.assertThatThrownBy {
            assert(arrayOf(1, 1.09, "awesome!", true)).contains(43)
        }.hasMessage("expected to contain:<43> but was:<[1, 1.09, \"awesome!\", true]>")
    }

    @Test
    fun it_Given_multiple_assertions_which_should_fail_test_should_fail_with_only_one_error_message_per_failed_assertion() {
        Assertions.assertThatThrownBy {
            assertAll {
                assert(arrayOf(1, 2, 3)).contains(43)
                assert(arrayOf(43, true, "awesome!")).contains(false)
            }
        }.hasMessage("The following 2 assertions failed:\n"
                + "- expected to contain:<43> but was:<[1, 2, 3]>\n"
                + "- expected to contain:<false> but was:<[43, true, \"awesome!\"]>")
    }

    @Test
    fun it_Given_multiple_assertions_which_should_fail_and_some_that_should_pass_test_should_fail_with_only_one_error_message_per_failed_assertion() {
        Assertions.assertThatThrownBy {
            assertAll {
                assert(arrayOf(1, "test", true)).contains(true)
                assert(arrayOf(1, 2, 3)).contains(43)
                assert(arrayOf(43, true, "awesome!")).contains(false)
            }
        }.hasMessage("The following 2 assertions failed:\n"
                + "- expected to contain:<43> but was:<[1, 2, 3]>\n"
                + "- expected to contain:<false> but was:<[43, true, \"awesome!\"]>")
    }
}

class ArraySpec_an_array_On_doesNotContain() {
    @Test
    fun it_Given_a_array_of_Ints_test_should_pass_when_expecting_an_Int_that_is_not_in_the_array() {
        assert(arrayOf(1, 2, 3, 4)).doesNotContain(43)
    }

    @Test
    fun it_Given_an_empty_array_test_should_pass_when_expecting_anything_to_be_contained_within_the_array() {
        assert(emptyArray<Any?>()).doesNotContain(4)
    }

    @Test
    fun it_Given_a_array_with_elements_of_multiple_types_test_should_pass_when_expecting_anything_which_is_not_contained_within_the_array() {
        assert(arrayOf(1, 1.09, "awesome!", true)).doesNotContain(43)
    }

    @Test
    fun it_Given_a_array_with_multiple_elements_of_the_same_type_test_should_fail_when_expecting_an_element_which_is_contained_in_the_array() {
        Assertions.assertThatThrownBy {
            assert(arrayOf(1, 2, 3)).doesNotContain(2)
        }.hasMessage("expected to not contain:<2> but was:<[1, 2, 3]>")
    }

    @Test
    fun it_Given_a_array_of_multiple_types_test_should_fail_when_expecting_an_element_which_is_contained_in_the_array() {
        Assertions.assertThatThrownBy {
            assert(arrayOf(1, 1.09, "awesome!", true)).doesNotContain(1.09)
        }.hasMessage("expected to not contain:<1.09> but was:<[1, 1.09, \"awesome!\", true]>")
    }

    @Test
    fun it_Given_multiple_assertions_which_should_fail_test_should_fail_with_only_one_error_message_per_failed_assertion() {
        Assertions.assertThatThrownBy {
            assertAll {
                assert(arrayOf(1, 2, 3)).doesNotContain(3)
                assert(arrayOf(43, true, "awesome!")).doesNotContain(true)
            }
        }.hasMessage("The following 2 assertions failed:\n"
                + "- expected to not contain:<3> but was:<[1, 2, 3]>\n"
                + "- expected to not contain:<true> but was:<[43, true, \"awesome!\"]>")
    }

    @Test
    fun it_Given_multiple_assertions_which_should_fail_and_some_that_should_pass_test_should_fail_with_only_one_error_message_per_failed_assertion() {
        Assertions.assertThatThrownBy {
            assertAll {
                assert(arrayOf(1, 2, 3)).doesNotContain(3)
                assert(arrayOf("this", "passes", true)).doesNotContain(false)
                assert(arrayOf(43, true, "awesome!")).doesNotContain(true)
            }
        }.hasMessage("The following 2 assertions failed:\n"
                + "- expected to not contain:<3> but was:<[1, 2, 3]>\n"
                + "- expected to not contain:<true> but was:<[43, true, \"awesome!\"]>")
    }
}

class ArraySpec_an_array_On_containsAll() {
    @Test
    fun it_Given_a_array_of_multiple_elements_test_should_pass_when_containing_all_elements_expected_regardless_of_order() {
        assert(arrayOf(1, 2, 3)).containsAll(3, 2, 1)
    }

    @Test
    fun it_Given_an_empty_array_test_should_pass_when_expecting_nothing() {
        assert(emptyArray<Any?>()).containsAll()
    }

    @Test
    fun it_Given_a_array_of_multiple_types_of_elements_test_should_pass_when_containing_all_elements_expected_regardless_of_order() {
        assert(arrayOf(1, 1.09, "awesome!", true)).containsAll(1, 1.09, "awesome!", true)
    }

    @Test
    fun it_Given_a_array_of_multiple_elements_with_more_elements_given_than_expected_test_should_fail_when_not_containing_all_elements_expected_regardless_of_order() {
        Assertions.assertThatThrownBy {
            assert(arrayOf(1, 2, 3)).containsAll(4, 3, 1, 2)
        }.hasMessage("expected to contain all:<[4, 3, 1, 2]> but was:<[1, 2, 3]>")
    }

    @Test
    fun it_Given_a_array_of_multiple_elements_with_less_elements_given_than_expected_test_should_fail_when_not_containing_all_elements_expected_regardless_of_order() {
        Assertions.assertThatThrownBy {
            assert(arrayOf(1, 2, 4, 5)).containsAll(2, 1, 3)
        }.hasMessage("expected to contain all:<[2, 1, 3]> but was:<[1, 2, 4, 5]>")
    }

    @Test
    fun it_Given_an_empty_array_test_should_fail_when_expecting_anything() {
        Assertions.assertThatThrownBy {
            assert(emptyArray<Any?>()).containsAll(1, 2, 3)
        }.hasMessage("expected to contain all:<[1, 2, 3]> but was:<[]>")
    }

    @Test
    fun it_Given_a_array_of_multiple_types_test_should_fail_when_not_containing_all_elements_expected_regardless_of_order() {
        Assertions.assertThatThrownBy {
            assert(arrayOf(1, "is", "awesome!")).containsAll("this", 4, "awesome!")
        }.hasMessage("expected to contain all:<[\"this\", 4, \"awesome!\"]> but was:<[1, \"is\", \"awesome!\"]>")
    }

    @Test
    fun it_Given_multiple_assertions_which_should_fail_test_should_fail_with_only_one_error_message_per_failed_assertion() {
        Assertions.assertThatThrownBy {
            assertAll {
                assert(arrayOf(1, 2, 3)).containsAll(5, 6, 7)
                assert(arrayOf(1, "is", "awesome!")).containsAll("this", 4, "awesome!")
            }
        }.hasMessage("The following 2 assertions failed:\n"
                + "- expected to contain all:<[5, 6, 7]> but was:<[1, 2, 3]>\n"
                + "- expected to contain all:<[\"this\", 4, \"awesome!\"]> but was:<[1, \"is\", \"awesome!\"]>")
    }

    @Test
    fun it_Given_multiple_assertions_which_should_fail_and_some_that_should_pass_test_should_fail_with_only_one_error_message_per_failed_assertion() {
        Assertions.assertThatThrownBy {
            assertAll {
                assert(arrayOf(1, 2, 3)).containsAll(3, 2, 1) // should pass
                assert(arrayOf(1, 2, 3)).containsAll(5, 6, 7) // should fail
                assert(arrayOf("this", "is", "awesome!")).containsAll("this", 4, "awesome!") // should fail
                assert(arrayOf(1, 1.09, "awesome!", true)).containsAll(1, 1.09, "awesome!", true) // should pass
            }
        }.hasMessage("The following 2 assertions failed:\n"
                + "- expected to contain all:<[5, 6, 7]> but was:<[1, 2, 3]>\n"
                + "- expected to contain all:<[\"this\", 4, \"awesome!\"]> but was:<[\"this\", \"is\", \"awesome!\"]>")
    }
}

class ArraySpec_an_array_On_containsExactly() {
    @Test
    fun it_Given_a_array_of_multiple_elements_test_should_pass_when_containing_all_elements_expected_exactly_in_order() {
        assert(arrayOf(1, 2, 3)).containsExactly(1, 2, 3)
    }

    @Test
    fun it_Given_an_empty_array_test_should_pass_when_expecting_nothing() {
        assert(emptyArray<Any?>()).containsExactly()
    }

    @Test
    fun it_Given_a_array_of_multiple_types_of_elements_test_should_pass_when_containing_all_elements_expected_exactly_in_order() {
        assert(arrayOf(1, 1.09, "awesome!", true)).containsExactly(1, 1.09, "awesome!", true)
    }

    @Test
    fun it_Given_a_array_of_multiple_elements_with_less_elements_given_than_expected_test_should_fail_when_not_containing_all_elements_expected_exactly_in_order() {
        Assertions.assertThatThrownBy {
            assert(arrayOf(1, 2, 3)).containsExactly(1, 2, 3, 4)
        }.hasMessage("expected to contain exactly:<[1, 2, 3, 4]> but was:<[1, 2, 3]>")
    }

    @Test
    fun it_Given_a_array_of_multiple_elements_with_more_elements_given_than_expected_test_should_fail_when_not_containing_all_elements_expected_exactly_in_order() {
        Assertions.assertThatThrownBy {
            assert(arrayOf(1, 2, 3, 4)).containsExactly(1, 2, 3)
        }.hasMessage("expected to contain exactly:<[1, 2, 3]> but was:<[1, 2, 3, 4]>")
    }

    @Test
    fun it_Given_an_empty_array_test_should_fail_when_expecting_anything() {
        Assertions.assertThatThrownBy {
            assert(emptyArray<Any?>()).containsExactly(1, 2, 3)
        }.hasMessage("expected to contain exactly:<[1, 2, 3]> but was:<[]>")
    }

    @Test
    fun it_Given_a_array_of_multiple_types_test_should_fail_when_not_containing_all_elements_expected_exactly_in_order() {
        Assertions.assertThatThrownBy {
            assert(arrayOf(1, "is", "awesome!")).containsExactly("this", 4, "awesome!")
        }.hasMessage("expected to contain exactly:<[\"this\", 4, \"awesome!\"]> but was:<[1, \"is\", \"awesome!\"]>")
    }

    @Test
    fun it_Given_multiple_assertions_which_should_fail_test_should_fail_with_only_one_error_message_per_failed_assertion() {
        Assertions.assertThatThrownBy {
            assertAll {
                assert(arrayOf(1, 2, 3)).containsExactly(5, 6, 7) // should fail
                assert(arrayOf(1, "is", "awesome!")).containsExactly("this", 4, "awesome!") // should fail
            }
        }.hasMessage("The following 2 assertions failed:\n"
                + "- expected to contain exactly:<[5, 6, 7]> but was:<[1, 2, 3]>\n"
                + "- expected to contain exactly:<[\"this\", 4, \"awesome!\"]> but was:<[1, \"is\", \"awesome!\"]>")
    }

    @Test
    fun it_Given_multiple_assertions_which_should_fail_and_some_that_should_pass_test_should_fail_with_only_one_error_message_per_failed_assertion() {
        Assertions.assertThatThrownBy {
            assertAll {
                assert(arrayOf(1, 2, 3)).containsExactly(1, 2, 3) // should pass
                assert(arrayOf(1, 2, 3)).containsExactly(5, 6, 7) // should fail
                assert(emptyArray<Any?>()).containsExactly() // should pass
                assert(arrayOf(1, "is", "awesome!")).containsExactly("this", 4, "awesome!") // should fail
            }
        }.hasMessage("The following 2 assertions failed:\n"
                + "- expected to contain exactly:<[5, 6, 7]> but was:<[1, 2, 3]>\n"
                + "- expected to contain exactly:<[\"this\", 4, \"awesome!\"]> but was:<[1, \"is\", \"awesome!\"]>")
    }
}

class ArraySpec_an_array_On_each() {
    @Test
    fun it_Given_an_empty_array_test_should_pass_when_expecting_nothing() {
        assert(emptyArray<Any>()).each { }
    }

    @Test
    fun it_Given_a_non_empty_array_test_should_pass_when_expecting_nothing() {
        assert(arrayOf("one", "two")).each { }
    }

    @Test
    fun it_Given_an_array_of_Strings_valid_assertion_should_pass_for_each_item() {
        assert(arrayOf("one", "two")).each {
            it.length().isEqualTo(3)
        }
    }

    @Test
    fun it_Given_an_array_invalid_assertion_should_fail() {
        Assertions.assertThatThrownBy {
            assert(arrayOf(1, 2, 3, 4)).each {
                it.isLessThan(3)
            }
        }.hasMessage("expected [[2]] to be less than:<3> but was:<3> ([1, 2, 3, 4])")
    }

    @Test
    fun it_Given_an_array_of_multiple_types_valid_assertion_should_pass_for_each_item() {
        assert(arrayOf("one", 2)).each {
            it.kClass().isNotEqualTo(Boolean::class)
        }
    }
}

