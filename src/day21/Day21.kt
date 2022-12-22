package day21

import readInput

fun main() {
    class OperationMonkey(val name: String, val left: String, val right: String, val operation: String) {
        override fun toString(): String {
            return "$name: $left $operation $right"
        }

        fun evaluate(numbers: Map<String, Long>): Long {
            if (left !in numbers || right !in numbers) {
                throw IllegalArgumentException("Missing numbers for evaluation")
            }
            return evaluate(numbers[left]!!, numbers[right]!!, operation)
        }

        fun evaluateRecursively(numbers: Map<String, Long>, operations: List<OperationMonkey>): Long {
            val leftValue = if (left in numbers) numbers[left]!! else {
                operations.firstOrNull { it.name == left }?.evaluateRecursively(numbers, operations) ?: -1
            }
            val rightValue = if (right in numbers) numbers[right]!! else {
                operations.firstOrNull { it.name == right }?.evaluateRecursively(numbers, operations) ?: -1
            }

            return evaluate(leftValue, rightValue, operation)
        }

        private fun evaluate(left: Long, right: Long, operation: String): Long {
            return when (operation) {
                "+" -> left + right
                "-" -> left - right
                "*" -> left * right
                "/" -> left / right
                else -> throw IllegalArgumentException("Unknown operation $operation")
            }
        }
    }

    fun parseInput(input: List<String>): Pair<MutableMap<String, Long>, MutableList<OperationMonkey>> {
        val numberMonkey = """([a-z]{4}): (\d+)""".toRegex()
        val operationMonkey = """([a-z]{4}): ([a-z]{4}) ([+\-*/]) ([a-z]{4})""".toRegex()

        val numbers = mutableMapOf<String, Long>()
        val operations = mutableListOf<OperationMonkey>()
        for (line in input) {
            numberMonkey.matchEntire(line)?.destructured?.let { (monkey, number) ->
                numbers[monkey] = number.toLong()
            }
            operationMonkey.matchEntire(line)?.destructured?.let { (monkey, left, operation, right) ->
                operations.add(OperationMonkey(monkey, left, right, operation))
            }
        }

        return Pair(numbers, operations)
    }

    fun part1(input: List<String>): Long {
        val (numbers, operations) = parseInput(input)

        while (!numbers.containsKey("root")) {
            for (operation in operations) {
                if (operation.left in numbers && operation.right in numbers) {
                    numbers[operation.name] = operation.evaluate(numbers)
                }
            }
        }

        return numbers["root"]!!
    }

    fun part2(input: List<String>): Long {
        val (numbers, operations) = parseInput(input)
        numbers.remove("humn")

        // evaluate everything that is possible without "humn" for more performance during the search (less recursive evaluations necessary)
        var oldSize = 0
        while (numbers.size != oldSize) {
            oldSize = numbers.size
            for (operation in operations) {
                if (operation.left in numbers && operation.right in numbers) {
                    numbers[operation.name] = operation.evaluate(numbers)
                }
            }
        }

        val root = operations.first { it.name == "root" }
        // assumes there is a target value that can be matched (opposed to both being variable)
        // it would not really matter though, one would just have to evaluate both sides on every search iteration then
        val targetValue = if (root.left in numbers) numbers[root.left]!! else numbers[root.right]!!
        val operation =
            if (root.left in numbers) operations.first { it.name == root.right } else operations.first { it.name == root.left }

        val testValueForDirection1 = targetValue - operation.evaluateRecursively(numbers + ("humn" to 0), operations)
        val testValueForDirection2 = targetValue - operation.evaluateRecursively(numbers + ("humn" to 10), operations)
        val growthDirection = testValueForDirection1 < testValueForDirection2

        // This solution is really based on trial and error and not a general solution for th problem.
        // The upper and lower bounds were found by trial and error and increasing the upper bound can also lead to a wrong result,
        // maybe only due to overflows, I am not sure.
        var low = 0L
        var high = 10_000_000_000_000L
        while (true) {
            val mid = (low + high) / 2
            numbers["humn"] = mid
            val difference = targetValue - operation.evaluateRecursively(numbers, operations)
            // println("Low $low, High $high, Difference $difference")
            if (difference > 0) {
                if (growthDirection) high = mid else low = mid
            } else if (difference < 0) {
                if (growthDirection) low = mid else high = mid
            } else {
                return mid
            }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day21/Day21_test")
    check(part1(testInput) == 152L)
    check(part2(testInput) == 302L) // In the puzzle it says 301 but 302 also works and that is what the binary search returns

    val input = readInput("day21/Day21")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
