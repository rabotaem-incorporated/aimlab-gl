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
import engine.LitTexturedMaterial
import engine.Scene
import engine.SolidColorMaterial
import engine.TexturedMaterial
import engine.components.Renderer
import engine.components.TextRenderer
import engine.components.UiRenderer
import engine.systems.*
import glm_.quat.Quat
import glm_.vec3.Vec3
import graphics.GlfwContext

fun createGameScene(glfwContext: GlfwContext): Scene {
    val scene = Scene(glfwContext)

    glfwContext.cursorHidden = true

    scene.systems.add(RenderPipeline(scene))
    scene.systems.add(AimlabKeyboardControls(scene))
    scene.systems.add(CollisionSystem(scene))
    // scene.systems.add(DebugCamera(scene))
    scene.systems.add(FpsCamera(scene))
    scene.systems.add(Shooter(scene))
    scene.systems.add(Light(scene, Vec3(0.0f, 1.0f, 0.0f)))

    scene.create {
        addComponent(BallSpawner(scene, this))

        transform.position = Vec3(0.0f, 0.0f, 10.0f)
    }

    scene.create {
        addComponent(Renderer(this, scene, SolidColorMaterial(Vec3(0.5, 0.7f, 1.0f)), Resources.ball))
        transform.scale = 100.0f
    }

    scene.create {
        addComponent(Renderer(this, scene, TexturedMaterial(Resources.pepega), Resources.quad))
        transform.position = Vec3(0.0f, 0.0f, 20.0f)
        transform.rotation = Quat.quatLookAt(Vec3(0.0, -1.0, 0.0f), Vec3(0.0f, 0.0f, 1.0f))
        transform.scale = 10.0f
    }

    scene.create {
        addComponent(Renderer(this, scene, LitTexturedMaterial(
            Resources.sand,
            Vec3(0.2f, 0.2f, 0.2f),
            Vec3(0.8f, 0.8f, 0.8f),
            Vec3(0.2f, 0.2f, 0.2f),
            1.0f
        ), Resources.terrain))
        transform.position = Vec3(0.0f, -6.0f, 0.0f)
        transform.scale = 2.0f
    }

    scene.create {
        addComponent(TextRenderer(this, scene, "", horizontalAlign = TextAlign.START))
        addComponent(ScoreCounter(this, scene))
        transform.position = Vec3(0.3f, 0.0f, 0.9f)
    }

    scene.create {
        addComponent(TextRenderer(this, scene, "", horizontalAlign = TextAlign.END))
        addComponent(Timer(this, scene))
        transform.position = Vec3(-0.3f, 0.0f, 0.9f)
    }

    when (Settings.crosshairShape) {
        CrosshairShape.SQUARE -> {
            scene.create {
                addComponent(UiRenderer(this, scene, Resources.quad, SolidColorMaterial(Settings.crosshairColor.color)))
                transform.scale = Settings.crosshairThickness
            }
        }

        CrosshairShape.CIRCLE -> {
            scene.create {
                addComponent(UiRenderer(this, scene, Resources.ball, SolidColorMaterial(Settings.crosshairColor.color)))
                transform.scale = Settings.crosshairThickness
            }
        }

        CrosshairShape.CROSS -> {
            scene.create {
                addComponent(UiRenderer(this, scene, Resources.coords, SolidColorMaterial(Settings.crosshairColor.color)))
                transform.scale = Settings.crosshairThickness
            }
        }
    }

    return scene
}