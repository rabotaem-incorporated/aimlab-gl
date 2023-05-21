package aimlab

import aimlab.components.BallSpawner
import aimlab.components.ScoreCounter
import aimlab.components.Timer
import aimlab.scenes.createGameOverScene
import aimlab.scenes.createGameScene
import aimlab.scenes.createMainMenu
import aimlab.systems.Shooter
import engine.Scene
import engine.components.Renderer
import engine.TexturedMaterial
import engine.components.TextRenderer
import engine.launchGame
import engine.systems.*
import glm_.quat.Quat
import glm_.vec3.Vec3

fun main() = launchGame {
    Resources.load(this)

    sceneManager.scene = createMainMenu(glfwContext)
    // sceneManager.scene = createGameScene(glfwContext)
    // val prevScene = createGameScene(glfwContext)
    // sceneManager.scene = createGameOverScene(0, prevScene)
}
