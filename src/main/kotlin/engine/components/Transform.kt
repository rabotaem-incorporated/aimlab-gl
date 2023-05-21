package engine.components

import engine.Component
import engine.Entity
import engine.Scene
import glm_.glm
import glm_.mat4x4.Mat4
import glm_.quat.Quat
import glm_.vec3.Vec3
import glm_.vec4.Vec4
import glm_.vec4.swizzle.xyz

class Transform(
    entity: Entity,
    scene: Scene,
    var position: Vec3,
    var rotation: Quat,
    var scale: Float,
) : Component(entity, scene) {
    val modelMatrix: Mat4
        get() {
            val translation = glm.translate(Mat4(1.0f), position)
            val rotation = rotation.toMat4()
            val scale = glm.scale(Mat4(1.0f), Vec3(scale))
            return translation * scale * rotation
        }

    val globalPosition: Vec3 get() = (modelToWorldMatrix * Vec4(0.0f, 0.0f, 0.0f, 1.0f)).xyz

    val globalScale: Float get() = entity.parent?.transform?.globalScale?.times(scale) ?: scale

    val modelToWorldMatrix: Mat4
        get() {
            val parent = entity.parent?.transform?.modelToWorldMatrix ?: Mat4(1.0f)
            return parent * modelMatrix
        }
}
