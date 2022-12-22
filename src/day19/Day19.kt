package day19

import readInput
import kotlin.math.max
import kotlin.math.min

fun main() {
    data class Resources(var ore: Int, var clay: Int, var obsidian: Int, var geode: Int) {
        operator fun minus(other: Resources): Resources {
            return Resources(ore - other.ore, clay - other.clay, obsidian - other.obsidian, geode - other.geode)
        }

        operator fun plus(other: Resources): Resources {
            return Resources(ore + other.ore, clay + other.clay, obsidian + other.obsidian, geode + other.geode)
        }

        fun canAfford(cost: Resources): Boolean {
            val remaining = this - cost
            return remaining.ore >= 0 && remaining.clay >= 0 && remaining.obsidian >= 0 && remaining.geode >= 0
        }
    }

    data class Blueprint(
        val id: Int,
        val oreRobotCost: Resources,
        val clayRobotCost: Resources,
        val obsidianRobotCost: Resources,
        val geodeRobotCost: Resources
    ) {
        fun getCosts(): List<Resources> {
            return listOf(oreRobotCost, clayRobotCost, obsidianRobotCost, geodeRobotCost)
        }
    }

    data class State(val materials: Resources, val robots: Resources)

    fun maxResourceNeeded(maxResourceCost: Int, resourceRobots: Int, remainingTime: Int): Int {
        return max(maxResourceCost, maxResourceCost * remainingTime - resourceRobots * remainingTime)
    }

    fun solveBlueprint(blueprint: Blueprint, time: Int): Int {
        val frontier = ArrayDeque<Pair<State, Int>>()
        val visited = mutableSetOf<State>()

        val maxOreCost = blueprint.getCosts().maxOf { it.ore }
        val maxClayCost = blueprint.getCosts().maxOf { it.clay }
        val maxObsidianCost = blueprint.getCosts().maxOf { it.obsidian }

        // Start with one ore robot
        val start = State(Resources(0, 0, 0, 0), Resources(1, 0, 0, 0))
        frontier.add(Pair(start, time))

        println("Solving blueprint ${blueprint.id} with $time minutes")

        var best = 0
        while (frontier.isNotEmpty()) {
            val (state, remainingTime) = frontier.removeFirst()
            val (materials, robots) = state

            if (materials.geode > best) {
                best = materials.geode
            }

            if (remainingTime == 0 || state in visited) {
                continue
            }

            visited.add(state)

            /*
            You do not need more of a resource than what can be used.
            For example if you need a maximum of 10 ore per round and you have ten or more ore robots,
            then you generate enough ore every round and do not need to keep any more.
            If you have less robots than the amount you have to keep is the difference of what you can
            spend in the remaining rounds and what you are currently producing in the remaining time
             */
            val maxOreNeeded = maxResourceNeeded(maxOreCost, robots.ore, remainingTime)
            val maxClayNeeded = maxResourceNeeded(maxClayCost, robots.clay, remainingTime)
            val maxObsidianNeeded = maxResourceNeeded(maxObsidianCost, robots.obsidian, remainingTime)

            val crop: (Resources) -> Resources =
                { resources ->
                    Resources(
                        min(resources.ore, maxOreNeeded),
                        min(resources.clay, maxClayNeeded),
                        min(resources.obsidian, maxObsidianNeeded),
                        resources.geode
                    )
                }

            if (materials.canAfford(blueprint.oreRobotCost) && robots.ore < maxOreCost) {
                val newMaterials = crop(materials + robots - blueprint.oreRobotCost)
                val newRobots = robots + Resources(1, 0, 0, 0)
                frontier.add(Pair(State(newMaterials, newRobots), remainingTime - 1))
            }

            if (materials.canAfford(blueprint.clayRobotCost) && robots.clay < maxClayCost) {
                val newMaterials = crop(materials + robots - blueprint.clayRobotCost)
                val newRobots = robots + Resources(0, 1, 0, 0)
                frontier.add(Pair(State(newMaterials, newRobots), remainingTime - 1))
            }

            if (materials.canAfford(blueprint.obsidianRobotCost) && robots.obsidian < maxObsidianCost) {
                val newMaterials = crop(materials + robots - blueprint.obsidianRobotCost)
                val newRobots = robots + Resources(0, 0, 1, 0)
                frontier.add(Pair(State(newMaterials, newRobots), remainingTime - 1))
            }

            if (materials.canAfford(blueprint.geodeRobotCost)) {
                val newMaterials = crop(materials + robots - blueprint.geodeRobotCost)
                val newRobots = robots + Resources(0, 0, 0, 1)
                frontier.add(Pair(State(newMaterials, newRobots), remainingTime - 1))
            } else {
                /*
                If it is not possible to build a geode robot maybe it is a good idea to do nothing.
                If it is possible however, then it does not make sense to do nothing
                 */
                val newMaterials = crop(materials + robots)
                frontier.add(Pair(State(newMaterials, robots), remainingTime - 1))
            }
        }

        return best
    }

    fun parseInput(input: List<String>): List<Blueprint> {
        return input.mapIndexed { index, line ->
            val words = line.split(" ")
            val oreRobotCost = Resources(words[6].toInt(), 0, 0, 0)
            val clayRobotCost = Resources(words[12].toInt(), 0, 0, 0)
            val obsidianRobotCost = Resources(words[18].toInt(), words[21].toInt(), 0, 0)
            val geodeRobotCost = Resources(words[27].toInt(), 0, words[30].toInt(), 0)

            Blueprint(index + 1, oreRobotCost, clayRobotCost, obsidianRobotCost, geodeRobotCost)
        }
    }

    fun part1(input: List<String>): Int {
        val blueprints = parseInput(input)

        val results = blueprints.map { solveBlueprint(it, 24) }
        return results.mapIndexed { index, result -> (index + 1) * result }.sum()
    }

    fun part2(input: List<String>): Int {
        val blueprints = parseInput(input)

        val results = blueprints.take(min(3, blueprints.size)).map { solveBlueprint(it, 32) }
        return results.reduce { acc, result -> acc * result }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day19/Day19_test")
//    check(part1(testInput) == 33)
//    check(part2(testInput) == 3472)

    val input = readInput("day19/Day19")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
