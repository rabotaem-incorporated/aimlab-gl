package aimlab.scenes

import aimlab.TextAlign
import aimlab.components.LeaderboardComponent
import aimlab.LeaderboardLines
import aimlab.systems.AimlabKeyboardControls
import engine.Scene
import engine.components.Button
import engine.components.TextRenderer
import engine.systems.Camera2d
import engine.systems.RenderPipeline
import engine.systems.UiManager
import glm_.vec3.Vec3
import graphics.GlfwContext
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

fun createLeaderboardScene(glfwContext: GlfwContext): Scene {
    val scene = Scene(glfwContext)

    scene.systems.add(RenderPipeline(scene))
    scene.systems.add(AimlabKeyboardControls(scene))
    scene.systems.add(Camera2d(scene))
    scene.systems.add(UiManager(scene))

    scene.create {
        addComponent(TextRenderer(this, scene, "Leaderboard", horizontalAlign = TextAlign.CENTER))
        transform.position = Vec3(0.0f, 0.0f, 0.9f)
        transform.scale = 0.2f
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

    val leaderboard = LeaderboardLines()

    if (leaderboard.isError) {
        scene.create {
            addComponent(
                TextRenderer(
                    this, scene, "",
                    horizontalAlign = TextAlign.CENTER,
                    verticalAlign = TextAlign.START,
                )
            )
            addComponent(LeaderboardComponent(this, scene, leaderboard.lineList[0]))
            transform.position = Vec3(0.0f, 0.0f, 0.7f)
        }
    } else {
        val positions = listOf(-1f, -0.6f, -0.3f, 0.0f, 0.4f)
        for (i in 0..4) {
            scene.create {
                addComponent(
                    TextRenderer(
                        this, scene, "",
                        horizontalAlign = TextAlign.START,
                        verticalAlign = TextAlign.START,
                    )
                )
                addComponent(LeaderboardComponent(this, scene, leaderboard.lineList[i]))
                transform.position = Vec3(positions[i], 0.0f, 0.7f)
            }
        }
    }

    return scene
}