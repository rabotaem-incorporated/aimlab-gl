package engine.components

import aimlab.TextAlign
import engine.Component
import engine.Entity
import engine.Scene

class DynamicTextRenderer(
    entity: Entity,
    scene: Scene,
    private val text: () -> String,
    private val horizontalAlign: TextAlign = TextAlign.START,
    private val verticalAlign: TextAlign = TextAlign.START,
) : Component(entity, scene) {
    private lateinit var textRenderer: TextRenderer

    override fun onCreate() {
        textRenderer = entity.addComponent(TextRenderer(entity, scene, "", horizontalAlign, verticalAlign))
    }

    override fun onTick() {
        textRenderer.text = text()
    }
}
