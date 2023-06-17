package flashcards

fun main(args: Array<String>) {
    val flashCards = Cards()
    val importFile: String? = if (args.contains("-import")) args[args.indexOf("-import") + 1] else null
    val exportFile: String? = if (args.contains("-export")) args[args.indexOf("-export") + 1] else null

    if (!importFile.isNullOrEmpty()) flashCards.import(importFile)

    do {
        flashCards.myPrintln("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):")
        val response = flashCards.myRead()

        when(response) {
            "add" -> flashCards.add()
            "remove" -> flashCards.remove()
            "import" -> flashCards.import(null)
            "export" -> flashCards.export(null)
            "ask" -> flashCards.ask()
            "log" -> flashCards.log()
            "hardest card" -> flashCards.hardest()
            "reset stats" -> flashCards.resetStats()
        }
        flashCards.myPrintln("") // // add a blank line after each task
    } while (response != "exit")

    if (!exportFile.isNullOrEmpty()) flashCards.export(exportFile)

    flashCards.myPrintln("Bye bye!")
}