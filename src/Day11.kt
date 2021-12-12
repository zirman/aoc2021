fun main() {
    fun part1(input: List<String>): Int {
        val levels = input.map { line ->
            line.map { it.toString().toInt() }
                .toMutableList()
        }

        var flashes = 0

        for (i in 1..100) {
            val flashed = mutableSetOf<Pair<Int, Int>>()

            fun inc(x: Int, y: Int) {
                if (y >= 0 && y < levels.size &&
                    x >= 0 && x < levels[0].size &&
                    flashed.contains(Pair(x, y)).not()
                ) {
                    levels[y][x]++

                    if (levels[y][x] > 9) {
                        flashed.add(Pair(x, y))
                        levels[y][x] = 0
                        inc(x - 1, y)
                        inc(x + 1, y)
                        inc(x, y - 1)
                        inc(x, y + 1)

                        inc(x - 1, y - 1)
                        inc(x - 1, y + 1)
                        inc(x + 1, y - 1)
                        inc(x + 1, y + 1)
                    }
                }
            }

            levels.indices.forEach { y ->
                levels[y].indices.forEach { x -> inc(x, y) }
            }

            flashes += flashed.size
        }

        return flashes
    }

    fun part2(input: List<String>): Int {
        val levels = input.map { line ->
            line.map { it.toString().toInt() }
                .toMutableList()
        }

        val synced = levels.map { it.map { 0 } }

        for (i in 1..Int.MAX_VALUE) {
            val flashed = mutableSetOf<Pair<Int, Int>>()

            fun inc(x: Int, y: Int) {
                if (y >= 0 && y < levels.size &&
                    x >= 0 && x < levels[0].size &&
                    flashed.contains(Pair(x, y)).not()
                ) {
                    levels[y][x]++

                    if (levels[y][x] > 9) {
                        flashed.add(Pair(x, y))
                        levels[y][x] = 0
                        inc(x - 1, y)
                        inc(x + 1, y)
                        inc(x, y - 1)
                        inc(x, y + 1)

                        inc(x - 1, y - 1)
                        inc(x - 1, y + 1)
                        inc(x + 1, y - 1)
                        inc(x + 1, y + 1)
                    }
                }
            }

            levels.indices.forEach { y -> levels[y].indices.forEach { x -> inc(x, y) } }

            if (levels == synced) {
                return i
            }
        }

        throw Exception()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 1656)
    check(part2(testInput) == 195)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}
