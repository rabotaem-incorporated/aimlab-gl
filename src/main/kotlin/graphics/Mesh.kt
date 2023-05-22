package graphics

import glm_.glm
import glm_.vec2.Vec2
import glm_.vec3.Vec3

/**
 * Представление меша в виде списка вершин и списка треугольников в оперативной памяти.
 *
 * Вершины содержат позицию, нормаль и текстурные координаты.
 *
 * Предполагается, что после отправки на GPU меш больше не будет меняться, поэтому
 * этот класс должен упросить работу с мешами, пока они на GPU не отправлены.
 *
 * @param vertices список вершин
 * @param triangles список треугольников
 */
class Mesh(private val vertices: MutableList<Vertex>, private val triangles: MutableList<Triangle>) {
    /**
     * Вершина меша.
     */
    data class Vertex(val position: Vec3, val normal: Vec3, val uv: Vec2)

    /**
     * Треугольник меша.
     */
    data class Triangle(val a: Int, val b: Int, val c: Int)

    /**
     * Превращает меш в неизменяемый VAO.
     *
     * @param managed если `true`, то VAO будет уничтожен в контексте глобального аллокатора
     *
     * @see [Vao]
     * @see [NativeAllocatorContext]
     */
    fun vao(managed: Boolean = true) = NativeAllocatorContext.scope {
        val vao = Vao(managed)

        vao.withBind {
            val vertexBuffer = allocStructs(
                vertexLayout,
                vertices.size,
            ).fill {
                for (vertex in vertices) {
                    put(vertex.position, vertex.normal, vertex.uv)
                }
            }

            val elementBuffer = allocStructs(
                Layout.of(Layout.Fragment.Int3),
                triangles.size,
            ).fill {
                for (triangle in triangles) {
                    put(Int3(triangle.a, triangle.b, triangle.c))
                }
            }

            vertexBuffer(Buffer.Usage.STATIC, vertexBuffer)
            elementBuffer(Buffer.Usage.STATIC, elementBuffer)
        }

        vao
    }

    /**
     * Возвращает [BoundingBox] меша. Работает за `O(len(vertices))`.
     */
    val boundingBox: BoundingBox get() {
        var min = Vec3(Float.POSITIVE_INFINITY)
        var max = Vec3(Float.NEGATIVE_INFINITY)

        for (vertex in vertices) {
            min = glm.min(min, vertex.position)
            max = glm.max(max, vertex.position)
        }

        return BoundingBox(min, max)
    }

    /**
     * Добавляет к мешу другой меш, сдвинутый на `shift`.
     *
     * Применяется для объединения мешей букв при генерации текста.
     *
     * @see [engine.components.TextRenderer]
     * @see [aimlab.Resources]
     */
    fun append(other: Mesh, shift: Vec3) {
        val offset = vertices.size

        vertices += other.vertices.map {
            Vertex(it.position + shift, it.normal, it.uv)
        }

        triangles += other.triangles.map { triangle ->
            Triangle(triangle.a + offset, triangle.b + offset, triangle.c + offset)
        }
    }

    /**
     * Сдвигает меш на `offset`, in-place.
     */
    fun shift(offset: Vec3) {
        for (vertex in vertices) {
            vertex.position += offset
        }
    }

    companion object {
        /**
         * [Layout] вершины меша.
         * @see [Vao]
         */
        val vertexLayout = Layout.of(Layout.Fragment.Float3, Layout.Fragment.Float3, Layout.Fragment.Float2)

        /**
         * Загружает меш из ресурса в формате `.obj`.
         */
        fun loadObj(path: String): Mesh {
            val positions = mutableListOf<Vec3>()
            val normals = mutableListOf<Vec3>()
            val uvs = mutableListOf<Vec2>()

            val triangles = mutableListOf<Triangle>()

            val vertices = mutableListOf<Vertex>()
            val uniqueVertices = mutableMapOf<Vertex, Int>()

            fun getVertex(position: Int, normal: Int, uv: Int): Int {
                val vertex = Vertex(positions[position], normals[normal], uvs[uv])
                return uniqueVertices.getOrPut(vertex) {
                    vertices.add(vertex)
                    vertices.size - 1
                }
            }

            val model = this::class.java.getResource(path)!!.readText()

            for (line in model.lines()) {
                val tokens = line.split(" ")
                when (tokens[0]) {
                    "v" -> positions.add(Vec3(tokens[1].toFloat(), tokens[2].toFloat(), -tokens[3].toFloat()))
                    "vn" -> normals.add(Vec3(tokens[1].toFloat(), tokens[2].toFloat(), -tokens[3].toFloat()))
                    "vt" -> uvs.add(Vec2(tokens[1].toFloat(), tokens[2].toFloat()))
                    "f" -> {
                        val a = tokens[1].split("/").map { it.toInt() - 1 }
                        val b = tokens[2].split("/").map { it.toInt() - 1 }
                        val c = tokens[3].split("/").map { it.toInt() - 1 }
                        val va = getVertex(a[0], a[2], a[1])
                        val vb = getVertex(b[0], b[2], b[1])
                        val vc = getVertex(c[0], c[2], c[1])
                        triangles.add(Triangle(va, vb, vc))
                    }
                }
            }

            return Mesh(vertices, triangles)
        }
    }
}