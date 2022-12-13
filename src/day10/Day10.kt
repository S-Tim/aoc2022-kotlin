package day10

import readInput

fun main() {
    abstract class Operation(var cyclesToComplete: Int, val operation: (Int) -> Int)
    class Noop : Operation(1, { it })
    class AddX(val value: Int) : Operation(2, { it + value })

    fun parseInput(input: List<String>): List<Operation> {
        return input.map { line ->
            when {
                line.startsWith("noop") -> Noop()
                line.startsWith("addx") -> {
                    val (_, value) = line.split(" ")
                    AddX(value.toInt())
                }

                else -> throw IllegalArgumentException("Unknown operation")
            }
        }
    }

    fun day10(input: List<String>): Pair<Int, String> {
        var operations = parseInput(input)
        var register = 1
        var cycle = 1
        val signalStrengths = mutableListOf<Int>()
        var output = ""

        while (operations.isNotEmpty()) {
            // println("Cycle: $cycle, X: $register")

            if ((cycle - 20) % 40 == 0) {
                signalStrengths.add(cycle * register)
                // println("Signal strength at cycle $cycle: ${cycle * register}")
            }

            // calculate pixel
            output += if ((cycle - 1) % 40 in (register - 1..register + 1)) "#" else "."

            // decrease cycle count
            operations.first().cyclesToComplete--

            // run command if cycle count is 0
            if (operations.first().cyclesToComplete == 0) {
                register = operations.first().operation(register)
                operations = operations.drop(1)
            }

            cycle++
        }

        return Pair(signalStrengths.sum(), output.chunked(40).joinToString("\n"))
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day10/Day10_test")
    check(day10(testInput).first == 13140)
    check(day10(testInput).second == readInput("day10/Day10_test_part2").joinToString("\n"))

    val input = readInput("day10/Day10")
    val (part1, part2) = day10(input)
    println("Part 1: $part1")
    println("Part 2:\n$part2")
}
