package test.assertk

expect val exceptionPackageName: String

enum class Platform { JVM, JS, NATIVE }

expect val platform: Platform