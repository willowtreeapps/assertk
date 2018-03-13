package test.assertk.assertions

import assertk.assert
import assertk.assertAll
import assertk.assertions.*
import test.assertk.Assertions
import kotlin.test.Test

class IterableSpec_an_iterable_On_contains() {
    @Test
    fun it_Given_a_list_that_contains_multiple_Ints__plus_test_should_pass_when_expecting_an_Int_that_is_actually_contained_in_the_list() {
        assert(listOf(1, 2, 3, 4) as Iterable<Int>).contains(3)
    }

    @Test
    fun it_Given_a_list_which_contains_a_null__plus_test_should_pass_when_expecting_the_list_to_contain_a_null() {
        assert(listOf(null) as Iterable<Int?>).contains(null)
    }

    @Test
    fun it_Given_a_list_that_contains_multiple_types__plus_test_should_pass_when_expecting_an_element_that_is_actually_contained_in_the_list() {
        assert(listOf(1, 1.09, "awesome!", true) as Iterable<Any>).contains(1.09)
    }

    @Test
    fun it_Given_a_list_that_contains_multiple_Ints__plus_test_should_fail_when_expecting_an_Int_that_is_not_contained_in_the_list() {
        Assertions.assertThatThrownBy {
            assert(listOf(1, 2, 3) as Iterable<Int>).contains(43)
        }.hasMessage("expected to contain:<43> but was:<[1, 2, 3]>")
    }

    @Test
    fun it_Given_an_empty_list__plus_test_should_fail_when_expecting_anything_to_be_contained_within_the_list() {
        Assertions.assertThatThrownBy {
            assert(emptyList<Any?>() as Iterable<Any?>).contains(null)
        }.hasMessage("expected to contain:<null> but was:<[]>")
    }

    @Test
    fun it_Given_a_list_that_contains_multiple_types__plus_test_should_fail_when_expecting_anything_that_is_not_contained_in_the_list() {
        Assertions.assertThatThrownBy {
            assert(listOf(1, 1.09, "awesome!", true) as Iterable<Any>).contains(43)
        }.hasMessage("expected to contain:<43> but was:<[1, 1.09, \"awesome!\", true]>")
    }

    @Test
    fun it_Given_multiple_assertions_which_should_fail__plus_test_should_fail_with_only_one_error_message_per_failed_assertion() {
        Assertions.assertThatThrownBy {
            assertAll {
                assert(listOf(1, 2, 3) as Iterable<Int>).contains(43)
                assert(listOf(43, true, "awesome!") as Iterable<Any>).contains(false)
            }
        }.hasMessage("The following 2 assertions failed:\n"
                + "- expected to contain:<43> but was:<[1, 2, 3]>\n"
                + "- expected to contain:<false> but was:<[43, true, \"awesome!\"]>")
    }

    @Test
    fun it_Given_multiple_assertions_which_should_fail_and_some_that_should_pass__plus_test_should_fail_with_only_one_error_message_per_failed_assertion() {
        Assertions.assertThatThrownBy {
            assertAll {
                assert(listOf(1, "test", true) as Iterable<Any>).contains(true)
                assert(listOf(1, 2, 3) as Iterable<Int>).contains(43)
                assert(listOf(43, true, "awesome!") as Iterable<Any>).contains(false)
            }
        }.hasMessage("The following 2 assertions failed:\n"
                + "- expected to contain:<43> but was:<[1, 2, 3]>\n"
                + "- expected to contain:<false> but was:<[43, true, \"awesome!\"]>")
    }
}

class IterableSpec_an_iterable_On_doesNotContain() {
    @Test
    fun it_Given_a_list_of_Ints__plus_test_should_pass_when_expecting_an_Int_that_is_not_in_the_list() {
        assert(listOf(1, 2, 3, 4) as Iterable<Int>).doesNotContain(43)
    }

    @Test
    fun it_Given_an_empty_list__plus_test_should_pass_when_expecting_anything_to_be_contained_within_the_list() {
        assert(emptyList<Any?>() as Iterable<Any?>).doesNotContain(4)
    }

    @Test
    fun it_Given_a_list_with_elements_of_multiple_types__plus_test_should_pass_when_expecting_anything_which_is_not_contained_within_the_list() {
        assert(listOf(1, 1.09, "awesome!", true) as Iterable<Any>).doesNotContain(43)
    }

    @Test
    fun it_Given_a_list_with_multiple_elements_of_the_same_type__plus_test_should_fail_when_expecting_an_element_which_is_contained_in_the_list() {
        Assertions.assertThatThrownBy {
            assert(listOf(1, 2, 3) as Iterable<Int>).doesNotContain(2)
        }.hasMessage("expected to not contain:<2> but was:<[1, 2, 3]>")
    }

    @Test
    fun it_Given_a_list_of_multiple_types__plus_test_should_fail_when_expecting_an_element_which_is_contained_in_the_list() {
        Assertions.assertThatThrownBy {
            assert(listOf(1, 1.09, "awesome!", true) as Iterable<Any?>).doesNotContain(1.09)
        }.hasMessage("expected to not contain:<1.09> but was:<[1, 1.09, \"awesome!\", true]>")
    }

    @Test
    fun it_Given_multiple_assertions_which_should_fail__plus_test_should_fail_with_only_one_error_message_per_failed_assertion() {
        Assertions.assertThatThrownBy {
            assertAll {
                assert(listOf(1, 2, 3) as Iterable<Int>).doesNotContain(3)
                assert(listOf(43, true, "awesome!") as Iterable<Any>).doesNotContain(true)
            }
        }.hasMessage("The following 2 assertions failed:\n"
                + "- expected to not contain:<3> but was:<[1, 2, 3]>\n"
                + "- expected to not contain:<true> but was:<[43, true, \"awesome!\"]>")
    }

    @Test
    fun it_Given_multiple_assertions_which_should_fail_and_some_that_should_pass__plus_test_should_fail_with_only_one_error_message_per_failed_assertion() {
        Assertions.assertThatThrownBy {
            assertAll {
                assert(listOf(1, 2, 3) as Iterable<Int>).doesNotContain(3)
                assert(listOf("this", "passes", true) as Iterable<Any>).doesNotContain(false)
                assert(listOf(43, true, "awesome!") as Iterable<Any>).doesNotContain(true)
            }
        }.hasMessage("The following 2 assertions failed:\n"
                + "- expected to not contain:<3> but was:<[1, 2, 3]>\n"
                + "- expected to not contain:<true> but was:<[43, true, \"awesome!\"]>")
    }
}

class IterableSpec_an_iterable_On_each() {
    @Test
    fun it_Given_an_empty_list_test_should_pass() {
        assert(emptyList<String>() as Iterable<String>).each {
            it.isEqualTo("test")
        }
    }

    @Test
    fun it_Given_a_list_with_one_item_test_should_pass_for_that_item() {
        assert(listOf("test") as Iterable<String>).each {
            it.isEqualTo("test")
        }
    }

    @Test
    fun it_Given_a_list_with_one_item_test_should_fail_for_that_item() {
        Assertions.assertThatThrownBy {
            assert(listOf("test") as Iterable<String>).each {
                it.isEqualTo("wrong")
            }
        }.hasMessage("expected [[0]]:<\"[wrong]\"> but was:<\"[test]\"> ([\"test\"])")
    }

    @Test
    fun it_Give_a_list_with_many_items_test_should_pass_for_all_items() {
        assert(listOf("one", "two", "six") as Iterable<String>).each {
            it.length().isEqualTo(3)
        }
    }

    @Test
    fun it_Give_a_list_with_many_items_test_should_fail_for_one_of_the_items() {
        Assertions.assertThatThrownBy {
            assert(listOf("one", "two", "three") as Iterable<String>).each {
                it.length().isEqualTo(3)
            }
        }.hasMessage("expected [[2].length]:<[3]> but was:<[5]> ([\"one\", \"two\", \"three\"])")
    }
}
