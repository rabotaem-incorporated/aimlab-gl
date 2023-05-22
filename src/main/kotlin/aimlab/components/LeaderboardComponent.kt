package aimlab.components

import aimlab.aimlabclient.get
import engine.Component
import engine.EPS
import engine.Entity
import engine.Scene
import engine.components.TextRenderer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LeaderboardComponent(entity: Entity, scene: Scene, content: String) : Component(entity, scene) {
    private lateinit var textRenderer: TextRenderer
    private var content = ""

    init {
        this.content = content
    }
    override fun onCreate() {
        textRenderer = entity.query()!!
    }

    override fun onTick() {
        textRenderer.text = content
    }
}