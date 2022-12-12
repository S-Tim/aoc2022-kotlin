fun main() {
    data class Monkey(
        val items: MutableList<Long>,
        val operation: (Long) -> Long,
        val test: (Long) -> Int,
        var inspections: Long = 0
    )

    fun part1(input: List<String>): Long {
        // Test input
//        val monkeys = mapOf(
//            0 to Monkey(mutableListOf(79, 98), { it * 19 }, { if (it % 23L == 0L) 2 else 3 }),
//            1 to Monkey(mutableListOf(54, 65, 75, 74), { it + 6 }, { if (it % 19L == 0L) 2 else 0 }),
//            2 to Monkey(mutableListOf(79, 60, 97), { it * it }, { if (it % 13L == 0L) 1 else 3 }),
//            3 to Monkey(mutableListOf(74), { it + 3 }, { if (it % 17L == 0L) 0 else 1 })
//        )
//        val commonMultiple = 23 * 19 * 13 * 17

        // Real input
        val monkeys = mapOf(
            0 to Monkey(mutableListOf(54, 53), { it * 3 }, { if (it % 2L == 0L) 2 else 6 }),
            1 to Monkey(mutableListOf(95, 88, 75, 81, 91, 67, 65, 84), { it * 11 }, { if (it % 7L == 0L) 3 else 4 }),
            2 to Monkey(mutableListOf(76, 81, 50, 93, 96, 81, 83), { it + 6 }, { if (it % 3L == 0L) 5 else 1 }),
            3 to Monkey(mutableListOf(83, 85, 85, 63), { it + 4 }, { if (it % 11L == 0L) 7 else 4 }),
            4 to Monkey(mutableListOf(85, 52, 64), { it + 8 }, { if (it % 17L == 0L) 0 else 7 }),
            5 to Monkey(mutableListOf(57), { it + 2 }, { if (it % 5L == 0L) 1 else 3 }),
            6 to Monkey(mutableListOf(60, 95, 76, 66, 91), { it * it }, { if (it % 13L == 0L) 2 else 5 }),
            7 to Monkey(mutableListOf(65, 84, 76, 72, 79, 65), { it + 5 }, { if (it % 19L == 0L) 6 else 0 }),
        )
        val commonMultiple = 2 * 7 * 3 * 11 * 17 * 5 * 13 * 19

        val rounds = 10000
        for (i in 0 until rounds) {
            for ((_, monkey) in monkeys) {
                for (item in monkey.items) {
                    var newValue = monkey.operation(item)
//                    newValue /= 3
                    newValue %= commonMultiple
                    monkeys[monkey.test(newValue)]!!.items.add(newValue)
                    monkey.inspections++
                }
                monkey.items.clear()
            }
        }

        for (monkey in monkeys) {
            println(monkey)
        }

        val inspections = monkeys.values.map { it.inspections }.sorted().reversed()
        return inspections[0] * inspections[1]
    }

    fun part2(input: List<String>): Long {
        println(part1(input))
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    // check(part1(testInput) == 13140)
//     check(part2(testInput) == 2713310158)

    val input = readInput("Day11")
    println("Part 1: " + part1(input))
    // println("Part 2: " + part2(input))
}
