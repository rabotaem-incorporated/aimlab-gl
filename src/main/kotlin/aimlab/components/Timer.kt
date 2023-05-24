package aimlab.components

import aimlab.systems.Shooter
import engine.Component
import engine.Entity
import java.text.DecimalFormat

class Timer(entity: Entity) : Component(entity) {
    private lateinit var shooter: Shooter
    private lateinit var textRenderer: TextRenderer

    override fun onCreate() {
        shooter = scene.query()
        textRenderer = entity.query()!!
    }

    private val format: DecimalFormat = DecimalFormat("0.00")

    override fun onTick() {
        textRenderer.text = "${format.format(shooter.timeLeft)}s left"
    }
}