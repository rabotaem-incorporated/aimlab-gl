package engine.components

import aimlab.Resources
import aimlab.TextAlign
import engine.*
import glm_.vec3.Vec3
import graphics.BoundingBox
import graphics.Vao

class TextRenderer(
    entity: Entity,
    scene: Scene,
    text: String,
    private val horizontalAlignment: TextAlign = TextAlign.START,
    private val verticalAlignment: TextAlign = TextAlign.START,
) : Component(entity, scene) {
    private var textVao: Vao? = null
    private val startString = text

    var text: String? = null
        set(value) {
            if (value == null || value == field) return
            textVao?.free()
            textVao = Resources.getText(value, horizontalAlignment, verticalAlignment)
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

