import java.util.PriorityQueue

data class AStarNode(val x: Int, val y: Int, val g: Int, val h: Int, val f: Int = g + h)

fun main() {
    fun search(cost: List<List<Int>>): Int {
        val targetX = cost[0].size - 1
        val targetY = cost.size - 1

        // assumes x/y is smaller than target x/y
        // dijkstra performs slightly better
        fun h(x: Int, y: Int): Int =
            0//(targetX - x) + (targetY - y)

        val minG: List<MutableList<Int?>> =
            cost.map { row -> row.indices.map { null }.toMutableList() }

        val fringe =
            PriorityQueue<AStarNode>(compareBy { it.f })

        fringe.add(AStarNode(0, 1, cost[1][0], h(0, 1)))
        fringe.add(AStarNode(1, 0, cost[0][1], h(1, 0)))
        minG[0][0] = 0
        minG[1][0] = cost[1][0]
        minG[0][1] = cost[0][1]

        fun addFringe(x: Int, y: Int, fromG: Int) {
            if (y >= 0 && y < cost.size &&
                x >= 0 && x < cost[0].size
            ) {
                val g = fromG + cost[y][x]

                if (minG[y][x]?.let { g < it } != false) {
                    minG[y][x] = g
                    fringe.add(AStarNode(x, y, g, h(x, y)))
                }
            }
        }

        while (true) {
            val next = fringe.remove()
            val (x, y, g) = next
            if (next.x == targetX && next.y == targetY) {
//                tailrec fun trace(x: Int, y: Int) {
//                    totalG[y][x] = 9999
//
//                    if (x == 0 && y == 0) {
//                        return
//                    }
//
//                    fun foo(x: Int, y: Int): Int {
//                        return if (y >= 0 && y < totalG.size && x >= 0 && x < totalG[0].size) {
//                            totalG[y][x] ?: Int.MAX_VALUE
//                        } else {
//                            Int.MAX_VALUE
//                        }
//                    }
//
//                    val m = min(
//                        min(
//                            foo(x, y - 1),
//                            foo(x - 1, y)
//                        ),
//                        min(
//                            foo(x, y + 1),
//                            foo(x + 1, y)
//                        )
//                    )
//
//                    if (y - 1 >= 0 && totalG[y - 1][x] == m) {
//                        trace(x, y - 1)
//                    } else if (x - 1 >= 0 && totalG[y][x - 1] == m) {
//                        trace(x - 1, y)
//                    } else if (y + 1 < totalG.size && totalG[y + 1][x] == m) {
//                        trace(x, y + 1)
//                    } else if (x + 1 < totalG[0].size && totalG[y][x + 1] == m) {
//                        trace(x + 1, y)
//                    }
//                }
//
//                trace(totalG[0].size - 1, totalG.size - 1)
//
//                println(totalG.joinToString("\n") { row ->
//                    row.joinToString("") {
//                        if (it == 9999) "#" else " "
//                    }
//                })

                return g
            }
            addFringe(x, y + 1, g)
            addFringe(x + 1, y, g)
            addFringe(x - 1, y, g)
            addFringe(x, y - 1, g)
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
