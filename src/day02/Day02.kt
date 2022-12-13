package day02

import readInput

fun main() {
    val winsAgainst = mapOf("A" to "C", "B" to "A", "C" to "B")
    val losesAgainst = mapOf("A" to "B", "B" to "C", "C" to "A")
    val convertSymbol = mapOf("X" to "A", "Y" to "B", "Z" to "C")

    fun calculateScore(player1: String, player2: String): Int {
        val symbolScores = mapOf("A" to 1, "B" to 2, "C" to 3)

        return symbolScores[player2]!! + when {
            winsAgainst[player2] == player1 -> 6
            player2 == player1 -> 3
            else -> 0
        }
    }

    fun part1(input: List<String>): Int {
        return input.map { it.split(" ") }
            .sumOf { (player1, player2) -> calculateScore(player1, convertSymbol[player2]!!) }
    }

    fun part2(input: List<String>): Int {
        return input.map { it.split(" ") }
            .sumOf { (player1, player2) ->
                val converted = when (player2) {
                    "X" -> winsAgainst[player1]!!
                    "Y" -> player1
                    else -> losesAgainst[player1]!!
                }
                calculateScore(player1, converted)
            }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day02/Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("day02/day02")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
