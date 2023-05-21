package engine.systems

import engine.Scene
import engine.System
import glm_.glm
import glm_.vec2.Vec2
import glm_.vec3.Vec3
import glm_.vec4.Vec4
import graphics.Camera
import graphics.InputKey

open class SceneCamera(val inner: Camera, scene: Scene) : System(scene) {
    fun getRay(screenPos: Vec2): Ray {
        val ndc = Vec2(
            (screenPos.x / scene.glfwContext.windowWidth) * 2.0f - 1.0f,
            (screenPos.y / scene.glfwContext.windowHeight) * 2.0f - 1.0f,
        )

        val clip = Vec4(ndc.x, ndc.y, -1.0f, 1.0f)
        val eye = glm.inverse(inner.projectionMatrix) * clip
        val world = glm.inverse(inner.viewMatrix) * eye
        val direction = Vec3(world.x, world.y, world.z).normalize()
        return Ray(inner.position, direction)
    }
}

class DebugCamera(scene: Scene) : SceneCamera(Camera(
    Vec3(0f, 0f, 0f),
    0.0f,
    0.0f,
    60f,
    scene.glfwContext,
), scene) {
    override fun beforeTick() {
        val input = scene.tickContext!!.input
        val time = scene.tickContext!!.time

        val speed = 10.0f
        if (input.isKeyPressed(InputKey.W)) inner.position.plusAssign(inner.direction * speed * time.delta)
        if (input.isKeyPressed(InputKey.S)) inner.position.plusAssign(-inner.direction * speed * time.delta)
        if (input.isKeyPressed(InputKey.D)) inner.position.plusAssign(inner.right * speed * time.delta)
        if (input.isKeyPressed(InputKey.A)) inner.position.plusAssign(-inner.right * speed * time.delta)

        inner.yaw -= input.mouseDelta.x * 0.005f
        inner.pitch -= input.mouseDelta.y * 0.005f
        inner.pitch = glm.clamp(inner.pitch, -glm.radians(89.0f), glm.radians(89.0f))
        input.setMousePosition(Vec2(scene.glfwContext.windowWidth / 2, scene.glfwContext.windowHeight / 2))
    }
}
