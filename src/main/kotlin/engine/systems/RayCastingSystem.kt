package engine.systems

import engine.Entity
import engine.Scene
import engine.System
import engine.components.Collider
import glm_.vec3.Vec3

data class Ray(val origin: Vec3, val direction: Vec3)
data class RayHit(val entity: Entity, val point: Vec3, val normal: Vec3, val distance: Float)

/**
 * Эта система хранит все коллайдеры и позволяет проверять пересечения луча с коллайдерами.
 *
 * @see Collider
 */
class RayCastingSystem(scene: Scene) : System(scene) {
    private val colliders = mutableListOf<Collider?>()

    /**
     * Идентификатор коллайдера в системе.
     */
    data class ColliderHandle(val id: Int)

    /**
     * Добавляет коллайдер в систему. Предполагается, что вызывается только в [engine.components.Collider.onCreate].
     */
    fun addCollider(collider: Collider): ColliderHandle {
        colliders.add(collider)
        return ColliderHandle(colliders.size - 1)
    }

    /**
     * Удаляет коллайдер из системы. Предполагается, что вызывается только в [engine.components.Collider.onDestroy].
     */
    fun removeCollider(collider: ColliderHandle) {
        colliders[collider.id] = null
    }

    /**
     * Проверяет пересечение луча с коллайдерами.
     */
    fun rayCast(ray: Ray): RayHit? {
        var bestHit: RayHit? = null
        for (collider in colliders) {
            val hit = collider?.intersectWithRay(ray)
            if (hit != null && (bestHit == null || hit.distance < bestHit.distance)) {
                bestHit = hit
            }
        }

        return bestHit
    }
}