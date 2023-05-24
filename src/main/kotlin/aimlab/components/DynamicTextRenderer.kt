package aimlab.components

import aimlab.TextAlign
import engine.Component
import engine.Entity

/**
 * Вариация [TextRenderer], которая обновляет текст каждый тик.
 *
 * Реализовано как компонент, добавляющий [TextRenderer] к сущности.
 */
class DynamicTextRenderer(
    entity: Entity,
    private val text: () -> String,
    private val horizontalAlign: TextAlign = TextAlign.START,
    private val verticalAlign: TextAlign = TextAlign.START,
) : Component(entity) {
    private lateinit var textRenderer: TextRenderer

    override fun onCreate() {
        textRenderer = entity.addComponent(TextRenderer(entity, "", horizontalAlign, verticalAlign))
    }

    override fun onTick() {
        textRenderer.text = text()
    }
}
