package aimlab.components

import aimlab.aimlabclient.get
import engine.Component
import engine.Entity
import engine.Scene
import engine.components.TextRenderer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class Leaderboard(entity: Entity, scene: Scene) : Component(entity, scene) {
    private lateinit var textRenderer: TextRenderer

    private var content = "Loading..."
    private val mutex = Mutex()

    override fun onCreate() {
        textRenderer = entity.query()!!

        runBlocking {
            launch(Dispatchers.Unconfined) {
                val stats = get()!!



                val text = buildString {
                    for (stat in stats.sortedByDescending { it.score }.take(10)) {
                        appendLine("${stat.id}: ${stat.score.toInt()}")
                    }
                }

                mutex.withLock {
                    content = text
                }
            }
        }
    }

    override fun onTick() {

        runBlocking {
            mutex.withLock {
                textRenderer.text = content
            }
        }
    }
}