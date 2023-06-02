package flashcards

fun main() {
    println("Input the number of cards:")
    val cardCount = readln().toInt()
    val flashCards = mutableMapOf<String, String>()

    for (i in 1..cardCount) {
        println("Card #$i:")
        val term = uniqueText("term", flashCards.keys.toList())
        println("The definition for card #$i:")
        val definition = uniqueText("definition", flashCards.values.toList())
        flashCards[term] = definition
    }

    for ((term, definition) in flashCards) {
        println("Print the definition of \"$term\":")
        val answer = readln()
        if (answer == definition) {
            println("Correct!")
        } else if (flashCards.containsValue(answer)) {
            val otherTerm = flashCards.filter { it.value == answer }.keys.first()
            println("Wrong. The right answer is \"$definition\", but your definition is correct for \"$otherTerm\"")
        }
        else {
            println("Wrong. The right answer is \"$definition\".")
        }
    }
}

fun uniqueText(cardType: String, stringList: List<String>): String {
    var item = readln()
    while (stringList.contains(item)) {
        println("The $cardType \"$item\" already exists. Try again:")
        item = readln()
    }
    return item
}