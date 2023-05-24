package aimlab.scenes

import aimlab.Resources.glfwContext
import aimlab.TextAlign
import aimlab.aimlabclient.models.Stat
import aimlab.aimlabclient.post
import aimlab.systems.AimlabKeyboardControls
import engine.Scene
import aimlab.components.Button
import aimlab.components.TextRenderer
import engine.systems.Camera2d
import engine.systems.RenderPipeline
import aimlab.systems.UiManager
import glm_.vec3.Vec3
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import java.util.*

fun createGameOverScene(score: Double, combo: UInt, accuracy: Double, gameScene: Scene) = gameScene.game.newScene {
    glfwContext.cursorHidden = false

    runBlocking {
        launch(Dispatchers.Unconfined) {
            try {
                post(
                    Stat(
                        id = UUID.randomUUID().toString(),
                        username = System.getProperty("user.name"),
                        score = score,
                        combo = combo,
                        accuracy = accuracy,
                        datetime = LocalDateTime.now().toString()
                    )
                )
            } catch(e: Exception) {
                println("Failed to post score")
            }
        }
    }

    systems.add(RenderPipeline(this))
    systems.add(AimlabKeyboardControls(this))
    systems.add(Camera2d(this))
    systems.add(UiManager(this))

    create {
        addComponent(TextRenderer(this, "Game Over", horizontalAlign = TextAlign.CENTER))
        transform.position = Vec3(0.0f, 0.0f, 0.5f)
    }

    create {
        addComponent(TextRenderer(this, "Score: $score", horizontalAlign = TextAlign.CENTER))
        transform.position = Vec3(0.0f, 0.0f, 0.4f)
    }

    create {
        addComponent(
            Button(
            this, "Restart",
            horizontalAlign = TextAlign.CENTER, onClick = {
            createGameScene(scene.game)
        })
        )

        transform.position = Vec3(0.0f, 0.0f, -0.3f)
    }

    create {
        addComponent(
            Button(
            this, "Main Menu",
            horizontalAlign = TextAlign.CENTER, onClick = {
            createMainMenu(scene.game)
        })
        )

        transform.position = Vec3(0.0f, 0.0f, -0.4f)
    }
}
