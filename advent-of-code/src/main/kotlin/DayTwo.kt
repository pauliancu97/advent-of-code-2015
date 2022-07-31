import kotlin.math.min

class DayTwo {
    data class Cuboid(
        val length: Int,
        val width: Int,
        val height: Int
    ) {

        companion object {
            val REGEX = """(\d+)x(\d+)x(\d+)""".toRegex()
        }

        fun getSurfaceArea() = 2 * length * width + 2 * length * height + 2 * width * height

        fun getSurfaceSmallestSide(): Int {
            val firstMin = min(min(length, width), height)
            val secondMin = when (firstMin) {
                length -> min(width, height)
                width -> min(length, height)
                height -> min(length, width)
                else -> min(width, height)
            }
            return firstMin * secondMin
        }

        fun getVolume() = length * width * height

        fun getRibbonLength(): Int {
            val firstMin = min(min(length, width), height)
            val secondMin = when (firstMin) {
                length -> min(width, height)
                width -> min(length, height)
                height -> min(length, width)
                else -> min(width, height)
            }
            return 2 * firstMin + 2 * secondMin + getVolume()
        }

        fun getPresentSurface() = getSurfaceArea() + getSurfaceSmallestSide()
    }

    fun String.toCuboid(): Cuboid? {
        val matchResult = Cuboid.REGEX.matchEntire(this) ?: return null
        val length = matchResult.groupValues[1].toIntOrNull() ?: return null
        val width = matchResult.groupValues[2].toIntOrNull() ?: return null
        val height = matchResult.groupValues[3].toIntOrNull() ?: return null
        return Cuboid(length, width, height)
    }

    fun readCuboids(path: String) =
        readLines(path)
            .mapNotNull { it.toCuboid() }

    fun getTotalPresentsArea(cuboids: List<Cuboid>)=
        cuboids.sumOf { it.getPresentSurface() }

    fun getTotalRibbonLength(cuboids: List<Cuboid>) =
        cuboids.sumOf { it.getRibbonLength() }

    fun solvePartOne() {
        val cuboids = readCuboids("day_two.txt")
        println(getTotalPresentsArea(cuboids))
    }

    fun solvePartTwo() {
        val cuboids = readCuboids("day_two.txt")
        println(getTotalRibbonLength(cuboids))
    }
}

fun main() {
    DayTwo().solvePartTwo()
}