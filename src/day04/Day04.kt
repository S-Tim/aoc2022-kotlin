package day04

import readInput

fun main() {
    fun parseAssignmentPair(assignment: String): Pair<Set<Int>, Set<Int>> {
        val sectionAssignmentFormat = """(\d+)-(\d+),(\d+)-(\d+)""".toRegex()
        val (start1, end1, start2, end2) = sectionAssignmentFormat.matchEntire(assignment)?.destructured
            ?: throw IllegalArgumentException("Incorrect input line $assignment")

        val sections1 = (start1.toInt()..end1.toInt()).toSet()
        val sections2 = (start2.toInt()..end2.toInt()).toSet()

        return Pair(sections1, sections2)
    }

    fun part1(input: List<String>): Int {
        return input
            .map(::parseAssignmentPair)
            .map { (section1, section2) -> section1.containsAll(section2) || section2.containsAll(section1) }
            .count { it }
    }

    fun part2(input: List<String>): Int {
        return input
            .map(::parseAssignmentPair)
            .map { (section1, section2) -> section1.intersect(section2).isNotEmpty() }
            .count { it }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day04/Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("day04/Day04")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
