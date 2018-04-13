package assertk.assertions.support

/**
 * List diffing using the Myers diff algorithm.
 */
internal object ListDiffer {

    fun diff(a: List<*>, b: List<*>): List<Edit> {
        val diff = mutableListOf<Edit>()
        backtrack(a, b) { prevX, prevY, x, y ->
            diff.add(
                0, when {
                    x == prevX -> Edit.Ins(prevY, b[prevY])
                    y == prevY -> Edit.Del(prevX, a[prevX])
                    else -> Edit.Eq(prevX, a[prevX], prevY, b[prevY])
                }
            )
        }
        return diff
    }

    sealed class Edit {
        data class Ins(val newIndex: Int, val newValue: Any?) : Edit()
        data class Del(val oldIndex: Int, val oldValue: Any?) : Edit()
        data class Eq(val oldIndex: Int, val oldValue: Any?, val newIndex: Int, val newValue: Any?) : Edit()
    }

    private fun shortestEdit(a: List<*>, b: List<*>): List<IntArray> {
        val n = a.size
        val m = b.size
        val max = n + m
        if (max == 0) {
            return emptyList()
        }
        val v = IntArray(2 * max + 1)
        val trace = mutableListOf<IntArray>()

        for (d in 0..max) {
            trace += v.copyOf()

            for (k in -d..d step 2) {
                var x = if (k == -d || (k != d && v.ringIndex(k - 1) < v.ringIndex(k + 1))) {
                    v.ringIndex(k + 1)
                } else {
                    v.ringIndex(k - 1) + 1
                }
                var y = x - k

                while (x < n && y < m && a[x] == b[y]) {
                    x += 1
                    y += 1
                }

                v.ringIndexAssign(k, x)

                if (x >= n && y >= m) {
                    return trace
                }
            }
        }

        return trace
    }

    private fun IntArray.ringIndex(index: Int): Int = this[if (index < 0) size + index else index]

    private fun IntArray.ringIndexAssign(index: Int, value: Int) {
        this[if (index < 0) size + index else index] = value
    }

    private fun backtrack(a: List<*>, b: List<*>, yield: (Int, Int, Int, Int) -> Unit) {
        var x = a.size
        var y = b.size

        val shortestEdit = shortestEdit(a, b)
        for (d in shortestEdit.size - 1 downTo 0) {
            val v = shortestEdit[d]

            val k = x - y

            val prevK = if (k == -d || (k != d && v.ringIndex(k - 1) < v.ringIndex(k + 1))) {
                k + 1
            } else {
                k - 1
            }

            val prevX = v[prevK]
            val prevY = prevX - prevK

            while (x > prevX && y > prevY) {
                yield(x - 1, y - 1, x, y)
                x -= 1
                y -= 1
            }
            if (d > 0) {
                yield(prevX, prevY, x, y)
            }
            x = prevX
            y = prevY
        }
    }
}
