package graphics

import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import java.nio.ByteBuffer

abstract class Buffer(
    type: Type,
    usage: Usage,
    rawData: ByteBuffer,
) {
    private var handle: Int = GL15.glGenBuffers()

    enum class Type {
        Vertex,
        Element,
    }

    enum class Usage {
        Static,
        Dynamic,
        Stream,
    }

    init {
        val glTarget = when (type) {
            Type.Vertex -> GL15.GL_ARRAY_BUFFER
            Type.Element -> GL15.GL_ELEMENT_ARRAY_BUFFER
        }

        val glUsage = when (usage) {
            Usage.Static -> GL15.GL_STATIC_DRAW
            Usage.Dynamic -> GL15.GL_DYNAMIC_DRAW
            Usage.Stream -> GL15.GL_STREAM_DRAW
        }

        GL15.glBindBuffer(glTarget, handle)
        GL15.glBufferData(glTarget, rawData, glUsage)

        NativeAllocatorContext.scope {
            defer { GL15.glDeleteBuffers(handle) }
        }
    }
}

class VertexBuffer(
    usage: Usage,
    data: StructArray,
) : Buffer(Type.Vertex, usage, data.buffer) {
    init {
        for ((index, fragment) in data.layout.fragments.withIndex()) {
            val (glType, size) = when (fragment) {
                Layout.Fragment.Float3 -> (GL15.GL_FLOAT to 3)
                Layout.Fragment.Float2 -> (GL15.GL_FLOAT to 2)
                Layout.Fragment.Int1 -> (GL15.GL_INT to 1)
                Layout.Fragment.Int3 -> (GL15.GL_INT to 3)
            }

            GL20.glVertexAttribPointer(
                index,
                size,
                glType,
                false,
                data.layout.width,
                data.layout.offsets[index].toLong(),
            )

            GL20.glEnableVertexAttribArray(index)
        }
    }
}

class ElementBuffer(
    usage: Usage,
    data: StructArray,
    private val mode: DrawMode
) : Buffer(Type.Element, usage, data.buffer) {
    enum class DrawMode {
        Triangles,
    }

    init {
        when (mode) {
            DrawMode.Triangles -> {
                assert(data.layout == Layout.of(Layout.Fragment.Int3))
            }
        }
    }

    private val count = when (mode) {
        DrawMode.Triangles -> data.count * 3
    }

    fun draw() {
        val glMode = when (mode) {
            DrawMode.Triangles -> GL15.GL_TRIANGLES
        }

        val glType = when (mode) {
            DrawMode.Triangles -> GL15.GL_UNSIGNED_INT
        }

        GL15.glDrawElements(glMode, count, glType, 0)
    }
}
