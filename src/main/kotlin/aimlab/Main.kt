package aimlab

import engine.Scene
import engine.components.Renderer
import engine.components.TexturedMaterial
import engine.launchGame
import engine.systems.CollisionSystem
import engine.systems.ExitOnEscape
import engine.systems.RenderPipeline
import glm_.quat.Quat
import glm_.vec3.Vec3

fun main() = launchGame {
    Resources.load(this)

    val scene = Scene(glfwContext)
    sceneManager.scene = scene

    scene.systems.add(RenderPipeline(scene))
    scene.systems.add(ExitOnEscape(scene))
    scene.systems.add(CollisionSystem(scene))
    scene.systems.add(engine.systems.DebugCamera(scene))
    // scene.systems.add(FpsCamera(scene))
    scene.systems.add(Shooter(scene))

    scene.create {
        addComponent(BallSpawner(scene, this))

        transform.position = Vec3(0.0f, 0.0f, 10.0f)
    }

    scene.create {
        addComponent(Renderer(this, scene, TexturedMaterial(Resources.pepega), Resources.quad))
        transform.position = Vec3(0.0f, 0.0f, 20.0f)
        transform.rotation = Quat.quatLookAt(Vec3(0.0, -1.0, 0.0f), Vec3(0.0f, 0.0f, 1.0f))
        transform.scale = 10.0f
    }
}
