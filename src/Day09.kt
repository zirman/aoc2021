fun main() {
    fun part1(input: List<String>): Int {
        val heights = input.map { line ->
            line.map { it.toString().toInt() }
        }

        var riskLevel = 0
        for (y in heights.indices) {
            val row = heights[y]
            for (x in row.indices) {
                val h = row[x]
                if ((x - 1 < 0 || h < row[x - 1]) &&
                    (x + 1 >= row.size || h < row[x + 1]) &&
                    (y - 1 < 0 || h < heights[y - 1][x]) &&
                    (y + 1 >= heights.size || h < heights[y + 1][x])
                ) {
                    riskLevel += h + 1
                }
            }
        }
        return riskLevel
    }

    fun part2(input: List<String>): Int {
        val heights = input.map { line ->
            line.map { it.toString().toInt() }
        }

        val basins = mutableListOf<Int>()
        for (y in heights.indices) {
            val row = heights[y]
            for (x in row.indices) {
                val h = row[x]
                if ((x - 1 < 0 || h < row[x - 1]) &&
                    (x + 1 >= row.size || h < row[x + 1]) &&
                    (y - 1 < 0 || h < heights[y - 1][x]) &&
                    (y + 1 >= heights.size || h < heights[y + 1][x])
                ) {
                    val visited = mutableSetOf<Pair<Int, Int>>()

                    fun search(x: Int, y: Int, h: Int): Int {
                        if (x < 0 || x >= heights[0].size ||
                            y < 0 || y >= heights.size ||
                            visited.contains(Pair(x, y))
                        ) {
                            return 0
                        }

                        visited.add(Pair(x, y))
                        if (heights[y][x] == 9) {
                            return 0
                        }

                        return 1 + search(x, y + 1, heights[y][x]) +
                                search(x, y - 1, heights[y][x]) +
                                search(x - 1, y, heights[y][x]) +
                                search(x + 1, y, heights[y][x])
                    }

                    basins += search(x, y, h)
                }
            }
        }

        return basins.sortedBy { it }.takeLast(3).fold(1) { a, b -> a * b }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 1134)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}
