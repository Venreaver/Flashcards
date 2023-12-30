package flashcards

import java.io.File

private var CARDS = mutableMapOf<String, Pair<String, Int>>()
private val LOG = mutableListOf<String>()
private val ACTIONS = mapOf(
    "add" to ::addCard,
    "remove" to ::removeCard,
    "import" to ::importCards,
    "export" to ::exportCards,
    "ask" to ::checkKnowledge,
    "exit" to ::exit,
    "log" to ::log,
    "hardest card" to ::hardestCard,
    "reset stats" to ::resetStats
)
private var exportFileName = ""

fun main(args: Array<String>) {
    processInput(args)
    runActionMenu()
}

fun processInput(input: Array<String>) {
    for (i in input.indices step 2) {
        val command = input[i]
        val fileName = input[i + 1]
        if (command == "-import") {
            importCards(fileName)
        } else {
           exportFileName = fileName
        }
    }
}

private fun runActionMenu() {
    val options = ACTIONS.keys.joinToString(", ", "(", ")")
    var name = ""
    while (name != "exit") {
        log("Input the action $options:")
        name = readAndAddToLog()
        ACTIONS[name]?.invoke()
    }
}

private fun addCard() {
    println("The card:")
    val term = readAndAddToLog()
    if (CARDS[term] != null) {
        log("The card \"$term\" already exists.")
        return
    }
    log("The definition of the card:")
    val definition = readAndAddToLog()
    if (CARDS.values.any { it.first == definition }) {
        log("The definition \"$definition\" already exists.")
        return
    }
    CARDS[term] = Pair(definition, 0)
    log("The pair (\"$term\":\"$definition\") has been added.")
}

private fun removeCard() {
    log("Which card?")
    val term = readAndAddToLog()
    if (CARDS[term] != null) {
        CARDS -= term
        log("The card has been removed.")
    } else {
        log("Can't remove \"$term\": there is no such card.")
    }
}

private fun importCards() {
    log("File name:")
    val fileName = readAndAddToLog()
    importCards(fileName)
}

private fun importCards(fileName: String) {
    val file = File(fileName)
    if (file.exists()) {
        val cards = file.readLines()
        cards.forEach {
            val card = it.split("=")
            CARDS[card[0]] = parsePair(card[1])
        }
        log("${cards.size} cards have been loaded.")
    } else {
        log("File not found.")
    }
}

private fun exportCards() {
    log("File name:")
    val fileName = readAndAddToLog()
    exportCards(fileName)
}

private fun exportCards(fileName: String) {
    val file = File(fileName)
    val content = CARDS.entries.joinToString("\n")
    file.writeText(content)
    log("${CARDS.size} cards have been saved")
}

private fun checkKnowledge() {
    log("How many times to ask?")
    val num = readAndAddToLog().toInt()
    repeat(num) {
        val (term, definition) = CARDS.entries.shuffled().first().toPair()
        log("Print the definition of \"$term\"")
        val answer = readAndAddToLog()
        val result = if (answer == definition.first) {
            "Correct!"
        } else {
            CARDS[term] = definition.copy(second = definition.second + 1)
            val otherTerm = CARDS.filterValues { it.first == answer }.keys.joinToString("")
                    .let { if (it.isEmpty()) "" else ", but your definition is correct for \"$it\"" }
            "Wrong. The right answer is \"${definition.first}\"$otherTerm"
        }
        log(result)
    }
}

private fun exit() {
    if (exportFileName.isNotBlank()) {
        exportCards(exportFileName)
    }
    log("Bye bye!")
}

private fun log() {
    log("File name:")
    val fileName = readAndAddToLog()
    val file = File(fileName)
    val content = LOG.joinToString("\n")
    file.writeText(content)
    log("The log has been saved.")
}

private fun hardestCard() {
    val name: String? = null
    val age: Int? = name?.length
    val result: Int? = age?.plus(3)
    print(result)
    val max = CARDS.values.maxOfOrNull { it.second } ?: 0
    if (max != 0) {
        val hardestCards = CARDS.entries.filter { it.value.second == max }.map { it.key to it.value.second }
        when (hardestCards.size) {
            1 -> log("The hardest card is \"${hardestCards.first().first}\". You have $max errors answering it.")
            else -> log("The hardest cards are ${
                hardestCards.joinToString(
                    "\", \"", "\"", "\""
                ) { it.first }
            }. You have $max errors answering them.")
        }
    } else {
        log("There are no cards with errors.")
    }
}

private fun resetStats() {
    CARDS.mapValues { it.value.copy(second = 0) }.toMap(CARDS)
    log("Card statistics have been reset.")
}

private fun log(message: String) {
    LOG.add(message)
    println(LOG.last())
}

private fun readAndAddToLog(): String {
    LOG.add(readln())
    return LOG.last()
}

private fun parsePair(definition: String) =
    definition.removePrefix("(").removeSuffix(")").split(", ").let { it[0] to it[1].toInt() }
