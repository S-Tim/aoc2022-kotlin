package day09

import readInput
import java.lang.Math.abs

fun main() {
    val input = readInput("Day09")

    var visited: Set<Pair<Int, Int>> = emptySet()
    var rope = Rope(knots = List(10) { Pair(0, 0) })
    for (line in input) {
        val (dir, count) = line.split(" ")

        for (i in 0 until count.toInt()) {
            rope = when (dir) {
                "R" -> rope.moveRight()
                "L" -> rope.moveLeft()
                "U" -> rope.moveUp()
                "D" -> rope.moveDown()
                else -> throw IllegalArgumentException("Nooooo.")
            }

            visited += rope.knots[9]
        }
    }

    println("Solution:  ${visited.size}")
}

private fun superimpose(rope: Rope) {
    val maxX = 30
    val maxY = 30

    val array = Array(maxY) { Array(maxX) { "." } }
    for (row in 0 until array.size) {
        for (col in 0 until array[0].size) {
            array[row][col] = "."
        }
    }

    for (i in rope.knots.indices) {
        val knot = rope.knots[i]
        val x = abs(knot.second)
        val y = abs(knot.first)
        array[x+10][y+10] = i.toString()
    }

    for (row in 0 until array.size) {
        for (col in 0 until array[0].size) {
            print(array[row][col])
        }
        println()
    }

    println("===========================")
}

data class Rope(val knots: List<Pair<Int, Int>> = List(10) { Pair(0, 0) }) {
    fun moveUp(): Rope {
        val head = knots.first()
        val updatedHead = head.copy(second = head.second + 1)

        return move(head, updatedHead, knots.drop(1), copy(knots = listOf(updatedHead)))
    }

    fun moveLeft(): Rope {
        val head = knots.first()
        val updatedHead = head.copy(first = head.first - 1)

        return move(head, updatedHead, knots.drop(1), copy(knots = listOf(updatedHead)))
    }

    fun moveRight(): Rope {
        val head = knots.first()
        val updatedHead = head.copy(first = head.first + 1)

        return move(head, updatedHead, knots.drop(1), copy(knots = listOf(updatedHead)))
    }

    fun moveDown(): Rope {
        val head = knots.first()
        val updatedHead = head.copy(second = head.second - 1)

        return move(head, updatedHead, knots.drop(1), copy(knots = listOf(updatedHead)))
    }

    fun move(prevPos: Pair<Int, Int>, currHead: Pair<Int, Int>, knots: List<Pair<Int, Int>>, newRope: Rope): Rope {
        if (knots.isEmpty()) return newRope

        val tail = knots.first()

        val isDiagonalUpRight = (currHead.first - tail.first >= 1 && currHead.second - tail.second >= 1) && (currHead.first - tail.first > 1 || currHead.second - tail.second > 1)
        val isDiagonalUpLeft = (currHead.first - tail.first <= -1 && currHead.second - tail.second >= 1) && (currHead.first - tail.first < -1 || currHead.second - tail.second > 1)
        val isDiagonalDownRight = (currHead.first - tail.first >= 1 && currHead.second - tail.second <= -1) && (currHead.first - tail.first > 1 || currHead.second - tail.second < -1)
        val isDiagonalDownLeft = (currHead.first - tail.first <= -1 && currHead.second - tail.second <= -1) && (currHead.first - tail.first < -1 || currHead.second - tail.second < -1)

        val newTail = when {
            isDiagonalUpRight -> Pair(tail.first + 1, tail.second + 1)
            isDiagonalUpLeft -> Pair(tail.first - 1, tail.second + 1)

            isDiagonalDownRight -> Pair(tail.first + 1, tail.second - 1)
            isDiagonalDownLeft -> Pair(tail.first - 1, tail.second - 1)

            currHead.first - tail.first > 1 -> Pair(tail.first + 1, tail.second)
            currHead.first - tail.first < -1 -> Pair(tail.first - 1, tail.second)

            currHead.second - tail.second > 1 -> Pair(tail.first, tail.second + 1)
            currHead.second - tail.second < -1 -> Pair(tail.first, tail.second - 1)

            else -> tail
        }

        return move(
            tail,
            newTail,
            knots.drop(1),
            Rope(knots = newRope.knots + newTail)
        )
    }
}