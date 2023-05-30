package flashcards

fun main() {
    println("Input the number of cards:")
    val cardCount = readln().toInt()
    val questions = mutableListOf<String>()
    val definitions = mutableListOf<String>()

    for (i in 0 until cardCount) { createCard(i, questions, definitions)}

    for (i in 0 until cardCount) { println(checkAnswer(i, questions, definitions)) }
}

fun createCard(index: Int, questions: MutableList<String>, definitions: MutableList<String>) {
    println("Card #${index + 1}")
    val question = readln()
    println("The definition for card #${index + 1}")
    val definition = readln()
    questions.add(question)
    definitions.add(definition)
}

fun checkAnswer(index: Int, questions: MutableList<String>, definitions: MutableList<String>): String {
    println("Print the definition of \"${questions[index]}\"")
    val answer = readln()
    return if (answer == definitions[index]) "Correct!" else "Wrong. The right answer is \"${definitions[index]}\"."
}