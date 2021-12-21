import kotlin.math.max
import kotlin.math.min

data class DiracState(val p1Position: Int, val p1Score: Long, val p2Position: Int, val p2Score: Long)

val distributions = (1..3).flatMap { d1 ->
    (1..3).flatMap { d2 ->
        (1..3).map { d3 ->
            d1 + d2 + d3
        }
    }
}
    .groupBy { it }
    .map { (k, v) -> Pair(k, v.size) }

val memo1 = mutableMapOf<DiracState, Pair<Long, Long>>()
fun rollDieP1(key: DiracState): Pair<Long, Long> = memo1
    .computeIfAbsent(key) { diracState ->
        val (p1Position, p1Score, _, _) = diracState

        var p1WinsTotal = 0L
        var p2WinsTotal = 0L

        distributions.forEach { (p1Move, distribution) ->
            val p1Next = (p1Position + p1Move) % 10
            val p1ScoreNext = p1Score + (p1Next + 1)
            if (p1ScoreNext >= 21) {
                p1WinsTotal += distribution
            } else {
                val (p1Wins, p2Wins) = rollDieP2(diracState.copy(p1Position = p1Next, p1Score = p1ScoreNext))
                p1WinsTotal += p1Wins * distribution
                p2WinsTotal += p2Wins * distribution
            }
        }

        Pair(p1WinsTotal, p2WinsTotal)
    }

val memo2 = mutableMapOf<DiracState, Pair<Long, Long>>()
fun rollDieP2(key: DiracState): Pair<Long, Long> = memo2
    .computeIfAbsent(key) { diracState ->
        val (_, _, p2Position, p2Score) = diracState

        var p1WinsTotal = 0L
        var p2WinsTotal = 0L

        distributions.forEach { (p2Move, distribution) ->
            val p2Next = (p2Position + p2Move) % 10
            val p2ScoreNext = p2Score + (p2Next + 1)
            if (p2ScoreNext >= 21) {
                p2WinsTotal += distribution
            } else {
                val (p1Wins, p2Wins) = rollDieP1(diracState.copy(p2Position = p2Next, p2Score = p2ScoreNext))
                p1WinsTotal += p1Wins * distribution
                p2WinsTotal += p2Wins * distribution
            }
        }

        Pair(p1WinsTotal, p2WinsTotal)
    }

fun main() {
    fun part1(p1: Int, p2: Int): Int {
        var player1 = p1 - 1
        var player1Score = 0
        var player2 = p2 - 1
        var player2Score = 0
        var die = 0
        var rolls = 0

        fun rollDie(): Int {
            val r = die + 1
            die = (die + 1) % 100
            rolls++
            return r
        }

        while (true) {
            player1 = (player1 + rollDie() + rollDie() + rollDie()) % 10
            player1Score += player1 + 1
            if (player1Score >= 1000) {
                break
            }

            player2 = (player2 + rollDie() + rollDie() + rollDie()) % 10
            player2Score += player2 + 1
            if (player2Score >= 1000) {
                break
            }
        }

        return min(player1Score, player2Score) * rolls
    }

    fun part2(p1: Int, p2: Int): Long {
        val (p1Wins, p2Wins) = rollDieP1(DiracState(p1Position = p1 - 1, p1Score = 0, p2Position = p2 -1, p2Score = 0))
        return max(p1Wins, p2Wins)
    }

    // test if implementation meets criteria from the description, like:
    check(part1(4, 8) == 739785)
    check(part2(4, 8) == 444_356_092_776_315L)

    println(part1(7, 5))
    println(part2(7, 5))
}
