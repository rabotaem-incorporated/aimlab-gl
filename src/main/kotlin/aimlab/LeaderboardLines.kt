package aimlab

import aimlab.aimlabclient.get
import engine.EPS
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LeaderboardLines {
    var isError = false
    var lineList: List<String> = List(5) {""}

    init {
        runBlocking {
            launch(newSingleThreadContext("thread")) {
                try {
                    val stats = get()!!
                    val decimalFormat = DecimalFormat("0.00")
                    val dateFormat = DateTimeFormatter.ofPattern("hh:mm yyyy/MM/dd")
                    val orderedStats = stats.sortedByDescending { it.score }.take(10)
                    val list = MutableList(5) { "" }

                    list[0] = buildString {
                        for (stat in orderedStats)
                            appendLine(stat.username)
                    }

                    list[1] = buildString {
                        for (stat in orderedStats)
                            appendLine((stat.score + EPS).toInt())
                    }

                    list[2] = buildString {
                        for (stat in orderedStats)
                            appendLine(stat.combo)
                    }

                    list[3] = buildString {
                        for (stat in orderedStats)
                            appendLine(decimalFormat.format(stat.accuracy))
                    }

                    list[4] = buildString {
                        for (stat in orderedStats)
                            appendLine(dateFormat.format(LocalDateTime.parse(stat.datetime)))
                    }

                    lineList = list

                } catch (e: Exception) {
                    lineList = listOf("Error: ${e.message}")
                    isError = true
                    return@launch
                }
            }
        }
    }

}