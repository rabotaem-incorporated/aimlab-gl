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

fun createVariableSetting(
    scene: Scene,
    y: Float,
    name: String,
    increase: () -> Unit,
    decrease: () -> Unit,
    getValue: () -> String
) {
    scene.create {
        addComponent(TextRenderer(this, scene, name, horizontalAlign = TextAlign.END))
        transform.position = Vec3(-0.1f, 0.0f, y)
    }

    scene.create {
        addComponent(Button(
            this, scene, "+", horizontalAlign = TextAlign.END, boundingBox = BoundingBox(
                Vec3(-1f, 0.0f, -1f),
                Vec3(1f, 0.0f, 1f)
            )
        ) {
            increase()
        })

        transform.position = Vec3(0.1f, 0.0f, y - 0.03f)
    }

    scene.create {
        addComponent(DynamicTextRenderer(this, scene, getValue, horizontalAlign = TextAlign.CENTER))

        transform.position = Vec3(0.25f, 0.0f, y)
    }

    scene.create {
        addComponent(Button(
            this, scene, "-", horizontalAlign = TextAlign.START, boundingBox = BoundingBox(
                Vec3(-1f, 0.0f, -1f),
                Vec3(1f, 0.0f, 1f)
            )
        ) {
            decrease()
        })

        transform.position = Vec3(0.4f, 0.0f, y - 0.03)
    }

}

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

    createVariableSetting(scene, 0.6f, "Sensitivity", increase = {
        Settings.sensitivity.timesAssign(1.1f)
    }, decrease = {
        Settings.sensitivity.divAssign(1.1f)
    }, {
        val decimalFormat = java.text.DecimalFormat("0.0")
        decimalFormat.format(Settings.sensitivity.x * 1000)
    })

    createVariableSetting(scene, 0.5f, "Game Time", increase = {
        Settings.time = when (Settings.time) {
            3.0f -> 10.0f
            10.0f -> 30.0f
            30.0f -> 60.0f
            else -> 3.0f
        }
    }, decrease = {
        Settings.time = when (Settings.time) {
            60.0f -> 30.0f
            30.0f -> 10.0f
            10.0f -> 3.0f
            else -> 60.0f
        }
    }, {
        Settings.time.toInt().toString()
    })

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
