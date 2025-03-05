package org.example

fun main(args: Array<String>) {
    val botToken = args[0]
    var updateId = 0
    val idRegex: Regex = "\"update_id\":(.+?),".toRegex()
    val messageTextRegex: Regex = "\"text\":\"(.+?)\"".toRegex()
    val chatIdRegex: Regex = "\"id\":(.+?),".toRegex()
    val messageText = "Hello"
    val telegramBotService = TelegramBotService(botToken)

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
        val chatId = groupsChatId?.get(1)?.value
        println(chatId)
        if (text == messageText)
            if (chatId != null)
                telegramBotService.sendMessage(chatId = chatId, messageText)

    }
}




