package assertk.assertions.support

object ListDiffer {

    fun shortestEdit(a: List<*>, b: List<*>): Int {
        val n = a.size
        val m = b.size
        val max = n + m
        val v = IntArray(2 * max + 1)

        for (d in 0 until max) {
            for (k in -d until d step 2) {
                var x = if (k == -d || (k != d && v[k - 1] < v[k + 1])) {
                    v[k + 1]
                } else {
                    v[k - 1] + 1
                }
                var y = x - k

                while (x < n && y < m && a[x] == b[y]) {
                    x += 1
                    y += 1
                }

                v[k] = x

                if (x >= n && y >= m) {
                    return d
                }
            }
        }

        return 0
    }

}
