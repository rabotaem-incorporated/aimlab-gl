package aimlab.components

import aimlab.systems.Shooter
import engine.Component
import engine.Entity
import engine.Scene
import engine.components.TextRenderer

class ScoreCounter(entity: Entity, scene: Scene) : Component(entity, scene) {
    private lateinit var shooter: Shooter
    private lateinit var textRenderer: TextRenderer

    override fun onCreate() {
        shooter = scene.query()
        textRenderer = entity.query()!!
    }

    override fun onTick() {
        textRenderer.text = "Score: ${shooter.score}"
    }
}