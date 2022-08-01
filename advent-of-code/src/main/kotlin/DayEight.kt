class DayEight {
    private fun getNumMemoryCharacters(string: String): Int {
        var currentString = string
        var result = 0
        while (currentString.isNotEmpty()) {
            val firstChar = currentString.first()
            if (firstChar != '"') {
                result++
            }
            if (firstChar == '\\') {
                if (currentString.length < 2) {
                    currentString = ""
                } else {
                    when (currentString[1]) {
                        '\\' -> currentString = currentString.drop(2)
                        '"' -> currentString = currentString.drop(2)
                        'x' -> currentString = currentString.drop(4)
                    }
                }
            } else {
                currentString = currentString.drop(1)
            }
        }
        return result
    }

    fun getNumEncodedCharacters(string: String): Int =
        string.sumOf { char ->
            when (char) {
                '\\', '"' -> 2 as Int
                else -> 1 as Int
            }
        } + 2

    private fun readStrings(path: String) =
        readLines(path)
            .map { string -> string.filterNot { it.isWhitespace() } }

    fun solvePartOne() {
        val strings = readStrings("day_eight.txt")
        val totalCodeLength = strings.sumOf { it.length }
        val totalMemoryLength = strings.sumOf { getNumMemoryCharacters(it) }
        val diff = totalCodeLength - totalMemoryLength
        println(diff)
    }

    fun solvePartTwo() {
        val strings = readStrings("day_eight.txt")
        val totalCodeLength = strings.sumOf { it.length }
        val totalEncodedLength = strings.sumOf { getNumEncodedCharacters(it) }
        val diff = totalEncodedLength - totalCodeLength
        println(diff)
    }
}

fun main() {
    DayEight().solvePartTwo()
}