import kotlin.math.max

typealias Qualities = List<Int>

class DayFifteen {

    private fun getQuantitiesHelper(
        ingredients: List<Qualities>,
        remainingQuantity: Int,
        quantitiesPerIngredient: List<Int>,
        evaluator: (List<Int>) -> Unit
    ) {
        if (quantitiesPerIngredient.size == ingredients.size - 1) {
            evaluator(quantitiesPerIngredient + remainingQuantity)
        } else {
            val depth = quantitiesPerIngredient.size
            val quantityUpperLimit = remainingQuantity - ingredients.size + depth + 1
            for (quantity in 1..quantityUpperLimit) {
                val updatedRemainingQuantity = remainingQuantity - quantity
                val updatedQuantitiesPerIngredient = quantitiesPerIngredient + quantity
                getQuantitiesHelper(
                    ingredients,
                    updatedRemainingQuantity,
                    updatedQuantitiesPerIngredient,
                    evaluator
                )
            }
        }
    }

    private fun getQuantities(
        ingredients: List<Qualities>,
        quantity: Int
    ): Int? {
        var maxMeasurementWithQuantities: Pair<Int, List<Int>>? = null
        getQuantitiesHelper(ingredients, quantity, emptyList()) { quantities ->
            val measurement = quantities.zip(ingredients)
                .map { (qty, qualities) -> qualities.map { it * qty } }
                .reduce { first, second -> first.zip(second).map { it.first + it.second } }
                .reduce { first, second -> max(first, 0) * max(second, 0) }
            val currentMaxMeasurementWithQuantities = maxMeasurementWithQuantities
            if (currentMaxMeasurementWithQuantities != null) {
                val (currentMeasurement, _) = currentMaxMeasurementWithQuantities
                if (measurement > currentMeasurement) {
                    maxMeasurementWithQuantities = measurement to quantities
                }
            } else {
                maxMeasurementWithQuantities = measurement to quantities
            }
        }
        return maxMeasurementWithQuantities?.first
    }

    private fun readFirstIngredients(path: String): List<Qualities> {
        val numberRegex = """[+-]?\d+""".toRegex()
        val lines = readLines(path)
        return lines
            .map { line ->
                val matchResults = numberRegex.findAll(line).toList()
                matchResults
                    .mapNotNull { matchResult ->
                        matchResult.groupValues.firstOrNull()?.toIntOrNull()
                    }
                    .dropLast(1)
            }
    }

    fun solvePartOne() {
        val ingredients = readFirstIngredients("day_fifteen.txt")
        val answer = getQuantities(ingredients,100)
        println(answer)
    }
}

fun main() {
    DayFifteen().solvePartOne()
}