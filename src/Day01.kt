fun main() {
    fun countCalories(input: List<String>): List<Int> {
        val calories = mutableListOf<Int>()
        var currentCalories = 0
        for (line: String in input) {
            if (line.isEmpty()) {
                calories.add(currentCalories)
                currentCalories = 0
            } else {
                currentCalories += line.toInt()
            }
        }
        return calories
    }

    fun part1(input: List<String>): Int {
        val calories = countCalories(input)
        return calories.max()
    }

    fun part2(input: List<String>): Int {
        val calories = countCalories(input)
        val sortedCalories = calories.sorted().reversed()
        return sortedCalories.slice(0..2).sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
