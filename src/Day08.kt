import java.lang.Integer.max

fun main() {
    fun parseInput(input: List<String>): List<List<Int>> {
        return input.map { line -> line.toList().map { it.digitToInt() } }
    }

    fun seenTrees(forest: List<List<Int>>): Set<Pair<Int, Int>> {
        val seenTrees = mutableSetOf<Pair<Int, Int>>()

        for (i in forest.indices) {
            var minHeightFromLeft = -1
            var minHeightFromTop = -1
            var minHeightFromRight = -1
            var minHeightFromBottom = -1

            for (j in forest.indices) {
                // from left
                if (forest[i][j] > minHeightFromLeft) {
                    seenTrees.add(Pair(i, j))
                    minHeightFromLeft = forest[i][j]
                }
                // from top
                if (forest[j][i] > minHeightFromTop) {
                    seenTrees.add(Pair(j, i))
                    minHeightFromTop = forest[j][i]
                }

                val reversedJ = forest.size - j - 1
                // from right
                if (forest[i][reversedJ] > minHeightFromRight) {
                    seenTrees.add(Pair(i, reversedJ))
                    minHeightFromRight = forest[i][reversedJ]
                }
                // from bottom
                if (forest[reversedJ][i] > minHeightFromBottom) {
                    seenTrees.add(Pair(reversedJ, i))
                    minHeightFromBottom = forest[reversedJ][i]
                }
            }
        }
        return seenTrees
    }

    fun part1(input: List<String>): Int {
        val forest = parseInput(input)
        return seenTrees(forest).size
    }

    fun calculateScenicScore(forest: List<List<Int>>, row: Int, column: Int): Int {
        val requiredHeight = forest[row][column]

        // right
        var tempColumn = column + 1
        var rightScore = 0
        while (tempColumn < forest.size && forest[row][tempColumn] < requiredHeight) {
            tempColumn++
            rightScore++
        }
        if (tempColumn < forest.size) {
            rightScore++
        }

        // left
        tempColumn = column - 1
        var leftScore = 0
        while (tempColumn >= 0 && forest[row][tempColumn] < requiredHeight) {
            tempColumn--
            leftScore++
        }
        if (tempColumn >= 0) {
            leftScore++
        }

        // bottom
        var tempRow = row + 1
        var bottomScore = 0
        while (tempRow < forest.size && forest[tempRow][column] < requiredHeight) {
            tempRow++
            bottomScore++
        }
        if (tempRow < forest.size) {
            bottomScore++
        }

        // top
        tempRow = row - 1
        var topScore = 0
        while (tempRow >= 0 && forest[tempRow][column] < requiredHeight) {
            tempRow--
            topScore++
        }
        if (tempRow >= 0) {
            topScore++
        }

        return leftScore * rightScore * topScore * bottomScore
    }

    fun part2(input: List<String>): Int {
        val forest = parseInput(input)

        var highScore = 0
        for (i in forest.indices) {
            for (j in forest.indices) {
                highScore = max(highScore, calculateScenicScore(forest, i, j))
            }
        }
        return highScore
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readInput("Day08")
    println("Part 1: " + part1(input))
    println("Part 2: " + part2(input))
}
