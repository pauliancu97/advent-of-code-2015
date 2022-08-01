class DayTen {
    private fun getNextOccurrenceString(string: String): String {
        var currentOccurrence = 1
        var nextString = ""
        for (index in 1 until string.length) {
            if (string[index] != string[index - 1]) {
                nextString += "${currentOccurrence}${string[index - 1]}"
                currentOccurrence = 1
            } else {
                currentOccurrence++
            }
        }
        nextString += "${currentOccurrence}${string.last()}"
        return nextString
    }

    private fun getFinalOccurrenceString(string: String, numOfIterations: Int): String {
        var currentString = string
        repeat(numOfIterations) {
            currentString = getNextOccurrenceString(currentString)
        }
        return currentString
    }

    fun solvePartOne() {
        println(getFinalOccurrenceString("1113222113", 50).length)
    }
}

fun main() {
    DayTen().solvePartOne()
}