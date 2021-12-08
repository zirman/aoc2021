import kotlin.math.absoluteValue

fun main() {
    fun part1(input: List<String>): Int {
        val pos = input[0].split(',').map { it.toInt() }

        return (pos.minOf { it }..pos.maxOf { it })
            .map { center -> pos.sumOf { (it - center).absoluteValue } }
            .minByOrNull { it }!!
    }

    fun part2(input: List<String>): Int {
        val pos = input[0].split(',').map { it.toInt() }

        // equivalent to (1..distance).sum()
        fun cost(distance: Int): Int =
            ((distance * distance) + distance) / 2

        return (pos.minOf { it }..pos.maxOf { it })
            .map { center -> pos.sumOf { cost((it - center).absoluteValue) } }
            .minByOrNull { it }!!
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}
