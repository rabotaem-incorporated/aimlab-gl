package aimlab

import engine.EPS
import glm_.vec2.Vec2
import glm_.vec3.Vec3

object Settings {
    val dSensitivity: Vec2 = Vec2(0.00001f, 0.00001f)
    val dSensitivityFast: Vec2 = Vec2(0.0001f, 0.0001f)
    var sensitivity: Vec2 = Vec2(0.0005f, 0.0005f)
    val maxSensitivity = 0.001f
    var time: Float = 3.0f
    var crosshairColor: CrosshairColor = CrosshairColor.RED
    var crosshairThickness: Float = 0.01f
    var crosshairShape: CrosshairShape = CrosshairShape.CIRCLE

    fun increaseSensitivity() {
        if (sensitivity.x > maxSensitivity - EPS) return
        if (sensitivity.x > maxSensitivity - dSensitivity.x - EPS) {
            sensitivity = Vec2(maxSensitivity, maxSensitivity)
        } else {
            sensitivity.plusAssign(dSensitivity)
        }
    }

    fun increaseSensitivityFast() {
        if (sensitivity.x > maxSensitivity - EPS) return
        if (sensitivity.x > maxSensitivity - dSensitivityFast.x - EPS) {
            sensitivity = Vec2(maxSensitivity, maxSensitivity)
        } else {
            sensitivity.plusAssign(dSensitivityFast)
        }
    }

    fun decreaseSensitivity() {
        if (sensitivity.x < EPS) return
        if (sensitivity.x < dSensitivity.x + EPS) {
            sensitivity = Vec2(0.0f, 0.0f)
        } else {
            sensitivity.minusAssign(dSensitivity)
        }
    }

    fun decreaseSensitivityFast() {
        if (sensitivity.x < EPS) return
        if (sensitivity.x < dSensitivityFast.x + EPS) {
            sensitivity = Vec2(0.0f, 0.0f)
        } else {
            sensitivity.minusAssign(dSensitivityFast)
        }
    }
}

enum class CrosshairColor(val color: Vec3) {
    RED(Vec3(1f, 0f, 0f)),
    GREEN(Vec3(0f, 1f, 0f)),
    BLUE(Vec3(0f, 0f, 1f)),
    BLACK(Vec3(0f, 0f, 0f)),
    WHITE(Vec3(1f, 1f, 1f)),
}

enum class CrosshairShape {
    CIRCLE,
    SQUARE,
    CROSS,
}