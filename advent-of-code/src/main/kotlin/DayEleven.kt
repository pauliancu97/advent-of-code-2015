class DayEleven {

    @JvmInline
    value class Password(val data: List<Int>) {

        constructor(stringPassword: String):
                this(
                    data = stringPassword
                        .lowercase()
                        .map { it.code - 'a'.code }
                        .reversed()
                )

        fun incremented(): Password {
            val updatedData = data.toMutableList()
            var isIncrementing = true
            var index = 0
            while (index < data.size && isIncrementing) {
                val (updatedDigit, updatedIsIncrementing) = if (data[index] + 1 >= 26) {
                    (26 - data[index] - 1) to true
                } else {
                    (data[index] + 1) to false
                }
                updatedData[index] = updatedDigit
                isIncrementing = updatedIsIncrementing
                index++
            }
            return Password(updatedData)
        }

        override fun toString(): String =
            data.joinToString(separator = "") { (it + 'a'.code).toChar().toString() }.reversed()
    }

    private fun hasIncreasingSequence(string: String): Boolean {
        for (index in 0 until string.length - 2) {
            val firstDiff = string[index + 1].code - string[index].code
            val secondDiff = string[index + 2].code - string[index + 1].code
            if (firstDiff == 1 && secondDiff == 1) {
                return true
            }
        }
        return false
    }

    private fun hasNotForbiddenCharacters(string: String) =
        !string.any { it in setOf('i', 'o', 'l') }

    private fun getPair(string: String): Pair<Boolean, String> {
        var currentString = string
        while (currentString.length >= 2) {
            if (currentString[0] == currentString[1]) {
                return true to currentString.drop(2)
            } else {
                currentString = currentString.drop(1)
            }
        }
        return false to currentString
    }

    private fun hasTwoPairs(string: String): Boolean {
        val (firstHasPair, remainingString) = getPair(string)
        if (!firstHasPair) {
            return false
        }
        val (secondHasPair, _) = getPair(remainingString)
        return secondHasPair
    }

    private fun isPasswordSecure(password: Password): Boolean {
        val passwordString = password.toString()
        return hasIncreasingSequence(passwordString) && hasNotForbiddenCharacters(passwordString) &&
                hasTwoPairs(passwordString)
    }

    private fun getNextPassword(password: Password): Password {
        var currentPassword = password.incremented()
        while (!isPasswordSecure(currentPassword)) {
            currentPassword = currentPassword.incremented()
        }
        return currentPassword
    }

    fun solvePartOne() {
        val nextPassword = getNextPassword(Password("vzbxkghb"))
        println(nextPassword)
    }

    fun solvePartTwo() {
        val firstNextPassword = getNextPassword(Password("vzbxkghb"))
        val secondNextPassword = getNextPassword(firstNextPassword)
        println(secondNextPassword)
    }
}

fun main() {
    DayEleven().solvePartTwo()
}