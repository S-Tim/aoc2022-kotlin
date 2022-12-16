@file:JvmName("Day14Kt")

package day14

import readInput
import kotlin.math.max
import kotlin.math.min

fun main() {
    class Cave(
        val spawn: Pair<Int, Int> = Pair(500, 0),
        val rocks: Set<Pair<Int, Int>>,
        var sand: MutableSet<Pair<Int, Int>> = mutableSetOf(),
        val abyssLevel: Int? = null
    ) {
        private var sandCount = 0
        private val abyss = if (abyssLevel != null) rocks.maxBy { it.second }.second + abyssLevel else null

        fun spawnSand(): Boolean {
            val elements = rocks.union(sand)
            val yMax = elements.maxBy { it.second }.second

            var currentPosition = spawn
            while (currentPosition.second <= yMax || abyss != null) {
                if (abyss != null && currentPosition.second == abyss - 1) {
                    sand.add(currentPosition)
                    sandCount++
                    return true
                }

                currentPosition = when {
                    Pair(currentPosition.first, currentPosition.second + 1) !in elements -> {
                        Pair(currentPosition.first, currentPosition.second + 1)
                    }

                    Pair(currentPosition.first - 1, currentPosition.second + 1) !in elements -> {
                        Pair(currentPosition.first - 1, currentPosition.second + 1)
                    }

                    Pair(currentPosition.first + 1, currentPosition.second + 1) !in elements -> {
                        Pair(currentPosition.first + 1, currentPosition.second + 1)
                    }

                    else -> {
                        sand.add(currentPosition)
                        sandCount++
                        return currentPosition != spawn
                    }
                }
            }
            return false
        }

        fun simulate(): Int {
            var comesToRest = spawnSand()
            while (comesToRest) {
                comesToRest = spawnSand()
            }

            return sandCount
        }

        override fun toString(): String {
            val padding = 2
            val elements = rocks.union(sand)
            val xMin = elements.minBy { it.first }.first
            val xMax = elements.maxBy { it.first }.first
            val yMax = elements.maxBy { it.second }.second

            var result = ""
            for (y in 0..(yMax + padding)) {
                result += "$y\t"
                for (x in (xMin - padding)..(xMax + padding)) {
                    val currentPosition = Pair(x, y)
                    result += when (currentPosition) {
                        in rocks -> "#"
                        in sand -> "X"
                        spawn -> "+"
                        else -> "."
                    }
                }
                result += "\n"
            }
            return result
        }
    }

    fun parseInput(input: List<String>): Set<Pair<Int, Int>> {
        val rocks = mutableSetOf<Pair<Int, Int>>()

        for (line in input) {
            val pairs = line.split("->").map { it.trim() }
            pairs.windowed(2).map { (pair1, pair2) ->
                val (x1, y1) = pair1.split(",").map { it.toInt() }
                val (x2, y2) = pair2.split(",").map { it.toInt() }

                val shape = mutableListOf<Pair<Int, Int>>()

                for (x in min(x1, x2)..max(x1, x2)) {
                    for (y in min(y1, y2)..max(y1, y2)) {
                        shape.add(Pair(x, y))
                    }
                }

                rocks.addAll(shape)
            }
        }
        return rocks
    }

    fun part1(input: List<String>): Int {
        val cave = Cave(Pair(500, 0), parseInput(input))
        println(cave)
        val sandCount = cave.simulate()
        println(cave)
        return sandCount
    }

    fun part2(input: List<String>): Int {
        val cave = Cave(Pair(500, 0), parseInput(input), abyssLevel = 2)
        println(cave)
        val sandCount = cave.simulate()
        println(cave)
        return sandCount
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day14/Day14_test")
    check(part1(testInput) == 24)
    check(part2(testInput) == 93)

    val input = readInput("day14/Day14")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
