package aimlab.scenes

import aimlab.Settings
import aimlab.TextAlign
import engine.Scene
import engine.components.Button
import engine.components.DynamicTextRenderer
import engine.components.TextRenderer
import engine.systems.Camera2d
import engine.systems.ExitOnEscape
import engine.systems.RenderPipeline
import engine.systems.UiManager
import glm_.vec3.Vec3
import graphics.BoundingBox
import graphics.GlfwContext

fun createSettingsScene(glfwContext: GlfwContext): Scene {
    val scene = Scene(glfwContext)

    scene.systems.add(RenderPipeline(scene))
    scene.systems.add(ExitOnEscape(scene))
    scene.systems.add(Camera2d(scene))
    scene.systems.add(UiManager(scene))

    scene.create {
        addComponent(TextRenderer(this, scene, "Settings", horizontalAlign = TextAlign.CENTER))
        transform.position = Vec3(0.0f, 0.0f, 0.8f)
        transform.scale = 0.2f
    }

    scene.create {
        addComponent(TextRenderer(this, scene, "Sensitivity", horizontalAlign = TextAlign.END))
        transform.position = Vec3(-0.1f, 0.0f, 0.6f)
    }

    scene.create {
        addComponent(Button(this, scene, "+", horizontalAlign = TextAlign.END, boundingBox = BoundingBox(
            Vec3(-1f, 0.0f, -1f),
            Vec3(1f, 0.0f, 1f)
        )) {
            Settings.sensitivity.timesAssign(1.1f)
        })

        transform.position = Vec3(0.1f, 0.0f, 0.57f)
    }

    scene.create {
        addComponent(DynamicTextRenderer(this, scene, {
            val decimalFormat = java.text.DecimalFormat("0.0")
            decimalFormat.format(Settings.sensitivity.x * 1000)
        }, horizontalAlign = TextAlign.CENTER))

        transform.position = Vec3(0.25f, 0.0f, 0.6f)
    }

    scene.create {
        addComponent(Button(this, scene, "-", horizontalAlign = TextAlign.START, boundingBox = BoundingBox(
            Vec3(-1f, 0.0f, -1f),
            Vec3(1f, 0.0f, 1f)
        )) {
            Settings.sensitivity.divAssign(1.1f)
        })

        transform.position = Vec3(0.4f, 0.0f, 0.57f)
    }

    scene.create {
        addComponent(Button(
            this, scene, "Back",
            horizontalAlign = TextAlign.CENTER, onClick = {
                scene.tickContext!!.sceneManager.scene = createMainMenu(glfwContext)
            }
        ))

        transform.position = Vec3(-1.0f, 0.0f, -0.9f)
    }

    scene.create {
        addComponent(Button(
            this, scene, "Toggle Fullscreen",
            horizontalAlign = TextAlign.CENTER, onClick = {
                glfwContext.fullscreen = !glfwContext.fullscreen
            }
        ))

        transform.position = Vec3(0.0f, 0.0f, -0.8f)
    }

    return scene
}
