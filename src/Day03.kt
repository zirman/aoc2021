fun main() {
    fun part1(input: List<String>): Int {
        val list = input.map { string -> string.map { it } }

        val gammaRate = list[0].indices.map { i ->
            val digits = list.map { it[i] }
            val zeros = digits.count { it == '0' }
            val ones = digits.count { it == '1' }
            if (ones > zeros) '1' else '0'
        }.joinToString("").toInt(2)

        val epsilonRate = list[0].indices.map { i ->
            val digits = list.map { it[i] }
            val zeros = digits.count { it == '0' }
            val ones = digits.count { it == '1' }
            if (ones > zeros) '0' else '1'
        }.joinToString("").toInt(2)

        return gammaRate * epsilonRate
    }

    fun part2(input: List<String>): Int {
        val list = input.map { string -> string.map { it } }

        val oxyList = list.toMutableList()

        var i = 0
        while (oxyList.size > 1) {
            val zeros = oxyList.map { it[i] }.count { it == '0' }
            val ones = oxyList.map { it[i] }.count { it == '1' }
            if (ones >= zeros) {
                oxyList.removeAll { it[i] == '0' }
            } else {
                oxyList.removeAll { it[i] == '1' }
            }
            i++
        }

        val scrubList = list.toMutableList()
        i = 0
        while (scrubList.size > 1) {
            val zeros = scrubList.map { it[i] }.count { it == '0' }
            val ones = scrubList.map { it[i] }.count { it == '1' }
            if (zeros <= ones) {
                scrubList.removeAll { it[i] == '1' }
            } else {
                scrubList.removeAll { it[i] == '0' }
            }
            i++
        }

        return oxyList[0].joinToString("").toInt(2) * scrubList[0].joinToString("").toInt(2)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
