package aimlab.systems

import aimlab.Settings
import engine.Scene
import engine.systems.SceneCamera
import glm_.glm
import glm_.vec2.Vec2
import glm_.vec3.Vec3
import graphics.Camera

class FpsCamera(scene: Scene) : SceneCamera(
    Camera(
        Vec3(0.0f, 0.0f, 0.0f),
        0.0f, 0.0f,
        60.0f,
        scene.game.glfwContext,
    ),
    scene,
) {
    override fun beforeTick() {
        val input = input

        inner.yaw -= input.mouseDelta.x * Settings.sensitivity.x
        inner.pitch -= input.mouseDelta.y * Settings.sensitivity.y
        inner.pitch = glm.clamp(inner.pitch, -glm.radians(45.0f), glm.radians(45.0f))
        inner.yaw = glm.clamp(inner.yaw, -glm.radians(45.0f), glm.radians(45.0f))
        input.setMousePosition(Vec2(game.glfwContext.windowWidth / 2, game.glfwContext.windowHeight / 2))
    }
}