package aimlab.components

import aimlab.TextAlign
import engine.Component
import engine.Entity
import engine.SolidColorMaterial
import engine.components.UiRenderer
import aimlab.systems.UiManager
import glm_.vec3.Vec3
import glm_.vec4.Vec4
import glm_.vec4.swizzle.xyz
import graphics.BoundingBox

/**
 * Элемент графического интерфейса, на который можно нажать. Меняет цвет при наведении и нажатии.
 *
 * @param text Текст, который будет отображаться на кнопке.
 * @param boundingBox Ограничивающий прямоугольник кнопки в локальных координатах.
 * @param onClick Функция, которая будет вызвана при нажатии на кнопку.
 */
class Button(
    entity: Entity,
    text: String,
    boundingBox: BoundingBox = BoundingBox(
        Vec3(-5f, 0.0f, -0.5f),
        Vec3(5f, 0.0f, 0.5f)
    ),
    horizontalAlign: TextAlign = TextAlign.START,
    verticalAlign: TextAlign = TextAlign.CENTER,
    private val onClick: () -> Unit
) : Component(entity) {
    private lateinit var renderer: UiRenderer
    private lateinit var uiManager: UiManager
    private lateinit var handle: UiManager.ButtonHandle

    private val modelBoundingBox = boundingBox

    val boundingBox get() = BoundingBox(
        (entity.transform.modelToWorldMatrix * Vec4(modelBoundingBox.min, 1.0f)).xyz,
        (entity.transform.modelToWorldMatrix * Vec4(modelBoundingBox.max, 1.0f)).xyz,
    )

    init {
        entity.addComponent(TextRenderer(entity, text, horizontalAlign, verticalAlign))
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