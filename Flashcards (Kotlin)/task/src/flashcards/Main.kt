package flashcards

import java.io.File
import java.util.NoSuchElementException
import kotlin.random.Random

fun main() {
    val flashCards = mutableMapOf<String, Pair<String, Int>>()
    val logList = mutableListOf<String>()

    val myPrintln : (String) -> Unit = {
        logList.add(it)
        println(it)
    }

    val myRead : () -> String = {
        val input = readln()
        logList.add("> $input")
        input
    }

    do {
        myPrintln("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):")
        val response = myRead()

        when(response) {
            "add" -> add(flashCards, myPrintln, myRead)
            "remove" -> remove(flashCards, myPrintln, myRead)
            "import" -> import(flashCards, myPrintln, myRead)
            "export" -> export(flashCards, myPrintln, myRead)
            "ask" -> ask(flashCards, myPrintln, myRead)
            "log" -> log(logList, myPrintln, myRead)
            "hardest card" -> hardest(flashCards,myPrintln)
            "reset stats" -> resetStats(flashCards, myPrintln)
        }
        myPrintln("") // // add a blank line after each task
    } while (response != "exit")

    myPrintln("Bye bye!")
}

fun log(logList: MutableList<String>, myPrintln: (String) -> Unit, myRead: () -> String) {
    myPrintln("File name:")
    val filename = myRead()
    val file = File(filename)
    myPrintln("The log has been saved.")
    file.writeText(logList.joinToString("\n") { it })
}


fun add(flashCards: MutableMap<String, Pair<String, Int>>,
        myPrintln: (String) -> Unit,
        myRead: () -> String) {
    myPrintln("The card:")
    val term = myRead()
    if (flashCards.contains(term)) myPrintln("The card \"$term\" already exists.")
    else {
        myPrintln("The definition of the card:")
        val definition = myRead()

        if (flashCards
            .values
            .map { it.first }
            .contains(definition)) myPrintln("The definition \"$definition\" already exists.")
        else {
            flashCards[term] = Pair(definition, 0)
            myPrintln("The pair (\"$term\":\"$definition\") has been added.")
        }
    }
}

fun remove(flashCards: MutableMap<String, Pair<String, Int>>,
           myPrintln: (String) -> Unit, myRead: () -> String) {
    myPrintln("Which card?")
    val term = myRead()
    if (!flashCards.contains(term)) myPrintln("can't remove \"$term\": there is no such card.")
    else {
        flashCards.remove(term)
        myPrintln("The card has been removed.")
    }
}

fun import(flashCards: MutableMap<String, Pair<String, Int>>,
           myPrintln: (String) -> Unit, myRead: () -> String) {
    myPrintln("File name:")
    val filename = myRead()
    val file = File(filename)
    if (!file.exists()) myPrintln("File not found.")
    else {
        val fileLines = file
            .readLines()
            .filter { it.isNotEmpty() }
        for (fileLine in fileLines) {
            val (k, v) = fileLine.split("=")
            val (definition, mistakes) = v
                .removePrefix("(")
                .removeSuffix(")")
                .split(", ")
            flashCards[k] = definition to mistakes.toInt()
        }
        myPrintln("${fileLines.size} cards have been loaded.")
    }
}

fun export(flashCards: MutableMap<String, Pair<String, Int>>,
           myPrintln: (String) -> Unit, myRead: () -> String) {
    myPrintln("File name:")
    val filename = myRead()
    val file = File(filename)
    if (flashCards.isNotEmpty()) {
        file.delete()
        file.createNewFile()
    }
    for (card in flashCards) file.appendText("$card\n")
    myPrintln("${flashCards.size} cards have been saved.")
}

fun ask(flashCards: MutableMap<String, Pair<String, Int>>,
        myPrintln: (String) -> Unit, myRead: () -> String) {
    myPrintln("How many times to ask?")
    val questionCount = myRead().toInt()
    repeat(questionCount) {
        val term = flashCards.keys.toList()[Random.nextInt(0, flashCards.size)]
        val definition = flashCards[term]!!.first
        myPrintln("Print the definition of \"$term\":")
        val answer = myRead()
        val otherTerm = getTermForDefinition(answer, flashCards)
        if (answer == definition) {
            myPrintln("Correct!")
        } else if (otherTerm.isNotEmpty()) {
            addMistake(term, flashCards)
            myPrintln("Wrong. The right answer is \"$definition\", but your definition is correct for \"$otherTerm\"")
        } else {
            addMistake(term, flashCards)
            myPrintln("Wrong. The right answer is \"$definition\".")
        }
    }
}

//returns the term for the definition or empty string if there is no term
fun getTermForDefinition(definition: String, cards: MutableMap<String, Pair<String, Int>>): String {
    return try {
        cards
            .filter { it.value.first == definition }
            .keys
            .first()
    } catch (e: NoSuchElementException) { "" }
}

fun addMistake(term: String, flashCards: MutableMap<String, Pair<String, Int>>) {
    val (definition, mistakes) = flashCards[term]!!
    flashCards[term] = definition to mistakes + 1
}

fun resetStats(flashCards: MutableMap<String, Pair<String, Int>>,
               myPrintln: (String) -> Unit) {
    for (card in flashCards) flashCards[card.key] = card.value.first to 0
    myPrintln("Card statistics have been reset.")
}

fun hardest(flashCards: MutableMap<String, Pair<String, Int>>, myPrintln: (String) -> Unit) {
    if (flashCards.isEmpty()) myPrintln("There are no cards with errors.")
    else {
        val mostMistakes = flashCards
            .maxOf { it.value.second }
        val cardsWithMostMistakes = if (mostMistakes == 0) emptySet<String>()
        else flashCards
            .filter { it.value.second == mostMistakes }
            .keys
            .map { "\"$it\"" }
        if (cardsWithMostMistakes.isEmpty()) myPrintln("There are no cards with errors.")
        else if (cardsWithMostMistakes.size > 1) myPrintln(
            "The hardest cards are \"${cardsWithMostMistakes.joinToString(", ")} " +
                    "You have $mostMistakes errors answering them."
        )
        else // must be one card with the highest mistakes.
            myPrintln(
                "The hardest card is ${cardsWithMostMistakes.first()}. " +
                        "You have $mostMistakes errors answering it."
            )
    }
}