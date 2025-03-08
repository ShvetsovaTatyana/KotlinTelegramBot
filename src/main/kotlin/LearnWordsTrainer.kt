package org.example

import java.io.File

const val MIN_NUMBER_OF_CORRECT_ANSWERS = 3
const val WORDS_TO_STUDY = 4

data class Word(
    val original: String,
    val translate: String,
    var correctAnswersCount: Int = 0
)

data class Statistics(
    val totalCount: Int,
    val learnedCount: Int,
    val percent: Int
)

data class Question(
    val variants: List<Word>,
    val correctAnswer: Word
)

class LearnWordsTrainer {
    private var question: Question? = null
    private val dictionary: MutableList<Word> = loadDictionary()

    fun getStatistics(): Statistics {
        val totalCount = dictionary.size
        val learnedCount = dictionary.filter { it.correctAnswersCount >= MIN_NUMBER_OF_CORRECT_ANSWERS }.size
        val percent = ((learnedCount.toDouble() / totalCount) * 100).toInt()
        return Statistics(totalCount, learnedCount, percent)
    }

    fun getNextQuestion(): Question? {
        val notLearnedList = dictionary.filter { it.correctAnswersCount < MIN_NUMBER_OF_CORRECT_ANSWERS }
        if (notLearnedList.isEmpty()) return null
        val questionWords = if (notLearnedList.size < WORDS_TO_STUDY) {
            val learnedList = dictionary.filter { it.correctAnswersCount >= MIN_NUMBER_OF_CORRECT_ANSWERS }.shuffled()
            notLearnedList.shuffled().take(WORDS_TO_STUDY) +
                    learnedList.take(WORDS_TO_STUDY - notLearnedList.size)
        } else {
            notLearnedList.shuffled().take(WORDS_TO_STUDY)
        }.shuffled()
        val correctAnswer = questionWords.random()
        question = Question(
            variants = questionWords,
            correctAnswer = correctAnswer,
        )
        return question
    }

    fun checkAnswer(userAnswerIndex: Int?): Boolean {
        return question?.let {
            val correctAnswerId =
                it.variants.indexOf(it.correctAnswer)
            if (correctAnswerId == userAnswerIndex) {
                it.correctAnswer.correctAnswersCount++
                saveDictionary(dictionary)
                true
            } else {
                false
            }
        } ?: false
    }

    private fun loadDictionary(): MutableList<Word> {
        try {
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
        } catch (e: IndexOutOfBoundsException) {
            throw IllegalStateException("некорректный файл")
        }
    }

    private fun saveDictionary(dictionary: MutableList<Word>) {
        val file = File("words.txt")
        file.writeText(dictionary.joinToString(separator = "\n") { "${it.original}|${it.translate}|${it.correctAnswersCount}" })
    }


}


