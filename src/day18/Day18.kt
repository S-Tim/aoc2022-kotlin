package day18

import readInput

typealias Point3D = Triple<Int, Int, Int>

fun main() {
    operator fun Point3D.plus(other: Point3D): Point3D {
        return Point3D(this.first + other.first, this.second + other.second, this.third + other.third)
    }

    val directions = setOf(
        Point3D(1, 0, 0),
        Point3D(-1, 0, 0),
        Point3D(0, 1, 0),
        Point3D(0, -1, 0),
        Point3D(0, 0, 1),
        Point3D(0, 0, -1)
    )

    fun getSurfaceArea(coordinates: Set<Point3D>, point: Point3D): Set<Point3D> {
        return directions.map { it + point }.filter { it !in coordinates }.toSet()
    }

    fun isSurface(coordinates: Set<Point3D>, point: Point3D): Boolean {
        val closedSet = mutableSetOf<Point3D>()
        val frontier = ArrayDeque(elements = listOf(point))

        while (frontier.isNotEmpty()) {
            val current = frontier.removeFirst()

            // Not air or already seen
            if (current in coordinates || current in closedSet) continue

            closedSet.add(current)

            // found a lot of points, so either huge air pocket or outside
            if (closedSet.size > 2000) return true

            directions.map { current + it }.forEach { frontier.add(it) }
        }
        // must be an air pocket if only a finite number of points can be found
        return false
    }

    fun parseInput(input: List<String>): Set<Point3D> {
        return input.map { line -> line.split(",").map { it.toInt() } }.map { (x, y, z) -> Triple(x, y, z) }.toSet()
    }

    fun part1(input: List<String>): Int {
        val coordinates = parseInput(input)
        return coordinates.sumOf { getSurfaceArea(coordinates, it).size }
    }

    fun part2(input: List<String>): Int {
        val coordinates = parseInput(input)
        return coordinates.sumOf {
            getSurfaceArea(coordinates, it).filter { emptySpot -> isSurface(coordinates, emptySpot) }.size
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day18/Day18_test")
    check(part1(testInput) == 64)
    check(part2(testInput) == 58)

    val input = readInput("day18/Day18")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
