package test.assertk.assertions

import assertk.assert
import assertk.assertAll
import assertk.assertions.*
import kotlin.test.Test
import test.assertk.Assertions

class CollectionSpec_a_collection_On_isEmpty() {
    @Test
    fun it_Given_an_empty_list_test_should_pass() {
        assert(emptyList<Any?>()).isEmpty()
    }

    @Test
    fun it_Given_a_nonempty_list_of_Ints_test_should_fail() {
        Assertions.assertThatThrownBy {
            assert(listOf(1, 2, 3, 4)).isEmpty()
        }.hasMessage("expected to be empty but was:<[1, 2, 3, 4]>")
    }

    @Test
    fun it_Given_a_nonempty_list_of_Nulls_test_should_fail() {
        Assertions.assertThatThrownBy {
            assert(listOf(null)).isEmpty()
        }.hasMessage("expected to be empty but was:<[null]>")
    }

    @Test
    fun it_Given_a_nonempty_list_of_mixed_types_test_should_fail() {
        Assertions.assertThatThrownBy {
            assert(listOf(1, 1.09, "awesome!", true)).isEmpty()
        }.hasMessage("expected to be empty but was:<[1, 1.09, \"awesome!\", true]>")
    }

    @Test
    fun it_Given_two_nonempty_lists_test_should_fail_with_only_one_error_message_per_failed_assertion() {
        Assertions.assertThatThrownBy {
            assertAll {
                assert(listOf(1, 2, 3, 4)).isEmpty()
                assert(listOf(1, 1.09, "awesome!", true)).isEmpty()
            }
        }.hasMessage("The following 2 assertions failed:\n"
                + "- expected to be empty but was:<[1, 2, 3, 4]>\n"
                + "- expected to be empty but was:<[1, 1.09, \"awesome!\", true]>")
    }

    @Test
    fun it_Given_one_empty_list_and_two_nonempty_lists_test_should_fail_for_only_the_nonempty_list_with_only_one_error_message_per_failed_assertion() {
        Assertions.assertThatThrownBy {
            assertAll {
                assert(emptyList<Any?>()).isEmpty()
                assert(listOf(1, 2, 3, 4)).isEmpty()
                assert(listOf(1, 1.09, "awesome!", true)).isEmpty()
            }
        }.hasMessage("The following 2 assertions failed:\n"
                + "- expected to be empty but was:<[1, 2, 3, 4]>\n"
                + "- expected to be empty but was:<[1, 1.09, \"awesome!\", true]>")
    }
}

class CollectionSpec_a_collection_On_isNotEmpty() {
    @Test
    fun it_Given_a_list_of_Ints_test_should_pass() {
        assert(listOf(1, 2, 3, 4)).isNotEmpty()
    }

    @Test
    fun it_Given_a_list_filled_with_a_null_test_should_pass() {
        assert(listOf(null)).isNotEmpty()
    }

    @Test
    fun it_Given_a_list_of_mixed_types_test_should_pass() {
        assert(listOf(1, 1.09, "awesome!", true)).isNotEmpty()
    }

    @Test
    fun it_Given_an_empty_list_passed_directly_in_test_should_fail() {
        Assertions.assertThatThrownBy {
            assert(emptyList<Any?>()).isNotEmpty()
        }.hasMessage("expected to not be empty")
    }

    @Test
    fun it_Given_a_list_that_hasnt_been_populated_test_should_fail() {
        Assertions.assertThatThrownBy {
            val anEmptyList: List<Any?> = listOf()
            assert(anEmptyList).isNotEmpty()
        }.hasMessage("expected to not be empty")
    }

    @Test
    fun it_Given_two_empty_lists_test_should_fail_with_only_one_error_message_per_failed_assertion() {
        Assertions.assertThatThrownBy {
            assertAll {
                assert(emptyList<Any?>()).isNotEmpty()
                val anEmptyList: List<Any?> = listOf()
                assert(anEmptyList).isNotEmpty()
            }
        }.hasMessage("The following 2 assertions failed:\n"
                + "- expected to not be empty\n"
                + "- expected to not be empty")
    }

    @Test
    fun it_Given_one_empty_list_and_two_empty_lists_test_should_fail_for_only_for_the_empty_lists_with_only_one_error_message_per_failed_assertion() {
        Assertions.assertThatThrownBy {
            assertAll {
                assert(emptyList<Any?>()).isNotEmpty()
                val anEmptyList: List<Any?> = listOf()
                assert(listOf(1, 2, 3)).isNotEmpty()
                assert(anEmptyList).isNotEmpty()
            }
        }.hasMessage("The following 2 assertions failed:\n"
                + "- expected to not be empty\n"
                + "- expected to not be empty")
    }
}

class CollectionSpec_a_collection_On_isNullOrEmpty() {
    @Test
    fun it_Given_a_forced_null_list_test_should_pass() {
        // Need to force a null here
        val nullList: List<Any?>? = null
        assert(nullList).isNullOrEmpty()
    }

    @Test
    fun it_Given_an_empty_list_test_should_pass() {
        assert(emptyList<Any?>()).isNullOrEmpty()
    }

    @Test
    fun it_Given_a_nonempty_list_of_Ints_test_should_fail() {
        Assertions.assertThatThrownBy {
            assert(listOf(1, 2, 3)).isNullOrEmpty()
        }.hasMessage("expected to be null or empty but was:<[1, 2, 3]>")
    }

    @Test
    fun it_Given_a_nonempty_list_with_a_null_test_should_fail() {
        Assertions.assertThatThrownBy {
            assert(listOf(null)).isNullOrEmpty()
        }.hasMessage("expected to be null or empty but was:<[null]>")
    }

    @Test
    fun it_Given_two_nonempty_nonnull_lists_test_should_fail_with_only_one_error_message_per_failed_assertion() {
        Assertions.assertThatThrownBy {
            assertAll {
                assert(listOf(1, 2, 3)).isNullOrEmpty()
                assert(listOf(43, true, "awesome!")).isNullOrEmpty()
            }
        }.hasMessage("The following 2 assertions failed:\n"
                + "- expected to be null or empty but was:<[1, 2, 3]>\n"
                + "- expected to be null or empty but was:<[43, true, \"awesome!\"]>")
    }

    @Test
    fun it_Given_one_empty_list_and_two_nonempty_nonnull_lists_test_should_fail_only_for_the_nonempty_nonnull_lists() {
        Assertions.assertThatThrownBy {
            assertAll {
                assert(listOf(1, 2, 3)).isNullOrEmpty()
                assert(listOf(43, true, "awesome!")).isNullOrEmpty()
                assert(emptyList<Any?>()).isNullOrEmpty()
            }
        }.hasMessage("The following 2 assertions failed:\n"
                + "- expected to be null or empty but was:<[1, 2, 3]>\n"
                + "- expected to be null or empty but was:<[43, true, \"awesome!\"]>")
    }
}

class CollectionSpec_a_collection_On_hasSize() {
    @Test
    fun it_Given_a_list_that_has_4_Ints_test_should_pass() {
        assert(listOf(1, 2, 3, 4)).hasSize(4)
    }

    @Test
    fun it_Given_an_empty_list_test_should_pass() {
        assert(emptyList<Any?>()).hasSize(0)
    }

    @Test
    fun it_Given_a_list_with_one_Null_object_test_should_pass() {
        assert(listOf(null)).hasSize(1)
    }

    @Test
    fun it_Given_a_list_with_4_mixed_type_elements_test_should_pass() {
        assert(listOf(1, 1.09, "awesome!", true)).hasSize(4)
    }

    @Test
    fun it_Given_a_list_of_3_Ints_test_should_fail_when_expecting_4_elements() {
        Assertions.assertThatThrownBy {
            assert(listOf(1, 2, 3)).hasSize(4)
        }.hasMessage("expected [size]:<[4]> but was:<[3]> ([1, 2, 3])")
    }

    @Test
    fun it_Given_an_empty_list_test_should_fail_when_expecting_more_than_0_elements() {
        Assertions.assertThatThrownBy {
            assert(emptyList<Any?>()).hasSize(1)
        }.hasMessage("expected [size]:<[1]> but was:<[0]> ([])")
    }

    @Test
    fun it_Given_a_list_of_1_null_test_should_fail_when_expecting_0_elements() {
        Assertions.assertThatThrownBy {
            assert(listOf(null)).hasSize(0)
        }.hasMessage("expected [size]:<[0]> but was:<[1]> ([null])")
    }

    @Test
    fun it_Given_two_lists_expecting_incorrect_size_for_each_test_should_fail_with_only_one_error_message_per_failed_assertion() {
        Assertions.assertThatThrownBy {
            assertAll {
                assert(listOf(1, 2, 3)).hasSize(4)
                assert(listOf(43, true, "awesome!")).hasSize(1)
            }
        }.hasMessage("The following 2 assertions failed:\n"
                + "- expected [size]:<[4]> but was:<[3]> ([1, 2, 3])\n"
                + "- expected [size]:<[1]> but was:<[3]> ([43, true, \"awesome!\"])")
    }

    @Test
    fun it_Given_one_list_expecting_the_correct_size_and_two_lists_expecting_incorrect_size_for_each_test_should_fail_with_only_one_error_message_per_failed_assertion() {
        Assertions.assertThatThrownBy {
            assertAll {
                assert(listOf(1, 2, 3)).hasSize(4)
                assert(listOf("this", "test")).hasSize(2)
                assert(listOf(43, true, "awesome!")).hasSize(1)
            }
        }.hasMessage("The following 2 assertions failed:\n"
                + "- expected [size]:<[4]> but was:<[3]> ([1, 2, 3])\n"
                + "- expected [size]:<[1]> but was:<[3]> ([43, true, \"awesome!\"])")
    }
}

class CollectionSpec_a_collection_On_hasSameSizeAs() {
    @Test
    fun it_Given_two_lists_with_4_Ints_each_test_should_pass() {
        assert(listOf(1, 2, 3, 4)).hasSameSizeAs(listOf(43, 2, 3, 3))
    }

    @Test
    fun it_Given_two_empty_lists_test_should_pass() {
        assert(emptyList<Any?>()).hasSameSizeAs(emptyList<Any?>())
    }

    @Test
    fun it_Given_two_lists_each_with_one_Null_test_should_pass() {
        assert(listOf(null)).hasSameSizeAs(listOf(null))
    }

    @Test
    fun it_Given_two_lists_each_with_4_elements_of_mixed_types_test_should_pass() {
        assert(listOf(1, 1.09, "awesome!", true)).hasSameSizeAs(listOf(1.09, "whoa!", false, true))
    }

    @Test
    fun it_Given_a_list_with_3_elements_and_a_list_with_1_element_test_should_fail() {
        Assertions.assertThatThrownBy {
            assert(listOf(1, 2, 3)).hasSameSizeAs(listOf(43))
        }.hasMessage("expected to have same size as:<[43]> (1) but was size:(3)")
    }

    @Test
    fun it_Given_an_empty_list_and_a_list_with_1_element_test_should_fail() {
        Assertions.assertThatThrownBy {
            assert(emptyList<Any?>()).hasSameSizeAs(listOf(43))
        }.hasMessage("expected to have same size as:<[43]> (1) but was size:(0)")
    }

    @Test
    fun it_Given_a_list_with_1_null_element_and_a_list_with_2_mixed_type_elements_test_should_fail() {
        Assertions.assertThatThrownBy {
            assert(listOf(null)).hasSameSizeAs(listOf(43, "whoa!"))
        }.hasMessage("expected to have same size as:<[43, \"whoa!\"]> (2) but was size:(1)")
    }

    @Test
    fun it_Given_two_failing_assertions_test_should_fail_with_only_one_error_message_per_failed_assertion() {
        Assertions.assertThatThrownBy {
            assertAll {
                assert(listOf(1, 2, 3)).hasSameSizeAs(listOf(1, 2, 3, 4))
                assert(listOf(43, true, "awesome!")).hasSameSizeAs(listOf(true, true))
            }
        }.hasMessage("The following 2 assertions failed:\n"
                + "- expected to have same size as:<[1, 2, 3, 4]> (4) but was size:(3)\n"
                + "- expected to have same size as:<[true, true]> (2) but was size:(3)")
    }

    @Test
    fun it_Given_one_passing_and_two_failing_assertions_test_should_fail_with_only_one_error_message_per_failed_assertion() {
        Assertions.assertThatThrownBy {
            assertAll {
                assert(listOf(1, 2, 3)).hasSameSizeAs(listOf(1, 2, 3, 4))
                assert(listOf(1, 2, 3, 4)).hasSameSizeAs(listOf("this", "test", "passes", true))
                assert(listOf(43, true, "awesome!")).hasSameSizeAs(listOf(true, true))
            }
        }.hasMessage("The following 2 assertions failed:\n"
                + "- expected to have same size as:<[1, 2, 3, 4]> (4) but was size:(3)\n"
                + "- expected to have same size as:<[true, true]> (2) but was size:(3)")
    }
}

class CollectionSpec_a_collection_On_containsNone() {
    @Test
    fun it_Given_a_list_with_more_elements_than_the_passed_in_number_of_elements_expected_test_should_pass_if_the_list_does_not_contain_any_of_the_elements_that_are_expected() {
        assert(listOf(1, 2, 3, 4)).containsNone(5, 6, 7)
    }

    @Test
    fun it_Given_a_list_with_less_elements_than_the_passed_in_number_of_elements_expected_test_should_pass_if_the_list_does_not_contain_any_of_the_elements_that_are_expected() {
        assert(listOf(1, 2, 3)).containsNone(4, 5, 6, 7)
    }

    @Test
    fun it_Given_an_empty_list_test_should_pass_when_expecting_it_to_contain_anything() {
        assert(emptyList<Any?>()).containsNone(4)
    }

    @Test
    fun it_Given_a_list_with_some_number_of_elements_test_should_pass_when_expecting_nothing() {
        assert(listOf(3, 4)).containsNone()
    }

    @Test
    fun it_Given_a_list_with_multiple_types_test_should_pass_when_expecting_none_of_the_elements_that_are_being_checked_for() {
        assert(listOf(1, 1.09, "awesome!", true)).containsNone(43, 1.43, "awesome")
    }

    @Test
    fun it_Given_a_list_with_more_elements_passed_in_than_the_number_of_elements_expected_test_should_fail_if_the_list_contains_any_of_the_elements_that_are_expected() {
        Assertions.assertThatThrownBy {
            assert(listOf(1, 2, 3)).containsNone(4, 5, 6, 1)
        }.hasMessage("expected to contain none of:<[4, 5, 6, 1]> but was:<[1, 2, 3]>")
    }

    @Test
    fun it_Given_a_list_with_less_elements_passed_in_than_the_number_of_elements_expected_test_should_fail_if_the_list_contains_any_of_the_elements_that_are_expected() {
        Assertions.assertThatThrownBy {
            assert(listOf(1, 2, 3, 4)).containsNone(8, 0, 4)
        }.hasMessage("expected to contain none of:<[8, 0, 4]> but was:<[1, 2, 3, 4]>")
    }

    @Test
    fun it_Given_a_list_of_multiple_types_containing_more_elements_passed_in_than_the_number_of_elements_expected_test_should_fail_if_the_list_contains_any_of_the_elements_that_are_expected() {
        Assertions.assertThatThrownBy {
            assert(listOf(1, 1.09, "awesome!", true)).containsNone(true, 43, "potato")
        }.hasMessage("expected to contain none of:<[true, 43, \"potato\"]> but was:<[1, 1.09, \"awesome!\", true]>")
    }

    @Test
    fun it_Given_multiple_assertions_which_should_fail_test_should_fail_with_only_one_error_message_per_failed_assertion() {
        Assertions.assertThatThrownBy {
            assertAll {
                assert(listOf(1, 2, 3)).containsNone(5, 6, 7, 1)
                assert(listOf("this", "is", "awesome!")).containsNone(true, 4, "awesome!")
            }
        }.hasMessage("The following 2 assertions failed:\n"
                + "- expected to contain none of:<[5, 6, 7, 1]> but was:<[1, 2, 3]>\n"
                + "- expected to contain none of:<[true, 4, \"awesome!\"]> but was:<[\"this\", \"is\", \"awesome!\"]>")
    }

    @Test
    fun it_Given_multiple_assertions_which_should_fail_and_some_that_should_pass_test_should_fail_with_only_one_error_message_per_failed_assertion() {
        Assertions.assertThatThrownBy {
            assertAll {
                assert(listOf(1, 2, 3)).containsNone(5, 6, 7, 1) // Should fail
                assert(listOf(1, 2, 3)).containsNone(4, 5, 6, 7) // Should pass
                assert(listOf("this", "is", "awesome!")).containsNone(true, 4, "awesome!") // Should fail
                assert(listOf(1, 2, 3, 4)).containsNone(5, 6, 7) // Should pass
            }
        }.hasMessage("The following 2 assertions failed:\n"
                + "- expected to contain none of:<[5, 6, 7, 1]> but was:<[1, 2, 3]>\n"
                + "- expected to contain none of:<[true, 4, \"awesome!\"]> but was:<[\"this\", \"is\", \"awesome!\"]>")
    }
}

class CollectionSpec_a_collection_On_containsAll() {
    @Test
    fun it_Given_a_list_of_multiple_elements_test_should_pass_when_containing_all_elements_expected_regardless_of_order() {
        assert(listOf(1, 2, 3)).containsAll(3, 2, 1)
    }

    @Test
    fun it_Given_an_empty_list_test_should_pass_when_expecting_nothing() {
        assert(emptyList<Any?>()).containsAll()
    }

    @Test
    fun it_Given_a_list_of_multiple_types_of_elements_test_should_pass_when_containing_all_elements_expected_regardless_of_order() {
        assert(listOf(1, 1.09, "awesome!", true)).containsAll(1, 1.09, "awesome!", true)
    }

    @Test
    fun it_Given_a_list_of_multiple_elements_with_more_elements_given_than_expected_test_should_fail_when_not_containing_all_elements_expected_regardless_of_order() {
        Assertions.assertThatThrownBy {
            assert(listOf(1, 2, 3)).containsAll(4, 3, 1, 2)
        }.hasMessage("expected to contain all:<[4, 3, 1, 2]> but was:<[1, 2, 3]>")
    }

    @Test
    fun it_Given_a_list_of_multiple_elements_with_less_elements_given_than_expected_test_should_fail_when_not_containing_all_elements_expected_regardless_of_order() {
        Assertions.assertThatThrownBy {
            assert(listOf(1, 2, 4, 5)).containsAll(2, 1, 3)
        }.hasMessage("expected to contain all:<[2, 1, 3]> but was:<[1, 2, 4, 5]>")
    }

    @Test
    fun it_Given_an_empty_list_test_should_fail_when_expecting_anything() {
        Assertions.assertThatThrownBy {
            assert(emptyList<Any?>()).containsAll(1, 2, 3)
        }.hasMessage("expected to contain all:<[1, 2, 3]> but was:<[]>")
    }

    @Test
    fun it_Given_a_list_of_multiple_types_test_should_fail_when_not_containing_all_elements_expected_regardless_of_order() {
        Assertions.assertThatThrownBy {
            assert(listOf(1, "is", "awesome!")).containsAll("this", 4, "awesome!")
        }.hasMessage("expected to contain all:<[\"this\", 4, \"awesome!\"]> but was:<[1, \"is\", \"awesome!\"]>")
    }

    @Test
    fun it_Given_multiple_assertions_which_should_fail_test_should_fail_with_only_one_error_message_per_failed_assertion() {
        Assertions.assertThatThrownBy {
            assertAll {
                assert(listOf(1, 2, 3)).containsAll(5, 6, 7)
                assert(listOf(1, "is", "awesome!")).containsAll("this", 4, "awesome!")
            }
        }.hasMessage("The following 2 assertions failed:\n"
                + "- expected to contain all:<[5, 6, 7]> but was:<[1, 2, 3]>\n"
                + "- expected to contain all:<[\"this\", 4, \"awesome!\"]> but was:<[1, \"is\", \"awesome!\"]>")
    }

    @Test
    fun it_Given_multiple_assertions_which_should_fail_and_some_that_should_pass_test_should_fail_with_only_one_error_message_per_failed_assertion() {
        Assertions.assertThatThrownBy {
            assertAll {
                assert(listOf(1, 2, 3)).containsAll(3, 2, 1) // should pass
                assert(listOf(1, 2, 3)).containsAll(5, 6, 7) // should fail
                assert(listOf("this", "is", "awesome!")).containsAll("this", 4, "awesome!") // should fail
                assert(listOf(1, 1.09, "awesome!", true)).containsAll(1, 1.09, "awesome!", true) // should pass
            }
        }.hasMessage("The following 2 assertions failed:\n"
                + "- expected to contain all:<[5, 6, 7]> but was:<[1, 2, 3]>\n"
                + "- expected to contain all:<[\"this\", 4, \"awesome!\"]> but was:<[\"this\", \"is\", \"awesome!\"]>")
    }
}

class CollectionSpec_a_collection_On_containsExactly() {
    @Test
    fun it_Given_a_list_of_multiple_elements_test_should_pass_when_containing_all_elements_expected_exactly_in_order() {
        assert(listOf(1, 2, 3)).containsExactly(1, 2, 3)
    }

    @Test
    fun it_Given_an_empty_list_test_should_pass_when_expecting_nothing() {
        assert(emptyList<Any?>()).containsExactly()
    }

    @Test
    fun it_Given_a_list_of_multiple_types_of_elements_test_should_pass_when_containing_all_elements_expected_exactly_in_order() {
        assert(listOf(1, 1.09, "awesome!", true)).containsExactly(1, 1.09, "awesome!", true)
    }

    @Test
    fun it_Given_a_list_of_multiple_elements_with_less_elements_given_than_expected_test_should_fail_when_not_containing_all_elements_expected_exactly_in_order() {
        Assertions.assertThatThrownBy {
            assert(listOf(1, 2, 3)).containsExactly(1, 2, 3, 4)
        }.hasMessage("expected to contain exactly:<[1, 2, 3, 4]> but was:<[1, 2, 3]>")
    }

    @Test
    fun it_Given_a_list_of_multiple_elements_with_more_elements_given_than_expected_test_should_fail_when_not_containing_all_elements_expected_exactly_in_order() {
        Assertions.assertThatThrownBy {
            assert(listOf(1, 2, 3, 4)).containsExactly(1, 2, 3)
        }.hasMessage("expected to contain exactly:<[1, 2, 3]> but was:<[1, 2, 3, 4]>")
    }

    @Test
    fun it_Given_an_empty_list_test_should_fail_when_expecting_anything() {
        Assertions.assertThatThrownBy {
            assert(emptyList<Any?>()).containsExactly(1, 2, 3)
        }.hasMessage("expected to contain exactly:<[1, 2, 3]> but was:<[]>")
    }

    @Test
    fun it_Given_a_list_of_multiple_types_test_should_fail_when_not_containing_all_elements_expected_exactly_in_order() {
        Assertions.assertThatThrownBy {
            assert(listOf(1, "is", "awesome!")).containsExactly("this", 4, "awesome!")
        }.hasMessage("expected to contain exactly:<[\"this\", 4, \"awesome!\"]> but was:<[1, \"is\", \"awesome!\"]>")
    }

    @Test
    fun it_Given_multiple_assertions_which_should_fail_test_should_fail_with_only_one_error_message_per_failed_assertion() {
        Assertions.assertThatThrownBy {
            assertAll {
                assert(listOf(1, 2, 3)).containsExactly(5, 6, 7) // should fail
                assert(listOf(1, "is", "awesome!")).containsExactly("this", 4, "awesome!") // should fail
            }
        }.hasMessage("The following 2 assertions failed:\n"
                + "- expected to contain exactly:<[5, 6, 7]> but was:<[1, 2, 3]>\n"
                + "- expected to contain exactly:<[\"this\", 4, \"awesome!\"]> but was:<[1, \"is\", \"awesome!\"]>")
    }

    @Test
    fun it_Given_multiple_assertions_which_should_fail_and_some_that_should_pass_test_should_fail_with_only_one_error_message_per_failed_assertion() {
        Assertions.assertThatThrownBy {
            assertAll {
                assert(listOf(1, 2, 3)).containsExactly(1, 2, 3) // should pass
                assert(listOf(1, 2, 3)).containsExactly(5, 6, 7) // should fail
                assert(emptyList<Any?>()).containsExactly() // should pass
                assert(listOf(1, "is", "awesome!")).containsExactly("this", 4, "awesome!") // should fail
            }
        }.hasMessage("The following 2 assertions failed:\n"
                + "- expected to contain exactly:<[5, 6, 7]> but was:<[1, 2, 3]>\n"
                + "- expected to contain exactly:<[\"this\", 4, \"awesome!\"]> but was:<[1, \"is\", \"awesome!\"]>")
    }
}
