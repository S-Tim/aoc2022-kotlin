package day02

import readInput

fun main() {
    fun part1(input: List<String>): Int {
        return 0
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day02/Day02_test")
    check(part1(testInput) == 24000)

    val input = readInput("day02/day02")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
