package stage2


import java.util.*
import kotlin.system.exitProcess

val scanner = Scanner(System.`in`)

fun main(args: Array<String>) {
    val dataType = if (args.isEmpty()) {
        "word"
    } else if (args.size != 2 ||
        (
                args[0] != "-dataType" ||
                args[1] !in listOf("long", "line", "word"))
    ) {
        println("usage: <script> -dataType [long|line|word]")
        exitProcess(1)
    } else {
        args[1]
    }

    when (dataType) {
        "line" -> processLines()
        "word" -> processWords()
        "long" -> processNumbers()
    }
}

fun processLines() {
    val lines = mutableListOf<String>()
    while (scanner.hasNextLine()) {
        lines.add(scanner.nextLine())
    }

    val frequencies = buildFrequencyMap(lines)
    val max = lines.maxByOrNull { it.length }.toString()
    val percentage = (frequencies[max] ?: 0).toDouble() / lines.size.toDouble() * 100.0

    println("Total lines: ${lines.size}.")
    println("The longest line:")
    println(max)
    println("(${frequencies[max]} time(s), ${percentage.toInt()}%).")
}

fun processWords() {
    val words = mutableListOf<String>()
    while (scanner.hasNext()) {
        words.add(scanner.next())
    }

    val frequencies = buildFrequencyMap(words)
    val max = words.maxByOrNull { it.length }.toString()
    val percentage = (frequencies[max] ?: 0).toDouble() / words.size.toDouble() * 100.0

    println("Total words: ${words.size}.")
    println("The longest word: $max (${frequencies[max]} time(s), ${percentage.toInt()}%).")
}

fun processNumbers() {
    val numbers = mutableListOf<Long>()
    while (scanner.hasNext()) {
        numbers.add(scanner.nextLong())
    }

    val frequencies = buildFrequencyMap(numbers)
    val max = numbers.maxOrNull().toString()

    println("Total numbers: ${numbers.size}.")
    println("The greatest number: $max (${frequencies[max.toLong()]} time(s)).")
}

fun buildFrequencyMap(list: List<Any>): Map<Any, Int> {
    return list.groupBy { it }
        .map { (value, eqValue) -> (value to eqValue.size) }
        .toMap()
}