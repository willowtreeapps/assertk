@file:JvmName("FailureJVMKt")

package assertk

@Suppress("NOTHING_TO_INLINE")
internal actual inline fun failWithNotInStacktrace(error: AssertionError): Nothing {
    val filtered = error.stackTrace
        .dropWhile { it.className.startsWith("assertk") }
        .toTypedArray()
    @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN", "UnsafeCast")
    error.stackTrace = filtered
    throw error
}

/*
 * Copyright (C) 2018 Touchlab, Inc.
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
actual typealias ThreadLocalRef<T> = InitialValueThreadLocal<T>

open class InitialValueThreadLocal<T>(private val initial: () -> T) : ThreadLocal<T>() {
    override fun initialValue(): T = initial()
}
