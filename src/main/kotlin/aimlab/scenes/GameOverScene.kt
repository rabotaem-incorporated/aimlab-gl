package aimlab.scenes

import aimlab.Resources.glfwContext
import aimlab.TextAlign
import aimlab.aimlabclient.models.Stat
import aimlab.aimlabclient.post
import engine.Scene
import engine.components.Button
import engine.components.TextRenderer
import engine.systems.Camera2d
import engine.systems.ExitOnEscape
import engine.systems.RenderPipeline
import engine.systems.UiManager
import glm_.vec3.Vec3
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun createGameOverScene(score: Int, gameScene: Scene): Scene {
    val scene = Scene(gameScene.glfwContext)

    glfwContext.cursorHidden = false

    runBlocking {
        launch(Dispatchers.Unconfined) {
            try {
                post(
                    Stat(
                        username = java.lang.System.getProperty("user.name"),
                        score = score.toDouble(),
                    )
                )
            } catch(e: Exception) {
                println("Failed to post score")
            }
        }
    }

    scene.systems.add(RenderPipeline(scene))
    scene.systems.add(ExitOnEscape(scene))
    scene.systems.add(Camera2d(scene))
    scene.systems.add(UiManager(scene))

    scene.create {
        addComponent(TextRenderer(this, scene, "Game Over", horizontalAlign = TextAlign.CENTER))
        transform.position = Vec3(0.0f, 0.0f, 0.5f)
    }

    scene.create {
        addComponent(TextRenderer(this, scene, "Score: $score", horizontalAlign = TextAlign.CENTER))
        transform.position = Vec3(0.0f, 0.0f, 0.4f)
    }

    scene.create {
        addComponent(Button(
            this, scene, "Restart",
            horizontalAlign = TextAlign.CENTER, onClick = {
            scene.tickContext!!.sceneManager.scene = createGameScene(gameScene.glfwContext)
        }))

        transform.position = Vec3(0.0f, 0.0f, -0.3f)
    }

    scene.create {
        addComponent(Button(
            this, scene, "Main Menu",
            horizontalAlign = TextAlign.CENTER, onClick = {
            scene.tickContext!!.sceneManager.scene = createMainMenu(gameScene.glfwContext)
        }))

        transform.position = Vec3(0.0f, 0.0f, -0.4f)
    }

    return scene
}
