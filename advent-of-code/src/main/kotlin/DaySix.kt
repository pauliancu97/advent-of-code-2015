import kotlin.math.max

class DaySix {

    data class CoordinateRange(
        val minRow: Int,
        val maxRow: Int,
        val minCol: Int,
        val maxCol: Int
    ) {
        val rowsIndices = minRow..maxRow
        val colsIndices = minCol..maxCol

        companion object {
            val REGEX = """(\d{1,3}),(\d{1,3}) through (\d{1,3}),(\d{1,3})""".toRegex()
        }
    }

    sealed class Instruction {
        abstract val coordinateRange: CoordinateRange

        data class TurnOn(override val coordinateRange: CoordinateRange) : Instruction() {
            companion object {
                val REGEX = """turn on (.+)""".toRegex()
            }
        }
        data class TurnOff(override val coordinateRange: CoordinateRange) : Instruction() {
            companion object {
                val REGEX = """turn off (.+)""".toRegex()
            }
        }
        data class Toggle(override val coordinateRange: CoordinateRange) : Instruction() {
            companion object {
                val REGEX = """toggle (.+)""".toRegex()
            }
        }
    }

    data class BinaryGrid(val data: MutableList<MutableList<Boolean>>) {
        val rows = data.size
        val cols = data.first().size

        constructor(rows: Int, cols: Int):
                this(MutableList(rows) { MutableList(cols) { false } })

        operator fun get(row: Int, col: Int) = data[row][col]

        operator fun set(row: Int, col: Int, value: Boolean) {
            data[row][col] = value
        }
    }

    data class NumberGrid(val data: MutableList<MutableList<Int>>) {
        val rows = data.size
        val cols = data.first().size

        constructor(rows: Int, cols: Int):
                this(MutableList(rows) { MutableList(cols) { 0 } })

        operator fun get(row: Int, col: Int) = data[row][col]

        operator fun set(row: Int, col: Int, value: Int) {
            data[row][col] = value
        }
    }

    private fun String.toCoordinateRange(): CoordinateRange? {
        val matchResult = CoordinateRange.REGEX.matchEntire(this) ?: return null
        val firstRow = matchResult.groupValues[1].toIntOrNull() ?: return null
        val firstCol = matchResult.groupValues[2].toIntOrNull() ?: return null
        val secondRow = matchResult.groupValues[3].toIntOrNull() ?: return null
        val secondCol = matchResult.groupValues[4].toIntOrNull() ?: return null
        return CoordinateRange(
            minRow = kotlin.math.min(firstRow, secondRow),
            maxRow = kotlin.math.max(firstRow, secondRow),
            minCol = kotlin.math.min(firstCol, secondCol),
            maxCol = kotlin.math.max(firstCol, secondCol)
        )
    }

    private fun String.toTurnOnInstruction(): Instruction.TurnOn? {
        val matchResult = Instruction.TurnOn.REGEX.matchEntire(this) ?: return null
        val coordinateRange = matchResult.groupValues[1].toCoordinateRange() ?: return null
        return Instruction.TurnOn(coordinateRange)
    }

    private fun String.toTurnOffInstruction(): Instruction.TurnOff? {
        val matchResult = Instruction.TurnOff.REGEX.matchEntire(this) ?: return null
        val coordinateRange = matchResult.groupValues[1].toCoordinateRange() ?: return null
        return Instruction.TurnOff(coordinateRange)
    }

    private fun String.toToggleInstruction(): Instruction.Toggle? {
        val matchResult = Instruction.Toggle.REGEX.matchEntire(this) ?: return null
        val coordinateRange = matchResult.groupValues[1].toCoordinateRange() ?: return null
        return Instruction.Toggle(coordinateRange)
    }

    private fun String.toInstruction(): Instruction? =
        toTurnOffInstruction() ?: toTurnOnInstruction() ?: toToggleInstruction()

    private fun performInstructionBinaryGrid(binaryGrid: BinaryGrid, instruction: Instruction) {
        for (row in instruction.coordinateRange.rowsIndices) {
            for (col in instruction.coordinateRange.colsIndices) {
                when (instruction) {
                    is Instruction.TurnOff -> binaryGrid[row, col] = false
                    is Instruction.TurnOn -> binaryGrid[row, col] = true
                    is Instruction.Toggle -> binaryGrid[row, col] = !binaryGrid[row, col]
                }
            }
        }
    }

    private fun performInstructionNumberGrid(binaryGrid: NumberGrid, instruction: Instruction) {
        for (row in instruction.coordinateRange.rowsIndices) {
            for (col in instruction.coordinateRange.colsIndices) {
                when (instruction) {
                    is Instruction.TurnOff -> binaryGrid[row, col] = max(0, binaryGrid[row, col] - 1)
                    is Instruction.TurnOn -> binaryGrid[row, col] = binaryGrid[row, col] + 1
                    is Instruction.Toggle -> binaryGrid[row, col] = binaryGrid[row, col] + 2
                }
            }
        }
    }

    private fun getFinalBinaryGrid(instructions: List<Instruction>): BinaryGrid {
        val binaryGrid = BinaryGrid(1000, 1000)
        for (instruction in instructions) {
            performInstructionBinaryGrid(binaryGrid, instruction)
        }
        return binaryGrid
    }

    private fun getFinalNumberGrid(instructions: List<Instruction>): NumberGrid {
        val numberGrid = NumberGrid(1000, 1000)
        for (instruction in instructions) {
            performInstructionNumberGrid(numberGrid, instruction)
        }
        return numberGrid
    }

    private fun getNumTurnedOnCells(binaryGrid: BinaryGrid): Int {
        var result = 0
        for (row in 0 until binaryGrid.rows) {
            for (col in 0 until binaryGrid.cols) {
                if (binaryGrid[row, col]) {
                    result++
                }
            }
        }
        return result
    }

    private fun getTotalBrightness(numberGrid: NumberGrid): Int {
        var result = 0
        for (row in 0 until numberGrid.rows) {
            for (col in 0 until numberGrid.cols) {
                result += numberGrid[row, col]
            }
        }
        return result
    }

    private fun readInstructions(path: String) =
        readLines(path)
            .mapNotNull { it.toInstruction() }

    fun solvePartOne() {
        val instructions = readInstructions("day_six.txt")
        val grid = getFinalBinaryGrid(instructions)
        println(getNumTurnedOnCells(grid))
    }

    fun solvePartTwo() {
        val instructions = readInstructions("day_six.txt")
        val grid = getFinalNumberGrid(instructions)
        println(getTotalBrightness(grid))
    }
}

fun main() {
    DaySix().solvePartTwo()
}