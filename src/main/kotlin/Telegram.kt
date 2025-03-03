package org.example

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

fun main(args: Array<String>) {
    val botToken = args[0]
    var updateId = 0
    val idRegex: Regex = "\"update_id\":(.+?),".toRegex()

    while (true) {
        Thread.sleep(2000)
        val updates: String = getUpdates(botToken, updateId)
        println(updates)

        val matchResultId = idRegex.findAll(updates)
        val matchResultIdLast = matchResultId.lastOrNull()
        val groupsId = matchResultIdLast?.groups
        val id = groupsId?.get(1)?.value
        updateId = id?.toInt()?.plus(1) ?: continue
        println(id)

        val messageTextRegex: Regex = "\"text\":\"(.+?)\"".toRegex()
        val matchResult = messageTextRegex.findAll(updates)
        val matchResultLast = matchResult.lastOrNull()
        val groups = matchResultLast?.groups
        val text = groups?.get(1)?.value
        println(text)
    }
}

fun getUpdates(botToken: String, updateId: Int): String {
    val urlGetUpdates = "https://api.telegram.org/bot$botToken/getUpdates?offset=$updateId"
    val client: HttpClient = HttpClient.newBuilder().build()
    val requestUpdate: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlGetUpdates)).build()
    val responseUpdate: HttpResponse<String> = client.send(requestUpdate, HttpResponse.BodyHandlers.ofString())
    return responseUpdate.body()
}
