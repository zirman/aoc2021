fun main() {
    fun part1(input: List<String>, iterations: Int): Int {
        val sections = input.joinToString("\n").split("\n\n")
        val enhance = sections[0].toCharArray()
        val image = sections[1].split("\n").map { line -> line.toList() }

        val borderWidth = 1
        var infinityPixels = '.'

        return (1..iterations)
            .fold(image) { acc, _ ->
                fun at(x: Int, y: Int): Char {
                    return if (y >= 0 && y < acc.size &&
                        x >= 0 && x < acc[0].size
                    ) {
                        acc[y][x]
                    } else {
                        infinityPixels
                    }
                }

                ((-2 - borderWidth) until acc.size + borderWidth)
                    .map { y ->
                        ((-2 - borderWidth) until acc[0].size + borderWidth).map { x ->
                            var kernel = 0
                            (y until y + 3).forEach { i ->
                                (x until x + 3).forEach { k ->
                                    kernel = kernel.shl(1)
                                        .or(
                                            when (at(k, i)) {
                                                '#' -> 0b1
                                                '.' -> 0b0
                                                else -> throw Exception()
                                            }
                                        )
                                }
                            }
                            enhance[kernel]
                        }
                    }
                    .also { row -> infinityPixels = row[borderWidth - 1][borderWidth - 1] }
            }
            .sumOf { row -> row.count { it == '#' } }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day20_test")
    check(part1(testInput, 2) == 35)

    val input = readInput("Day20")
    println(part1(input, 2))
    println(part1(input, 50))
}
