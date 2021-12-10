import java.util.Stack

fun main() {
    fun part1(input: List<String>): Int {

        return input.map { line ->
            val stack = Stack<Char>()
            var score = 0
            line.forEach { c ->
                when (c) {
                    '(', '[', '{', '<' -> {
                        stack.push(c)
                    }
                    ')', ']', '}', '>' -> {
                        if (c != when (stack.pop()) {
                                '(' -> {
                                    ')'
                                }
                                '[' -> {
                                    ']'
                                }
                                '{' -> {
                                    '}'
                                }
                                '<' -> {
                                    '>'
                                }
                                else -> {
                                    throw Exception()
                                }
                            }
                        ) {
                            score += when (c) {
                                ')' -> {
                                    3
                                }
                                ']' -> {
                                    57
                                }
                                '}' -> {
                                    1197
                                }
                                '>' -> {
                                    25137
                                }
                                else -> {
                                    throw Exception()
                                }
                            }
                        }
                    }
                    else -> {
                        throw Exception()
                    }
                }
            }

            score
        }.sum()
    }

    fun part2(input: List<String>): Long {
        val scores = input.map { line ->
            val stack = Stack<Char>()

            line.forEach { c ->
                when (c) {
                    '(', '[', '{', '<' -> {
                        stack.push(c)
                    }
                    ')', ']', '}', '>' -> {
                        if (c != when (stack.pop()) {
                                '(' -> {
                                    ')'
                                }
                                '[' -> {
                                    ']'
                                }
                                '{' -> {
                                    '}'
                                }
                                '<' -> {
                                    '>'
                                }
                                else -> {
                                    throw Exception()
                                }
                            }
                        ) {
                            return@map 0
                        }
                    }
                    else -> {
                        throw Exception()
                    }
                }
            }

            var score = 0L
            while (stack.isNotEmpty()) {
                score *= 5L
                score +=
                    when (stack.pop()) {
                        '(' -> {
                            1
                        }
                        '[' -> {
                            2
                        }
                        '{' -> {
                            3
                        }
                        '<' -> {
                            4
                        }
                        else -> {
                            throw Exception()
                        }
                    }
            }
            score
        }.filter { it != 0L }.sorted()

        return scores[scores.size / 2]
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 26397)
    check(part2(testInput) == 288957L)

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
