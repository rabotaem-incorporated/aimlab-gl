package aimlab

import glm_.vec2.Vec2
import glm_.vec3.Vec3

object Settings {
    var sensitivity: Vec2 = Vec2(0.005f, 0.005f)
    var time: Float = 3.0f
    var crosshairColor: Vec3 = Vec3(0.0f, 0.0f, 0.0f)
    var crosshairThickness: Float = 0.01f
    var crosshairShape: CrosshairShape = CrosshairShape.CIRCLE
}

enum class CrosshairShape {
    CIRCLE,
    SQUARE,
    CROSS,
}