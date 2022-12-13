package day12

import readInput

fun main() {
    data class Node(val position: Pair<Int, Int>, val symbol: Char) {
        val height: Int = when (symbol) {
            'S' -> 0
            'E' -> 25
            else -> symbol.code - 'a'.code
        }
    }

    fun getNode(nodes: List<Node>, position: Pair<Int, Int>): Node? {
        return nodes.firstOrNull { it.position == position }
    }

    fun getNeighbors(nodes: List<Node>, node: Node): List<Node> {
        val row = node.position.first
        val col = node.position.second

        return listOfNotNull(
            getNode(nodes, Pair(row - 1, col)),
            getNode(nodes, Pair(row + 1, col)),
            getNode(nodes, Pair(row, col - 1)),
            getNode(nodes, Pair(row, col + 1))
        ).filter { it.height - node.height <= 1 }
    }

    fun parseInput(input: List<String>): Pair<List<Node>, Map<Node, List<Node>>> {
        val nodes = mutableListOf<Node>()
        for (i in input.indices) {
            for (j in 0 until input.first().length) {
                nodes.add(Node(Pair(i, j), input[i][j]))
            }
        }
        val edges = nodes.associateWith { getNeighbors(nodes, it) }

        return Pair(nodes, edges)
    }

    fun dijkstra(nodes: List<Node>, edges: Map<Node, List<Node>>, start: Node, end: Node): Map<Node, Int> {
        val unvisited = mutableListOf<Node>()
        unvisited.addAll(nodes)

        val visited = mutableListOf<Node>()

        val distances = mutableMapOf<Node, Int>()
        unvisited.forEach { distances[it] = Int.MAX_VALUE }
        distances[start] = 0


        var currentNode = start

        while (unvisited.isNotEmpty()) {
            for (neighbor in edges[currentNode]?.filter { !visited.contains(it) }!!) {
                distances[neighbor] = kotlin.math.min(distances[neighbor]!!, distances[currentNode]!! + 1)
            }
            visited.add(currentNode)
            unvisited.remove(currentNode)

            if (currentNode == end) {
                return distances
            }

            currentNode = unvisited.minBy { distances[it]!! }
        }

        return distances
    }

    fun part1(input: List<String>): Int {
        val (nodes, edges) = parseInput(input)
        // println(nodes)
        // println(edges)

        val start = nodes.first { it.symbol == 'S' }
        val end = nodes.first { it.symbol == 'E' }
        val distances = dijkstra(nodes, edges, start, end)

        // println(distances)
        // println(distances[end])
        return distances[end]!!
    }

    fun part2(input: List<String>): Int {
        val (nodes, edges) = parseInput(input)

        val starts = nodes.filter { it.symbol == 'S' || it.symbol == 'a' }
        val end = nodes.first { it.symbol == 'E' }

        val distances = starts.mapIndexed { index, node ->
            // println("Trying a number $index")
            dijkstra(nodes, edges, node, end)
        }

        return distances.map { it[end]!! }.filter { it > 0 }.min()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day12/Day12_test")
    check(part1(testInput) == 31)
    check(part2(testInput) == 29)

    val input = readInput("day12/Day12")
    println("Part 1: ${part1(input)}")
    // println("Part 2: ${part2(input)}")
}
