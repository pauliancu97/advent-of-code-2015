typealias RelationshipsMap = Map<String, Map<String, Int>>

class DayThirteen {
    data class Input(
        val mainPerson: String,
        val secondaryPerson: String,
        val happinessUnits: Int
    ) {
        companion object {
            val GAIN_REGEX = """([a-zA-Z]+) would gain (\d+) happiness units by sitting next to ([a-zA-Z]+)\."""
                .toRegex()
            val LOSE_REGEX = """([a-zA-Z]+) would lose (\d+) happiness units by sitting next to ([a-zA-Z]+)\."""
                .toRegex()
        }
    }

    private fun String.toInput(): Input? {
        val (happinessUnitsSign, regex) = when {
            Input.GAIN_REGEX.matches(this) -> 1 to Input.GAIN_REGEX
            Input.LOSE_REGEX.matches(this) -> -1 to Input.LOSE_REGEX
            else -> return null
        }
        val matchResult = regex.matchEntire(this) ?: return null
        val mainPerson = matchResult.groupValues.getOrNull(1) ?: return null
        val happinessUnits = matchResult.groupValues.getOrNull(2)?.toIntOrNull()?.times(happinessUnitsSign) ?:
            return null
        val secondaryPerson = matchResult.groupValues.getOrNull(3) ?: return null
        return Input(mainPerson, secondaryPerson, happinessUnits)
    }

    private fun getRelationshipsForPerson(person: String, inputs: List<Input>): Map<String, Int> =
        inputs
            .mapNotNull { input ->
                if (input.mainPerson == person) {
                    input.secondaryPerson to input.happinessUnits
                } else {
                    null
                }
            }
            .toMap()

    private fun getRelationshipMap(inputs: List<Input>): RelationshipsMap {
        val persons = inputs.flatMap { listOf(it.mainPerson, it.secondaryPerson) }.distinct()
        return persons.associateWith { person ->
            getRelationshipsForPerson(person, inputs)
        }
    }

    private fun readInputs(path: String) =
        readLines(path).mapNotNull { it.toInput() }

    private fun getCostOfSeating(seating: List<String>, relationshipsMap: RelationshipsMap): Int {
        var cost = 0
        for ((index, person) in seating.withIndex()) {
            val firstNeighbour = if (index == 0) {
                seating.last()
            } else {
                seating[index - 1]
            }
            val secondNeighbour = if (index == seating.size - 1) {
                seating.first()
            } else {
                seating[index + 1]
            }
            cost += relationshipsMap[person]?.getOrDefault(firstNeighbour, 0) ?: 0
            cost += relationshipsMap[person]?.getOrDefault(secondNeighbour, 0) ?: 0
        }
        return cost
    }

    private fun getOptimalTotalHappiness(relationshipsMap: RelationshipsMap): Int {
        val persons = relationshipsMap.keys.toList()
        return getPermutations(persons).maxOfOrNull { seating -> getCostOfSeating(seating, relationshipsMap) } ?: 0
    }

    private fun getOptimalTotalHappinessWithExtraPerson(relationshipsMap: RelationshipsMap): Int {
        val persons = relationshipsMap.keys.toList() + "Paul"
        return getPermutations(persons).maxOfOrNull { seating -> getCostOfSeating(seating, relationshipsMap) } ?: 0
    }

    fun solvePartOne() {
        val inputs = readInputs("day_thirteen.txt")
        val relationshipsMap = getRelationshipMap(inputs)
        val optimalTotalHappiness = getOptimalTotalHappiness(relationshipsMap)
        println(optimalTotalHappiness)
    }

    fun solvePartTwo() {
        val inputs = readInputs("day_thirteen.txt")
        val relationshipsMap = getRelationshipMap(inputs)
        val optimalTotalHappiness = getOptimalTotalHappinessWithExtraPerson(relationshipsMap)
        println(optimalTotalHappiness)
    }
}

fun main() {
    DayThirteen().solvePartTwo()
}