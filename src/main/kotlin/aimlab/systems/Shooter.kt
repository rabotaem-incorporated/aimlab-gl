package aimlab.systems

import aimlab.scenes.createGameOverScene
import engine.Scene
import engine.System
import engine.systems.CollisionSystem
import engine.systems.SceneCamera
import graphics.MouseButton

class Shooter(scene: Scene) : System(scene) {
    private lateinit var camera: SceneCamera
    private lateinit var collisionSystem: CollisionSystem

    private var mousePressedLastTick = false

    var score = 0
        private set

    private var duration: Float = 3.0f
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

        mousePressedLastTick = if (input.isMousePressed(MouseButton.LEFT)) {
            if (!mousePressedLastTick) {
                val ray = camera.getForwardRay()
                val hit = collisionSystem.rayCast(ray)
                hit?.entity?.destroy()
                if (hit != null) {
                    score++
                }
            }

            true
        } else {
            false
        }

        if (timeLeft <= 0.0f) {
            scene.tickContext!!.sceneManager.scene = createGameOverScene(score, scene)
        }
    }
}