sealed class LinkedList<out T> {
    object End : LinkedList<Nothing>()
    data class Link<T>(val head: T, val tail: LinkedList<T>) : LinkedList<T>()
}

tailrec fun <T> LinkedList<T>.contains(v: T): Boolean =
    when (this) {
        is LinkedList.End -> false
        is LinkedList.Link ->
            if (head == v) {
                true
            } else {
                tail.contains(v)
            }
    }

fun <T> LinkedList<T>.cons(v: T): LinkedList<T> =
    LinkedList.Link(v, this)

fun main() {
    fun part1(input: List<String>): Int {
        val paths = mutableMapOf<String, MutableList<String>>()

        input.forEach { line ->
            val cols = line.split('-')
            paths.getOrPut(cols[0]) { mutableListOf() }.add(cols[1])
            // don't link back to starting cave
            if (cols[0] != "start" && cols[1] != "end") {
                paths.getOrPut(cols[1]) { mutableListOf() }.add(cols[0])
            }
        }

        fun recur(path: LinkedList<String>, cave: String): Sequence<LinkedList<String>> =
            when {
                // check for base cases
                cave == "end" -> sequenceOf(path.cons("end"))
                cave[0].isLowerCase() && path.contains(cave) -> emptySequence()
                else -> paths[cave]
                    ?.asSequence()
                    ?.flatMap { nextCave -> recur(path.cons(cave), nextCave) }
                    ?: emptySequence()
            }

        return recur(LinkedList.End, "start").count()
    }

    fun part2(input: List<String>): Int {
        val paths = mutableMapOf<String, MutableList<String>>()

        input.forEach { line ->
            val cols = line.split('-')
            paths.getOrPut(cols[0]) { mutableListOf() }.add(cols[1])
            // don't link back to starting cave
            if (cols[0] != "start" && cols[1] != "end") {
                paths.getOrPut(cols[1]) { mutableListOf() }.add(cols[0])
            }
        }

        fun recur(path: LinkedList<String>, cave: String, hasSecond: Boolean): Sequence<LinkedList<String>> =
            when {
                // check for base cases
                cave == "end" -> sequenceOf(path.cons("end"))
                cave[0].isLowerCase() && path.contains(cave) && hasSecond -> emptySequence()
                else -> paths[cave]
                    ?.asSequence()
                    ?.flatMap { nextCave ->
                        recur(
                            path.cons(cave),
                            nextCave,
                            hasSecond || (cave[0].isLowerCase() && path.contains(cave))
                        )
                    }
                    ?: emptySequence()
            }

        return recur(LinkedList.End, "start", false).count()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 226)
    check(part2(testInput) == 3509)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}
