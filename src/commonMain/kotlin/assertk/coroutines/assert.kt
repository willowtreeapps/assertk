package assertk.coroutines

import assertk.AssertBlock
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
expect fun <T> assertThat(f: suspend () -> T): AssertBlock<T>