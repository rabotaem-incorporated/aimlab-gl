package aimlab.aimlabclient

import aimlab.aimlabclient.models.Stat
import kotlinx.coroutines.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.time.LocalDateTime

private val client = OkHttpClient()
val url = "http://localhost:8080/"


fun get() : List<Stat>? {
    val request = Request.Builder()
        .url(url)
        .build()

    var res : List<Stat>? = null
    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw IOException("Unexpected code $response")
        res = Json.decodeFromString(response.body!!.string())
    }
    return res
}

fun post(stat: Stat) {
    val mediaType = "application/json; charset=utf-8".toMediaType()

    val requestBody = Json.encodeToString(stat).toRequestBody(mediaType)

    val request = Request.Builder()
        .url(url)
        .post(requestBody)
        .build()

    client.newCall(request).execute().use { response ->
        println("posted ${response.body?.string()}")
    }
}

fun main() = runBlocking {

}
