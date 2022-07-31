private class DayOne {

    enum class Instruction(val id: Char) {
        Up('('),
        Down(')')
    }

    fun String.toInstructions(): List<Instruction> =
        this.mapNotNull { char -> Instruction.values().firstOrNull { instr -> char == instr.id } }

    fun getFinalFloor(instructions: List<Instruction>): Int =
        instructions.fold(0) { floor, instruction ->
            when (instruction) {
                Instruction.Up -> floor + 1
                Instruction.Down -> floor - 1
            }
        }

    fun getPositionBasement(instructions: List<Instruction>): Int? {
        var floor = 0
        for ((index, instruction) in instructions.withIndex()) {
            when (instruction) {
                Instruction.Up -> floor++
                Instruction.Down -> floor--
            }
            if (floor == -1) {
                return index + 1
            }
        }
        return null
    }

    fun solvePartOne() {
        val line = readLines("day_one.txt").first()
        val instructions = line.toInstructions()
        val floor = getFinalFloor(instructions)
        println(floor)
    }

    fun solvePartTwo() {
        val line = readLines("day_one.txt").first()
        val instructions = line.toInstructions()
        val position = getPositionBasement(instructions)
        println(position)
    }
}

fun main() {
    val day = DayOne()
    day.solvePartTwo()
}