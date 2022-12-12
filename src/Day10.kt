fun main() {
    abstract class Operation(var cyclesToComplete: Int, val operation: (Int) -> Int)

    class Noop : Operation(1, { it })
    class AddX(val value: Int) : Operation(2, { it + value })

    fun part1(input: List<String>): Int {
        val operations = mutableListOf<Operation>()
        var register = 1

        // Parse input
        for (line in input) {
            when {
                line.startsWith("noop") -> operations.add(Noop())
                line.startsWith("addx") -> {
                    val (_, value) = line.split(" ")
                    operations.add(AddX(value.toInt()))
                }
                else -> throw IllegalArgumentException("Unknown operation")
            }
        }

        var cycle = 1
        val signalStrengths = mutableListOf<Int>()
        var output = ""
        while (operations.isNotEmpty()) {
             // println("Cycle: $cycle, X: $register")

            if ((cycle - 20) % 40 == 0) {
                signalStrengths.add(cycle * register)
                // println("Signal strength at cycle $cycle: ${cycle * register}")
            }

            // print pixel
            output += if ((cycle - 1) % 40 in (register - 1..register + 1)) {
                "#"
            } else {
                "."
            }

            // decrease cycle counts
            operations.first().cyclesToComplete--

            // run all commands which have cycle count 0
            if (operations.first().cyclesToComplete == 0) {
                register = operations.first().operation(register)
                operations.removeFirst()
            }

            cycle++
        }

        output.chunked(40).forEach { println(it) }
        return signalStrengths.sum()
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    // check(part1(testInput) == 13140)
    // check(part2(testInput) == 8)

    val input = readInput("Day10")
    println("Part 1: " + part1(input))
    // println("Part 2: " + part2(input))
}
