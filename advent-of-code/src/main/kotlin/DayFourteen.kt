import kotlin.math.min

class DayFourteen {

    data class Reindeer(
        val name: String,
        val speed: Int,
        val upTime: Int,
        val downTime: Int
    ) {

        companion object {
            val REGEX = """([a-zA-Z]+) can fly (\d+) km\/s for (\d+) seconds, but then must rest for (\d+) seconds\."""
                .toRegex()
        }

        fun getDistanceTraveled(time: Int): Int {
            val cycleTime = upTime + downTime
            val numOfCycles = time / cycleTime
            val distanceTraveledInCycles = numOfCycles * upTime * speed
            val remainingTime = time % cycleTime
            val remainingTimeUptime = min(remainingTime, upTime)
            val distanceTraveledInRest = remainingTimeUptime * speed
            return distanceTraveledInCycles + distanceTraveledInRest
        }
    }

    private fun String.toReindeer(): Reindeer? {
        val matchResult = Reindeer.REGEX.matchEntire(this) ?: return null
        val name = matchResult.groupValues.getOrNull(1) ?: return null
        val speed = matchResult.groupValues.getOrNull(2)?.toIntOrNull() ?: return null
        val upTime = matchResult.groupValues.getOrNull(3)?.toIntOrNull() ?: return null
        val downTime = matchResult.groupValues.getOrNull(4)?.toIntOrNull() ?: return null
        return Reindeer(name, speed, upTime, downTime)
    }

    private fun readReindeers(path: String) =
        readLines(path).mapNotNull { it.toReindeer() }

    private fun getFurthestDistance(reindeers: List<Reindeer>, time: Int): Int =
        reindeers.maxOfOrNull { it.getDistanceTraveled(time) } ?: 0

    private fun getWinningReindeerNumPoints(reindeers: List<Reindeer>, time: Int): Int {
        val pointsMap = reindeers
            .associate { it.name to 0 }
            .toMutableMap()
        for (currentTime in 1..time) {
            val maxDistance = reindeers.maxOf { it.getDistanceTraveled(currentTime) }
            val awardedReindeers = reindeers
                .filter { it.getDistanceTraveled(currentTime) == maxDistance }
                .map { it.name }
            for (awardedReindeer in awardedReindeers) {
                pointsMap[awardedReindeer] = (pointsMap[awardedReindeer] ?: 0) + 1
            }
        }
        return pointsMap.values.max()
    }

    fun solvePartOne() {
        val reindeers = readReindeers("day_fourteen.txt")
        val maxDistance = getFurthestDistance(reindeers, 2503)
        println(maxDistance)
    }

    fun solvePartTwo() {
        val reindeers = readReindeers("day_fourteen.txt")
        val maxPoints = getWinningReindeerNumPoints(reindeers, 2503)
        println(maxPoints)
    }
}

fun main() {
    DayFourteen().solvePartTwo()
}