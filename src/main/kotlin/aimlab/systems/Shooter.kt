package aimlab.systems

import aimlab.Settings
import aimlab.scenes.createGameOverScene
import engine.Scene
import engine.System
import engine.systems.CollisionSystem
import engine.systems.SceneCamera
import engine.systems.UiManager
import graphics.KeyButtonStatus
import graphics.MouseButton
import kotlin.math.max

class Shooter(scene: Scene) : System(scene) {
    private lateinit var camera: SceneCamera
    private lateinit var collisionSystem: CollisionSystem

    var totalHit = 0
        private set

    var totalShot = 0
        private set
    var score = 0.0
        private set

    var curCombo : UInt = 0u
        private set

    var combo : UInt = 0u
        private set

    var accuracy = 100.0
        private set

    private var duration: Float = Settings.time
    private var startTime: Float? = null

    val timeLeft get() = duration - (scene.time!! - startTime!!)

    override fun onStart() {
        camera = scene.query()
        collisionSystem = scene.query()
    }

    override fun beforeTick() {
        if (startTime == null) {
            startTime = scene.time
        }
    }

    override fun afterTick() {
        val input = scene.tickContext!!.input

        if (input.getMouseButtonStatus(MouseButton.LEFT) == KeyButtonStatus.PRESS) {
            val ray = camera.getForwardRay()
            val hit = collisionSystem.rayCast(ray)
            hit?.entity?.destroy()
            if (hit != null) {
                curCombo++
                score += curCombo.toDouble()
                combo = max(combo, curCombo)
                totalHit++
            } else {
                curCombo = 0u
            }
            totalShot++
            accuracy = 100.0 * totalHit / totalShot
        }

        if (timeLeft <= 0.0f) {
            scene.tickContext!!.sceneManager.scene = createGameOverScene(score, combo, accuracy, scene)
        }
    }
}