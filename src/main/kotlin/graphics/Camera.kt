package graphics

import glm_.glm
import glm_.mat4x4.Mat4
import glm_.vec3.Vec3
import kotlin.math.cos
import kotlin.math.sin

/**
 * Камера. Содержит позицию, направление, угол обзора и матрицы проекции и вида.
 *
 * Используется для генерации матрицы проекции-вида, которая передаётся в шейдерную программу.
 *
 * @param position позиция камеры, в мировых координатах
 * @param yaw угол поворота вокруг оси Y, то есть "влево-вправо", в радианах
 * @param pitch угол поворота вокруг оси X в координатах камеры, то есть "вверх-вниз", в радианах
 * @param fovDegrees угол обзора в градусах
 * @param glfwContext контекст GLFW, используется для получения размеров окна
 * @param nearFar ближняя и дальняя плоскости отсечения
 */
data class Camera(
    var position: Vec3,
    var yaw: Float,
    var pitch: Float,
    var fovDegrees: Float,
    val glfwContext: GlfwContext,
    var nearFar: Pair<Float, Float> = 0.1f to 5000.0f,
) {
    /**
     * Матрица проекции, используется для преобразования координат из координат камеры в clipping space.
     */
    val projectionMatrix: Mat4
        get() {
            return glm.perspective(
                glm.radians(fovDegrees),
                glfwContext.windowWidth.toFloat() / glfwContext.windowHeight.toFloat(),
                nearFar.first,
                nearFar.second
            )
        }

    /**
     * Матрица вида, используется для преобразования координат из мировых в координаты камеры.
     */
    val viewMatrix get() = glm.lookAt(position, position + direction, up)

    /**
     * Матрица проекции-вида, используется для преобразования координат из мировых в clipping space.
     *
     * Это произведение матриц проекции и вида. Обычно в шейдер надо передавать именно ее.
     */
    val projectionViewMatrix get() = projectionMatrix * viewMatrix

    /**
     * Направление камеры, в мировых координатах.
     */
    val direction: Vec3
        get() = Vec3(
            sin(yaw) * cos(pitch),
            sin(pitch),
            cos(yaw) * cos(pitch),
        ).normalize()

    private val up = Vec3(0.0f, 1.0f, 0.0f)

    /**
     * Вектор вправо от камеры, в мировых координатах.
     */
    val right: Vec3 get() = direction.cross(up).normalize()
}
