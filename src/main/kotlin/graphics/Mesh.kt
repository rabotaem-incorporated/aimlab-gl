package graphics

import glm_.vec2.Vec2
import glm_.vec3.Vec3

class Mesh(private val vertices: List<Vertex>, private val triangles: List<Triangle>) {
    data class Vertex(val position: Vec3, val normal: Vec3, val uv: Vec2)
    data class Triangle(val a: Int, val b: Int, val c: Int)

    fun vao(context: GlfwContext) = NativeAllocatorContext.scope {
        val vao = context.vao()

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

            vertexBuffer(Buffer.Usage.Static, vertexBuffer)
            elementBuffer(Buffer.Usage.Static, elementBuffer)
        }

        vao
    }

    companion object {
        val vertexLayout = Layout.of(Layout.Fragment.Float3, Layout.Fragment.Float3, Layout.Fragment.Float2)

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