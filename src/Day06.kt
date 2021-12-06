fun main() {
    fun part1(input: List<String>, days: Int): Int {
        var fish = input[0].split(",").map { it.toInt() }

        for (x in 0 until days) {
            fish = fish.flatMap { x ->
                if (x == 0) {
                    listOf(6, 8)
                } else {
                    listOf(x - 1)
                }
            }
        }

        return fish.size
    }

    fun part2(input: List<String>, days: Int): ULong {
        val fish = input[0].split(",").map { it.toULong() }

        var bar = listOf(
            fish.count { it == 0UL }.toULong(),
            fish.count { it == 1UL }.toULong(),
            fish.count { it == 2UL }.toULong(),
            fish.count { it == 3UL }.toULong(),
            fish.count { it == 4UL }.toULong(),
            fish.count { it == 5UL }.toULong(),
            fish.count { it == 6UL }.toULong(),
            fish.count { it == 7UL }.toULong(),
            fish.count { it == 8UL }.toULong()
        )

        for (x in 0 until days) {
            bar = listOf(
                bar[1],
                bar[2],
                bar[3],
                bar[4],
                bar[5],
                bar[6],
                bar[7] + bar[0],
                bar[8],
                bar[0],
            )
        }

        //println(Long.MAX_VALUE)
        //println(bar.fold(0L) { a, b -> a + b })
        return bar.fold(0UL) { a, b -> a + b }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part2(testInput, 18) == 26UL)
    check(part2(testInput, 80) == 5934UL)
    check(part2(testInput, 256) == 26_984_457_539UL)

    val input = readInput("Day06")
    println(part2(input, 80))
    println(part2(input, 256))
}
