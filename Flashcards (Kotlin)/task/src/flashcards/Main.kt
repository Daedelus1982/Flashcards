package flashcards

import java.io.File
import java.util.NoSuchElementException
import kotlin.random.Random

fun main() {
    val flashCards = mutableMapOf<String, Pair<String, Int>>()
    do {
        println("Input the action (add, remove, import, export, ask, exit):")
        val response = readln()

        when(response) {
            "add" -> add(flashCards)
            "remove" -> remove(flashCards)
            "import" -> import(flashCards)
            "export" -> export(flashCards)
            "ask" -> ask(flashCards)
        }
        println() // add a blank line after each task
    } while (response != "exit")

    println("Bye bye!")
}

fun add(flashCards: MutableMap<String, Pair<String, Int>>) {
    println("The card:")
    val term = readln()
    if (flashCards.contains(term)) println("The card \"$term\" already exists.")
    else {
        println("The definition of the card:")
        val definition = readln()
        if (flashCards
            .values
            .map { it.first }
            .contains(definition)) print("The definition \"$definition\" already exists.")
        else {
            flashCards[term] = Pair(definition, 0)
            println("The pair (\"$term\":\"$definition\") has been added.")
        }
    }
}

fun remove(flashCards: MutableMap<String, Pair<String, Int>>) {
    println("Which card?")
    val term = readln()
    if (!flashCards.contains(term)) println("can't remove \"$term\": there is no such card.")
    else {
        flashCards.remove(term)
        println("The card has been removed.")
    }
}

fun import(flashCards: MutableMap<String, Pair<String, Int>>) {
    println("File name:")
    val filename = readln()
    val file = File(filename)
    if (!file.exists()) println("File not found.")
    else {
        val fileLines = file.readLines().filter { it.isNotEmpty() }
        for (fileLine in fileLines) {
            val (k, v) = fileLine.split("=")
            val (definition, mistakes) = v.removePrefix("(").removeSuffix(")").split(", ")
            flashCards[k] = definition to mistakes.toInt()
        }
        println("${fileLines.size} cards have been loaded.")
    }
}

fun export(flashCards: MutableMap<String, Pair<String, Int>>) {
    println("File name:")
    val filename = readln()
    val file = File(filename)
    if (flashCards.isNotEmpty()) {
        file.delete()
        file.createNewFile()
    }
    for (card in flashCards) file.appendText("$card\n")
    println("${flashCards.size} cards have been saved.")
}

fun ask(flashCards: MutableMap<String, Pair<String, Int>>) {
    println("How many times to ask?")
    val questionCount = readln().toInt()
    repeat(questionCount) {
        val term = flashCards.keys.toList()[Random.nextInt(0, flashCards.size)]
        val definition = flashCards[term]!!.first
        println("Print the definition of \"$term\":")
        val answer = readln()
        val otherTerm = getTermForDefinition(answer, flashCards)
        if (answer == definition) {
            println("Correct!")
        } else if (otherTerm.isNotEmpty()) {
            addMistake(term, flashCards)
            println("Wrong. The right answer is \"$definition\", but your definition is correct for \"$otherTerm\"")
        } else {
            addMistake(term, flashCards)
            println("Wrong. The right answer is \"$definition\".")
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