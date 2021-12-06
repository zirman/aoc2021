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

    fun part2(input: List<String>, days: Int): Long {
        val fish = input[0].split(",").map { it.toInt() }
        val initialBuckets = (0..8).map { timer -> fish.count { it == timer }.toLong() }

        val finalBuckets = (1..days).fold(initialBuckets) { buckets, _ ->
            listOf(
                buckets[1],
                buckets[2],
                buckets[3],
                buckets[4],
                buckets[5],
                buckets[6],
                buckets[7] + buckets[0],
                buckets[8],
                buckets[0],
            )
        }

        return finalBuckets.fold(0L) { a, b -> a + b }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part2(testInput, 18) == 26L)
    check(part2(testInput, 80) == 5934L)
    check(part2(testInput, 256) == 26_984_457_539L)

    val input = readInput("Day06")
    println(part2(input, 80))
    println(part2(input, 256))
}
