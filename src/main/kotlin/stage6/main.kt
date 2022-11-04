package stage6


import java.io.File
import java.io.FileReader
import java.util.*
import kotlin.math.roundToInt
import kotlin.system.exitProcess

data class Arguments(
    val dataType: String,
    val sortingType: String,
    val inputFile: String,
    val outputFile: String
)

fun main(args: Array<String>) {
    val arguments = parseArgs(args)
    when (arguments.dataType) {
        "line" -> processLines(arguments)
        "word" -> processWords(arguments)
        "long" -> processNumbers(arguments)
    }
}

fun parseArgs(args: Array<String>): Arguments {
    if (args.size !in listOf(2, 4, 6, 8)) {
        exitWithUsage()
    }

    var dataType = "word"
    var sortingType = "natural"
    var inputFile = ""
    var outputFile = ""

    for (arg in args.asList().chunked(2)) {
        val paramKey = arg[0]
        val paramValue = arg[1]

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
            "-inputFile" -> inputFile = paramValue
            "-outputFile" -> outputFile = paramValue
        }
    }

    return Arguments(dataType, sortingType, inputFile, outputFile)
}

fun exitWithUsage() {
    println("usage: <script> -dataType [long|line|word] -sortingType [natural|byCount]")
    exitProcess(1)
}

fun processLines(arguments: Arguments) {
    val lines = mutableListOf<String>()
    val scanner = getInputScanner(arguments)
    while (scanner.hasNextLine()) {
        lines.add(scanner.nextLine())
    }
    scanner.close()

    val outputLines = mutableListOf<String>()
    outputLines.add("Total lines: ${lines.size}.")

    if (arguments.sortingType == "natural") {
        outputLines.add("Sorted data:")
        lines.sorted().forEach { outputLines.add(it) }
    } else if (arguments.sortingType == "byCount") {
        val comparator = compareBy<Map.Entry<String, Int>> { it.value }.thenBy { it.key }

        val frequencies = lines.groupingBy { it }.eachCount()

        frequencies.entries.sortedWith(comparator).forEach {
            val percentage = it.value.toDouble() / lines.size * 100.0
            outputLines.add("${it.key}: ${it.value} time(s), ${percentage.roundToInt()}%")
        }
    }

    writeResult(outputLines, arguments)
}

fun processWords(arguments: Arguments) {
    val words = mutableListOf<String>()
    val scanner = getInputScanner(arguments)
    while (scanner.hasNext()) {
        words.add(scanner.next())
    }
    scanner.close()
    val outputLines = mutableListOf<String>()
    outputLines.add("Total words: ${words.size}.")

    if (arguments.sortingType == "natural") {
        outputLines.add("Sorted data: ${words.sorted().joinToString(" ")}")
    } else if (arguments.sortingType == "byCount") {
        val comparator = compareBy<Map.Entry<String, Int>> { it.value }.thenBy { it.key }

        val frequencies = words.groupingBy { it }.eachCount()

        frequencies.entries.sortedWith(comparator).forEach {
            val percentage = it.value.toDouble() / words.size * 100.0
            outputLines.add("${it.key}: ${it.value} time(s), ${percentage.roundToInt()}%")
        }
    }

    writeResult(outputLines, arguments)
}

fun processNumbers(arguments: Arguments) {
    val numbers = mutableListOf<Long>()
    val scanner = getInputScanner(arguments)
    while (scanner.hasNext()) {
        numbers.add(scanner.nextLong())
    }
    scanner.close()
    val outputLines = mutableListOf<String>()
    outputLines.add("Total numbers: ${numbers.size}.")

    if (arguments.sortingType == "natural") {
        outputLines.add("Sorted data: ${numbers.sorted().joinToString(" ")}")
    } else if (arguments.sortingType == "byCount") {
        val comparator = compareBy<Map.Entry<Long, Int>> { it.value }.thenBy { it.key }

        val frequencies = numbers.groupingBy { it }.eachCount()

        frequencies.entries.sortedWith(comparator).forEach {
            val percentage = it.value.toDouble() / numbers.size * 100.0
            outputLines.add("${it.key}: ${it.value} time(s), ${percentage.roundToInt()}%")
        }
    }

    writeResult(outputLines, arguments)
}

fun getInputScanner(arguments: Arguments): Scanner {
    return if (arguments.inputFile.isNotEmpty())
        Scanner(FileReader(arguments.inputFile))
    else
        Scanner(System.`in`)
}

fun writeResult(outputLines: List<String>, arguments: Arguments) {
    if (arguments.outputFile.isNotEmpty()) {
        File(arguments.outputFile).writeText(outputLines.joinToString("\n"))
    } else {
        outputLines.forEach { println(it) }
    }
}