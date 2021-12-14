fun <K, V : Any> Map<K, V>.merge(m: Map<K, V>, f: (V?, V?) -> V): Map<K, V> {
    return keys.plus(m.keys).associateWith { key -> f(this[key], m[key]) }
}

fun <K, V : Any> Map<K, V>.merge(key: K, f: (V?) -> V): Map<K, V> {
    return plus(Pair(key, f(this[key])))
}

fun main() {
    fun part1(input: List<String>, iterations: Int): Int {
        val polymerInsertMap = mutableMapOf<List<Char>, Char>()
        val sections = input.joinToString("\n").split("\n\n")

        sections[1].split("\n").map { line ->
            val cols = line.split(" -> ")
            polymerInsertMap[cols[0].map { it }] = cols[1][0]
        }

        val polymer = (1..iterations).fold(sections[0].map { it }) { cs, _ ->
            cs.windowed(2)
                .flatMap { listOf(it[0], polymerInsertMap[it]!!) }
                .plus(cs.last())
        }

        val counts = polymer.groupBy { it }.map { (_, charList) -> charList.count() }
        val min = counts.minByOrNull { it }!!
        val max = counts.maxByOrNull { it }!!
        return max - min
    }

    fun part2(input: List<String>, iterations: Int): Long {
        val polymerInsertMap = mutableMapOf<Pair<Char, Char>, Char>()
        val sections = input.joinToString("\n").split("\n\n")

        sections[1].split("\n").map { line ->
            val cols = line.split(" -> ")
            polymerInsertMap[Pair(cols[0][0], cols[0][1])] = cols[1][0]
        }

        val memo = mutableMapOf<Triple<Char, Char, Int>, Map<Char, Long>>()

        // gets the count of chars between c1 and c2
        fun recur(c1: Char, c2: Char, depth: Int): Map<Char, Long> =
            memo.computeIfAbsent(Triple(c1, c2, depth)) {
                if (depth == iterations) {
                    mapOf()
                } else {
                    val cMid = polymerInsertMap[Pair(c1, c2)]!!

                    recur(c1, cMid, depth + 1)
                        .merge(recur(cMid, c2, depth + 1)) { a, b -> (a ?: 0) + (b ?: 0) }
                        .merge(cMid) { x -> (x ?: 0) + 1 }
                }
            }

        val cs = sections[0].map { it }

        val counts = cs.windowed(2).fold(mapOf<Char, Long>(cs.last() to 1)) { m1, it ->
            recur(it[0], it[1], 0)
                .merge(m1) { a, b -> (a ?: 0) + (b ?: 0) }
                .merge(it[0]) { (it ?: 0) + 1 }
        }

        val min = counts.minByOrNull { (_, b) -> b }!!.value
        val max = counts.maxByOrNull { (_, b) -> b }!!.value
        return max - min
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part2(testInput, 10) == 1_588L)
    check(part2(testInput, 40) == 2_188_189_693_529L)


    val input = readInput("Day14")
    println(part2(input, 10))
    println(part2(input, 40))
}
