package stage

import java.util.*

val scanner = Scanner(System.`in`)

fun main() {
    val numbers = mutableListOf<Int>()
    while (scanner.hasNext()) {
        numbers.add(scanner.nextInt())
    }

    val frequencies = numbers.groupBy { it }
        .map { (value, eqValue) -> (value to eqValue.size) }
        .toMap()
    val max = numbers.maxOrNull()

    println("Total numbers: ${numbers.size}")
    println("The greatest number: $max (${frequencies[max]} time(s)).")
}