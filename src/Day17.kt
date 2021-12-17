fun main() {
    fun part1(input: String): Int {
        val groupValues = Regex("""target area: x=(\d+)\.\.(\d+), y=(-?\d+)\.\.(-?\d+)""")
            .matchEntire(input)!!
            .groupValues

        val yMin = groupValues[3].toInt()
        val yVel = -(yMin + 1)
        return ((yVel + 1) * yVel) / 2
    }

    fun part2(input: String): Int {
        val groupValues = Regex("""target area: x=(\d+)\.\.(\d+), y=(-?\d+)\.\.(-?\d+)""")
            .matchEntire(input)!!
            .groupValues

        val xRange = groupValues[1].toInt()..groupValues[2].toInt()
        val yRange = groupValues[3].toInt()..groupValues[4].toInt()

        val xMinVel = 0
        val xMaxVel = xRange.last
        val yMaxVel = -(yRange.first + 1)
        val yMinVel = yRange.first

        var count = 0

        (xMinVel..xMaxVel).forEach { xInitialVel ->
            (yMinVel..yMaxVel).forEach { yInitialVel ->
                var x = 0
                var y = 0
                var xVel = xInitialVel
                var yVel = yInitialVel

                while (x <= xRange.last && y >= yRange.first) {
                    x += xVel
                    y += yVel
                    if (x in xRange && y in yRange) {
                        count++
                        break
                    }
                    if (xVel > 0) {
                        xVel--
                    } else if (xVel < 0) {
                        xVel++
                    }
                    yVel--
                }
            }
        }

        return count
    }

    // test if implementation meets criteria from the description, like:
    check(part1("target area: x=20..30, y=-10..-5") == 45)
    check(part2("target area: x=20..30, y=-10..-5") == 112)

    println(part1("target area: x=195..238, y=-93..-67"))
    println(part2("target area: x=195..238, y=-93..-67"))
}
