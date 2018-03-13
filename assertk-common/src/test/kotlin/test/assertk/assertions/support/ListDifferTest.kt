package test.assertk.assertions.support

import assertk.assertions.support.ListDiffer
import kotlin.test.assertEquals
import kotlin.test.Test

class ListDifferTest {
    @Test
    fun empty_diff() {
        val diff = ListDiffer.diff(emptyList<Any>(), emptyList<Any>())

        assertEquals(emptyList(), diff)
    }

    @Test
    fun single_item_no_change() {
        val diff = ListDiffer.diff(listOf(1), listOf(1))

        assertEquals(listOf(ListDiffer.Edit.Eq(oldIndex = 0, oldValue = 1, newIndex = 0, newValue = 1)), diff)
    }

    @Test
    fun singe_insert() {
        val diff = ListDiffer.diff(emptyList<Int>(), listOf(1))

        assertEquals(listOf(ListDiffer.Edit.Ins(newIndex = 0, newValue = 1)), diff)
    }

    @Test
    fun singe_delete() {
        val diff = ListDiffer.diff(listOf(1), emptyList<Int>())

        assertEquals(listOf(ListDiffer.Edit.Del(oldIndex = 0, oldValue = 1)), diff)
    }

    @Test
    fun single_insert_middle() {
        val diff = ListDiffer.diff(listOf(1, 3), listOf(1, 2, 3))

        assertEquals(
                listOf(
                        ListDiffer.Edit.Eq(oldIndex = 0, oldValue = 1, newIndex = 0, newValue = 1),
                        ListDiffer.Edit.Ins(newIndex = 1, newValue = 2),
                        ListDiffer.Edit.Eq(oldIndex = 1, oldValue = 3, newIndex = 2, newValue = 3)
                ), diff
        )
    }

    @Test
    fun singe_delete_middle() {
        val diff = ListDiffer.diff(listOf(1, 2, 3), listOf(1, 3))

        assertEquals(
                listOf(
                        ListDiffer.Edit.Eq(oldIndex = 0, oldValue = 1, newIndex = 0, newValue = 1),
                        ListDiffer.Edit.Del(oldIndex = 1, oldValue = 2),
                        ListDiffer.Edit.Eq(oldIndex = 2, oldValue = 3, newIndex = 1, newValue = 3)
                ), diff
        )
    }

    @Test
    fun single_delete_multiple_inserts() {
        val diff = ListDiffer.diff(listOf(3), listOf(1, 2))

        assertEquals(
                listOf(
                        ListDiffer.Edit.Del(oldIndex = 0, oldValue = 3),
                        ListDiffer.Edit.Ins(newIndex = 0, newValue = 1),
                        ListDiffer.Edit.Ins(newIndex = 1, newValue = 2)
                ), diff
        )
    }
}
