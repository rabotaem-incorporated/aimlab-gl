package aimlab.components

import engine.Component
import engine.Entity

class LeaderboardComponent(entity: Entity, content: String) : Component(entity) {
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