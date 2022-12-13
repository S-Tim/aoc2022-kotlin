package day01

import readInput

fun main() {
    fun countCalories(input: List<String>): List<Int> {
        val calories = mutableListOf<Int>()

        var currentCalories = 0
        for (line: String in input) {
            if (line.isEmpty()) {
                calories.add(currentCalories)
                currentCalories = 0
            } else {
                currentCalories += line.toInt()
            }
        }

        // Add the last elf
        calories.add(currentCalories)
        return calories
    }

    fun part1(input: List<String>): Int {
        val calories = countCalories(input)
        return calories.max()
    }

    fun part2(input: List<String>): Int {
        val calories = countCalories(input)
        val sortedCalories = calories.sorted().reversed()
        return sortedCalories.slice(0..2).sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day01/Day01_test")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readInput("day01/day01")
    println("Part 1: ${part1(input)}")
    println("Part 1: ${part2(input)}")
}
