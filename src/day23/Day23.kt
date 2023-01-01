package day23

import Point
import day23.Directions.*
import plus
import printAsGrid
import readInput

enum class Directions { N, NE, NW, S, SE, SW, W, E }

fun main() {
    fun parseInput(input: List<String>): Set<Point> {
        return input.flatMapIndexed { rowIndex, row ->
            row.mapIndexed { columnIndex, tile ->
                if (tile == '#') Point(
                    columnIndex,
                    rowIndex
                ) else null
            }.filterNotNull()
        }.toSet()
    }

    val directions =
        mapOf(
            N to Point(0, -1),
            NE to Point(1, -1),
            NW to Point(-1, -1),
            S to Point(0, 1),
            SE to Point(1, 1),
            SW to Point(-1, 1),
            W to Point(-1, 0),
            E to Point(1, 0)
        )

    fun allFree(point: Point, points: Set<Point>): Boolean {
        return directions.values.map { point + it }.none { it in points }
    }

    fun isFree(point: Point, directions: List<Point>, points: Set<Point>): Boolean {
        return directions.map { point + it }.none { it in points }
    }

    val isNorthFree = { point: Point, points: Set<Point> ->
        isFree(point, listOfNotNull(directions[N], directions[NE], directions[NW]), points)
    }

    val isSouthFree = { point: Point, points: Set<Point> ->
        isFree(point, listOfNotNull(directions[S], directions[SE], directions[SW]), points)
    }

    val isWestFree = { point: Point, points: Set<Point> ->
        isFree(point, listOfNotNull(directions[W], directions[NW], directions[SW]), points)
    }

    val isEastFree = { point: Point, points: Set<Point> ->
        isFree(point, listOfNotNull(directions[E], directions[NE], directions[SE]), points)
    }

    fun calculateNextState(
        directions: List<Pair<(Point, Set<Point>) -> Boolean, Point>>,
        elves: Set<Point>
    ): Set<Point> {
        val proposedPositions = elves.associate { elf ->
            if (allFree(elf, elves)) {
                elf to elf
            } else {
                elf to elf + (directions.firstOrNull { it.first(elf, elves) }?.second ?: Point(0, 0))
            }
        }

        return elves.map { elf -> if (proposedPositions.values.count { it == proposedPositions[elf] } == 1) proposedPositions[elf]!! else elf }
            .toSet()
    }

    fun part1(input: List<String>): Int {
        val dirs = mutableListOf(
            isNorthFree to directions[N]!!,
            isSouthFree to directions[S]!!,
            isWestFree to directions[W]!!,
            isEastFree to directions[E]!!
        )
        var elves = parseInput(input)

        printAsGrid(elves)
        repeat(10) {
            elves = calculateNextState(dirs, elves)
            dirs.add(dirs.removeFirst())
            printAsGrid(elves)
        }

        // calculate score
        val minX = elves.minBy { it.first }.first
        val maxX = elves.maxBy { it.first }.first
        val minY = elves.minBy { it.second }.second
        val maxY = elves.maxBy { it.second }.second
        return (maxX - minX + 1) * (maxY - minY + 1) - elves.size
    }

    fun part2(input: List<String>): Int {
        val dirs = mutableListOf(
            isNorthFree to directions[N]!!,
            isSouthFree to directions[S]!!,
            isWestFree to directions[W]!!,
            isEastFree to directions[E]!!
        )
        var elves = parseInput(input)

        var round = 1
        while (true) {
            if (round % 100 == 0) {
                println("Currently on Round $round")
            }

            val newElves = calculateNextState(dirs, elves)
            if (newElves == elves) {
                return round
            }

            elves = newElves
            dirs.add(dirs.removeFirst())
            round++
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day23/Day23_test")
    check(part1(testInput) == 110)
    check(part2(testInput) == 20)

    val input = readInput("day23/Day23")
//    println("Part 1: ${part1(input)}")
//    println("Part 2: ${part2(input)}")
}
