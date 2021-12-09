fun main() {
    fun part1(input: List<String>): Int {
        val segments = input.map { line ->
            line.split(" | ")[1].split(" ")
        }

        return segments.flatMap { segment ->
            segment.map { it.length }.filter { it == 2 || it == 4 || it == 3 || it == 7 }
        }.count()
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            val columns = line.split(" | ")
            val digitSegments = columns[0].split(" ").map { string -> string.map { it }.toSet() }

            val one = digitSegments.find { it.size == 2 }!!
            val four = digitSegments.find { it.size == 4 }!!
            val seven = digitSegments.find { it.size == 3 }!!
            val eight = digitSegments.find { it.size == 7 }!!

            val fiveSegments = digitSegments.filter { it.size == 5 }

            val three = fiveSegments
                .find { it.containsAll(one) }!!

            val five = fiveSegments
                .find { it != three && it.intersect(four).size == 3 }!!

            val two = fiveSegments
                .find { it != three && it != five }!!

            val sixSegments = digitSegments.filter { it.size == 6 }

            val nine = sixSegments
                .find { it.containsAll(four) }!!

            val zero = sixSegments
                .find { it != nine && it.containsAll(one) }!!

            val six = sixSegments
                .find { it != nine && it != zero }!!

            columns[1].split(" ").map { string ->
                when (string.map { it }.toSet()) {
                    zero -> 0
                    one -> 1
                    two -> 2
                    three -> 3
                    four -> 4
                    five -> 5
                    six -> 6
                    seven -> 7
                    eight -> 8
                    nine -> 9
                    else -> {
                        throw Exception()
                    }
                }
            }.joinToString("").toInt()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 26)
    check(part2(testInput) == 61229)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
