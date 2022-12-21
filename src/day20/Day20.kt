package day20

import readInput

fun main() {
    class Number(val value: Long)

    fun mix(numbers: List<Number>, number: Number): List<Number> {
        if (number.value == 0L) return numbers

        val oldIndex = numbers.indexOf(number)
        val newList = numbers.toMutableList()
        newList.removeAt(oldIndex)

        var newIndex = (oldIndex + number.value) % newList.size

        if (newIndex == 0L) newIndex = newList.size.toLong()
        if (newIndex < 0L) newIndex += newList.size

        newList.add(newIndex.toInt(), number)

        return newList
    }

    fun calculateScore(numbers: List<Number>): Long {
        val zero = numbers.indexOfFirst { it.value == 0L }
        return listOf(1000, 2000, 3000).map { (zero + it) % numbers.size }.sumOf { numbers[it].value }
    }

    fun part1(input: List<String>): Long {
        val numbers = input.map { Number(it.toLong()) }

        var mixed = numbers.toList()
        for (number in numbers) {
            mixed = mix(mixed, number)
        }

        return calculateScore(mixed)
    }

    fun part2(input: List<String>): Long {
        val decryptionKey = 811589153L
        val numbers = input.map { Number(it.toLong() * decryptionKey) }

        var mixed = numbers.toList()
        repeat(10) {
            for (number in numbers) {
                mixed = mix(mixed, number)
            }
        }

        return calculateScore(mixed)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day20/Day20_test")
    check(part1(testInput) == 3L)
    check(part2(testInput) == 1623178306L)

    val input = readInput("day20/Day20")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
