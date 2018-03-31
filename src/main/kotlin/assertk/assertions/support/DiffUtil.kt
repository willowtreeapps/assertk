/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package assertk.assertions.support

import java.util.ArrayList
import java.util.Arrays
import java.util.Collections
import java.util.Comparator

/**
 * DiffUtil is a utility class that can calculate the difference between two lists and output a
 * list of update operations that converts the first list into the second one.
 *
 *
 * It can be used to calculate updates for a RecyclerView Adapter.
 *
 *
 * DiffUtil uses Eugene W. Myers's difference algorithm to calculate the minimal number of updates
 * to convert one list into another. Myers's algorithm does not handle items that are moved so
 * DiffUtil runs a second pass on the result to detect items that were moved.
 *
 *
 * If the lists are large, this operation may take significant time so you are advised to run this
 * on a background thread, get the [DiffResult] then apply it on the RecyclerView on the main
 * thread.
 *
 *
 * This algorithm is optimized for space and uses O(N) space to find the minimal
 * number of addition and removal operations between the two lists. It has O(N + D^2) expected time
 * performance where D is the length of the edit script.
 *
 *
 * If move detection is enabled, it takes an additional O(N^2) time where N is the total number of
 * added and removed items. If your lists are already sorted by the same constraint (e.g. a created
 * timestamp for a list of posts), you can disable move detection to improve performance.
 *
 *
 * The actual runtime of the algorithm significantly depends on the number of changes in the list
 * and the cost of your comparison methods. Below are some average run times for reference:
 * (The test list is composed of random UUID Strings and the tests are run on Nexus 5X with M)
 *
 *  * 100 items and 10 modifications: avg: 0.39 ms, median: 0.35 ms
 *  * 100 items and 100 modifications: 3.82 ms, median: 3.75 ms
 *  * 100 items and 100 modifications without moves: 2.09 ms, median: 2.06 ms
 *  * 1000 items and 50 modifications: avg: 4.67 ms, median: 4.59 ms
 *  * 1000 items and 50 modifications without moves: avg: 3.59 ms, median: 3.50 ms
 *  * 1000 items and 200 modifications: 27.07 ms, median: 26.92 ms
 *  * 1000 items and 200 modifications without moves: 13.54 ms, median: 13.36 ms
 *
 *
 *
 * Due to implementation constraints, the max size of the list can be 2^26.
 */
internal object DiffUtil {

    private val SNAKE_COMPARATOR = Comparator<Snake> { o1, o2 ->
        val cmpX = o1.x - o2.x
        if (cmpX == 0) o1.y - o2.y else cmpX
    }

    /**
     * Calculates the list of update operations that can covert one list into the other one.
     *
     *
     * If your old and new lists are sorted by the same constraint and items never move (swap
     * positions), you can disable move detection which takes `O(N^2)` time where
     * N is the number of added, moved, removed items.
     *
     * @param cb          The callback that acts as a gateway to the backing list data
     * @param detectMoves True if DiffUtil should try to detect moved items, false otherwise.
     * @return A DiffResult that contains the information about the edit sequence to convert the
     * old list into the new list.
     */
    @JvmOverloads fun calculateDiff(cb: Callback, detectMoves: Boolean = true): DiffResult {
        val oldSize = cb.oldListSize
        val newSize = cb.newListSize
        val snakes = ArrayList<Snake>()
        // instead of a recursive implementation, we keep our own stack to avoid potential stack
        // overflow exceptions
        val stack = ArrayList<Range>()
        stack.add(Range(0, oldSize, 0, newSize))
        val max = oldSize + newSize + Math.abs(oldSize - newSize)
        // allocate forward and backward k-lines. K lines are diagonal lines in the matrix. (see the
        // paper for details)
        // These arrays lines keep the max reachable position for each k-line.
        val forward = IntArray(max * 2)
        val backward = IntArray(max * 2)
        // We pool the ranges to avoid allocations for each recursive call.
        val rangePool = ArrayList<Range>()
        while (!stack.isEmpty()) {
            val range = stack.removeAt(stack.size - 1)
            val snake = diffPartial(
                cb, range.oldListStart, range.oldListEnd,
                range.newListStart, range.newListEnd, forward, backward, max
            )
            if (snake != null) {
                if (snake.size > 0) {
                    snakes.add(snake)
                }
                // offset the snake to convert its coordinates from the Range's area to global
                snake.x += range.oldListStart
                snake.y += range.newListStart
                // add new ranges for left and right
                val left = if (rangePool.isEmpty())
                    Range()
                else
                    rangePool.removeAt(
                        rangePool.size - 1
                    )
                left.oldListStart = range.oldListStart
                left.newListStart = range.newListStart
                if (snake.reverse) {
                    left.oldListEnd = snake.x
                    left.newListEnd = snake.y
                } else {
                    if (snake.removal) {
                        left.oldListEnd = snake.x - 1
                        left.newListEnd = snake.y
                    } else {
                        left.oldListEnd = snake.x
                        left.newListEnd = snake.y - 1
                    }
                }
                stack.add(left)
                // re-use range for right

                if (snake.reverse) {
                    if (snake.removal) {
                        range.oldListStart = snake.x + snake.size + 1
                        range.newListStart = snake.y + snake.size
                    } else {
                        range.oldListStart = snake.x + snake.size
                        range.newListStart = snake.y + snake.size + 1
                    }
                } else {
                    range.oldListStart = snake.x + snake.size
                    range.newListStart = snake.y + snake.size
                }
                stack.add(range)
            } else {
                rangePool.add(range)
            }
        }
        // sort snakes
        Collections.sort(snakes, SNAKE_COMPARATOR)
        return DiffResult(cb, snakes, forward, backward, detectMoves)
    }

    private fun diffPartial(
        cb: Callback, startOld: Int, endOld: Int,
        startNew: Int, endNew: Int, forward: IntArray, backward: IntArray, kOffset: Int
    ): Snake? {
        val oldSize = endOld - startOld
        val newSize = endNew - startNew
        if (endOld - startOld < 1 || endNew - startNew < 1) {
            return null
        }
        val delta = oldSize - newSize
        val dLimit = (oldSize + newSize + 1) / 2
        Arrays.fill(forward, kOffset - dLimit - 1, kOffset + dLimit + 1, 0)
        Arrays.fill(backward, kOffset - dLimit - 1 + delta, kOffset + dLimit + 1 + delta, oldSize)
        val checkInFwd = delta % 2 != 0
        for (d in 0..dLimit) {
            run {
                var k = -d
                while (k <= d) {
                    // find forward path
                    // we can reach k from k - 1 or k + 1. Check which one is further in the graph
                    var x: Int
                    val removal: Boolean
                    if (k == -d || k != d && forward[kOffset + k - 1] < forward[kOffset + k + 1]) {
                        x = forward[kOffset + k + 1]
                        removal = false
                    } else {
                        x = forward[kOffset + k - 1] + 1
                        removal = true
                    }
                    // set y based on x
                    var y = x - k
                    // move diagonal as long as items match
                    while (x < oldSize && y < newSize
                        && cb.areItemsTheSame(startOld + x, startNew + y)
                    ) {
                        x++
                        y++
                    }
                    forward[kOffset + k] = x
                    if (checkInFwd && k >= delta - d + 1 && k <= delta + d - 1) {
                        if (forward[kOffset + k] >= backward[kOffset + k]) {
                            val outSnake = Snake()
                            outSnake.x = backward[kOffset + k]
                            outSnake.y = outSnake.x - k
                            outSnake.size = forward[kOffset + k] - backward[kOffset + k]
                            outSnake.removal = removal
                            outSnake.reverse = false
                            return outSnake
                        }
                    }
                    k += 2
                }
            }
            var k = -d
            while (k <= d) {
                // find reverse path at k + delta, in reverse
                val backwardK = k + delta
                var x: Int
                val removal: Boolean
                if (backwardK == d + delta || backwardK != -d + delta && backward[kOffset + backwardK - 1] < backward[kOffset + backwardK + 1]) {
                    x = backward[kOffset + backwardK - 1]
                    removal = false
                } else {
                    x = backward[kOffset + backwardK + 1] - 1
                    removal = true
                }
                // set y based on x
                var y = x - backwardK
                // move diagonal as long as items match
                while (x > 0 && y > 0
                    && cb.areItemsTheSame(startOld + x - 1, startNew + y - 1)
                ) {
                    x--
                    y--
                }
                backward[kOffset + backwardK] = x
                if (!checkInFwd && k + delta >= -d && k + delta <= d) {
                    if (forward[kOffset + backwardK] >= backward[kOffset + backwardK]) {
                        val outSnake = Snake()
                        outSnake.x = backward[kOffset + backwardK]
                        outSnake.y = outSnake.x - backwardK
                        outSnake.size = forward[kOffset + backwardK] - backward[kOffset + backwardK]
                        outSnake.removal = removal
                        outSnake.reverse = true
                        return outSnake
                    }
                }
                k += 2
            }
        }
        throw IllegalStateException(
            "DiffUtil hit an unexpected case while trying to calculate"
                    + " the optimal path. Please make sure your data is not changing during the"
                    + " diff calculation."
        )
    }

    /**
     * A Callback class used by DiffUtil while calculating the diff between two lists.
     */
    abstract class Callback {
        /**
         * Returns the size of the old list.
         *
         * @return The size of the old list.
         */
        abstract val oldListSize: Int

        /**
         * Returns the size of the new list.
         *
         * @return The size of the new list.
         */
        abstract val newListSize: Int

        /**
         * Called by the DiffUtil to decide whether two object represent the same Item.
         *
         *
         * For example, if your items have unique ids, this method should check their id equality.
         *
         * @param oldItemPosition The position of the item in the old list
         * @param newItemPosition The position of the item in the new list
         * @return True if the two items represent the same object or false if they are different.
         */
        abstract fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean

        /**
         * Called by the DiffUtil when it wants to check whether two items have the same data.
         * DiffUtil uses this information to detect if the contents of an item has changed.
         *
         *
         * DiffUtil uses this method to check equality instead of [Object.equals]
         * so that you can change its behavior depending on your UI.
         *
         *
         * This method is called only if [.areItemsTheSame] returns
         * `true` for these items.
         *
         * @param oldItemPosition The position of the item in the old list
         * @param newItemPosition The position of the item in the new list which replaces the
         * oldItem
         * @return True if the contents of the items are the same or false if they are different.
         */
        abstract fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
    }

    /**
     * Snakes represent a match between two lists. It is optionally prefixed or postfixed with an
     * add or remove operation. See the Myers' paper for details.
     */
    internal class Snake {
        /**
         * Position in the old list
         */
        var x: Int = 0
        /**
         * Position in the new list
         */
        var y: Int = 0
        /**
         * Number of matches. Might be 0.
         */
        var size: Int = 0
        /**
         * If true, this is a removal from the original list followed by `size` matches.
         * If false, this is an addition from the new list followed by `size` matches.
         */
        var removal: Boolean = false
        /**
         * If true, the addition or removal is at the end of the snake.
         * If false, the addition or removal is at the beginning of the snake.
         */
        var reverse: Boolean = false
    }

    /**
     * Represents a range in two lists that needs to be solved.
     *
     *
     * This internal class is used when running Myers' algorithm without recursion.
     */
    internal class Range {
        var oldListStart: Int = 0
        var oldListEnd: Int = 0
        var newListStart: Int = 0
        var newListEnd: Int = 0

        constructor()

        constructor(oldListStart: Int, oldListEnd: Int, newListStart: Int, newListEnd: Int) {
            this.oldListStart = oldListStart
            this.oldListEnd = oldListEnd
            this.newListStart = newListStart
            this.newListEnd = newListEnd
        }
    }

    /**
     * This class holds the information about the result of a
     * [DiffUtil.calculateDiff] call.
     */
    class DiffResult
    /**
     * @param callback        The callback that was used to calculate the diff
     * @param snakes          The list of Myers' snakes
     * @param oldItemStatuses An int[] that can be re-purposed to keep metadata
     * @param newItemStatuses An int[] that can be re-purposed to keep metadata
     * @param detectMoves     True if this DiffResult will try to detect moved items
     */
    internal constructor(// The callback that was given to calcualte diff method.
        private val mCallback: Callback, // The Myers' snakes. At this point, we only care about their diagonal sections.
        private val mSnakes: MutableList<Snake>, // The list to keep oldItemStatuses. As we traverse old items, we assign flags to them
        // which also includes whether they were a real removal or a move (and its new index).
        private val mOldItemStatuses: IntArray,
        // The list to keep newItemStatuses. As we traverse new items, we assign flags to them
        // which also includes whether they were a real addition or a move(and its old index).
        private val mNewItemStatuses: IntArray, private val mDetectMoves: Boolean
    ) {
        private val mOldListSize: Int
        private val mNewListSize: Int

        internal val snakes: List<Snake>
            get() = mSnakes

        init {
            Arrays.fill(mOldItemStatuses, 0)
            Arrays.fill(mNewItemStatuses, 0)
            mOldListSize = mCallback.oldListSize
            mNewListSize = mCallback.newListSize
            addRootSnake()
            findMatchingItems()
        }

        /**
         * We always add a Snake to 0/0 so that we can run loops from end to beginning and be done
         * when we run out of snakes.
         */
        private fun addRootSnake() {
            val firstSnake = if (mSnakes.isEmpty()) null else mSnakes[0]
            if (firstSnake == null || firstSnake.x != 0 || firstSnake.y != 0) {
                val root = Snake()
                root.x = 0
                root.y = 0
                root.removal = false
                root.size = 0
                root.reverse = false
                mSnakes.add(0, root)
            }
        }

        /**
         * This method traverses each addition / removal and tries to match it to a previous
         * removal / addition. This is how we detect move operations.
         *
         *
         * This class also flags whether an item has been changed or not.
         *
         *
         * DiffUtil does this pre-processing so that if it is running on a big list, it can be moved
         * to background thread where most of the expensive stuff will be calculated and kept in
         * the statuses maps. DiffResult uses this pre-calculated information while dispatching
         * the updates (which is probably being called on the main thread).
         */
        private fun findMatchingItems() {
            var posOld = mOldListSize
            var posNew = mNewListSize
            // traverse the matrix from right bottom to 0,0.
            for (i in mSnakes.indices.reversed()) {
                val snake = mSnakes[i]
                val endX = snake.x + snake.size
                val endY = snake.y + snake.size
                if (mDetectMoves) {
                    while (posOld > endX) {
                        // this is a removal. Check remaining snakes to see if this was added before
                        findAddition(posOld, posNew, i)
                        posOld--
                    }
                    while (posNew > endY) {
                        // this is an addition. Check remaining snakes to see if this was removed
                        // before
                        findRemoval(posOld, posNew, i)
                        posNew--
                    }
                }
                for (j in 0 until snake.size) {
                    // matching items. Check if it is changed or not
                    val oldItemPos = snake.x + j
                    val newItemPos = snake.y + j
                    val theSame = mCallback
                        .areContentsTheSame(oldItemPos, newItemPos)
                    val changeFlag = if (theSame) FLAG_NOT_CHANGED else FLAG_CHANGED
                    mOldItemStatuses[oldItemPos] = newItemPos shl FLAG_OFFSET or changeFlag
                    mNewItemStatuses[newItemPos] = oldItemPos shl FLAG_OFFSET or changeFlag
                }
                posOld = snake.x
                posNew = snake.y
            }
        }

        private fun findAddition(x: Int, y: Int, snakeIndex: Int) {
            if (mOldItemStatuses[x - 1] != 0) {
                return  // already set by a latter item
            }
            findMatchingItem(x, y, snakeIndex, false)
        }

        private fun findRemoval(x: Int, y: Int, snakeIndex: Int) {
            if (mNewItemStatuses[y - 1] != 0) {
                return  // already set by a latter item
            }
            findMatchingItem(x, y, snakeIndex, true)
        }

        /**
         * Finds a matching item that is before the given coordinates in the matrix
         * (before : left and above).
         *
         * @param x          The x position in the matrix (position in the old list)
         * @param y          The y position in the matrix (position in the new list)
         * @param snakeIndex The current snake index
         * @param removal    True if we are looking for a removal, false otherwise
         * @return True if such item is found.
         */
        private fun findMatchingItem(
            x: Int, y: Int, snakeIndex: Int,
            removal: Boolean
        ): Boolean {
            val myItemPos: Int
            var curX: Int
            var curY: Int
            if (removal) {
                myItemPos = y - 1
                curX = x
                curY = y - 1
            } else {
                myItemPos = x - 1
                curX = x - 1
                curY = y
            }
            for (i in snakeIndex downTo 0) {
                val snake = mSnakes[i]
                val endX = snake.x + snake.size
                val endY = snake.y + snake.size
                if (removal) {
                    // check removals for a match
                    for (pos in curX - 1 downTo endX) {
                        if (mCallback.areItemsTheSame(pos, myItemPos)) {
                            // found!
                            val theSame = mCallback.areContentsTheSame(pos, myItemPos)
                            val changeFlag = if (theSame)
                                FLAG_MOVED_NOT_CHANGED
                            else
                                FLAG_MOVED_CHANGED
                            mNewItemStatuses[myItemPos] = pos shl FLAG_OFFSET or FLAG_IGNORE
                            mOldItemStatuses[pos] = myItemPos shl FLAG_OFFSET or changeFlag
                            return true
                        }
                    }
                } else {
                    // check for additions for a match
                    for (pos in curY - 1 downTo endY) {
                        if (mCallback.areItemsTheSame(myItemPos, pos)) {
                            // found
                            val theSame = mCallback.areContentsTheSame(myItemPos, pos)
                            val changeFlag = if (theSame)
                                FLAG_MOVED_NOT_CHANGED
                            else
                                FLAG_MOVED_CHANGED
                            mOldItemStatuses[x - 1] = pos shl FLAG_OFFSET or FLAG_IGNORE
                            mNewItemStatuses[pos] = x - 1 shl FLAG_OFFSET or changeFlag
                            return true
                        }
                    }
                }
                curX = snake.x
                curY = snake.y
            }
            return false
        }

        fun createDiffList(oldList: List<*>, newList: List<*>): List<Entry> {
            val result = mutableListOf<Entry>()
            var posOld = 0
            var posNew = 0
            for (snakeIndex in mSnakes.indices) {
                val snake = mSnakes[snakeIndex]
                val snakeSize = snake.size
                val nextSnakeStart = if (snakeIndex < mSnakes.size) {
                    mSnakes[snakeIndex + 1].x
                } else {
                    oldList.size
                }
                if (snake.reverse) {
                    for (i in snake.x until snake.x + snakeSize) {
                        result.add(Entry(snake.x, oldList[snake.x], OP_UNCHANGED))
                    }
                    if (snake.removal) {
                       for (i in snake.x + snakeSize until nextSnakeStart)  {
                           result.add(Entry(snake.x, oldList[snake.x], OP_REMOVE))
                       }
                    } else {
                        for (i in snake.x + snakeSize until nextSnakeStart)  {
                            result.add(Entry(snake.y, newList[snake.y], OP_ADD))
                        }
                    }
                } else {
                    if (snake.removal) {

                    } else {

                    }
                    for (i in snake.x until snake.x + snakeSize) {
                        result.add(Entry(snake.x, oldList[snake.x], OP_UNCHANGED))
                    }
                }

                if (snake.removal) {

                }

                val endX = snake.x + snakeSize
                val endY = snake.y + snakeSize
                if (endX < posOld) {
                    for (i in endX until posOld - endX) {
                        result.add(Entry(result.size, oldList[snake.x + i], OP_REMOVE))
                    }
                }
                if (endY < posNew) {
                    for (i in endX until posNew - endX) {
                        result.add(Entry(result.size, newList[snake.x + i], OP_ADD))
                    }
                }
                for (i in snakeSize - 1 downTo 0) {
                    result.add(Entry(result.size, oldList[snake.x + i], OP_UNCHANGED))
                }
                posOld = snake.x
                posNew = snake.y
            }
            return result
        }

        data class Entry(val index: Int, val item: Any?, val op: Int)

        /**
         * Dispatches update operations to the given Callback.
         *
         *
         * These updates are atomic such that the first update call effects every update call that
         * comes after it (the same as RecyclerView).
         *
         * @param updateCallback The callback to receive the update operations.
         */
        fun dispatchUpdatesTo(updateCallback: ListUpdateCallback) {
            var updateCallback = updateCallback
            val batchingCallback: BatchingListUpdateCallback
            if (updateCallback is BatchingListUpdateCallback) {
                batchingCallback = updateCallback
            } else {
                batchingCallback = BatchingListUpdateCallback(updateCallback)
                // replace updateCallback with a batching callback and override references to
                // updateCallback so that we don't call it directly by mistake

                updateCallback = batchingCallback
            }
            // These are add/remove ops that are converted to moves. We track their positions until
            // their respective update operations are processed.
            val postponedUpdates = ArrayList<PostponedUpdate>()
            var posOld = mOldListSize
            var posNew = mNewListSize
            for (snakeIndex in mSnakes.indices.reversed()) {
                val snake = mSnakes[snakeIndex]
                val snakeSize = snake.size
                val endX = snake.x + snakeSize
                val endY = snake.y + snakeSize
                if (endX < posOld) {
                    dispatchRemovals(postponedUpdates, batchingCallback, endX, posOld - endX, endX)
                }
                if (endY < posNew) {
                    dispatchAdditions(
                        postponedUpdates, batchingCallback, endX, posNew - endY,
                        endY
                    )
                }
                for (i in snakeSize - 1 downTo 0) {
                    if (mOldItemStatuses[snake.x + i] and FLAG_MASK == FLAG_CHANGED) {
                        batchingCallback.onChanged(snake.x + i, 1)
                    }
                }
                posOld = snake.x
                posNew = snake.y
            }
            batchingCallback.dispatchLastEvent()
        }

        private fun dispatchAdditions(
            postponedUpdates: MutableList<PostponedUpdate>,
            updateCallback: ListUpdateCallback, start: Int, count: Int, globalIndex: Int
        ) {
            if (!mDetectMoves) {
                updateCallback.onInserted(start, count)
                return
            }
            for (i in count - 1 downTo 0) {
                val status = mNewItemStatuses[globalIndex + i] and FLAG_MASK
                when (status) {
                    0 // real addition
                    -> {
                        updateCallback.onInserted(start, 1)
                        for (update in postponedUpdates) {
                            update.currentPos += 1
                        }
                    }
                    FLAG_MOVED_CHANGED, FLAG_MOVED_NOT_CHANGED -> {
                        val pos = mNewItemStatuses[globalIndex + i] shr FLAG_OFFSET
                        val update = removePostponedUpdate(
                            postponedUpdates, pos,
                            true
                        )
                        // the item was moved from that position

                        updateCallback.onMoved(update!!.currentPos, start)
                        if (status == FLAG_MOVED_CHANGED) {
                            // also dispatch a change
                            updateCallback.onChanged(start, 1)
                        }
                    }
                    FLAG_IGNORE // ignoring this
                    -> postponedUpdates.add(PostponedUpdate(globalIndex + i, start, false))
                    else -> throw IllegalStateException(
                        "unknown flag for pos " + (globalIndex + i) + " " + java.lang.Long
                            .toBinaryString(status.toLong())
                    )
                }
            }
        }

        private fun dispatchRemovals(
            postponedUpdates: MutableList<PostponedUpdate>,
            updateCallback: ListUpdateCallback, start: Int, count: Int, globalIndex: Int
        ) {
            if (!mDetectMoves) {
                updateCallback.onRemoved(start, count)
                return
            }
            for (i in count - 1 downTo 0) {
                val status = mOldItemStatuses[globalIndex + i] and FLAG_MASK
                when (status) {
                    0 // real removal
                    -> {
                        updateCallback.onRemoved(start + i, 1)
                        for (update in postponedUpdates) {
                            update.currentPos -= 1
                        }
                    }
                    FLAG_MOVED_CHANGED, FLAG_MOVED_NOT_CHANGED -> {
                        val pos = mOldItemStatuses[globalIndex + i] shr FLAG_OFFSET
                        val update = removePostponedUpdate(
                            postponedUpdates, pos,
                            false
                        )
                        // the item was moved to that position. we do -1 because this is a move not
                        // add and removing current item offsets the target move by 1

                        updateCallback.onMoved(start + i, update!!.currentPos - 1)
                        if (status == FLAG_MOVED_CHANGED) {
                            // also dispatch a change
                            updateCallback.onChanged(update.currentPos - 1, 1)
                        }
                    }
                    FLAG_IGNORE // ignoring this
                    -> postponedUpdates.add(PostponedUpdate(globalIndex + i, start + i, true))
                    else -> throw IllegalStateException(
                        "unknown flag for pos " + (globalIndex + i) + " " + java.lang.Long
                            .toBinaryString(status.toLong())
                    )
                }
            }
        }

        companion object {
            /**
             * While reading the flags below, keep in mind that when multiple items move in a list,
             * Myers's may pick any of them as the anchor item and consider that one NOT_CHANGED while
             * picking others as additions and removals. This is completely fine as we later detect
             * all moves.
             *
             *
             * Below, when an item is mentioned to stay in the same "location", it means we won't
             * dispatch a move/add/remove for it, it DOES NOT mean the item is still in the same
             * position.
             */
            // item stayed the same.
            private const val FLAG_NOT_CHANGED = 1
            // item stayed in the same location but changed.
            private const val FLAG_CHANGED = FLAG_NOT_CHANGED shl 1
            // Item has moved and also changed.
            private const val FLAG_MOVED_CHANGED = FLAG_CHANGED shl 1
            // Item has moved but did not change.
            private const val FLAG_MOVED_NOT_CHANGED = FLAG_MOVED_CHANGED shl 1
            // Ignore this update.
            // If this is an addition from the new list, it means the item is actually removed from an
            // earlier position and its move will be dispatched when we process the matching removal
            // from the old list.
            // If this is a removal from the old list, it means the item is actually added back to an
            // earlier index in the new list and we'll dispatch its move when we are processing that
            // addition.
            private const val FLAG_IGNORE = FLAG_MOVED_NOT_CHANGED shl 1
            // since we are re-using the int arrays that were created in the Myers' step, we mask
            // change flags
            private const val FLAG_OFFSET = 5
            private const val FLAG_MASK = (1 shl FLAG_OFFSET) - 1

            private fun removePostponedUpdate(
                updates: MutableList<PostponedUpdate>,
                pos: Int, removal: Boolean
            ): PostponedUpdate? {
                for (i in updates.indices.reversed()) {
                    val update = updates[i]
                    if (update.posInOwnerList == pos && update.removal == removal) {
                        updates.removeAt(i)
                        for (j in i until updates.size) {
                            // offset other ops since they swapped positions
                            updates[j].currentPos += if (removal) 1 else -1
                        }
                        return update
                    }
                }
                return null
            }

            const val OP_UNCHANGED = 0
            const val OP_ADD = 1
            const val OP_REMOVE = 2
        }
    }

    /**
     * Represents an update that we skipped because it was a move.
     *
     *
     * When an update is skipped, it is tracked as other updates are dispatched until the matching
     * add/remove operation is found at which point the tracked position is used to dispatch the
     * update.
     */
    private class PostponedUpdate(
        internal var posInOwnerList: Int,
        internal var currentPos: Int,
        internal var removal: Boolean
    )
}// utility class, no instance.
// Myers' algorithm uses two lists as axis labels. In DiffUtil's implementation, `x` axis is
// used for old list and `y` axis is used for new list.
/**
 * Calculates the list of update operations that can covert one list into the other one.
 *
 * @param cb The callback that acts as a gateway to the backing list data
 * @return A DiffResult that contains the information about the edit sequence to convert the
 * old list into the new list.
 */