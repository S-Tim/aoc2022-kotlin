package day11

import readInput

fun main() {
    data class Monkey(
        val items: MutableList<Long>,
        val operation: (Long) -> Long,
        val test: (Long) -> Int,
        var inspections: Long = 0
    )

    fun playRounds(monkeys: Map<Int, Monkey>, rounds: Int, roundFunction: (Long) -> Long = { it }): Map<Int, Monkey> {
        for (i in 0 until rounds) {
            for ((_, monkey) in monkeys) {
                for (item in monkey.items) {
                    var newValue = monkey.operation(item)
                    newValue = roundFunction(newValue)
                    monkeys[monkey.test(newValue)]!!.items.add(newValue)
                    monkey.inspections++
                }
                monkey.items.clear()
            }
        }

        return monkeys
    }

    fun calculateMonkeyBusiness(monkeys: Collection<Monkey>): Long {
        val inspections = monkeys.map { it.inspections }.sorted().reversed()
        return inspections[0] * inspections[1]
    }

    fun part1(monkeys: Map<Int, Monkey>): Long {
        return calculateMonkeyBusiness(playRounds(monkeys, 20) { it / 3 }.values)
    }

    fun part2(monkeys: Map<Int, Monkey>, commonMultiple: Int): Long {
        return calculateMonkeyBusiness(playRounds(monkeys, 10000) { it % commonMultiple }.values)
    }

    fun getTestData(): Pair<Map<Int, Monkey>, Int> {
        val testMonkeys = mapOf(
            0 to Monkey(mutableListOf(79, 98), { it * 19 }, { if (it % 23L == 0L) 2 else 3 }),
            1 to Monkey(mutableListOf(54, 65, 75, 74), { it + 6 }, { if (it % 19L == 0L) 2 else 0 }),
            2 to Monkey(mutableListOf(79, 60, 97), { it * it }, { if (it % 13L == 0L) 1 else 3 }),
            3 to Monkey(mutableListOf(74), { it + 3 }, { if (it % 17L == 0L) 0 else 1 })
        )
        val testCommonMultiple = 23 * 19 * 13 * 17
        return Pair(testMonkeys, testCommonMultiple)
    }

    fun getRealData(): Pair<Map<Int, Monkey>, Int> {
        val realMonkeys = mapOf(
            0 to Monkey(mutableListOf(54, 53), { it * 3 }, { if (it % 2L == 0L) 2 else 6 }),
            1 to Monkey(mutableListOf(95, 88, 75, 81, 91, 67, 65, 84), { it * 11 }, { if (it % 7L == 0L) 3 else 4 }),
            2 to Monkey(mutableListOf(76, 81, 50, 93, 96, 81, 83), { it + 6 }, { if (it % 3L == 0L) 5 else 1 }),
            3 to Monkey(mutableListOf(83, 85, 85, 63), { it + 4 }, { if (it % 11L == 0L) 7 else 4 }),
            4 to Monkey(mutableListOf(85, 52, 64), { it + 8 }, { if (it % 17L == 0L) 0 else 7 }),
            5 to Monkey(mutableListOf(57), { it + 2 }, { if (it % 5L == 0L) 1 else 3 }),
            6 to Monkey(mutableListOf(60, 95, 76, 66, 91), { it * it }, { if (it % 13L == 0L) 2 else 5 }),
            7 to Monkey(mutableListOf(65, 84, 76, 72, 79, 65), { it + 5 }, { if (it % 19L == 0L) 6 else 0 }),
        )
        val realCommonMultiple = 2 * 7 * 3 * 11 * 17 * 5 * 13 * 19

        return Pair(realMonkeys, realCommonMultiple)
    }

    // test if implementation meets criteria from the description, like:
    check(part1(getTestData().first) == 10605L)
    val (testMonkeys, testCommonMultiple) = getTestData()
    check(part2(testMonkeys, testCommonMultiple) == 2713310158L)

    // Real input
    println("Part 1: ${part1(getRealData().first)}")
    val (realMonkeys, realCommonMultiple) = getRealData()
    println("Part 2: ${part2(realMonkeys, realCommonMultiple)}")
}
