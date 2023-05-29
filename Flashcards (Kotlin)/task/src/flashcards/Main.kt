package flashcards

fun main() {
    val definition = createCard(readln(), readln())

    val answer = readln()

    val rightOrWrong = checkAnswer(definition, answer)

    println("Your answer is $rightOrWrong")
}

fun createCard(question: String, definition: String): String = definition

fun checkAnswer(definition: String, answer: String) = if (definition == answer) "right" else "wrong"