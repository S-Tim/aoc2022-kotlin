package day05

import readInput
import java.lang.IllegalArgumentException

fun main() {
    fun parseInput(input: List<String>): Pair<MutableMap<Int, ArrayDeque<Char>>, List<String>> {
        val stacks = mutableMapOf<Int, ArrayDeque<Char>>()

        // initialize stacks
        val stackInitEnd = input.indexOf("") - 1
        for (i in 0..stackInitEnd) {
            for (j in 1..input[i].length step 4) {
                if (input[i][j].isLetter()) {
                    val stackIndex = (j - 1) / 4 + 1
                    if (stacks[stackIndex] == null) {
                        stacks[stackIndex] = ArrayDeque()
                    }
                    stacks[stackIndex]?.add(input[i][j])
                }
            }
        }

        return Pair(stacks, input.slice(stackInitEnd + 2 until input.size))
    }

    fun parseMove(move: String): Triple<Int, Int, Int>{
        val instructionPattern = """move (\d+) from (\d+) to (\d+)""".toRegex()
        val (count, from, to) = instructionPattern.matchEntire(move)?.destructured
            ?: throw IllegalArgumentException("Incorrect format of instruction $move")

        return Triple(count.toInt(), from.toInt(), to.toInt())
    }

    fun move(instruction: String, stacks: MutableMap<Int, ArrayDeque<Char>>): MutableMap<Int, ArrayDeque<Char>> {
        val (count, from, to) = parseMove(instruction)

        for (i in 0 until count){
            val item = stacks[from]?.removeFirst() ?: throw IllegalArgumentException("Stack is empty")
            stacks[to]?.addFirst(item)
        }

        return stacks;
    }

    fun move2(instruction: String, stacks: MutableMap<Int, ArrayDeque<Char>>): MutableMap<Int, ArrayDeque<Char>> {
        val (count, from, to) = parseMove(instruction)

        // Move items to temp stack first so that the order is correct
        val tempStack = ArrayDeque<Char>()
        for (i in 0 until count){
            val item = stacks[from]?.removeFirst() ?: throw IllegalArgumentException("Stack is empty")
            tempStack.addFirst(item)
        }

        while(tempStack.isNotEmpty()){
            stacks[to]?.addFirst(tempStack.removeFirst())
        }

        return stacks;
    }

    fun part1(input: List<String>): String {
        var (stacks, moves) = parseInput(input)
        for (move in moves){
            stacks = move(move, stacks)
        }

        return stacks.toSortedMap().values.mapNotNull { it.firstOrNull() }.joinToString(separator = "") { it.toString() }
    }

    fun part2(input: List<String>): String {
        var (stacks, moves) = parseInput(input)
        for (move in moves){
            stacks = move2(move, stacks)
        }

        return stacks.toSortedMap().values.mapNotNull { it.firstOrNull() }.joinToString(separator = "") { it.toString() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day05/Day05_test")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readInput("day05/Day05")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
