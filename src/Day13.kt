fun main() {
    fun part1(input: List<String>): Int {
        val sections = input.joinToString("\n").split("\n\n")

        val dots = sections[0].split("\n").fold(setOf<Pair<Int, Int>>()) { dots, line ->
            val cols = line.split(",")
            dots.plus(Pair(cols[0].toInt(), cols[1].toInt()))
        }

        val line = sections[1].split("\n").first()

        val groupValues = Regex("""fold along ([xy])=(\d+)""").matchEntire(line)!!.groupValues
        val fold = groupValues[2].toInt()
        return when (groupValues[1]) {
            "x" -> dots
                .map { pair ->
                    val (x, y) = pair
                    if (x > fold) {
                        Pair(fold - (x - fold), y)
                    } else {
                        pair
                    }
                }
                .toSet()
            "y" -> dots
                .map { pair ->
                    val (x, y) = pair
                    if (y > fold) {
                        Pair(x, fold - (y - fold))
                    } else {
                        pair
                    }
                }
                .toSet()
            else -> {
                throw Exception()
            }
        }.size
    }

    fun part2(input: List<String>): String {
        val sections = input.joinToString("\n").split("\n\n")

        return sections[1]
            .split("\n")
            .fold(
                sections[0]
                    .split("\n")
                    .fold(setOf<Pair<Int, Int>>()) { dots, line ->
                        val cols = line.split(",")
                        dots.plus(Pair(cols[0].toInt(), cols[1].toInt()))
                    }
                    .toSet()
            ) { dots, line ->
                val groupValues = Regex("""fold along ([xy])=(\d+)""").matchEntire(line)!!.groupValues
                when (groupValues[1]) {
                    "x" -> {
                        val foldX = groupValues[2].toInt()
                        dots
                            .map { pair ->
                                val (x, y) = pair
                                if (x > foldX) {
                                    Pair(foldX - (x - foldX), y)
                                } else {
                                    pair
                                }
                            }
                            .toSet()
                    }
                    "y" -> {
                        val foldY = groupValues[2].toInt()
                        dots
                            .map { pair ->
                                val (x, y) = pair
                                if (y > foldY) {
                                    Pair(x, foldY - (y - foldY))
                                } else {
                                    pair
                                }
                            }
                            .toSet()
                    }
                    else -> {
                        throw Exception()
                    }
                }
            }.let { dots ->
                val minX = dots.minByOrNull { (x, _) -> x }!!.first
                val maxX = dots.maxByOrNull { (x, _) -> x }!!.first
                val minY = dots.minByOrNull { (_, y) -> y }!!.second
                val maxY = dots.maxByOrNull { (_, y) -> y }!!.second

                (minY..maxY).joinToString("\n") { y ->
                    (minX..maxX)
                        .map { x -> if (dots.contains(Pair(x, y))) '#' else ' ' }
                        .joinToString("")
                }
            }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 17)

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}
