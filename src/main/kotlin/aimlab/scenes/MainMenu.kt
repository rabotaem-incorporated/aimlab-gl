package aimlab.scenes

import aimlab.TextAlign
import aimlab.systems.AimlabKeyboardControls
import engine.Game
import aimlab.components.Button
import aimlab.components.TextRenderer
import engine.systems.Camera2d
import engine.systems.RenderPipeline
import aimlab.systems.UiManager
import glm_.vec3.Vec3

fun createMainMenu(game: Game) = game.newScene {
    systems.add(RenderPipeline(this))
    systems.add(AimlabKeyboardControls(this))
    systems.add(Camera2d(this))
    systems.add(UiManager(this))

    create {
        addComponent(TextRenderer(this, "\"Aimlab\"", horizontalAlign = TextAlign.CENTER))
        transform.position = Vec3(0.0f, 0.0f, 0.5f)
        transform.scale = 0.2f
    }

    create {
        addComponent(Button(
            this, "Play",
            horizontalAlign = TextAlign.CENTER, onClick = {
                createGameScene(game)
            }
        ))
    }

    create {
        addComponent(Button(
            this, "Leaderboard",
            horizontalAlign = TextAlign.CENTER, onClick = {
                createLeaderboardScene(game)
            }
        ))
        transform.position = Vec3(0.0f, 0.0f, -0.1f)
    }

    create {
        addComponent(Button(
            this, "Settings",
            horizontalAlign = TextAlign.CENTER, onClick = {
                createSettingsScene(game)
            }
        ))
        transform.position = Vec3(0.0f, 0.0f, -0.2f)
    }

    create {
        addComponent(Button(
            this, "Quit W",
            horizontalAlign = TextAlign.CENTER, onClick = {
                game.glfwContext.close()
            }
        ))
        transform.position = Vec3(0.0f, 0.0f, -0.3f)
    }
}
