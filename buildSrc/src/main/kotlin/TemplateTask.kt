import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

/**
 * Generates multiple copies of kotlin code from a template, with simple text replacement. ex:
 * ```
 * package test
 *
 * $T = String, Int
 *
 * fun foo(arg: $T) {
 * }
 * ```
 *
 * will generate
 *
 * ```
 * package test
 *
 * fun foo(arg: String) {
 * }
 *
 * fun foo(arg: Int) {
 * }
 * ```
 *
 * Note that anything above replacement declaration (`$T = ...`) is placed once and everything after is placed n times.
 *
 * You can specify multiple 'groups' of replacements by seperating them with a ':'. This is useful for generics
 * replacements when the type is known.
 *
 * ```
 * package test
 *
 * $T:$E = ByteArray:Byte, CharArray:Char
 *
 * fun foo(one: $T, two: $E) {
 * }
 * ```
 *
 * will generate
 *
 * ```
 * package test
 *
 * fun foo(one: ByteArray, two: Byte) {
 * }
 *
 * fun foo(one: CharArray, two: Char) {
 * }
 * ```
 */
abstract class TemplateTask : DefaultTask() {
    @get:InputDirectory
    abstract val inputDir: DirectoryProperty
    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun run() {
        val inDir = inputDir.get().asFile
        val outDir = outputDir.get().asFile

        outDir.deleteRecursively()
        inDir.walkTopDown().filter { it.isFile && it.extension == "kt" }.forEach { template ->
            val outputFile = outDir.resolve(template.relativeTo(inDir))
            outputFile.parentFile.mkdirs()

            val content = template.readText()

            val tokensStart = content.indexOf('$')
            val tokensStop = content.indexOf("\n\n", startIndex = tokensStart)
            val tokenLine = content.substring(tokensStart until tokensStop)
            val tokens = tokenLine.substringBefore('=').split(':').cleanup()
            val replacements = tokenLine.substringAfter('=')
                .split(',')
                .cleanup()
                .map { it.split(':').cleanup() }

            val body = content.substring(tokensStop)
            val result = buildString {
                append(content.substring(0 until tokensStart))

                for (element in replacements) {
                    var expanded = body
                    for (tokenIndex in tokens.indices) {
                        val token = tokens[tokenIndex]
                        val replacement = element[tokenIndex]
                        expanded = expanded.replace(token, replacement)
                    }
                    append(expanded)
                }
            }

            outputFile.writeText(result)
        }
    }
}

private fun List<String>.cleanup(): List<String> = map { it.trim() }.filter { it.isNotEmpty() }