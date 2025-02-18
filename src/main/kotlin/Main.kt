package org.example

const val MIN_NUMBER_OF_CORRECT_ANSWERS = 3
const val WORDS_TO_STUDY = 4
fun Question.asConsoleString(): String {
    val variants = this.variants.mapIndexed { index, value ->
        val indexNew = index + 1
        "$indexNew - ${value.translate}"
    }.joinToString(
        separator = "\n"
    )
    return this.correctAnswer.original + "\n" + variants + "\n0 - Выйти в меню"
}

fun main() {
    val trainer = LearnWordsTrainer()
    while (true) {
        val menu = """|Меню:
            |1 – Учить слова
            |2 – Статистика
            |0 – Выход""".trimMargin()
        println(menu)
        val number = readln().toInt()

        if (number !in 0..2) println("Введите число 1, 2 или 0")
        else when (number) {
            1 -> {
                println("Вы выбрали учить слова")
                while (true) {
                    val question = trainer.getNextQuestion()
                    if (question == null) {
                        println("Все слова в словаре выучены")
                        continue
                    } else {
                        println(question.asConsoleString())
                        val userAnswerInput = readln().toInt()
                        if (userAnswerInput == 0) break

                        if (trainer.checkAnswer(userAnswerInput?.minus(1))) {
                            println("Правильно!")
                        } else {
                            println(
                                "Неправильно! ${question.correctAnswer.original} - это " +
                                        question.correctAnswer.translate
                            )
                        }
                    }
                }
            }

            2 -> {
                val statistics = trainer.getStatistics()
                println(
                    "Ваша статистика: Выучено ${statistics.learnedCount} из " +
                            "${statistics.totalCount}  слов | ${statistics.percent}%\n"
                )
            }

            0 -> {
                return
            }
        }
    }
}

