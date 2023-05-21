package aimlab.components

import aimlab.systems.Shooter
import engine.Component
import engine.Entity
import engine.Scene
import engine.components.TextRenderer
import java.text.DecimalFormat

class Timer(entity: Entity, scene: Scene) : Component(entity, scene) {
    private lateinit var shooter: Shooter
    private lateinit var textRenderer: TextRenderer

    override fun onCreate() {
        shooter = scene.query()
        textRenderer = entity.query()!!
    }

    private val format: DecimalFormat = DecimalFormat("0.00");

    override fun onTick() {
        textRenderer.text = "${format.format(shooter.timeLeft)}s left"
    }
}