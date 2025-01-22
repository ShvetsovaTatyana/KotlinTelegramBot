package org.example

import java.io.File

fun main() {
    val dictionary: MutableList<Word> = loadDictionary()
    while (true) {
        val menu = """|Меню:
            |1 – Учить слова
            |2 – Статистика
            |0 – Выход""".trimMargin()
        println(menu)
        val number = readln().toInt()
        if (number < 0 || number > 2)
            println("Введите число 1, 2 или 0")
        else
            when (number) {
                1 -> println("Вы выбрали учить слова")
                2 -> println("Ваша статистика:")
                0 -> return
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