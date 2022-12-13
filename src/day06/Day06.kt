package day06

import readInput

fun main() {
    fun getMarker(signal: String, length: Int): Int {
        val result = signal.windowed(length).indexOfFirst { window -> window.toSet().size == window.length }
        return result + length
    }

    fun part1(input: List<String>): Int {
        return getMarker(input[0], 4)
    }

    fun part2(input: List<String>): Int {
        return getMarker(input[0], 14)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day06/Day06_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 19)

    val input = readInput("day06/Day06")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
