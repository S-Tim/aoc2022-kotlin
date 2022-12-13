package day08

import readInput
import java.lang.Integer.max

fun main() {
    fun parseInput(input: List<String>): List<List<Int>> {
        return input.map { line -> line.toList().map { it.digitToInt() } }
    }

    fun addTreeIfVisible(
        seenTrees: MutableSet<Pair<Int, Int>>,
        forest: List<List<Int>>,
        row: Int,
        column: Int,
        minHeight: Int
    ): Int {
        if (forest[row][column] > minHeight) {
            seenTrees.add(Pair(row, column))
            // return new minHeight because tree was added
            return forest[row][column]
        }
        return minHeight
    }

    fun part1(input: List<String>): Int {
        val forest = parseInput(input)
        val seenTrees = mutableSetOf<Pair<Int, Int>>()

        for (i in forest.indices) {
            var minHeightFromLeft = -1
            var minHeightFromTop = -1
            var minHeightFromRight = -1
            var minHeightFromBottom = -1

            for (j in forest.indices) {
                val reversedJ = forest.size - j - 1

                minHeightFromLeft = addTreeIfVisible(seenTrees, forest, i, j, minHeightFromLeft)
                minHeightFromTop = addTreeIfVisible(seenTrees, forest, j, i, minHeightFromTop)
                minHeightFromRight = addTreeIfVisible(seenTrees, forest, i, reversedJ, minHeightFromRight)
                minHeightFromBottom = addTreeIfVisible(seenTrees, forest, reversedJ, i, minHeightFromBottom)
            }
        }
        return seenTrees.size
    }

    fun calcScoreForDirection(trees: List<Int>, treeHouseHeight: Int): Int {
        return when {
            trees.all { it < treeHouseHeight } -> trees.size
            else -> trees.takeWhile { it < treeHouseHeight }.count() + 1
        }
    }

    fun calculateScenicScore(forest: List<List<Int>>, row: Int, column: Int): Int {
        val treeHouseHeight = forest[row][column]
        val neighboringTrees = listOf(
            ((column + 1) until forest.size).map { forest[row][it] }, // right
            ((column - 1) downTo 0).map { forest[row][it] }, // left
            ((row + 1) until forest.size).map { forest[it][column] }, // down
            ((row - 1) downTo 0).map { forest[it][column] } // up
        )
        return neighboringTrees.map { calcScoreForDirection(it, treeHouseHeight) }.reduce { acc, i -> acc * i }
    }

    fun part2(input: List<String>): Int {
        val forest = parseInput(input)

        var highScore = 0
        for (i in forest.indices) {
            for (j in forest.indices) {
                highScore = max(highScore, calculateScenicScore(forest, i, j))
            }
        }
        return highScore
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day08/Day08_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readInput("day08/Day08")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
