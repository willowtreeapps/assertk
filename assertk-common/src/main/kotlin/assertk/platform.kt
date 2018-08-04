package assertk

/**
 * Allows to resolve name conflicts from common modules.
 */
expect annotation class PlatformName(val name: String)

/**
 * Kotlin native requires objects to be ThreadLocal to be mutable.
 */
expect annotation class ThreadLocal()
