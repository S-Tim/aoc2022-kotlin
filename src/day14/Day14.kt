package day14

import readInput
import kotlin.math.max
import kotlin.math.min

enum class CaveElementType { ROCK, SAND, SPAWN }
data class CaveElement(val position: Pair<Int, Int>, val type: CaveElementType)

fun main() {
    fun parseInput(input: List<String>): MutableList<CaveElement> {
        val rocks = mutableListOf<CaveElement>()

        for (line in input) {
            val pairs = line.split("->").map { it.trim() }
            pairs.windowed(2).map { (pair1, pair2) ->
                val (x1, y1) = pair1.split(",").map { it.toInt() }
                val (x2, y2) = pair2.split(",").map { it.toInt() }

                val shape = mutableListOf<CaveElement>()

                for (x in min(x1, x2)..max(x1, x2)) {
                    for (y in min(y1, y2)..max(y1, y2)) {
                        shape.add(CaveElement(Pair(x, y), CaveElementType.ROCK))
                    }
                }

                rocks.addAll(shape)
            }
        }
        rocks.add(CaveElement(Pair(500, 0), CaveElementType.SPAWN))
        return rocks
    }

    fun printCave(elements: List<CaveElement>) {
        val xMin = elements.minBy { it.position.first }.position.first

        val xMax = elements.maxBy { it.position.first }.position.first
        val yMax = elements.maxBy { it.position.second }.position.second

        for (y in 0..(yMax + 1)) {
            print("$y\t")
            for (x in (xMin - 2)..(xMax + 2)) {
                val currentTile = elements.firstOrNull { it.position == Pair(x, y) }?.type
                print(
                    when (currentTile) {
                        CaveElementType.ROCK -> "#"
                        CaveElementType.SAND -> "X"
                        CaveElementType.SPAWN -> "+"
                        null -> "."
                    }
                )
            }
            println()
        }

    }

    fun isFree(elements: List<CaveElement>, x: Int, y: Int, floorHeight: Int): Boolean {
        if (y >= floorHeight) {
            return false
        }

        return elements.none { it.position.first == x && it.position.second == y }
    }

    fun part1(input: List<String>): Int {
        val elements = parseInput(input)
        printCave(elements)

        var sandCounter = 0
        var currentPosition = Pair(500, 0)
        val maxY = elements.maxBy { it.position.second }.position.second

        while (currentPosition.second <= maxY) {
            if (isFree(elements, currentPosition.first, currentPosition.second + 1, maxY + 10)) {
                currentPosition = Pair(currentPosition.first, currentPosition.second + 1)
            } else if (isFree(elements, currentPosition.first - 1, currentPosition.second + 1, maxY + 10)) {
                currentPosition = Pair(currentPosition.first - 1, currentPosition.second + 1)
            } else if (isFree(elements, currentPosition.first + 1, currentPosition.second + 1, maxY + 10)) {
                currentPosition = Pair(currentPosition.first + 1, currentPosition.second + 1)
            } else {
                elements.add(CaveElement(currentPosition, CaveElementType.SAND))
                currentPosition = Pair(500, 0)
                sandCounter++
            }
        }

        printCave(elements)
        return sandCounter
    }

    fun part2(input: List<String>): Int {
        val elements = parseInput(input)
        printCave(elements)

        var sandCounter = 0
        var currentPosition = Pair(500, 0)
        val maxY = elements.maxBy { it.position.second }.position.second

        while (true) {
            if (isFree(elements, currentPosition.first, currentPosition.second + 1, maxY + 2)) {
                currentPosition = Pair(currentPosition.first, currentPosition.second + 1)
            } else if (isFree(elements, currentPosition.first - 1, currentPosition.second + 1, maxY + 2)) {
                currentPosition = Pair(currentPosition.first - 1, currentPosition.second + 1)
            } else if (isFree(elements, currentPosition.first + 1, currentPosition.second + 1, maxY + 2)) {
                currentPosition = Pair(currentPosition.first + 1, currentPosition.second + 1)
            } else {
                elements.add(CaveElement(currentPosition, CaveElementType.SAND))
                sandCounter++
                if(sandCounter % 100 == 0){
                    println("Units of Sand: $sandCounter")
                }
                if (currentPosition == Pair(500, 0)) {
                    break
                }
                currentPosition = Pair(500, 0)
            }
        }

        printCave(elements)
        println(sandCounter)
        return sandCounter
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("day14/Day14_test")
    check(part1(testInput) == 24)
    check(part2(testInput) == 93)

    val input = readInput("day14/Day14")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
