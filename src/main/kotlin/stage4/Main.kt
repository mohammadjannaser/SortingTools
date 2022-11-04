package stage4


import java.util.*
import kotlin.math.roundToInt
import kotlin.system.exitProcess

val scanner = Scanner(System.`in`)

fun main(args: Array<String>) {
    val (dataType, sortingType) = parseArgs(args)
    when (dataType) {
        "line" -> processLines(sortingType)
        "word" -> processWords(sortingType)
        "long" -> processNumbers(sortingType)
    }
}

fun parseArgs(args: Array<String>): Pair<String, String> {
    if (args.size !in listOf(2, 4)) {
        exitWithUsage()
    }

    var dataType = "word"
    var sortingType = "natural"

    for ((paramKey, paramValue) in args.asList().chunked(2)) {
        when (paramKey) {
            "-dataType" -> {
                if (paramValue in listOf("long", "line", "word")) {
                    dataType = paramValue
                } else {
                    exitWithUsage()
                }
            }
            "-sortingType" -> {
                if (paramValue in listOf("natural", "byCount")) {
                    sortingType = paramValue
                } else {
                    exitWithUsage()
                }
            }
        }
    }

    return Pair(dataType, sortingType)
}

fun exitWithUsage() {
    println("usage: <script> -dataType [long|line|word] -sortingType [natural|byCount]")
    exitProcess(1)
}

fun processLines(sortingType: String) {
    val lines = mutableListOf<String>()
    while (scanner.hasNextLine()) {
        lines.add(scanner.nextLine())
    }

    println("Total lines: ${lines.size}.")

    if (sortingType == "natural") {
        println("Sorted data:")
        lines.sorted().forEach { println(it) }
    } else if (sortingType == "byCount") {
        val comparator = compareBy<Map.Entry<String, Int>> { it.value }.thenBy { it.key }

        val frequencies = lines.groupingBy { it }.eachCount()

        frequencies.entries.sortedWith(comparator).forEach {
            val percentage = it.value.toDouble() / lines.size * 100.0
            println("${it.key}: ${it.value} time(s), ${percentage.roundToInt()}%")
        }
    }
}

fun processWords(sortingType: String) {
    val words = mutableListOf<String>()
    while (scanner.hasNext()) {
        words.add(scanner.next())
    }

    println("Total words: ${words.size}.")

    if (sortingType == "natural") {
        println("Sorted data: ${words.sorted().joinToString(" ")}")
    } else if (sortingType == "byCount") {
        val comparator = compareBy<Map.Entry<String, Int>> { it.value }.thenBy { it.key }

        val frequencies = words.groupingBy { it }.eachCount()

        frequencies.entries.sortedWith(comparator).forEach {
            val percentage = it.value.toDouble() / words.size * 100.0
            println("${it.key}: ${it.value} time(s), ${percentage.roundToInt()}%")
        }
    }
}

fun processNumbers(sortingType: String) {
    val numbers = mutableListOf<Long>()
    while (scanner.hasNext()) {
        numbers.add(scanner.nextLong())
    }
    println("Total numbers: ${numbers.size}.")

    if (sortingType == "natural") {
        println("Sorted data: ${numbers.sorted().joinToString(" ")}")
    } else if (sortingType == "byCount") {
        val comparator = compareBy<Map.Entry<Long, Int>> { it.value }.thenBy { it.key }

        val frequencies = numbers.groupingBy { it }.eachCount()

        frequencies.entries.sortedWith(comparator).forEach {
            val percentage = it.value.toDouble() / numbers.size * 100.0
            println("${it.key}: ${it.value} time(s), ${percentage.roundToInt()}%")
        }
    }
}