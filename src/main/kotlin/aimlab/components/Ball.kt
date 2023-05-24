package aimlab.components

import engine.Component
import engine.Entity
import engine.components.SphereCollider
import glm_.vec3.Vec3

class Ball(entity: Entity) : Component(entity) {
    override fun onCreate() {
        entity.addComponent(SphereCollider(entity, 1.0f))
    }

    override fun onTick() {
        entity.transform.position.plusAssign(Vec3(0.0f, 0.0f, 0.0f))
    }
}
