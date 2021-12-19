@file:OptIn(ExperimentalStdlibApi::class)

import kotlin.math.absoluteValue
import kotlin.math.max

typealias Beacon = Triple<Int, Int, Int>
typealias Scanner = Pair<List<Triple<Int, Int, Int>>, Set<Beacon>>

fun main() {
    fun rotateFirst(scanner: Scanner): Scanner =
        Pair(
            scanner.first.map { (a, b, c) -> Triple(a, -c, b) },
            scanner.second.map { (a, b, c) -> Triple(a, -c, b) }.toSet()
        )

    fun rotateSecond(scanner: Scanner): Scanner =
        Pair(
            scanner.first.map { (a, b, c) -> Triple(c, b, -a) },
            scanner.second.map { (a, b, c) -> Triple(c, b, -a) }.toSet()
        )

    fun rotateThird(scanner: Scanner): Scanner =
        Pair(
            scanner.first.map { (a, b, c) -> Triple(-b, a, c) },
            scanner.second.map { (a, b, c) -> Triple(-b, a, c) }.toSet()
        )

    // tries to merge two scanners with only translations in first dimension
    fun tryMergeInFirstDimension(
        scanner1: Scanner,
        scanner2: Scanner,
        overlapCount: Int,
    ): Scanner? {
        val sortedByFirst1 = scanner1.second.sortedBy { it.first }
        val sortedByFirst2 = scanner2.second.sortedBy { it.first }

        for (i in 0 until (sortedByFirst1.size - overlapCount) + 1) {
            val p1 = sortedByFirst1[i]
            val sortedByFirst1Sublist = sortedByFirst1.subList(i + 1, sortedByFirst1.size)

            for (k in 0 until (sortedByFirst2.size - overlapCount) + 1) {
                val p2 = sortedByFirst2[k]
                var matchCount = 1
                val firstOffset = p1.first - p2.first
                val secondOffset = p1.second - p2.second
                val thirdOffset = p1.third - p2.third

                (k + 1 until sortedByFirst2.size).forEach { t ->
                    val p3 = sortedByFirst2[t]
                    if (sortedByFirst1Sublist.contains(
                            Triple(
                                p3.first + firstOffset,
                                p3.second + secondOffset,
                                p3.third + thirdOffset,
                            )
                        )
                    ) {
                        matchCount++

                        if (matchCount >= overlapCount) {
                            return Pair(
                                scanner1.first.plus(scanner2.first.map { (a, b, c) ->
                                    Triple(
                                        a + firstOffset,
                                        b + secondOffset,
                                        c + thirdOffset,
                                    )
                                }),
                                scanner1.second.plus(scanner2.second.map { (first, second, third) ->
                                    Triple(
                                        first + firstOffset,
                                        second + secondOffset,
                                        third + thirdOffset,
                                    )
                                })
                            )
                        }
                    }
                }
            }
        }

        return null
    }

    fun scannerPermutation(scanner: Scanner): Set<Scanner> =
        buildSet {
            add(scanner)

            val rf1 = rotateFirst(scanner)
            add(rf1)
            val rf2 = rotateFirst(rf1)
            add(rf2)
            val rf3 = rotateFirst(rf2)
            add(rf3)

            val rf4 = rotateSecond(scanner)
            add(rf4)
            val rf5 = rotateSecond(rf4)
            add(rf5)
            val rf6 = rotateSecond(rf5)
            add(rf6)

            val rf7 = rotateThird(scanner)
            add(rf7)
            val rf8 = rotateThird(rf7)
            add(rf8)
            val rf9 = rotateThird(rf8)
            add(rf9)

            val rf10 = rotateSecond(rf7)
            add(rf10)
            val rf11 = rotateSecond(rf10)
            add(rf11)
            val rf12 = rotateSecond(rf11)
            add(rf12)

            val rf13 = rotateThird(rf1)
            val rf14 = rotateThird(rf13)
            add(rf14)
            val rf15 = rotateThird(rf14)
            add(rf15)

            val rf16 = rotateThird(rf4)
            add(rf16)
            val rf17 = rotateThird(rf16)
            add(rf17)
            val rf18 = rotateThird(rf17)
            add(rf18)

            val rf19 = rotateSecond(rf9)
            add(rf19)
            val rf20 = rotateSecond(rf19)
            add(rf20)

            val rf21 = rotateSecond(rotateSecond(rotateFirst(scanner)))
            add(rf21)
            val rf22 = rotateFirst(rotateFirst(rotateSecond(scanner)))
            add(rf22)
            val rf23 = rotateFirst(rf7)
            add(rf23)
            val rf24 = rotateFirst(rf11)
            add(rf24)
        }

    fun merge(
        scanner1: Scanner,
        scanner2: Scanner,
        overlapCount: Int,
    ): Scanner? {
        for (s in scannerPermutation(scanner2)) {
            val m = tryMergeInFirstDimension(scanner1, s, overlapCount)
            if (m != null) {
                return m
            }
        }

        return null
    }

    fun part1(input: List<String>, overlapCount: Int): Int {
        val scanners = input.joinToString("\n").split("\n\n")
            .map { scanner ->
                scanner.split("\n")
                    .drop(1)
                    .map { beacon ->
                        val coords = beacon.split(",")
                        Triple(coords[0].toInt(), coords[1].toInt(), coords[2].toInt())
                    }
                    .toSet()
            }
            .map { Pair(listOf(Triple(0, 0, 0)), it) }
            .toMutableList()

        while (scanners.size > 1) {
            var i = 0
            while (i < scanners.size) {
                var k = i + 1
                while (k < scanners.size) {
                    val c = merge(
                        scanners[i],
                        scanners[k],
                        overlapCount,
                    )

                    if (c != null) {
                        scanners[i] = c
                        scanners.removeAt(k)
                    }
                    k++
                }
                i++
            }
        }

        return scanners[0].second.size
    }

    fun part2(input: List<String>, overlapCount: Int): Int {
        val scanners = input.joinToString("\n").split("\n\n")
            .map { scanner ->
                scanner.split("\n")
                    .drop(1)
                    .map { beacon ->
                        val coords = beacon.split(",")
                        Triple(coords[0].toInt(), coords[1].toInt(), coords[2].toInt())
                    }
                    .toSet()
            }
            .map { Pair(listOf(Triple(0, 0, 0)), it) }
            .toMutableList()

        while (scanners.size > 1) {
            var i = 0
            while (i < scanners.size) {
                var k = i + 1
                while (k < scanners.size) {
                    val c = merge(
                        scanners[i],
                        scanners[k],
                        overlapCount,
                    )

                    if (c != null) {
                        scanners[i] = c
                        scanners.removeAt(k)
                    }
                    k++
                }
                i++
            }
        }

        var m = Int.MIN_VALUE
        scanners[0].first.forEach { (a1, b1, c1) ->
            scanners[0].first.forEach { (a2, b2, c2) ->
                m = max(m, (a1 - a2).absoluteValue + (b1 - b2).absoluteValue + (c1 - c2).absoluteValue)
            }
        }

        return m
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day19_test")
    check(part1(testInput, 12) == 79)
    check(part2(testInput, 12) == 3621)

    val input = readInput("Day19")
    println(part1(input, 12))
    println(part2(input, 12))
}
