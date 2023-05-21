package engine.components

import aimlab.TextAlign
import engine.Component
import engine.Entity
import engine.Scene
import engine.SolidColorMaterial
import engine.systems.UiManager
import glm_.vec3.Vec3
import glm_.vec4.Vec4
import glm_.vec4.swizzle.xyz
import graphics.BoundingBox

class Button(
    entity: Entity,
    scene: Scene,
    text: String,
    boundingBox: BoundingBox = BoundingBox(
        Vec3(-5f, 0.0f, -0.5f),
        Vec3(5f, 0.0f, 0.5f)
    ),
    horizontalAlign: TextAlign = TextAlign.START,
    verticalAlign: TextAlign = TextAlign.CENTER,
    private val onClick: () -> Unit
) : Component(entity, scene) {
    private lateinit var renderer: UiRenderer
    private lateinit var uiManager: UiManager
    private lateinit var handle: UiManager.ButtonHandle

    private val modelBoundingBox = boundingBox

    val boundingBox get() = BoundingBox(
        (entity.transform.modelToWorldMatrix * Vec4(modelBoundingBox.min, 1.0f)).xyz,
        (entity.transform.modelToWorldMatrix * Vec4(modelBoundingBox.max, 1.0f)).xyz,
    )

    init {
        entity.addComponent(TextRenderer(entity, scene, text, horizontalAlign, verticalAlign))
    }

    override fun onCreate() {
        renderer = entity.query()!!
        uiManager = scene.query()
        handle = uiManager.register(this)
        state = State.DEFAULT
    }

    override fun onDestroy() {
        uiManager.unregister(handle)
    }

    companion object {
        val defaultMaterial = SolidColorMaterial(Vec3(0.6f, 0.8f, 0.5f))
        val highlightedMaterial = SolidColorMaterial(Vec3(1.0f, 1.0f, 0.0f))
        val pressedMaterial = SolidColorMaterial(Vec3(1.0f, 0.5f, 0.0f))
    }

    enum class State {
        DEFAULT,
        HIGHLIGHTED,
        PRESSED,
    }

    var state = State.DEFAULT
        set(value) {
            field = value
            renderer.material = when (value) {
                State.DEFAULT -> defaultMaterial
                State.HIGHLIGHTED -> highlightedMaterial
                State.PRESSED -> pressedMaterial
            }
        }

    fun click() {
        onClick()
    }
}