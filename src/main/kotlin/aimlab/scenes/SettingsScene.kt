package aimlab.scenes

import aimlab.CrosshairColor
import aimlab.CrosshairShape
import aimlab.Settings
import aimlab.TextAlign
import aimlab.systems.AimlabKeyboardControls
import engine.Game
import engine.Scene
import aimlab.components.Button
import aimlab.components.DynamicTextRenderer
import aimlab.components.TextRenderer
import engine.systems.Camera2d
import engine.systems.RenderPipeline
import aimlab.systems.UiManager
import glm_.vec3.Vec3
import graphics.BoundingBox
import kotlin.math.max
import kotlin.math.min

fun createVariableSetting(
    scene: Scene,
    y: Float,
    name: String,
    increase: () -> Unit,
    decrease: () -> Unit,
    getValue: () -> String
) {
    scene.create {
        addComponent(TextRenderer(this, name, horizontalAlign = TextAlign.END))
        transform.position = Vec3(-0.1f, 0.0f, y)
    }

    scene.create {
        addComponent(Button(
            this, "<", horizontalAlign = TextAlign.END, boundingBox = BoundingBox(
                Vec3(-1f, 0.0f, -1f),
                Vec3(1f, 0.0f, 1f)
            )
        ) {
            decrease()
        })

        transform.position = Vec3(0.1f, 0.0f, y - 0.03f)
    }

    scene.create {
        addComponent(DynamicTextRenderer(this, getValue, horizontalAlign = TextAlign.CENTER))

        transform.position = Vec3(0.25f, 0.0f, y)
    }

    scene.create {
        addComponent(Button(
            this, ">", horizontalAlign = TextAlign.START, boundingBox = BoundingBox(
                Vec3(-1f, 0.0f, -1f),
                Vec3(1f, 0.0f, 1f)
            )
        ) {
            increase()
        })

        transform.position = Vec3(0.4f, 0.0f, y - 0.03)
    }

}

fun createSettingsScene(game: Game): Unit = game.newScene {
    systems.add(RenderPipeline(this))
    systems.add(AimlabKeyboardControls(this))
    systems.add(Camera2d(this))
    systems.add(UiManager(this))

    create {
        addComponent(TextRenderer(this, "Settings", horizontalAlign = TextAlign.CENTER))
        transform.position = Vec3(0.0f, 0.0f, 0.8f)
        transform.scale = 0.2f
    }

    createVariableSetting(this, 0.6f, "Sensitivity", increase = {
        Settings.increaseSensitivity()
    }, decrease = {
        Settings.decreaseSensitivity()
    }, {
        val decimalFormat = java.text.DecimalFormat("0.00")
        decimalFormat.format(Settings.sensitivity.x * 1000)
    })

    createVariableSetting(this, 0.45f, "Game Time", increase = {
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

    createVariableSetting(this, 0.3f, "Crosshair Thickness", increase = {
        Settings.crosshairThickness = min(Settings.crosshairThickness + 0.001f, 0.1f)
    }, decrease = {
        Settings.crosshairThickness = max(Settings.crosshairThickness - 0.001f, 0.001f)
    }, {
        val decimalFormat = java.text.DecimalFormat("0.00")
        decimalFormat.format(max(Settings.crosshairThickness, 0.0f) * 1000)
    })

    createVariableSetting(this, 0.15f, "Crosshair Color", increase = {
        Settings.crosshairColor = when (Settings.crosshairColor) {
            CrosshairColor.RED -> CrosshairColor.GREEN
            CrosshairColor.GREEN -> CrosshairColor.BLUE
            CrosshairColor.BLUE -> CrosshairColor.BLACK
            CrosshairColor.BLACK -> CrosshairColor.WHITE
            CrosshairColor.WHITE -> CrosshairColor.RED
        }
    }, decrease = {
        Settings.crosshairColor = when (Settings.crosshairColor) {
            CrosshairColor.RED -> CrosshairColor.WHITE
            CrosshairColor.GREEN -> CrosshairColor.RED
            CrosshairColor.BLUE -> CrosshairColor.GREEN
            CrosshairColor.BLACK -> CrosshairColor.BLUE
            CrosshairColor.WHITE -> CrosshairColor.BLACK
        }
    }, {
        when (Settings.crosshairColor) {
            CrosshairColor.RED -> "Red"
            CrosshairColor.GREEN -> "Green"
            CrosshairColor.BLUE -> "Blue"
            CrosshairColor.BLACK -> "Black"
            CrosshairColor.WHITE -> "White"
        }
    })

    createVariableSetting(this, 0.0f, "Crosshair Shape", increase = {
        Settings.crosshairShape = when (Settings.crosshairShape) {
            CrosshairShape.CIRCLE -> CrosshairShape.SQUARE
            CrosshairShape.SQUARE -> CrosshairShape.CROSS
            CrosshairShape.CROSS -> CrosshairShape.CIRCLE
        }
    }, decrease = {
        Settings.crosshairShape = when (Settings.crosshairShape) {
            CrosshairShape.CIRCLE -> CrosshairShape.CROSS
            CrosshairShape.SQUARE -> CrosshairShape.CIRCLE
            CrosshairShape.CROSS -> CrosshairShape.SQUARE
        }
    }, getValue = {
        when (Settings.crosshairShape) {
            CrosshairShape.CIRCLE -> "Circle"
            CrosshairShape.SQUARE -> "Square"
            CrosshairShape.CROSS -> "Cross"
        }
    })

    create {
        addComponent(Button(
            this, "Back",
            horizontalAlign = TextAlign.CENTER, onClick = {
                createMainMenu(game)
            }
        ))

        transform.position = Vec3(-1.0f, 0.0f, -0.9f)
    }

    create {
        addComponent(Button(
            this, "Play",
            horizontalAlign = TextAlign.CENTER, onClick = {
                createGameScene(game)
            }
        ))

        transform.position = Vec3(1.0f, 0.0f, -0.9f)
    }

    create {
        addComponent(Button(
            this, "Toggle Fullscreen",
            horizontalAlign = TextAlign.CENTER, onClick = {
                game.glfwContext.fullscreen = !game.glfwContext.fullscreen
            }
        ))

        transform.position = Vec3(0.0f, 0.0f, -0.8f)
    }
}
