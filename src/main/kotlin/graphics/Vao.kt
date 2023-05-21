package graphics

import org.lwjgl.opengl.GL30

class Vao(manage: Boolean = true) {
    val handle: Int = GL30.glGenVertexArrays()
    private val vertexBuffers: MutableList<VertexBuffer> = mutableListOf()
    private val elementsBuffer: MutableList<ElementBuffer> = mutableListOf()

    init {
        if (manage) {
            NativeAllocatorContext.scope {
                defer { free() }
            }
        }
    }

    fun free() {
        GL30.glDeleteVertexArrays(handle)
    }

    inline fun <T> withBind(crossinline block: BoundVaoContext.() -> T): T {
        GL30.glBindVertexArray(handle)
        return BoundVaoContext(this).with(block)
    }

    fun drawAll() {
        withBind {
            for (buffer in elementsBuffer) {
                buffer.draw()
            }
        }
    }

    class BoundVaoContext(private val vao: Vao) {
        inline fun <T> with(crossinline block: BoundVaoContext.() -> T): T = block(this)

        fun vertexBuffer(
            usage: Buffer.Usage,
            data: StructArray,
        ): VertexBuffer {
            val buffer = VertexBuffer(usage, data)
            vao.vertexBuffers.add(buffer)
            return buffer
        }

        fun elementBuffer(
            usage: Buffer.Usage,
            data: StructArray,
        ): ElementBuffer {
            val buffer = ElementBuffer(usage, data, ElementBuffer.DrawMode.Triangles)
            vao.elementsBuffer.add(buffer)
            return buffer
        }
    }
}
