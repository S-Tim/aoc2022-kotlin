package day13

import readInput
import kotlin.math.min

sealed class Element : Comparable<Element>

class Container(val children: List<Element> = emptyList()) : Element() {
    override fun compareTo(other: Element): Int {
        return when (other) {
            is Value -> compareTo(Container(listOf(other)))
            is Container -> {
                val minLength = min(children.size, other.children.size)
                for (i in 0 until minLength) {
                    val compared = children[i].compareTo(other.children[i])
                    if (compared != 0) {
                        return compared
                    }
                }
                children.size.compareTo(other.children.size) * -1
            }
        }
    }

    override fun toString(): String {
        return "[${children.joinToString(",")}]"
    }
}

class Value(val value: Int) : Element() {
    override fun compareTo(other: Element): Int {
        return when (other) {
            is Value -> value.compareTo(other.value) * -1
            is Container -> Container(listOf(this)).compareTo(other)
        }
    }

    override fun toString(): String {
        return value.toString()
    }
}

fun main() {
    fun parsePacket(packet: String, start: Int = 0): Pair<Container, Int> {
        val children = mutableListOf<Element>()

        var offset = 1
        while (start + offset < packet.length) {
            val currentSymbol = packet[start + offset]
            if (currentSymbol.isDigit()) {
                var number = ""
                while (packet[start + offset].isDigit()) {
                    number += packet[start + offset]
                    offset += 1
                }
                children.add(Value(number.toInt()))
            } else if (currentSymbol == '[') {
                val (container, newOffset) = parsePacket(packet, start + offset)
                children.add(container)
                offset += newOffset
                offset += 1
            } else if (currentSymbol == ',') {
                offset += 1
            } else if (currentSymbol == ']') {
                return Pair(Container(children), offset)
            }
        }

        throw IllegalArgumentException("Unexpected symbol ${packet[start + offset]} at position ${start + offset}")
    }

    fun part1(input: List<String>): Int {
        // compare input and parsed packets converted back to strings
        input.filter { it != "" }.associateWith { parsePacket(it).first.toString() == it }.filter { !it.value }
            .forEach {
                println("${it.key}\n${parsePacket(it.key).first}\n")
            }

        return input.windowed(2, 3).map { pair -> pair.map { parsePacket(it).first } }
            .mapIndexed { index, (first, second) ->
                if (first.compareTo(second) == 1) index + 1 else 0
            }.sum()
    }

    fun part2(input: List<String>): Int {
        val two = "[[2]]"
        val six = "[[6]]"
        val sortedInputs = (input + two + six).filter { it != "" }.map { parsePacket(it).first }.sorted().reversed()

        return (sortedInputs.indexOfFirst { it.toString() == two } + 1) * (sortedInputs.indexOfFirst { it.toString() == six } + 1)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day13/Day13_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 140)

    val input = readInput("day13/Day13")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
