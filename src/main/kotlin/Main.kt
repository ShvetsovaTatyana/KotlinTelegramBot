package org.example

import java.io.File

const val MIN_NUMBER_OF_CORRECT_ANSWERS = 3

fun main() {
    val dictionary: MutableList<Word> = loadDictionary()
    while (true) {
        val menu = """|Меню:
            |1 – Учить слова
            |2 – Статистика
            |0 – Выход""".trimMargin()
        println(menu)
        val number = readln().toInt()

        if (number !in 0..2)
            println("Введите число 1, 2 или 0")
        else
            when (number) {
                1 -> {
                    println("Вы выбрали учить слова")
                    val notLearnedList = dictionary.map { it.original }
                    val questionWords = notLearnedList.shuffled().take(4)
                    val correctAnswer = questionWords.take(1)
                    if (notLearnedList.isEmpty()) {
                        println("Все слова в словаре выучены")
                        continue
                    } else {

                        while (notLearnedList.isNotEmpty()) {
                            println(
                                """|${correctAnswer[0]}:
                                    |1 - ${dictionary[0].translate}
                                    |2 - ${dictionary[1].translate}
                                    |3 - ${dictionary[2].translate}
                                    |4 - ${dictionary[3].translate}"""
                                    .trimMargin()
                            )
                            val userAnswerInput = readln().toInt()
                        }
                    }
                }

                2 -> {
                    val totalCount = dictionary.size.toDouble()
                    val learnedCount =
                        dictionary.filter { it.correctAnswersCount >= MIN_NUMBER_OF_CORRECT_ANSWERS }.size
                    val percent = ((learnedCount / totalCount) * 100).toInt()
                    println("Ваша статистика: Выучено ${learnedCount} из ${totalCount.toInt()}  слов | $percent%\n")
                }

                0 -> {
                    return
                }
            }
    }
}

fun loadDictionary(): MutableList<Word> {
    val file = File("words.txt")
    val listOfStrings = file.readLines()
    val dictionary: MutableList<Word> = mutableListOf()
    listOfStrings.forEach {
        val line = it.split("|")
        val word =
            Word(
                original = line[0],
                translate = line[1],
                correctAnswersCount = line.getOrNull(2)?.toIntOrNull() ?: 0
            )
        dictionary.add(word)
    }
    return dictionary
}