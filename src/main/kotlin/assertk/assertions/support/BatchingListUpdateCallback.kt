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

/**
 * Wraps a [ListUpdateCallback] callback and batches operations that can be merged.
 *
 *
 * For instance, when 2 add operations comes that adds 2 consecutive elements,
 * BatchingListUpdateCallback merges them and calls the wrapped callback only once.
 *
 *
 * If you use this class to batch updates, you must call [.dispatchLastEvent] when the
 * stream of update events drain.
 */
internal class BatchingListUpdateCallback(val mWrapped: ListUpdateCallback) : ListUpdateCallback {
    var mLastEventType = TYPE_NONE
    var mLastEventPosition = -1
    var mLastEventCount = -1

    /**
     * BatchingListUpdateCallback holds onto the last event to see if it can be merged with the
     * next one. When stream of events finish, you should call this method to dispatch the last
     * event.
     */
    fun dispatchLastEvent() {
        if (mLastEventType == TYPE_NONE) {
            return
        }
        when (mLastEventType) {
            TYPE_ADD -> mWrapped.onInserted(mLastEventPosition, mLastEventCount)
            TYPE_REMOVE -> mWrapped.onRemoved(mLastEventPosition, mLastEventCount)
            TYPE_CHANGE -> mWrapped.onChanged(mLastEventPosition, mLastEventCount)
        }
        mLastEventType = TYPE_NONE
    }

    override fun onInserted(position: Int, count: Int) {
        if (mLastEventType == TYPE_ADD && position >= mLastEventPosition
            && position <= mLastEventPosition + mLastEventCount
        ) {
            mLastEventCount += count
            mLastEventPosition = Math.min(position, mLastEventPosition)
            return
        }
        dispatchLastEvent()
        mLastEventPosition = position
        mLastEventCount = count
        mLastEventType = TYPE_ADD
    }

    override fun onRemoved(position: Int, count: Int) {
        if (mLastEventType == TYPE_REMOVE && mLastEventPosition >= position &&
            mLastEventPosition <= position + count
        ) {
            mLastEventCount += count
            mLastEventPosition = position
            return
        }
        dispatchLastEvent()
        mLastEventPosition = position
        mLastEventCount = count
        mLastEventType = TYPE_REMOVE
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
        dispatchLastEvent() // moves are not merged
        mWrapped.onMoved(fromPosition, toPosition)
    }

    override fun onChanged(position: Int, count: Int) {
        if (mLastEventType == TYPE_CHANGE && !(position > mLastEventPosition + mLastEventCount || position + count < mLastEventPosition)) {
            // take potential overlap into account
            val previousEnd = mLastEventPosition + mLastEventCount
            mLastEventPosition = Math.min(position, mLastEventPosition)
            mLastEventCount = Math.max(previousEnd, position + count) - mLastEventPosition
            return
        }
        dispatchLastEvent()
        mLastEventPosition = position
        mLastEventCount = count
        mLastEventType = TYPE_CHANGE
    }

    companion object {
        private const val TYPE_NONE = 0
        private const val TYPE_ADD = 1
        private const val TYPE_REMOVE = 2
        private const val TYPE_CHANGE = 3
    }
}