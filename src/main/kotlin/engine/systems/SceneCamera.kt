package engine.systems

import aimlab.systems.UiManager
import engine.Scene
import engine.System
import glm_.glm
import glm_.vec2.Vec2
import glm_.vec3.Vec3
import graphics.Camera
import graphics.Key

/**
 * Расширяемый класс камеры, который можно использовать в сцене.
 *
 * Можно вешать дополнительную логику, в, например, [beforeTick].
 *
 * Желательно обрабатывать события до вызова [RenderPipeline.afterTick].
 */
open class SceneCamera(val inner: Camera, scene: Scene) : System(scene) {
    fun getForwardRay(): Ray {
        return Ray(inner.position, inner.direction.normalize())
    }
}

/**
 * Фиксированная камера, полезна для сцен, где есть только UI.
 */
class Camera2d(
    scene: Scene,
) : SceneCamera(
    Camera(
        Vec3(),
        0.0f, 0.0f,
        60.0f,
        scene.game.glfwContext,
    ), scene
) {
    private lateinit var uiManager: UiManager

    override fun onStart() {
        uiManager = scene.query()
    }
}

/**
 * Отладочная камера, которая может перемещаться и вращаться.
 *
 * Полезна для тестирования сцен.
 */
class DebugCamera(scene: Scene) : SceneCamera(
    Camera(
        Vec3(0f, 0f, 0f),
        0.0f,
        0.0f,
        60f,
        scene.game.glfwContext,
    ), scene
) {
    override fun beforeTick() {
        val speed = 10.0f
        if (input.isKeyPressed(Key.W)) inner.position.plusAssign(inner.direction * speed * time.delta)
        if (input.isKeyPressed(Key.S)) inner.position.plusAssign(-inner.direction * speed * time.delta)
        if (input.isKeyPressed(Key.D)) inner.position.plusAssign(inner.right * speed * time.delta)
        if (input.isKeyPressed(Key.A)) inner.position.plusAssign(-inner.right * speed * time.delta)

        inner.yaw -= input.mouseDelta.x * 0.005f
        inner.pitch -= input.mouseDelta.y * 0.005f
        inner.pitch = glm.clamp(inner.pitch, -glm.radians(89.0f), glm.radians(89.0f))
        input.setMousePosition(Vec2(game.glfwContext.windowWidth / 2, game.glfwContext.windowHeight / 2))
    }
}
