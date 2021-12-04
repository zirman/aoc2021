fun main() {
    fun part1(input: List<String>): Int {
        val cards = input.subList(2, input.size).joinToString("\n").split("\n\n").map { string ->
            string.split("\n").map {
                it.trim().split(Regex("""\s+""")).map { it.toInt() }.toMutableList<Int?>()
            }
        }

        input[0].split(",").map { it.toInt() }.forEach { num ->
            cards.forEach { card ->
                card.forEach { row ->
                    row.forEachIndexed { i, n ->
                        if (n == num) {
                            row[i] = null
                        }
                    }
                }
            }

            cards.forEach { card ->
                card.forEach { row ->
                    if (row.all { it == null }) {
                        return card.sumOf { it.filterNotNull().sum() } * num
                    }
                }

                card.indices.forEach { col ->
                    if (card.indices.map { row -> card[row][col] }.all { it == null }) {
                        return card.sumOf { it.filterNotNull().sum() } * num
                    }
                }
            }
        }

        throw Exception()
    }

    fun part2(input: List<String>): Int {

        val cards = input.subList(2, input.size).joinToString("\n").split("\n\n").map { string ->
            string.split("\n").map {
                it.trim().split(Regex("""\s+""")).map { it.toInt() }.toMutableList<Int?>()
            }
        }

        val winningCards = mutableSetOf<Int>()

        for (num in input[0].split(",").map { it.toInt() }) {
            cards.forEachIndexed { i, card ->
                if (winningCards.contains(i).not()) {
                    card.forEach { row ->
                        row.forEachIndexed { i, n ->
                            if (n == num) {
                                row[i] = null
                            }
                        }
                    }
                }
            }

            cards.forEachIndexed() { i, card ->
                if (winningCards.contains(i).not()) {
                    card.forEach { row ->
                        if (row.all { it == null }) {
                            winningCards.add(i)
                            if (winningCards.size == cards.size) {
                                return card.sumOf { it.filterNotNull().sum() } * num
                            }
                        }
                    }

                    card.indices.forEach { col ->
                        if (card.indices.map { row -> card[row][col] }.all { it == null }) {
                            winningCards.add(i)
                            if (winningCards.size == cards.size) {
                                return card.sumOf { it.filterNotNull().sum() } * num
                            }
                        }
                    }
                }
            }
        }

        throw Exception()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
