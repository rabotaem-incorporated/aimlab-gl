package engine.components

import engine.Component
import engine.Entity
import engine.systems.RayCastingSystem
import engine.systems.Ray
import engine.systems.RayHit
import kotlin.math.sqrt

/**
 * Компонент, добавляющий коллайдер к сущности.
 *
 * Все, что должен делать коллайдер - это возвращать информацию о пересечении с лучом.
 *
 * Добавление коллайдера к сущности автоматически добавляет её в [RayCastingSystem].
 */
abstract class Collider(entity: Entity) : Component(entity) {
    private lateinit var rayCastingSystem: RayCastingSystem
    private lateinit var handle: RayCastingSystem.ColliderHandle

    override fun onCreate() {
        rayCastingSystem = scene.query<RayCastingSystem>()
        handle = rayCastingSystem.addCollider(this)
    }

    override fun onDestroy() {
        rayCastingSystem.removeCollider(handle)
    }

    abstract fun intersectWithRay(ray: Ray): RayHit?
}

/**
 * Коллайдер, ограниченный сферой.
 */
class SphereCollider(entity: Entity, private val radius: Float) : Collider(entity) {
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