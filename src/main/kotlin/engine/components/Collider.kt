package engine.components

import engine.Component
import engine.Entity
import engine.Scene
import engine.systems.CollisionSystem
import engine.systems.Ray
import engine.systems.RayHit
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
        val position = entity.transform.globalPosition
        val r = entity.transform.globalScale * radius
        val rayVec = position - ray.origin

        val b = ray.direction dot rayVec
        val c = (rayVec dot rayVec) - r * r

        val discriminant = b * b - c

        if (discriminant < 0) return null

        val smaller = (b - sqrt(discriminant))
        val larger = (b + sqrt(discriminant))

        if (smaller < 0 && larger < 0) return null

        val t = if (smaller < 0) larger else smaller

        val point = ray.origin + ray.direction * t
        val distance = (point - ray.origin).length()
        val normal = (point - position).normalize()

        return RayHit(entity, point, normal, distance)
    }
}