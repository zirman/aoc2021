import kotlin.math.max

sealed class SnailNum {
    data class Num(val x: Int) : SnailNum() {
        override fun toString(): String =
            x.toString()
    }

    data class Node(val left: SnailNum, val right: SnailNum) : SnailNum() {
        override fun toString(): String =
            "[${left},${right}]"
    }
}

sealed class Explosion {
    data class NormalForm(val sn: SnailNum) : Explosion()
    data class Exploding(val left: Int, val sn: SnailNum, val right: Int) : Explosion()
}

sealed class Split {
    object NormalForm : Split()
    data class Splitting(val sn: SnailNum) : Split()
}

fun magnitude(sn: SnailNum): Int =
    when (sn) {
        is SnailNum.Num -> sn.x
        is SnailNum.Node -> magnitude(sn.left) * 3 + magnitude(sn.right) * 2
    }

fun add(sn1: SnailNum, sn2: SnailNum): SnailNum =
    snailNumReduction(SnailNum.Node(sn1, sn2))

fun mergeRight(sn: SnailNum, right: Int): SnailNum =
    when (sn) {
        is SnailNum.Node -> SnailNum.Node(sn.left, mergeRight(sn.right, right))
        is SnailNum.Num -> SnailNum.Num(sn.x + right)
    }

fun mergeLeft(sn: SnailNum, left: Int): SnailNum =
    when (sn) {
        is SnailNum.Node -> SnailNum.Node(mergeLeft(sn.left, left), sn.right)
        is SnailNum.Num -> SnailNum.Num(sn.x + left)
    }

fun explosionReduction(sn: SnailNum, depth: Int): Explosion =
    when (sn) {
        is SnailNum.Num -> Explosion.NormalForm(sn)
        is SnailNum.Node -> {
            if (depth >= 4 && sn.left is SnailNum.Num && sn.right is SnailNum.Num) {
                Explosion.Exploding(sn.left.x, SnailNum.Num(0), sn.right.x)
            } else {
                when (val reductionLeft = explosionReduction(sn.left, depth + 1)) {
                    is Explosion.Exploding ->
                        Explosion.Exploding(
                            left = reductionLeft.left,
                            sn = SnailNum.Node(
                                reductionLeft.sn,
                                mergeLeft(sn.right, reductionLeft.right)
                            ),
                            right = 0,
                        )
                    is Explosion.NormalForm -> {
                        when (val reductionRight = explosionReduction(sn.right, depth + 1)) {
                            is Explosion.Exploding ->
                                Explosion.Exploding(
                                    left = 0,
                                    sn = SnailNum.Node(
                                        mergeRight(sn.left, reductionRight.left),
                                        reductionRight.sn,
                                    ),
                                    right = reductionRight.right,
                                )
                            is Explosion.NormalForm -> Explosion.NormalForm(sn)
                        }
                    }
                }
            }
        }
    }

fun splitReduction(sn: SnailNum): Split =
    when (sn) {
        is SnailNum.Num -> {
            if (sn.x > 9) {
                Split.Splitting(SnailNum.Node(SnailNum.Num(sn.x / 2), SnailNum.Num(sn.x - (sn.x / 2))))
            } else {
                Split.NormalForm
            }
        }
        is SnailNum.Node -> {
            when (val reduceLeft = splitReduction(sn.left)) {
                is Split.NormalForm ->
                    when (val reduceRight = splitReduction(sn.right)) {
                        is Split.NormalForm -> {
                            Split.NormalForm
                        }
                        is Split.Splitting -> {
                            Split.Splitting(SnailNum.Node(sn.left, reduceRight.sn))
                        }
                    }
                is Split.Splitting -> Split.Splitting(SnailNum.Node(reduceLeft.sn, sn.right))
            }
        }
    }

fun snailNumReduction(sn: SnailNum): SnailNum {
    var s = sn

    while (true) {
        when (val reduction = explosionReduction(s, 0)) {
            is Explosion.Exploding -> {
                s = reduction.sn
                continue
            }
            is Explosion.NormalForm -> {
                when (val reduction2 = splitReduction(s)) {
                    is Split.Splitting -> {
                        s = reduction2.sn
                        continue
                    }
                    is Split.NormalForm -> {
                    }
                }
            }
        }

        break
    }

    return s
}

fun main() {
    fun part1(input: List<String>): Int {
        return magnitude(
            input
                .map { line ->
                    val (squidNum, i) = parseSquidNum()(line.toCharArray(), 0)!!
                    assert(i == line.count())
                    squidNum
                }
                .reduce { acc, snailNum -> add(acc, snailNum) }
        )
    }

    fun part2(input: List<String>): Int {
        val snailNumbers = input.map { line ->
            val (squidNum, i) = parseSquidNum()(line.toCharArray(), 0)!!
            assert(i == line.count())
            squidNum
        }

        var m = Int.MIN_VALUE

        snailNumbers.indices.forEach { i ->
            val sn1 = snailNumbers[i]
            (i + 1 until snailNumbers.size).forEach { k ->
                val sn2 = snailNumbers[k]
                m = max(m, max(magnitude(add(sn1, sn2)), magnitude(add(sn2, sn1))))
            }
        }

        return m
    }

    check(
        (explosionReduction(
            parseSquidNum()("[[[[[9,8],1],2],3],4]".toCharArray(), 0)!!.first,
            0
        ) as Explosion.Exploding).sn.toString() == "[[[[0,9],2],3],4]"
    )

    check(
        (explosionReduction(
            parseSquidNum()("[7,[6,[5,[4,[3,2]]]]]".toCharArray(), 0)!!.first,
            0
        ) as Explosion.Exploding).sn.toString() == "[7,[6,[5,[7,0]]]]"
    )

    check(
        (explosionReduction(
            parseSquidNum()("[[6,[5,[4,[3,2]]]],1]".toCharArray(), 0)!!.first,
            0
        ) as Explosion.Exploding).sn.toString() == "[[6,[5,[7,0]]],3]"
    )

    check(
        (explosionReduction(
            parseSquidNum()("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]".toCharArray(), 0)!!.first,
            0
        ) as Explosion.Exploding).sn.toString() == "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]"
    )

    check(
        (explosionReduction(
            parseSquidNum()("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]".toCharArray(), 0)!!.first,
            0
        ) as Explosion.Exploding).sn.toString() == "[[3,[2,[8,0]]],[9,[5,[7,0]]]]"
    )

    check(
        (splitReduction(
            (explosionReduction(
                parseSquidNum()("[[[[0,7],4],[7,[[8,4],9]]],[1,1]]".toCharArray(), 0)!!.first, 0
            ) as Explosion.Exploding).sn
        ) as Split.Splitting).sn.toString() == "[[[[0,7],4],[[7,8],[0,13]]],[1,1]]"
    )

    check(
        snailNumReduction(
            parseSquidNum()(
                "[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]".toCharArray(),
                0
            )!!.first
        ).toString() ==
                "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]"
    )

    check(
        snailNumReduction(
            add(
                parseSquidNum()("[[[[4,3],4],4],[7,[[8,4],9]]]".toCharArray(), 0)!!.first,
                parseSquidNum()("[1,1]".toCharArray(), 0)!!.first,
            )
        ).toString() == "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]"
    )

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test4")
    check(part2(testInput) == 3993)

    val input = readInput("Day18")
    println(part1(input))
    println(part2(input))
}


typealias Parser<T> = (CharArray, Int) -> Pair<T, Int>?

infix fun <T, R> Parser<T>.map(f: (T) -> R): Parser<R> =
    { cs, i -> this@map(cs, i)?.let { (x, i) -> Pair(f(x), i) } }

infix fun <T, R> Parser<T>.then(f: (T) -> Parser<R>): Parser<R> =
    { cs, i -> this@then(cs, i)?.let { (x, k) -> f(x)(cs, k) } }

infix fun <T> Parser<T>.or(f: Parser<T>): Parser<T> =
    { cs, i -> this@or(cs, i) ?: f(cs, i) }

fun parseChar(c: Char): Parser<Char> =
    { cs, i -> if (cs[i] == c) Pair(c, i + 1) else null }

val parseNum: Parser<Int> =
    { cs, i -> if (cs[i].isDigit()) Pair(cs[i].digitToInt(), i + 1) else null }

fun parseSquidNum(): Parser<SnailNum> =
    (parseNum map { x -> SnailNum.Num(x) }) or
            (parseChar('[') then {
                parseSquidNum()
            } then { n1 ->
                parseChar(',') then {
                    parseSquidNum()
                } then { n2 ->
                    parseChar(']') map {
                        SnailNum.Node(n1, n2)
                    }
                }
            })
