fun main() {
    fun part1(input: List<String>): Int {
        val binary = input.flatMap { line ->
            line.flatMap { char ->
                char.digitToInt(16)
                    .toString(2)
                    .padStart(4, '0')
                    .map { it }
            }
        }

        var i = 0
        fun readBits(bits: Int): Int {
            val num = binary.subList(i, i + bits).joinToString("").toInt(2)
            i += bits
            return num
        }

        var versionAcc = 0

        fun readPacket() {
            val version = readBits(3)
            versionAcc += version
            val typeId = readBits(3)
            if (typeId == 4) {
                // literal
                var num = 0
                while (true) {
                    val bit = readBits(1)
                    num = num.shl(4).or(readBits(4))
                    if (bit == 0) {
                        break
                    }
                }
            } else {
                // operator
                val lengthTypeId = readBits(1)
                if (lengthTypeId == 0) {
                    val lengthSubPackets = readBits(15)
                    val q = i + lengthSubPackets
                    while (i < q) {
                        readPacket()
                    }
                } else {
                    val numSubPackets = readBits(11)
                    repeat(numSubPackets) {
                        readPacket()
                    }
                }
            }
        }

        readPacket()
        return versionAcc
    }

    fun part2(input: List<String>): Long {
        val binary = input.flatMap { line ->
            line.flatMap { char ->
                char.digitToInt(16)
                    .toString(2)
                    .padStart(4, '0')
                    .map { it }
            }
        }

        var i = 0
        fun readBits(bits: Int): Long {
            val num = binary.subList(i, i + bits).joinToString("").toLong(2)
            i += bits
            return num
        }

        fun readPacket(): Long {
            readBits(3)
            val typeId = readBits(3)
            if (typeId == 4L) {
                // literal
                var num = 0L
                while (true) {
                    val bit = readBits(1)
                    num = num.shl(4).or(readBits(4))
                    if (bit == 0L) {
                        break
                    }
                }

                return num
            } else {
                // operator
                val lengthTypeId = readBits(1)

                val nums = mutableListOf<Long>()
                if (lengthTypeId == 0L) {
                    val lengthSubPackets = readBits(15)
                    val maxBit = i + lengthSubPackets
                    while (i < maxBit) {
                        nums.add(readPacket())
                    }
                } else {
                    val numSubPackets = readBits(11)
                    repeat(numSubPackets.toInt()) {
                        nums.add(readPacket())
                    }
                }

                return when (typeId) {
                    0L -> nums.sum()
                    1L -> nums.reduce { a, b -> a * b }
                    2L -> nums.minOrNull()!!
                    3L -> nums.maxOrNull()!!
                    5L -> if (nums[0] > nums[1]) 1 else 0
                    6L -> if (nums[0] < nums[1]) 1 else 0
                    7L -> if (nums[0] == nums[1]) 1 else 0
                    else -> {
                        throw Exception()
                    }
                }
            }
        }

        return readPacket()
    }

    // test if implementation meets criteria from the description, like:
    check(part1(listOf("620080001611562C8802118E34")) == 12)
    check(part1(listOf("C0015000016115A2E0802F182340")) == 23)
    check(part1(listOf("A0016C880162017C3686B18A3D4780")) == 31)

    check(part2(listOf("C200B40A82")) == 3L)
    check(part2(listOf("04005AC33890")) == 54L)
    check(part2(listOf("880086C3E88112")) == 7L)
    check(part2(listOf("CE00C43D881120")) == 9L)
    check(part2(listOf("D8005AC2A8F0")) == 1L)
    check(part2(listOf("F600BC2D8F")) == 0L)
    check(part2(listOf("9C005AC2F8F0")) == 0L)
    check(part2(listOf("9C0141080250320F1802104A08")) == 1L)

    val input = readInput("Day16")
    println(part1(input))
    println(part2(input))
}
