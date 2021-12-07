import kotlin.math.absoluteValue

fun main() {
    fun part1(input: List<String>): Int {
        val pos = input[0].split(',').map { it.toInt() }

        return (pos.minOf { it }..pos.maxOf { it })
            .map { center -> pos.sumOf { (it - center).absoluteValue } }
            .minByOrNull { it }!!
    }

    fun part2(input: List<String>): Int {
        fun <T, R> ((T) -> R).memoize(): (T) -> R {
            val table = mutableMapOf<T, R>()
            return { x -> table.getOrPut(x) { invoke(x) } }
        }

        val pos = input[0].split(',').map { it.toInt() }

        fun cost(distance: Int): Int =
            (1..distance).sum()

        val costMemoized =
            ::cost.memoize()

        return (pos.minOf { it }..pos.maxOf { it })
            .map { center -> pos.sumOf { costMemoized((it - center).absoluteValue) } }
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
