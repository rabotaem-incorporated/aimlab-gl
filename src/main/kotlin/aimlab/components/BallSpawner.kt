package aimlab.components

import aimlab.Resources
import engine.Component
import engine.Entity
import engine.Scene
import engine.LitMaterial
import engine.components.Renderer
import glm_.vec3.Vec3
import kotlin.random.Random

class BallSpawner(scene: Scene, entity: Entity) : Component(entity, scene) {
    var targetCount: Int = 5
    val spread = 3.0f
    val size = 0.2f

    override fun onTick() {
        while (entity.children.size < targetCount) {
            val entity = scene.create(entity) {
                addComponent(
                    Renderer(
                        this, scene, LitMaterial(
                            Vec3(1.0f, 0.0f, 1.0f),
                            Vec3(0.2f, 0.2f, 0.2f),
                            Vec3(0.6f, 0.6f, 0.6f),
                            Vec3(1.0f, 1.0f, 1.0f),
                            8.0f,
                        ), Resources.ball
                    )
                    // Renderer(this, scene, SolidColorMaterial(Vec3(1.0f, 0.0f, 1.0f)), Resources.ball)
                )
                addComponent(Ball(this, scene))
            }

            entity.transform.position = Vec3(
                (Random.nextFloat() - 0.5) * spread * 2,
                (Random.nextFloat() - 0.5) * spread * 2,
                0.0f,
            )

            entity.transform.scale = size
        }
    }
}