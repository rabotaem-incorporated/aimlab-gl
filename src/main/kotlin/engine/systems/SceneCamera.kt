package engine.systems

import engine.Scene
import engine.System
import glm_.glm
import glm_.vec2.Vec2
import glm_.vec3.Vec3
import graphics.Camera
import graphics.Key

open class SceneCamera(val inner: Camera, scene: Scene) : System(scene) {
    fun getForwardRay(): Ray {
        return Ray(inner.position, inner.direction.normalize())
    }
}

class Camera2d(
    scene: Scene,
) : SceneCamera(
    Camera(
        Vec3(),
        0.0f, 0.0f,
        60.0f,
        scene.glfwContext,
    ), scene
) {
    private lateinit var uiManager: UiManager

    override fun onStart() {
        uiManager = scene.query()
    }
}

class DebugCamera(scene: Scene) : SceneCamera(
    Camera(
        Vec3(0f, 0f, 0f),
        0.0f,
        0.0f,
        60f,
        scene.glfwContext,
    ), scene
) {
    override fun beforeTick() {
        val input = scene.tickContext!!.input
        val time = scene.tickContext!!.time

        val speed = 10.0f
        if (input.isKeyPressed(Key.W)) inner.position.plusAssign(inner.direction * speed * time.delta)
        if (input.isKeyPressed(Key.S)) inner.position.plusAssign(-inner.direction * speed * time.delta)
        if (input.isKeyPressed(Key.D)) inner.position.plusAssign(inner.right * speed * time.delta)
        if (input.isKeyPressed(Key.A)) inner.position.plusAssign(-inner.right * speed * time.delta)

        inner.yaw -= input.mouseDelta.x * 0.005f
        inner.pitch -= input.mouseDelta.y * 0.005f
        inner.pitch = glm.clamp(inner.pitch, -glm.radians(89.0f), glm.radians(89.0f))
        input.setMousePosition(Vec2(scene.glfwContext.windowWidth / 2, scene.glfwContext.windowHeight / 2))
    }
}
