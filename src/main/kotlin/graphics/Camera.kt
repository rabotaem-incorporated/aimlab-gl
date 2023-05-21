package graphics

import glm_.glm
import glm_.mat4x4.Mat4
import glm_.vec3.Vec3
import kotlin.math.cos
import kotlin.math.sin

data class Camera(
    var position: Vec3,
    var yaw: Float,
    var pitch: Float,
    var fovDegrees: Float,
    val glfwContext: GlfwContext,
    var nearFar: Pair<Float, Float> = 0.1f to 5000.0f,
) {
    val projectionMatrix: Mat4
        get() {
            return glm.perspective(
                glm.radians(fovDegrees),
                glfwContext.windowWidth.toFloat() / glfwContext.windowHeight.toFloat(),
                nearFar.first,
                nearFar.second
            )
        }

    val viewMatrix get() = glm.lookAt(position, position + direction, up)

    val projectionViewMatrix get() = projectionMatrix * viewMatrix

    val direction: Vec3
        get() = Vec3(
            sin(yaw) * cos(pitch),
            sin(pitch),
            cos(yaw) * cos(pitch),
        ).normalize()

    private val up = Vec3(0.0f, 1.0f, 0.0f)

    val right: Vec3 get() = direction.cross(up).normalize()
}
