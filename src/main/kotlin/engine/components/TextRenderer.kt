package engine.components

import aimlab.Resources
import aimlab.TextAlign
import engine.*
import graphics.Vao

class TextRenderer(
    entity: Entity,
    scene: Scene,
    text: String,
    private val horizontalAlign: TextAlign = TextAlign.START,
    private val verticalAlign: TextAlign = TextAlign.START,
) : Component(entity, scene) {
    private var textVao: Vao? = null
    private val startString = text

    var text: String? = null
        set(value) {
            if (value == null || value == field) return
            textVao?.free()
            textVao = Resources.getText(value, horizontalAlign, verticalAlign)
            if (uiRenderer != null) {
                uiRenderer!!.model = textVao!!
            }

            field = value
        }

    private var uiRenderer: UiRenderer? = null

    override fun onCreate() {
        this.text = startString

        uiRenderer = entity.query()

        if (uiRenderer == null) {
            uiRenderer = entity.addComponent(UiRenderer(entity, scene, textVao!!))
        }

        entity.transform.scale = 0.1f
    }

    override fun onDestroy() {
        textVao?.free()
    }
}

