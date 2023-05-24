package aimlab.scenes

import aimlab.TextAlign
import aimlab.components.LeaderboardComponent
import aimlab.LeaderboardLines
import aimlab.systems.AimlabKeyboardControls
import engine.Game
import aimlab.components.Button
import aimlab.components.TextRenderer
import engine.systems.Camera2d
import engine.systems.RenderPipeline
import aimlab.systems.UiManager
import glm_.vec3.Vec3

fun createLeaderboardScene(game: Game): Unit = game.newScene {
    systems.add(RenderPipeline(this))
    systems.add(AimlabKeyboardControls(this))
    systems.add(Camera2d(this))
    systems.add(UiManager(this))

    create {
        addComponent(TextRenderer(this, "Leaderboard", horizontalAlign = TextAlign.CENTER))
        transform.position = Vec3(0.0f, 0.0f, 0.9f)
        transform.scale = 0.2f
    }

    create {
        addComponent(Button(
            this, "Back",
            horizontalAlign = TextAlign.CENTER, onClick = {
                createMainMenu(game)
            }
        ))

        transform.position = Vec3(-1.0f, 0.0f, -0.9f)
    }

    val leaderboard = LeaderboardLines()

    if (leaderboard.isError) {
        create {
            addComponent(
                TextRenderer(
                    this, "",
                    horizontalAlign = TextAlign.CENTER,
                    verticalAlign = TextAlign.START,
                )
            )
            addComponent(LeaderboardComponent(this, leaderboard.lineList[0]))
            transform.position = Vec3(0.0f, 0.0f, 0.7f)
        }
    } else {
        val positions = listOf(-1f, -0.6f, -0.3f, 0.0f, 0.4f)
        for (i in 0..4) {
            create {
                addComponent(
                    TextRenderer(
                        this, "",
                        horizontalAlign = TextAlign.START,
                        verticalAlign = TextAlign.START,
                    )
                )
                addComponent(LeaderboardComponent(this, leaderboard.lineList[i]))
                transform.position = Vec3(positions[i], 0.0f, 0.7f)
            }
        }
    }
}