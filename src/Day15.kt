import java.util.PriorityQueue
import kotlin.system.measureTimeMillis

data class AStar(val x: Int, val y: Int, val g: Int, val h: Int, val gh: Int = g + h)

fun main() {
    fun search(risks: List<List<Int>>): Int {
        val targetX = risks[0].size - 1
        val targetY = risks.size - 1

        // assumes x/y is smaller than target x/y
        fun h(x: Int, y: Int): Int =
            (targetX - x) + (targetY - y)

        val minRisks: List<MutableList<Int?>> =
            risks.map { row -> row.indices.map { null }.toMutableList() }

        val queue =
            PriorityQueue<AStar>(compareBy { it.gh })

        queue.add(AStar(0, 1, risks[1][0], h(0, 1)))
        queue.add(AStar(1, 0, risks[0][1], h(1, 0)))
        minRisks[1][0] = risks[1][0]
        minRisks[0][1] = risks[0][1]

        fun addQueue(x: Int, y: Int, g: Int) {
            if (y >= 0 && y < risks.size &&
                x >= 0 && x < risks[0].size &&
                minRisks[y][x]?.let { g + risks[y][x] < it } != false
            ) {
                minRisks[y][x] = g + risks[y][x]
                queue.add(AStar(x, y, g + risks[y][x], h(x, y)))
            }
        }

        while (true) {
            val next = queue.remove()
            val (x, y, g) = next
            if (next.x == targetX && next.y == targetY) {
                return g
            }
            addQueue(x, y + 1, g)
            addQueue(x + 1, y, g)
            addQueue(x - 1, y, g)
            addQueue(x, y - 1, g)
        }
    }

    fun part1(input: List<String>): Int {
        val risks = input.map { line -> line.map { it.digitToInt() } }
        return search(risks)
    }

    fun part2(input: List<String>): Int {
        val risks = input.map { line ->
            val row = line.map { it.digitToInt() }
            (0 until 5).flatMap { inc ->
                row.map { risk ->
                    val newRisk = risk + inc
                    if (newRisk > 9) {
                        newRisk - 9
                    } else {
                        newRisk
                    }
                }
            }
        }.let { cols ->
            (0 until 5).flatMap { inc ->
                cols.map { row ->
                    row.map { risk ->
                        val newRisk = risk + inc
                        if (newRisk > 9) {
                            newRisk - 9
                        } else {
                            newRisk
                        }
                    }
                }
            }
        }

        return search(risks)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput) == 40)
    check(part2(testInput) == 315)

    val input = readInput("Day15")
    println(part1(input))
    println(part2(input))
}
