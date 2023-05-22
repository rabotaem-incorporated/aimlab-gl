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
        transform.scale = 0.1f
    }

    scene.create {
        addComponent(Button(this, scene, "+", horizontalAlign = TextAlign.END) {
            Settings.sensitivity.timesAssign(1.1f)
        })

        transform.position = Vec3(0.1f, 0.0f, 0.6f)
        transform.scale = 0.1f
    }

    scene.create {
        addComponent(DynamicTextRenderer(this, scene, {
            Settings.sensitivity.x.toString()
        }, horizontalAlign = TextAlign.CENTER))

        transform.position = Vec3(0.0f, 0.0f, 0.5f)
        transform.scale = 0.1f
    }

    scene.create {
        addComponent(Button(this, scene, "-", horizontalAlign = TextAlign.END) {
            Settings.sensitivity.divAssign(1.1f)
        })

        transform.position = Vec3(0.4f, 0.0f, 0.6f)
        transform.scale = 0.1f
    }

    return scene
}
