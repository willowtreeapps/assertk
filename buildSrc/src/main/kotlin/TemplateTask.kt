import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

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
open class TemplateTask : DefaultTask() {
    @InputDirectory
    lateinit var inputDir: File
    @OutputDirectory
    lateinit var outputDir: File

    @TaskAction
    fun run() {
        outputDir.deleteRecursively()
        inputDir.walkTopDown().filter { it.isFile && it.extension == "kt" }.forEach { template ->
            val outputFile = outputDir.resolve(template.relativeTo(inputDir))
            outputFile.parentFile.mkdirs()
            val lines = template.readLines()
            val tokenLineIndex = lines.indexOfFirst { it.startsWith("$") }
            val tokenLine = lines[tokenLineIndex]
            val tokens = tokenLine.substringBefore('=').split(':').cleanup()
            val replacements = tokenLine.substringAfter('=')
                .split(',')
                .cleanup()
                .map { it.split(':').cleanup() }

            outputFile.bufferedWriter().use { out ->
                for (lineIndex in 0 until tokenLineIndex) {
                    out.write(lines[lineIndex])
                    out.write("\n")
                }

                for (replacementIndex in 0 until replacements.size) {
                    for (lineIndex in tokenLineIndex + 1 until lines.size) {
                        var line = lines[lineIndex]
                        if (lineIndex == tokenLineIndex + 1 && line.isBlank()) {
                            continue
                        }
                        for (tokenIndex in 0 until tokens.size) {
                            val token = tokens[tokenIndex]
                            val replacement = replacements[replacementIndex][tokenIndex]
                            line = line.replace(token, replacement)
                        }
                        out.write(line)
                        out.write("\n")
                    }
                    out.write("\n")
                }
            }
        }
    }
}

private fun List<String>.cleanup(): List<String> = map { it.trim() }.filter { it.isNotEmpty() }
