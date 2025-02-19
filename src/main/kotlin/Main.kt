package org.example

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
    val trainer = try {
        LearnWordsTrainer()
    } catch (e: Exception) {
        println("Невозможно загрузить словарь")
        return
    }
    while (true) {
        val menu = """|Меню:
            |1 – Учить слова
            |2 – Статистика
            |0 – Выход""".trimMargin()
        println(menu)
        val number = readln().toIntOrNull()
        if (number !in 0..2 || number == 0) println("Введите число 1, 2 или 0")
        else when (number) {
            1 -> {
                println("Вы выбрали учить слова")
                while (true) {
                    val question = trainer.getNextQuestion()
                    if (question == null) {
                        println("Все слова в словаре выучены")
                        break
                    } else {
                        println(question.asConsoleString())
                        val userAnswerInput = readln().toIntOrNull()
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

