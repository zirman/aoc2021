import kotlin.math.absoluteValue

fun main() {
    fun part1(input: List<String>): Int {
        val pos = input[0].split(',').map { it.toInt() }

        return (pos.minOf { it }..pos.maxOf { it })
            .map { center -> pos.sumOf { (it - center).absoluteValue } }
            .minByOrNull { it }!!
    }

    fun part2(input: List<String>): Long {
        val pos = input[0].split(',').map { it.toInt() }

        return (pos.minOf { it }..pos.maxOf { it })
            .map { center -> pos.sumOf { (1L..(it - center).absoluteValue).sum() } }
            .minByOrNull { it }!!
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 37)
    check(part2(testInput) == 168L)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}
