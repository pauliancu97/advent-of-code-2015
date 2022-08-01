class DayFive {
    companion object {
        private val VOWELS = setOf('a', 'e', 'i', 'o', 'u')
        private val FORBIDDEN_STRINGS = setOf("ab", "cd", "pq", "xy")
    }

    private fun isStringNice(string: String): Boolean {
        val hasSufficientVowels = string.count { it in VOWELS } >= 3
        val hasRepeatedLetter = string.dropLast(1).zip(string.drop(1))
            .any { (first, second) -> first == second }
        val hasNotForbiddenStrings = !FORBIDDEN_STRINGS.any { it in string }
        return hasSufficientVowels && hasRepeatedLetter && hasNotForbiddenStrings
    }

    private fun hasRepeatedTwoCharacters(string: String): Boolean {
        for (index in 0 until (string.length - 1)) {
            val testedSubstring = string.substring(index until (index + 2))
            val remainingSubstring = string.substring((index + 2) until string.length)
            if (testedSubstring in remainingSubstring) {
                return true
            }
        }
        return false
    }

    private fun hasSeparatedSameCharacter(string: String): Boolean {
        for (index in 0 until (string.length - 2)) {
            if (string[index] == string[index + 2]) {
                return true
            }
        }
        return false
    }

    private fun isSuperNiceString(string: String) =
        hasRepeatedTwoCharacters(string) && hasSeparatedSameCharacter(string)

    fun solvePartOne() {
        val strings = readLines("day_five.txt")
        val numOfNiceStrings = strings.count { isStringNice(it) }
        println(numOfNiceStrings)
    }

    fun solvePartTwo() {
        val strings = readLines("day_five.txt")
        val numOfNiceStrings = strings.count { isSuperNiceString(it) }
        println(numOfNiceStrings)
    }
}

fun main() {
    DayFive().solvePartTwo()
}