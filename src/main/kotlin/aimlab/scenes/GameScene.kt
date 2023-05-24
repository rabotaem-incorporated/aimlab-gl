package aimlab.scenes

import aimlab.CrosshairShape
import aimlab.Resources
import aimlab.Settings
import aimlab.TextAlign
import aimlab.components.BallSpawner
import aimlab.components.ScoreCounter
import aimlab.components.Timer
import aimlab.systems.AimlabKeyboardControls
import aimlab.systems.FpsCamera
import aimlab.systems.Shooter
import engine.*
import engine.components.Renderer
import aimlab.components.TextRenderer
import engine.components.UiRenderer
import engine.systems.*
import glm_.quat.Quat
import glm_.vec3.Vec3

fun createGameScene(game: Game): Unit = game.newScene {
    game.glfwContext.cursorHidden = true

    systems.add(RenderPipeline(this))
    systems.add(AimlabKeyboardControls(this))
    systems.add(RayCastingSystem(this))
    systems.add(FpsCamera(this))
    systems.add(Shooter(this))
    systems.add(Light(this, Vec3(0.0f, 1.0f, 0.0f)))

    create {
        addComponent(BallSpawner(this))

        transform.position = Vec3(0.0f, 0.0f, 10.0f)
    }

    create {
        addComponent(
            Renderer(
                this,
                SolidColorMaterial(Vec3(0.5, 0.7f, 1.0f)),
                Resources.ball
            )
        )
        transform.scale = 100.0f
    }

    create {
        addComponent(Renderer(this, TexturedMaterial(Resources.pepega), Resources.quad))
        transform.position = Vec3(0.0f, 0.0f, 20.0f)
        transform.rotation = Quat.quatLookAt(Vec3(0.0, -1.0, 0.0f), Vec3(0.0f, 0.0f, 1.0f))
        transform.scale = 10.0f
    }

    create {
        addComponent(
            Renderer(
                this, LitTexturedMaterial(
                    Resources.sand,
                    Vec3(0.2f, 0.2f, 0.2f),
                    Vec3(0.8f, 0.8f, 0.8f),
                    Vec3(0.2f, 0.2f, 0.2f),
                    1.0f
                ), Resources.terrain
            )
        )
        transform.position = Vec3(0.0f, -6.0f, 0.0f)
        transform.scale = 2.0f
    }

    create {
        addComponent(TextRenderer(this, "", horizontalAlign = TextAlign.START))
        addComponent(ScoreCounter(this))
        transform.position = Vec3(0.3f, 0.0f, 0.9f)
    }

    create {
        addComponent(TextRenderer(this, "", horizontalAlign = TextAlign.END))
        addComponent(Timer(this))
        transform.position = Vec3(-0.3f, 0.0f, 0.9f)
    }

    when (Settings.crosshairShape) {
        CrosshairShape.SQUARE -> {
            create {
                addComponent(UiRenderer(this, Resources.quad, SolidColorMaterial(Settings.crosshairColor.color)))
                transform.scale = Settings.crosshairThickness
            }
        }

        CrosshairShape.CIRCLE -> {
            create {
                addComponent(UiRenderer(this, Resources.ball, SolidColorMaterial(Settings.crosshairColor.color)))
                transform.scale = Settings.crosshairThickness
            }
        }

        CrosshairShape.CROSS -> {
            create {
                addComponent(
                    UiRenderer(
                        this,
                        Resources.coords,
                        SolidColorMaterial(Settings.crosshairColor.color)
                    )
                )
                transform.scale = Settings.crosshairThickness
            }
        }
    }
}