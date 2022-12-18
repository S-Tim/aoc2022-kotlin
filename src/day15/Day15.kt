package day15

import readInput
import kotlin.math.abs
import kotlin.math.min

fun main() {
    fun Pair<Int, Int>.distance(other: Pair<Int, Int>): Int {
        return abs(this.first - other.first) + abs(this.second - other.second)
    }

    fun parseInput(input: List<String>): Triple<List<Pair<Int, Int>>, List<Pair<Int, Int>>, List<Int>> {
        val inputPattern = """Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)""".toRegex()

        val sensors = mutableListOf<Pair<Int, Int>>()
        val beacons = mutableListOf<Pair<Int, Int>>()
        val distances = mutableListOf<Int>()

        for (line in input) {
            val (sensorX, sensorY, beaconX, beaconY) = inputPattern.matchEntire(line)?.destructured
                ?: throw IllegalArgumentException("Line is not in the correct format $line")

            val sensor = Pair(sensorX.toInt(), sensorY.toInt())
            val beacon = Pair(beaconX.toInt(), beaconY.toInt())

            sensors.add(sensor)
            beacons.add(beacon)
            distances.add(sensor.distance(beacon))
        }

        return Triple(sensors, beacons, distances)
    }

    fun isPossibleBeaconLocation(
        sensors: List<Pair<Int, Int>>,
        beacons: List<Pair<Int, Int>>,
        distances: List<Int>,
        position: Pair<Int, Int>
    ): Boolean {
        if (position in beacons) {
            return true
        }

        for (j in sensors.indices) {
            val distance = position.distance(sensors[j])
            if (distance <= distances[j]) {
                return false
            }
        }

        return true
    }

    fun part1(input: List<String>, y: Int): Int {
        val (sensors, beacons, distances) = parseInput(input)

        // minimum x value corrected by its distance to the row y
        val minX = sensors.minBy { it.first - abs(y - it.second) }.first
        // maximum x value corrected by its distance to the row y
        val maxX = sensors.maxBy { it.first - abs(y - it.second) }.first
        // distances are bounded by the maximum distance to any sensor for a pessimistic guess
        val maxDistance = distances.max()

        var impossibleLocationCount = 0
        for (i in (minX - maxDistance)..(maxX + maxDistance)) {
            val currentPosition = Pair(i, y)
            if (!isPossibleBeaconLocation(sensors, beacons, distances, currentPosition)) {
                impossibleLocationCount++
                // print("#")
            } else {
                // print("B")
            }
        }

        // println()
        // println(impossibleLocationCount)
        return impossibleLocationCount
    }

    fun part2(input: List<String>, maxValue: Int): Long {
        val (sensors, beacons, distances) = parseInput(input)

        for (i in 0..maxValue) {
            for (j in 0..maxValue) {
                if (isPossibleBeaconLocation(sensors, beacons, distances, Pair(i, j)) && Pair(i, j) !in beacons) {
                    return i * 4000000L + j
                }
            }
        }
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day15/Day15_test")
    check(part1(testInput, 10) == 26)
    check(part2(testInput, 20) == 56000011L)

    val input = readInput("day15/Day15")
    println("Part 1: ${part1(input, 2000000)}")
    // println("Part 2: ${part2(input, 4000000)}") // Too slow!
}
