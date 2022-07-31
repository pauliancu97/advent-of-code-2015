import java.io.File

fun readLines(path: String): List<String> =
    File(path).readLines()
        .map { string ->
            string
                .mapNotNull { if (it != '\n') it else null }
                .joinToString(separator = "")
        }