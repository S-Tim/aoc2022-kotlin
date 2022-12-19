package day16

import readInput

fun main() {
    fun parseInput(input: List<String>): Triple<Set<String>, Map<String, Int>, Map<String, Set<String>>> {
        // Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
        val inputPattern = """Valve ([A-Z]{2}) has flow rate=(\d+); tunnels? leads? to valves? ([A-Z, ]+)""".toRegex()
        val valves = mutableSetOf<String>()
        val flowRates = mutableMapOf<String, Int>()
        val connections = mutableMapOf<String, Set<String>>()

        for (line in input) {
            val (valve, flowRate, valveConnections) = inputPattern.matchEntire(line)?.destructured
                ?: throw IllegalArgumentException("$line is not in the correct format")
            valves += valve
            flowRates[valve] = flowRate.toInt()
            connections[valve] = valveConnections.split(", ").toSet()
        }

        return Triple(valves, flowRates, connections)
    }

    data class Node(val name: String, val opened: Boolean, val passedTime: Int)

    fun part1(input: List<String>): Int {
        val (valves, flowRates, connections) = parseInput(input)

        val openValves = mutableSetOf<String>()
        var remainingTime = 30
        var score = 0

        score += openValves.sumOf { flowRates[it]!! }

        var current = "AA"
        val openSet = mutableSetOf(current)
        val cameFrom = mutableMapOf<String, String>()

        val gScore = mutableMapOf<String, Int>()
        gScore["AA"] = 0

        val fScore = mutableMapOf<String, Int>()
        val h: (String) -> Int = { valve -> 1 / (flowRates[valve]!!) }

        while (openSet.isNotEmpty()) {
            current = fScore.minBy { it.value }.key

            openSet.remove(current)
            connections[current]!!.forEach {
                val tentativeGScore = gScore[current]!! + 1
                if (tentativeGScore < gScore[it]!!) {
                    cameFrom[it] = current
                    gScore[it] = tentativeGScore
                    fScore[it] = tentativeGScore + h(it)
                    openSet + it
                }
            }
        }
        // println(valves)
        // println(flowRates)
        // println(connections)

        return 0
    }

    fun part2(input: List<String>): Long {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day16/Day16_test")
    check(part1(testInput) == 1651)
    // check(part2(testInput) == 56000011L)

    val input = readInput("day16/Day16")
    // println("Part 1: ${part1(input)}")
    // println("Part 2: ${part2(input)}")
}
