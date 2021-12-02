fun main() {
    fun part1(input: List<String>): Int {
        var depth = 0
        var distance = 0

        input.forEach { string ->
            Regex("""forward (\d+)""").matchEntire(string)
                ?.let {
                    val x = it.groups[1]!!.value.toInt()
                    distance += x
                }
                ?: Regex("""down (\d+)""").matchEntire(string)?.let {
                    val x = it.groups[1]!!.value.toInt()
                    depth += x
                }
                ?: Regex("""up (\d+)""").matchEntire(string)?.let {
                    val x = it.groups[1]!!.value.toInt()
                    depth -= x
                }
                ?: throw Exception()
        }

        return depth * distance
    }

    fun part2(input: List<String>): Int {
        var depth = 0
        var distance = 0
        var aim = 0

        input.forEach { string ->
            Regex("""forward (\d+)""").matchEntire(string)
                ?.let {
                    val x = it.groups[1]!!.value.toInt()
                    distance += x
                    depth += aim * x
                }
                ?: Regex("""down (\d+)""").matchEntire(string)?.let {
                    val x = it.groups[1]!!.value.toInt()
                    aim += x
                }
                ?: Regex("""up (\d+)""").matchEntire(string)?.let {
                    val x = it.groups[1]!!.value.toInt()
                    aim -= x
                }
                ?: throw Exception()
        }

        return depth * distance
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
