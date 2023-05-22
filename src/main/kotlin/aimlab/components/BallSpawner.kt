package aimlab.components

import aimlab.Resources
import engine.Component
import engine.Entity
import engine.Scene
import engine.LitMaterial
import engine.components.Renderer
import glm_.Java.Companion.glm
import glm_.mat2x2.Mat2
import glm_.vec2.Vec2
import glm_.vec3.Vec3
import glm_.vec3.swizzle.xyz
import glm_.vec4.Vec4
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class BallSpawner(scene: Scene, entity: Entity) : Component(entity, scene) {
    private var targetCount: Int = 5
    private val spread = 3.0f
    private val size = 0.2f

    override fun onTick() {
        while (entity.children.size < targetCount) {
            val entity = scene.create(entity) {
                val phi = Random.nextFloat() * 2 * Math.PI.toFloat()
                val r = Vec3(sin(phi), cos(phi), 0.0f)
                val g = r.xyz
                val b = r.xyz
                glm.rotateZ(g, r, 2 * Math.PI.toFloat() / 3)
                glm.rotateZ(b, r, 4 * Math.PI.toFloat() / 3)

                addComponent(
                    Renderer(
                        this, scene, LitMaterial(
                            Vec3(r.x, g.x, b.x),
                            Vec3(0.6f, 0.6f, 0.6f),
                            Vec3(0.6f, 0.6f, 0.5f),
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