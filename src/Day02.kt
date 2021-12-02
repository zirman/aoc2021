sealed class Movement {
    data class Forward(val x: Int) : Movement()
    data class Down(val x: Int) : Movement()
    data class Up(val x: Int) : Movement()
}

fun main() {
    fun part1(input: List<String>): Int {
        val movements = input.map { string ->
            Regex("""forward (\d+)""").matchEntire(string)
                ?.let {
                    Movement.Forward(it.groups[1]!!.value.toInt())
                }
                ?: Regex("""down (\d+)""").matchEntire(string)?.let {
                    Movement.Down(it.groups[1]!!.value.toInt())
                }
                ?: Regex("""up (\d+)""").matchEntire(string)?.let {
                    Movement.Up(it.groups[1]!!.value.toInt())
                }
                ?: throw Exception()
        }

        var depth = 0
        var distance = 0
        movements.forEach { movement ->
            when (movement) {
                is Movement.Forward -> {
                    distance += movement.x
                }
                is Movement.Down -> {
                    depth += movement.x
                }
                is Movement.Up -> {
                    depth -= movement.x
                }
            }
        }

        return depth * distance
    }

    fun part2(input: List<String>): Int {
        val movements = input.map { string ->
            Regex("""forward (\d+)""").matchEntire(string)
                ?.let {
                    Movement.Forward(it.groups[1]!!.value.toInt())
                }
                ?: Regex("""down (\d+)""").matchEntire(string)?.let {
                    Movement.Down(it.groups[1]!!.value.toInt())
                }
                ?: Regex("""up (\d+)""").matchEntire(string)?.let {
                    Movement.Up(it.groups[1]!!.value.toInt())
                }
                ?: throw Exception()
        }

        var depth = 0
        var distance = 0
        var aim = 0

        movements.forEach { movement ->
            when (movement) {
                is Movement.Forward -> {
                    distance += movement.x
                    depth += aim * movement.x
                }
                is Movement.Down -> {
                    aim += movement.x
                }
                is Movement.Up -> {
                    aim -= movement.x
                }
            }
        }

        return depth * distance
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
