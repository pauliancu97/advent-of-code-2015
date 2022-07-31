import java.security.MessageDigest

class DayFour {

    fun getMd5(string: String): String {
        val md5ByteArray = MessageDigest.getInstance("MD5").digest(string.toByteArray(Charsets.UTF_8))
        return md5ByteArray.joinToString(separator = "") { byte -> "%02x".format(byte) }
    }

    fun getFirstNumber(key: String): Int {
        var number = 1
        while (true) {
            val string = "${key}${number}"
            val md5 = getMd5(string)
            val allZeros = md5.substring(0, 5).all { it == '0' }
            if (allZeros) {
                return number
            }
            number++
        }
    }

    fun getSecondNumber(key: String): Int {
        var number = 1
        while (true) {
            val string = "${key}${number}"
            val md5 = getMd5(string)
            val allZeros = md5.substring(0, 6).all { it == '0' }
            if (allZeros) {
                return number
            }
            number++
        }
    }

    fun solvePartOne() {
        println(getFirstNumber("yzbqklnj"))
    }

    fun solvePartTwo() {
        println(getSecondNumber("yzbqklnj"))
    }
}

fun main() {
    DayFour().solvePartTwo()
}