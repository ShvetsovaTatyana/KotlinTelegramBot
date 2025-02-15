package org.example

import java.io.File

const val MIN_NUMBER_OF_CORRECT_ANSWERS = 3
const val WORDS_TO_STUDY = 4

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
                    val notLearnedList = dictionary.filter { it.correctAnswersCount < 3 }
                    if (notLearnedList.isEmpty()) {
                        println("Все слова в словаре выучены")
                        continue
                    } else {
                        val questionWords = notLearnedList.shuffled().take(WORDS_TO_STUDY)
                        val correctAnswer = questionWords.random()
                        while (notLearnedList.isNotEmpty()) {
                            println(
                                questionWords.mapIndexed { index, value ->
                                    val indexNew = index + 1
                                    "$indexNew - ${value.translate}"
                                }.joinToString(
                                    separator = "\n",
                                    prefix = "${correctAnswer.original}:\n",
                                    postfix = ("|\n----------" +
                                            "\n|0 - Меню").trimMargin()
                                )
                            )
                            val userAnswerInput = readln().toInt()
                            if (userAnswerInput == 0) {
                                break
                            } else {
                                val correctAnswerId =
                                    questionWords.indexOfFirst { it.translate == correctAnswer.translate } + 1
                                if (userAnswerInput == correctAnswerId) {
                                    println("Правильно!")
                                    correctAnswer.correctAnswersCount += 1
                                    saveDictionary(dictionary)
                                } else
                                    println("Неправильно! ${correctAnswer.original} - это ${correctAnswer.translate}")
                            }
                        }
                    }
                }

                2 -> {
                    val totalCount = dictionary.size.toDouble()
                    val learnedCount =
                        dictionary.filter { it.correctAnswersCount >= MIN_NUMBER_OF_CORRECT_ANSWERS }.size
                    val percent = ((learnedCount / totalCount) * 100).toInt()
                    println("Ваша статистика: Выучено $learnedCount из ${totalCount.toInt()}  слов | $percent%\n")
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

fun saveDictionary(dictionary: MutableList<Word>) {
    val file = File("words.txt")
    file.writeText(dictionary.joinToString(separator = "\n") { "${it.original}|${it.translate}|${it.correctAnswersCount}" })
}