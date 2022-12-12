fun main() {
    data class Node(val position: Pair<Int, Int>, val height: Int, val symbol: Char)

    fun getHeight(height: Char): Int {
        return when (height) {
            'S' -> 0
            'E' -> 25
            else -> height.code - 'a'.code
        }
    }

    fun getNode(nodes: List<Node>, position: Pair<Int, Int>): Node? {
        return nodes.firstOrNull { it.position == position }
    }

    fun parseInput(input: List<String>): Pair<List<Node>, Map<Node, List<Node>>> {
        val nodes = mutableListOf<Node>()
        val edges = mutableMapOf<Node, List<Node>>()

        for (i in input.indices) {
            for (j in 0 until input.first().length) {
                nodes.add(Node(Pair(i, j), getHeight(input[i][j]), input[i][j]))
            }
        }

        for (i in input.indices) {
            for (j in 0 until input.first().length) {
                val currentNode = getNode(nodes, Pair(i, j))!!
                val neighbors = mutableListOf<Node>()

                val top = getNode(nodes, Pair(i - 1, j))
                if ((top != null) && ((top.height - currentNode.height) <= 1)) {
                    neighbors.add(top)
                }
                val bottom = getNode(nodes, Pair(i + 1, j))
                if ((bottom != null) && ((bottom.height - currentNode.height) <= 1)) {
                    neighbors.add(bottom)
                }
                val left = getNode(nodes, Pair(i, j - 1))
                if ((left != null) && ((left.height - currentNode.height) <= 1)) {
                    neighbors.add(left)
                }
                val right = getNode(nodes, Pair(i, j + 1))
                if ((right != null) && ((right.height - currentNode.height) <= 1)) {
                    neighbors.add(right)
                }

                edges[currentNode] = neighbors
            }
        }

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
//        println(nodes)
//        println(edges)

        val start = nodes.first { it.symbol == 'S' }
        val end = nodes.first { it.symbol == 'E' }
        val distances = dijkstra(nodes, edges, start, end)

//        println(distances)
//        println(distances[end])
        return distances[end]!!
    }

    fun part2(input: List<String>): Int {
        val (nodes, edges) = parseInput(input)

        val starts = nodes.filter { it.symbol == 'S' || it.symbol == 'a' }
        val end = nodes.first { it.symbol == 'E' }

        var counter = 1
        val distances = starts.map {
            println("Trying a number $counter")
            counter++
            dijkstra(nodes, edges, it, end)
    }

        print(distances)
        return distances.map { it[end]!! }.filter { it > 0 }.min()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 31)
    check(part2(testInput) == 29)

    val input = readInput("Day12")
    println("Part 1: " + part1(input))
    println("Part 2: " + part2(input))
}
