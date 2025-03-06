package org.example

import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets

const val API_ADDRESS = "https://api.telegram.org/bot"

class TelegramBotService(private var botToken: String) {
    private var client: HttpClient = HttpClient.newBuilder().build()
    fun getUpdates(updateId: Int): String {
        val urlGetUpdates = "$API_ADDRESS$botToken/getUpdates?offset=$updateId"
        val requestUpdate: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlGetUpdates)).build()
        val responseUpdate: HttpResponse<String> = client.send(requestUpdate, HttpResponse.BodyHandlers.ofString())
        return responseUpdate.body()
    }

    fun sendMessage(chatId: String, text: String) {
        val urlSendMessage =
            "$API_ADDRESS$botToken/sendMessage?chat_id=$chatId&text=${URLEncoder.encode(text, "UTF-8")}"
        val requestSendMessage: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlSendMessage)).build()
        client.send(requestSendMessage, HttpResponse.BodyHandlers.ofString())
    }

    fun sendMenu(chatId: String) {
        val urlSendMessage =
            "$API_ADDRESS$botToken/sendMessage"
        val sendMenuBody = """
            {
            "chat_id":$chatId,
            "text":"Основное меню",
            "reply_markup":{
            "inline_keyboard":[
            [
            {
            "text":"Изучить слова",
            "callback_data":"learn_words_clicked"
            },{
            "text":"Статистика",
            "callback_data":"statistics_clicked"
            }
            ]
            ]
            }
            }
        """.trimIndent()
        val requestSendMessage: HttpRequest =
            HttpRequest.newBuilder().uri(URI.create(urlSendMessage)).header("Content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(sendMenuBody)).build()
        client.send(requestSendMessage, HttpResponse.BodyHandlers.ofString())
    }
}