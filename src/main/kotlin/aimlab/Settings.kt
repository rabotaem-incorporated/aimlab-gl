package aimlab

import glm_.vec2.Vec2
import glm_.vec3.Vec3

object Settings {
    val dSensitivity: Vec2 = Vec2(0.00001f, 0.00001f)
    var sensitivity: Vec2 = Vec2(0.00025f, 0.00025f)
    var time: Float = 3.0f
    var crosshairColor: Vec3 = Vec3(0.0f, 0.0f, 0.0f)
    var crosshairThickness: Float = 0.01f
    var crosshairShape: CrosshairShape = CrosshairShape.CIRCLE

    fun increaseSensitivity() {
        if (sensitivity.x > 10) return
        sensitivity.plusAssign(dSensitivity)
    }

    fun decreaseSensitivity() {
        if (sensitivity.x < 1e-6) return
        sensitivity.minusAssign(dSensitivity)
    }
}

enum class CrosshairShape {
    CIRCLE,
    SQUARE,
    CROSS,
}