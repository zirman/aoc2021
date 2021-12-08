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
        return input.map { line ->
            val segments = line.split(" | ")[0].split(" ").map { it.map { it }.toSet() }

            val one = segments.find { it.size == 2 }!!
            val four = segments.find { it.size == 4 }!!
            val seven = segments.find { it.size == 3 }!!
            val eight = segments.find { it.size == 7 }!!

            val three = segments.filter { it.size == 5 }
                .find { it.containsAll(one) }!!

            val five = segments.filter { it.size == 5 }
                .find { it != three && it.intersect(four).size == 3 }!!

            val two = segments.filter { it.size == 5 }
                .find { it != three && it != five }!!

            val nine = segments.filter { it.size == 6 }
                .find { it.containsAll(four) }!!

            val zero = segments.filter { it.size == 6 }
                .find { it != nine && it.containsAll(one) }!!

            val six = segments.filter { it.size == 6 }
                .find { it != nine && it != zero }!!

            line.split(" | ")[1].split(" ").map {
                when (it.map { it }.toSet()) {
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
            }.fold(0) { a, b ->
                a * 10 + b
            }
        }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 26)
    check(part2(testInput) == 61229)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
