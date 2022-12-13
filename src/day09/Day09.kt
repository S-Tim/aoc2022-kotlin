package day09

import readInput

fun main() {
    data class Rope(
        val head: Pair<Int, Int> = Pair(0, 0),
        val knots: List<Pair<Int, Int>> = List(1) { Pair(0, 0) },
        val visited: Set<Pair<Int, Int>> = emptySet()
    ) {
        fun moveUp(): Rope {
            return move(
                Pair(head.first, head.second - 1),
            )
        }

        fun moveDown(): Rope {
            return move(
                Pair(head.first, head.second + 1),
            )
        }

        fun moveLeft(): Rope {
            return move(
                Pair(head.first - 1, head.second),
            )
        }

        fun moveRight(): Rope {
            return move(
                Pair(head.first + 1, head.second),
            )
        }

        private fun move(
            newPosition: Pair<Int, Int>,
        ): Rope {
            val newTailX = when {
                newPosition.first - knots.first().first == 2 -> knots.first().first + 1
                newPosition.first - knots.first().first == -2 -> knots.first().first - 1
                else -> knots.first().first
            }
            val newTailY = when {
                newPosition.second - knots.first().second == 2 -> knots.first().second + 1
                newPosition.second - knots.first().second == -2 -> knots.first().second - 1
                else -> knots.first().second
            }
            val newTailPosition = Pair(newTailX, newTailY)
            println("$newPosition, $newTailPosition")

            return Rope(newPosition, listOf(newTailPosition), visited + newTailPosition)
        }
    }

    fun part1(input: List<String>): Int {
        var rope = Rope()

        for (command in input) {
            val (direction, count) = command.split(" ")
//            println("$rope, Direction: $direction, count: $count")
            for (i in 0 until count.toInt()) {
                rope = when (direction) {
                    "R" -> rope.moveRight()
                    "L" -> rope.moveLeft()
                    "U" -> rope.moveUp()
                    "D" -> rope.moveDown()
                    else -> throw IllegalArgumentException("Direction $direction is unknown")
                }
            }
        }

        println(rope.visited.size)
        return rope.visited.size
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day9/Day09_test")
    check(part1(testInput) == 13)
//    check(part2(testInput) == 8)

    val input = readInput("day9/Day09")
    println("Part 1: ${part1(input)}")
//    println("Part 2: ${part2(input)}")
}
