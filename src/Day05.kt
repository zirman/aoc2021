import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part1(input: List<String>): Int {
        val intersections = mutableMapOf<Pair<Int, Int>, Int>()

        input.forEach { line ->
            val groups = Regex("""(\d+),(\d+) -> (\d+),(\d+)""").matchEntire(line)!!.groupValues
            val x1 = groups[1].toInt()
            val y1 = groups[2].toInt()
            val x2 = groups[3].toInt()
            val y2 = groups[4].toInt()

            if (x1 == x2) {
                for (y in min(y1, y2)..max(y1, y2)) {
                    intersections[Pair(x1, y)] = intersections.getOrDefault(Pair(x1, y), 0) + 1
                }
            } else if (y1 == y2) {
                for (x in min(x1, x2)..max(x1, x2)) {
                    intersections[Pair(x, y1)] = intersections.getOrDefault(Pair(x, y1), 0) + 1
                }
            } else {
                // blank
            }
        }

        return intersections.values.count { it > 1 }
    }

    fun part2(input: List<String>): Int {
        val intersections = mutableMapOf<Pair<Int, Int>, Int>()
        input.forEach { line ->
            val groups = Regex("""(\d+),(\d+) -> (\d+),(\d+)""").matchEntire(line)!!.groupValues
            val x1 = groups[1].toInt()
            val y1 = groups[2].toInt()
            val x2 = groups[3].toInt()
            val y2 = groups[4].toInt()

            if (x1 == x2) {
                for (y in min(y1, y2)..max(y1, y2)) {
                    intersections[Pair(x1, y)] = intersections.getOrDefault(Pair(x1, y), 0) + 1
                }
            } else if (y1 == y2) {
                for (x in min(x1, x2)..max(x1, x2)) {
                    intersections[Pair(x, y1)] = intersections.getOrDefault(Pair(x, y1), 0) + 1
                }
            } else {
                val xd = if (x2 > x1) 1 else -1
                val yd = if (y2 > y1) 1 else -1
                var x = x1
                var y = y1
                while (x != x2) {
                    intersections[Pair(x, y)] = intersections.getOrDefault(Pair(x, y), 0) + 1
                    x += xd
                    y += yd
                }
                intersections[Pair(x, y)] = intersections.getOrDefault(Pair(x, y), 0) + 1
            }
        }

        return intersections.values.count { it > 1 }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
