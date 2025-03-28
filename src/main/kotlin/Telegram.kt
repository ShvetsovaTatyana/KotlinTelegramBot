package org.example

fun main(args: Array<String>) {
    val botToken = args[0]
    var updateId = 0
    val idRegex: Regex = "\"update_id\":(.+?),".toRegex()
    val messageTextRegex: Regex = "\"text\":\"(.+?)\"".toRegex()
    val chatIdRegex: Regex = "\"id\":(.+?),".toRegex()
    val dataRegex: Regex = "\"data\":\"(.+?)\"".toRegex()
    val messageText = ""
    val telegramBotService = TelegramBotService(botToken)
    val trainer = LearnWordsTrainer()

    while (true) {
        Thread.sleep(2000)
        val updates = telegramBotService.getUpdates(updateId)
        println(updates)

        val matchResultId = idRegex.findAll(updates)
        val matchResultIdLast = matchResultId.lastOrNull()
        val groupsId = matchResultIdLast?.groups
        val id = groupsId?.get(1)?.value
        updateId = id?.toIntOrNull()?.plus(1) ?: continue
        println(id)

        val matchResult = messageTextRegex.findAll(updates)
        val matchResultLast = matchResult.lastOrNull()
        val groups = matchResultLast?.groups
        val text = groups?.get(1)?.value
        println(text)

        val matchResultChatId = chatIdRegex.findAll(updates)
        val matchResultChatIdLast = matchResultChatId.lastOrNull()
        val groupsChatId = matchResultChatIdLast?.groups
        val chatId = groupsChatId?.get(1)?.value?.toLongOrNull() ?: continue
        println(chatId)

        val matchResultData = dataRegex.findAll(updates)
        val matchResultDataLast = matchResultData.lastOrNull()
        val groupsData = matchResultDataLast?.groups
        val data = groupsData?.get(1)?.value
        if (text == "Hello") telegramBotService.sendMessage(chatId = chatId, messageText)

        if (text == "/start") telegramBotService.sendMenu(chatId = chatId)

        if (data == STATISTICS_CLICKED) {
            val resultStatistics = trainer.getStatistics()
            telegramBotService.sendMessage(
                chatId = chatId,
                "Выучено: ${resultStatistics.learnedCount} из ${resultStatistics.totalCount} слов | " +
                        "${resultStatistics.percent}%"
            )
        }
        if (data == LEARN_WORDS_CLICKED) {
            checkNextQuestionAndSend(trainer, telegramBotService, chatId)
        }
        if (data?.startsWith(CALLBACK_DATA_ANSWER_PREFIX) == true) {
            val dataToInt = data.substringAfter(CALLBACK_DATA_ANSWER_PREFIX).toInt()
            if (trainer.checkAnswer(userAnswerIndex = dataToInt))
                telegramBotService.sendMessage(chatId, "Правильно!")
            else {
                telegramBotService.sendMessage(
                    chatId,
                    "Неправильно! ${trainer.question?.correctAnswer?.original} - это ${
                        trainer.question?.correctAnswer?.translate
                    }"
                )
            }
            checkNextQuestionAndSend(trainer, telegramBotService, chatId)
        }
    }
}

fun checkNextQuestionAndSend(
    trainer: LearnWordsTrainer,
    telegramBotService: TelegramBotService,
    chatId: Long
) {
    val question = trainer.getNextQuestion()
    if (question == null) {
        telegramBotService.sendMessage(chatId, "Все слова в словаре выучены")
        return
    }
    telegramBotService.sendQuestion(chatId = chatId, question)
}
