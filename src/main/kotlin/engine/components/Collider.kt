package engine.components

import aimlab.Resources
import engine.Component
import engine.Entity
import engine.Scene
import engine.systems.CollisionSystem
import engine.systems.Ray
import engine.systems.RayHit
import glm_.vec3.Vec3
import kotlin.math.sqrt

abstract class Collider(entity: Entity, scene: Scene) : Component(entity, scene) {
    private lateinit var collisionSystem: CollisionSystem
    private lateinit var handle: CollisionSystem.ColliderHandle

    override fun onCreate() {
        collisionSystem = scene.query<CollisionSystem>()
        handle = collisionSystem.addCollider(this)
    }

    override fun onDestroy() {
        collisionSystem.removeCollider(handle)
    }

    abstract fun intersectWithRay(ray: Ray): RayHit?
}

class SphereCollider(entity: Entity, scene: Scene, private val radius: Float) : Collider(entity, scene) {
    override fun intersectWithRay(ray: Ray): RayHit? {
        println("$ray")
        println("${entity.transform.globalPosition}")
        println("${entity.transform.globalScale}")

        scene.create {
            addComponent(Renderer(this, scene, SolidColorMaterial(Vec3(1.0f, 0.0f, 0.0f)), Resources.coords))
            transform.position = ray.origin
            transform.scale = 0.1f
            transform.rotation = glm_.quat.Quat.quatLookAt(ray.direction, Vec3(0.0f, 0.0f, 1.0f))
        }

        val position = entity.transform.globalPosition
        val r = entity.transform.globalScale * radius
        val center = position - ray.origin

        val a = ray.direction dot ray.direction
        val b = ray.direction dot center
        val c = center dot center - r * r

        val discriminant = b * b - a * c

        if (discriminant < 0) return null

        val smaller = (b - sqrt(discriminant)) / a
        val larger = (b + sqrt(discriminant)) / a

        if (smaller < 0 && larger < 0) return null

        val t = if (smaller < 0) larger else smaller

        val point = ray.origin + ray.direction * t
        val distance = (point - ray.origin).length()
        val normal = (point - position).normalize()

        return RayHit(entity, point, normal, distance)
    }
}