fun main() {
    fun search(paths: Map<String, MutableList<String>>): List<List<String>> {
        fun recur(cave: String, visited: Set<String>): List<List<String>> {
            if (visited.contains(cave)) {
                return emptyList()
            } else if (cave == "end") {
                return listOf(listOf("end"))
            }

            val vis = if (cave[0].isUpperCase()) {
                visited
            } else {
                visited.plus(cave)
            }

            return (paths[cave]
                ?.flatMap { next -> recur(next, vis) }
                ?.map { listOf(listOf(cave), it).flatten() }
                ?: emptyList())
        }

        return recur("start", emptySet())
    }

    fun part1(input: List<String>): Int {
        val paths = mutableMapOf<String, MutableList<String>>()

        input.forEach { line ->
            val cols = line.split("-")
            paths.getOrPut(cols[0]) { mutableListOf() }.add(cols[1])
            paths.getOrPut(cols[1]) { mutableListOf() }.add(cols[0])
        }

        return search(paths).size
    }

    fun part2(input: List<String>): Int {
        val smallCaves = mutableSetOf<String>()

        input.forEach { line ->
            val cols = line.split("-")
            if (cols[0][0].isLowerCase() && cols[0] != "start" && cols[0] != "end") {
                smallCaves.add(cols[0])
            }
            if (cols[1][0].isLowerCase() && cols[1] != "start" && cols[1] != "end") {
                smallCaves.add(cols[1])
            }
        }

        return smallCaves.flatMap { smallCave ->
            val paths = mutableMapOf<String, MutableList<String>>()
            val alias = "$smallCave-alias"

            input.forEach { line ->
                val cols = line.split("-")
                paths.getOrPut(cols[0]) { mutableListOf() }.add(cols[1])
                paths.getOrPut(cols[1]) { mutableListOf() }.add(cols[0])

                if (cols[0] == smallCave) {
                    paths.getOrPut(alias) { mutableListOf() }.add(cols[1])
                    paths.getOrPut(cols[1]) { mutableListOf() }.add(alias)
                }

                if (cols[1] == smallCave) {
                    paths.getOrPut(cols[0]) { mutableListOf() }.add(alias)
                    paths.getOrPut(alias) { mutableListOf() }.add(cols[0])
                }
            }

            search(paths).map { path ->
                path.map { if (it == alias) smallCave else it }
            }
        }.toSet().size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 226)
    check(part2(testInput) == 3509)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}
