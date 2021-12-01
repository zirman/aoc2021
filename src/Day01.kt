fun main() {
    fun part1(input: List<String>): Int = input
        .map { it.toInt() }
        .fold(Pair<Int?, Int>(null, 0)) { (previousDepth, count), depth ->
            Pair(depth, count + if (previousDepth != null && previousDepth < depth) 1 else 0)
        }
        .second

    fun part2(input: List<String>): Int = input
        .map { it.toInt() }
        .windowed(3)
        .map { it.sum() }
        .fold(Pair<Int?, Int>(null, 0)) { (previousNumber, count), depth ->
            Pair(depth, count + if (previousNumber != null && previousNumber < depth) 1 else 0)
        }
        .second

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
