package engine.systems

import engine.Entity
import engine.Scene
import engine.System
import engine.components.Collider
import glm_.vec3.Vec3

data class Ray(val origin: Vec3, val direction: Vec3)
data class RayHit(val entity: Entity, val point: Vec3, val normal: Vec3, val distance: Float)

class CollisionSystem(scene: Scene) : System(scene) {
    private val colliders = mutableListOf<Collider?>()

    data class ColliderHandle(val id: Int)

    fun addCollider(collider: Collider): ColliderHandle {
        colliders.add(collider)
        return ColliderHandle(colliders.size - 1)
    }

    fun removeCollider(collider: ColliderHandle) {
        colliders[collider.id] = null
    }

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