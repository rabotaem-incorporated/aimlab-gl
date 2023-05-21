package aimlab

import engine.Scene
import engine.System
import engine.systems.CollisionSystem
import engine.systems.SceneCamera
import graphics.MouseButton

class Shooter(scene: Scene) : System(scene) {
    private lateinit var camera: SceneCamera
    private lateinit var collisionSystem: CollisionSystem

    private var mousePressedLastTick = false

    override fun onStart() {
        camera = scene.query()
        collisionSystem = scene.query()
    }

    override fun afterTick() {
        val input = scene.tickContext!!.input

        mousePressedLastTick = if (input.isMousePressed(MouseButton.LEFT)) {
            if (!mousePressedLastTick) {
                val ray = camera.getRay(input.mousePosition)
                println(ray)
                val hit = collisionSystem.rayCast(ray)
                hit?.entity?.destory()
            }

            true
        } else {
            false
        }
    }
}