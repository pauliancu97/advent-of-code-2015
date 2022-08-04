import kotlin.math.max
import kotlin.math.min

private typealias Graph = Map<String, DayNine.Node>

private fun getPermutationsHelper(
    currentPermutation: List<String>,
    availableElements: List<String>,
    permutations: MutableList<List<String>>
) {
    if (availableElements.isEmpty()) {
        permutations.add(currentPermutation)
    } else {
        for (element in availableElements) {
            val updatedPermutation = currentPermutation + element
            val updatedAvailableElements = availableElements - element
            getPermutationsHelper(
                updatedPermutation,
                updatedAvailableElements,
                permutations
            )
        }
    }
}

fun getPermutations(elements: List<String>): List<List<String>> {
    val permutations: MutableList<List<String>> = mutableListOf()
    getPermutationsHelper(emptyList(), elements, permutations)
    return permutations
}
class DayNine {

    data class InputEdge(
        val sourceNodeName: String,
        val destinationNodeName: String,
        val cost: Int
    ) {
        companion object {
            val REGEX = """([a-zA-Z]+) to ([a-zA-Z]+) = (\d+)""".toRegex()
        }
    }

    data class Edge(
        val nodeName: String,
        val cost: Int
    )

    data class Node(
        val name: String,
        val edges: List<Edge>
    ) {
        fun getCost(destinationName: String): Int? =
            edges.firstOrNull { it.nodeName == destinationName }?.cost
    }

    private fun String.toInputEdge(): InputEdge? {
        val matchResult = InputEdge.REGEX.matchEntire(this) ?: return null
        val sourceNodeName = matchResult.groupValues.getOrNull(1) ?: return null
        val destinationNodeName = matchResult.groupValues.getOrNull(2) ?: return null
        val cost = matchResult.groupValues.getOrNull(3)?.toIntOrNull() ?: return null
        return InputEdge(sourceNodeName, destinationNodeName, cost)
    }

    private fun getGraph(inputEdges: List<InputEdge>): Graph {
        val nodesNames = inputEdges.flatMap { listOf(it.sourceNodeName, it.destinationNodeName) }.distinct()
        val nodes = nodesNames
            .map { nodeName ->
                val edges = inputEdges
                    .mapNotNull { inputEdge ->
                        when {
                            inputEdge.sourceNodeName == nodeName -> Edge(inputEdge.destinationNodeName, inputEdge.cost)
                            inputEdge.destinationNodeName == nodeName -> Edge(inputEdge.sourceNodeName, inputEdge.cost)
                            else -> null
                        }
                    }
                Node(name = nodeName, edges = edges)
            }
        return nodes.associateBy { it.name }
    }

    private fun readInputEdges(path: String) =
        readLines(path).mapNotNull { it.toInputEdge() }

    private fun getPathCost(path: List<String>, graph: Graph): Int? {
        val pairedNodes = path.dropLast(1).zip(path.drop(1))
        var cost = 0
        for ((source, destination) in pairedNodes) {
            val edgeCost = graph[source]?.getCost(destination) ?: return null
            cost += edgeCost
        }
        return cost
    }

    private fun Graph.getNodesNames() = this.keys.toList()

    private fun getTravellingSalesmanSolution(graph: Graph): Int? {
        val nodes = graph.getNodesNames()
        var minCost: Int? = null
        for (path in getPermutations(nodes)) {
            val pathCost = getPathCost(path, graph)
            if (pathCost != null) {
                val currentMinCost = minCost
                minCost = if (currentMinCost != null) {
                    min(currentMinCost, pathCost)
                } else {
                    pathCost
                }
            }
        }
        return minCost
    }

    private fun getReversedTravellingSalesmanSolution(graph: Graph): Int? {
        val nodes = graph.getNodesNames()
        var maxCost: Int? = null
        for (path in getPermutations(nodes)) {
            val pathCost = getPathCost(path, graph)
            if (pathCost != null) {
                val currentMaxCost = maxCost
                maxCost = if (currentMaxCost != null) {
                    max(currentMaxCost, pathCost)
                } else {
                    pathCost
                }
            }
        }
        return maxCost
    }

    fun solvePartOne() {
        val inputEdges = readInputEdges("day_nine.txt")
        val graph = getGraph(inputEdges)
        val solution = getTravellingSalesmanSolution(graph)
        println(solution)
    }

    fun solvePartTwo() {
        val inputEdges = readInputEdges("day_nine.txt")
        val graph = getGraph(inputEdges)
        val solution = getReversedTravellingSalesmanSolution(graph)
        println(solution)
    }
}

fun main() {
    DayNine().solvePartTwo()
}