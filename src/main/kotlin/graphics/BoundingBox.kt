package graphics

import glm_.vec3.Vec3

/**
 * Кубоид, ограничивающий множество точек.
 */
data class BoundingBox(val min: Vec3, val max: Vec3) {
    /**
     * Возвращает `true`, если кубоид пустой.
     */
    fun isEmpty(): Boolean = min.x >= max.x || min.y >= max.y || min.z >= max.z

    /**
     * Возвращает размер кубоида (разность между максимальной и минимальной точками).
     */
    fun size(): Vec3 = max - min
}