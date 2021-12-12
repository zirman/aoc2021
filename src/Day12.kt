

fun main() {
    fun part1(input: List<String>): Int {
        val paths = mutableMapOf<String, MutableList<String>>()

        input.forEach { line ->
            val cols = line.split("-")
            paths.getOrPut(cols[0]) { mutableListOf() }.add(cols[1])
            // don't link back to starting cave
            if (cols[0] != "start" && cols[1] != "end") {
                paths.getOrPut(cols[1]) { mutableListOf() }.add(cols[0])
            }
        }

        fun recur(path: List<String>, cave: String): Sequence<List<String>> =
            when {
                // check for base cases
                cave == "end" -> sequenceOf(path.plus("end"))
                cave[0].isLowerCase() && path.contains(cave) -> emptySequence()
                else -> paths[cave]
                    ?.asSequence()
                    ?.flatMap { nextCave ->
                        recur(path.plus(cave), nextCave)
                    }
                    ?.map { listOf(listOf(cave), it).flatten() }
                    ?: emptySequence()
            }

        return recur(emptyList(), "start").count()
    }

    fun part2(input: List<String>): Int {
        val paths = mutableMapOf<String, MutableList<String>>()

        input.forEach { line ->
            val cols = line.split("-")
            paths.getOrPut(cols[0]) { mutableListOf() }.add(cols[1])
            // don't link back to starting cave
            if (cols[0] != "start" && cols[1] != "end") {
                paths.getOrPut(cols[1]) { mutableListOf() }.add(cols[0])
            }
        }

        fun recur(path: List<String>, cave: String, hasSecond: Boolean): Sequence<List<String>> =
            when {
                // check for base cases
                cave == "end" -> sequenceOf(path.plus("end"))
                cave[0].isLowerCase() && path.contains(cave) && hasSecond -> emptySequence()
                else -> paths[cave]
                    ?.asSequence()
                    ?.flatMap { nextCave ->
                        recur(
                            path.plus(cave),
                            nextCave,
                            hasSecond || (cave[0].isLowerCase() && path.contains(cave))
                        )
                    }
                    ?.map { listOf(listOf(cave), it).flatten() }
                    ?: emptySequence()
            }

        return recur(emptyList(), "start", false).count()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 226)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}
