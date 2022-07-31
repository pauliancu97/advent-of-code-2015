class DayThree {
    enum class Direction(val id: Char) {
        North('^'),
        South('v'),
        East('>'),
        West('<');

        companion object {
            fun getDirection(char: Char) =
                Direction.values().firstOrNull { it.id == char }
        }
    }

    fun String.toDirections() =
        this.mapNotNull { Direction.getDirection(it) }

    fun getNumVisitedPositions(directions: List<Direction>): Int {
        val positions: MutableSet<Pair<Int, Int>> = mutableSetOf()
        var currentPositions = 0 to 0
        positions.add(currentPositions)
        for (direction in directions) {
            currentPositions = when (direction) {
                Direction.North -> currentPositions.first to (currentPositions.second + 1)
                Direction.East -> (currentPositions.first + 1) to currentPositions.second
                Direction.South -> currentPositions.first to (currentPositions.second - 1)
                Direction.West -> (currentPositions.first - 1) to currentPositions.second
            }
            positions.add(currentPositions)
        }
        return positions.size
    }

    fun getNumVisitedPositionsAndRobotSanta(directions: List<Direction>): Int {
        val positions: MutableSet<Pair<Int, Int>> = mutableSetOf()
        var currentPositions = 0 to 0
        positions.add(currentPositions)
        val santaDirections = directions.filterIndexed { index, _ -> index % 2 == 0 }
        for (direction in santaDirections) {
            currentPositions = when (direction) {
                Direction.North -> currentPositions.first to (currentPositions.second + 1)
                Direction.East -> (currentPositions.first + 1) to currentPositions.second
                Direction.South -> currentPositions.first to (currentPositions.second - 1)
                Direction.West -> (currentPositions.first - 1) to currentPositions.second
            }
            positions.add(currentPositions)
        }
        currentPositions = 0 to 0
        val robotSantaDirections = directions.filterIndexed { index, _ -> index % 2 == 1 }
        for (direction in robotSantaDirections) {
            currentPositions = when (direction) {
                Direction.North -> currentPositions.first to (currentPositions.second + 1)
                Direction.East -> (currentPositions.first + 1) to currentPositions.second
                Direction.South -> currentPositions.first to (currentPositions.second - 1)
                Direction.West -> (currentPositions.first - 1) to currentPositions.second
            }
            positions.add(currentPositions)
        }
        return positions.size
    }

    fun solvePartOne() {
        val line = readLines("day_three.txt").first()
        val directions = line.toDirections()
        println(getNumVisitedPositions(directions))
    }

    fun solvePartTwo() {
        val line = readLines("day_three.txt").first()
        val directions = line.toDirections()
        println(getNumVisitedPositionsAndRobotSanta(directions))
    }
}

fun main() {
    DayThree().solvePartTwo()
}