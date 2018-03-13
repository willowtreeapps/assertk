package test.assertk

actual val exceptionPackageName = "java.lang."
actual val testExceptionPackageName = "test.assertk.assertions."
actual val shortTestExceptionPackageName = "...${testExceptionPackageName.takeLast(5)}"
