fun main() {
    fun getPriority(c: Char): Int {
        return when {
            c.isLowerCase() -> c.code - 'a'.code + 1
            else -> c.code - 'A'.code + 27
        }
    }

    fun part1(input: List<String>): Int {
        val compartments = input.map { it.chunked(it.length / 2) }
        val intersections = compartments.map { (first, second) -> first.toSet().intersect(second.toSet()) }
        val values = intersections.map { it.sumOf(::getPriority) }

        return values.sum()
    }

    fun part2(input: List<String>): Int {
        // val intersections = input.windowed(3, 3)
        //  .map { (first, second, third) -> first.toSet().intersect(second.toSet()).intersect(third.toSet()) }
        val intersections = input.windowed(3, 3)
            .map { group -> group.map { rucksack -> rucksack.toSet() } }
            .map { group -> group.reduce { intersection, rucksack -> intersection.intersect(rucksack) } }
        val values = intersections.map { it.sumOf(::getPriority) }

        return values.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
