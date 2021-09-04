import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
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

                for (element in replacements) {
                    for (lineIndex in tokenLineIndex + 1 until lines.size) {
                        var line = lines[lineIndex]
                        if (lineIndex == tokenLineIndex + 1 && line.isBlank()) {
                            continue
                        }
                        for (tokenIndex in tokens.indices) {
                            val token = tokens[tokenIndex]
                            val replacement = element[tokenIndex]
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