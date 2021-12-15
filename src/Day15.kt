import java.util.PriorityQueue
import kotlin.math.absoluteValue

fun main() {
    fun search(risks: List<List<Int>>): Int {
        //        println(risks.joinToString("\n") {
//            it.joinToString("") { "%1d".format(it) }
//        })

        val targetX = risks[0].size - 1
        val targetY = risks.size - 1

        fun h(x: Int, y: Int): Int {
            return ((x - targetX).absoluteValue + (y - targetY).absoluteValue)
        }

        val maxRisk = mutableMapOf<Pair<Int, Int>, Int>()

        val queue =
            PriorityQueue<Triple<Int, Int, Int>>(compareBy { (x, y, g) -> h(x, y) + g })

        queue.add(Triple(0, 1, risks[1][0]))
        queue.add(Triple(1, 0, risks[0][1]))
        maxRisk[Pair(0, 1)] = risks[1][0]
        maxRisk[Pair(1, 0)] = risks[0][1]

        fun addQueue(x: Int, y: Int, g: Int) {
            if (y >= 0 && y < risks.size && x >= 0 && x < risks[0].size) {
                queue.find { (xx, yy) -> xx == x && yy == y }
                if (g + risks[y][x] < (maxRisk[Pair(x, y)] ?: Int.MAX_VALUE)) {
                    maxRisk[Pair(x, y)] = g + risks[y][x]
                    queue.add(Triple(x, y, g + risks[y][x]))
                }
            }
        }

        while (true) {
            val next = queue.remove()
            val (x, y, risk) = next
            if (next.first == targetX && next.second == targetY) {
                return risk
            }
            addQueue(x, y + 1, risk)
            addQueue(x + 1, y, risk)
            addQueue(x - 1, y, risk)
            addQueue(x, y - 1, risk)
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
                        (newRisk % 10) + 1
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
                            (newRisk % 10) + 1
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
