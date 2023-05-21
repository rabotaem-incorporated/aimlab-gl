package aimlab.scenes

import aimlab.TextAlign
import engine.Scene
import engine.components.Button
import engine.components.TextRenderer
import engine.components.UiRenderer
import engine.systems.Camera2d
import engine.systems.ExitOnEscape
import engine.systems.RenderPipeline
import engine.systems.UiManager
import glm_.vec3.Vec3
import graphics.BoundingBox
import graphics.GlfwContext

fun createMainMenu(glfwContext: GlfwContext): Scene {
    val scene = Scene(glfwContext)

    scene.systems.add(RenderPipeline(scene))
    scene.systems.add(ExitOnEscape(scene))
    scene.systems.add(Camera2d(scene))
    scene.systems.add(UiManager(scene))

    scene.create {
        addComponent(TextRenderer(this, scene, "\"Aimlab\"", horizontalAlignment = TextAlign.CENTER))
        transform.position = Vec3(0.0f, 0.0f, 0.5f)
        transform.scale = 0.2f
    }

    scene.create {
        addComponent(Button(
            this, scene, "Play",
            horizontalAlign = TextAlign.CENTER, onClick = {
                scene.tickContext!!.sceneManager.scene = createGameScene(glfwContext)
            }
        ))
    }

    return scene
}
