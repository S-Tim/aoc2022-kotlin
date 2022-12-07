
fun main() {
    fun part1(input: List<String>): Int {
        val signal = input[0]
        val result = signal.windowed(4).indexOfFirst { window -> window.toSet().size == window.length }
        return result + 4
    }

    fun part2(input: List<String>): Int {
        val signal = input[0]
        val result = signal.windowed(14).indexOfFirst { window -> window.toSet().size == window.length }
        return result + 14
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 19)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}
