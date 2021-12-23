@file:OptIn(ExperimentalStdlibApi::class)

import kotlin.system.measureTimeMillis

data class Range(val min: Long, val max: Long)
typealias Cuboid = List<Range>

fun Range.containsElement(i: Long): Boolean =
    i >= min && i <= max

fun Range.length(): Long =
    (max - min) + 1

fun Range.intersection(range: Range): Range? =
    if (containsElement(range.min)) {
        if (containsElement(range.max)) {
            range
        } else {
            Range(range.min, max)
        }
    } else if (range.containsElement(min)) {
        if (range.containsElement(max)) {
            this
        } else {
            Range(min, range.max)
        }
    } else {
        null
    }

fun Range.overlapping(range: Range): Triple<List<Range>, Range?, List<Range>> =
    if (containsElement(range.min)) {
        if (containsElement(range.max)) {
            Triple(
                buildList {
                    if (min <= range.min - 1) {
                        add(Range(min, range.min - 1))
                    }

                    if (range.max + 1 <= max) {
                        add(Range(range.max + 1, max))
                    }
                },
                range,
                emptyList(),
            )
        } else {
            Triple(
                buildList {
                    if (min <= range.min - 1) {
                        add(Range(min, range.min - 1))
                    }
                },
                Range(range.min, max),
                buildList {
                    if (max + 1 <= range.max) {
                        add(Range(max + 1, range.max))
                    }
                },
            )
        }
    } else if (range.containsElement(min)) {
        if (range.containsElement(max)) {
            Triple(
                emptyList(),
                this,
                buildList {
                    if (range.min <= min - 1) {
                        add(Range(range.min, min - 1))
                    }

                    if (max + 1 <= range.max) {
                        add(Range(max + 1, range.max))
                    }
                },
            )
        } else {
            Triple(
                listOf(Range(range.max + 1, max)),
                Range(min, range.max),
                listOf(Range(range.min, min - 1)),
            )
        }
    } else {
        Triple(
            listOf(this),
            null,
            listOf(range),
        )
    }

operator fun Cuboid.minus(cuboid: Cuboid): List<Cuboid> {
    val (xSlice1, xOverlap, _) = this[0].overlapping(cuboid[0])
    val mutableList = mutableListOf<Cuboid>()
    xSlice1.forEach { mutableList.add(listOf(it, this[1], this[2])) }

    xOverlap?.let { xRange ->
        val (ySlice1, yOverlap, _) = this[1].overlapping(cuboid[1])
        ySlice1.forEach { mutableList.add(listOf(xRange, it, this[2])) }

        yOverlap?.let { yRange ->
            val (zSlice1, _, _) = this[2].overlapping(cuboid[2])
            zSlice1.forEach { mutableList.add(listOf(xRange, yRange, it)) }
        }
    }

    return mutableList
}

fun Cuboid.volume(): Long =
    fold(1L) { acc, i -> acc * i.length() }

fun main() {
    fun part1(input: List<String>): Int {
        val cuboids = (-50..50).map { x ->
            (-50..50).map { y ->
                (-50..50).map { z ->
                    false
                }.toMutableList()
            }
        }
        input.forEach { line ->
            val (onOff, xMinString, xMaxString, yMinString, yMaxString, zMinString, zMaxString) =
                """(on|off) x=(-?\d+)\.\.(-?\d+),y=(-?\d+)\.\.(-?\d+),z=(-?\d+)\.\.(-?\d+)"""
                    .toRegex().matchEntire(line)!!.destructured

            val xMin = xMinString.toInt()
            val xMax = xMaxString.toInt()

            val yMin = yMinString.toInt()
            val yMax = yMaxString.toInt()

            val zMin = zMinString.toInt()
            val zMax = zMaxString.toInt()

            when (onOff) {
                "on" -> {
                    (xMin..xMax).forEach { x ->
                        if (x >= -50 && x <= 50) {
                            (yMin..yMax).forEach { y ->
                                if (y >= -50 && y <= 50) {
                                    (zMin..zMax).forEach { z ->
                                        if (z >= -50 && z <= 50) {
                                            cuboids[x + 50][y + 50][z + 50] = true
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                "off" -> {
                    (xMin..xMax).forEach { x ->
                        if (x >= -50 && x <= 50) {
                            (yMin..yMax).forEach { y ->
                                if (y >= -50 && y <= 50) {
                                    (zMin..zMax).forEach { z ->
                                        if (z >= -50 && z <= 50) {
                                            cuboids[x + 50][y + 50][z + 50] = false
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                else -> {
                    throw Exception()
                }
            }
        }

        var count = 0
        (-50..50).forEach { x ->
            (-50..50).forEach { y ->
                (-50..50).forEach { z ->
                    if (cuboids[x + 50][y + 50][z + 50]) {
                        count++
                    }
                }
            }
        }

        return count
    }

    fun part2(input: List<String>): Long {
        var cuboids = listOf<Cuboid>()

        val cs = input.map { line ->
            val (onOff, xMinString, xMaxString, yMinString, yMaxString, zMinString, zMaxString) =
                """(on|off) x=(-?\d+)\.\.(-?\d+),y=(-?\d+)\.\.(-?\d+),z=(-?\d+)\.\.(-?\d+)"""
                    .toRegex().matchEntire(line)!!.destructured

            Pair(
                onOff,
                listOf(
                    Range(xMinString.toLong(), xMaxString.toLong()),
                    Range(yMinString.toLong(), yMaxString.toLong()),
                    Range(zMinString.toLong(), zMaxString.toLong()),
                ),
            )
        }


        cs.forEach { (onOff, c) ->
            //println("${cuboids.size} $onOff $c")
            when (onOff) {
                "on" -> {
                    cuboids =
                        cuboids.plus(
                            cuboids.fold(listOf(c)) { acc, c1 ->
                                acc.flatMap { c2 -> c2 - c1 }
                            }
                        )
                }
                "off" -> {
                    cuboids = cuboids.flatMap { it - c }
                }
                else -> {
                    throw Exception()
                }
            }
        }

        return cuboids.sumOf { it.volume() }
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("Day22_test1")) == 590784)
    check(part2(readInput("Day22_test2")) == 2_758_514_936_282_235L)

    val input = readInput("Day22")
    println(part1(input))
    println("time: ${measureTimeMillis { part2(input) }}")
}
