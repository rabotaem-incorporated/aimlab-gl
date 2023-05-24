package graphics

import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import java.nio.ByteBuffer

/**
 * Буфер OpenGL, то есть то, что мы отправили на видеокарту.
 *
 * Содержит хэндл OpenGL.
 *
 * @param usage Вообще в теории может потребоваться возможность в них писать, но пока что не нужно,
 * поэтому этот параметр логично ставить в [Usage.STATIC].
 */
abstract class Buffer(
    type: Type,
    usage: Usage,
    rawData: ByteBuffer,
    manage: Boolean = true,
) {
    private var handle: Int = GL15.glGenBuffers()

    /**
     * Тип буфера: вершинный или элементный.
     */
    enum class Type {
        VERTEX,
        ELEMENT,
    }

    /**
     * Как часто будут меняться данные в буфере.
     *
     * @see [GL15.glBufferData]
     *
     * @property STATIC Данные не будут меняться, наиболее вероятный вариант
     * @property DYNAMIC Данные будут меняться, но не часто
     * @property STREAM Данные будут меняться часто
     */
    enum class Usage {
        STATIC,
        DYNAMIC,
        STREAM,
    }

    init {
        AllocationStats.gpuAllocated++

        val glTarget = when (type) {
            Type.VERTEX -> GL15.GL_ARRAY_BUFFER
            Type.ELEMENT -> GL15.GL_ELEMENT_ARRAY_BUFFER
        }

        val glUsage = when (usage) {
            Usage.STATIC -> GL15.GL_STATIC_DRAW
            Usage.DYNAMIC -> GL15.GL_DYNAMIC_DRAW
            Usage.STREAM -> GL15.GL_STREAM_DRAW
        }

        GL15.glBindBuffer(glTarget, handle)
        GL15.glBufferData(glTarget, rawData, glUsage)

        if (manage) {
            NativeAllocatorContext.scope {
                defer {
                    GL15.glDeleteBuffers(handle)
                    AllocationStats.gpuFreed++
                }
            }
        }
    }
}

/**
 * Буфер вершин.
 *
 * @param usage [Buffer.Usage]
 * @param data Данные, которые будут отправлены на видеокарту. Хранятся в [StructArray].
 */
class VertexBuffer(
    usage: Usage,
    data: StructArray,
    manage: Boolean = true,
) : Buffer(Type.VERTEX, usage, data.buffer, manage) {
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

/**
 * Буфер индексов. Показывает, что рисовать.
 *
 * @param usage [Buffer.Usage]
 * @param data Данные, которые будут отправлены на видеокарту. Хранятся в [StructArray].
 * Пока предполагается, что структура будет состоять из троек индексов ([Int3]).
 * @param mode Режим отрисовки, поддерживается только [DrawMode.Triangles].
 */
class ElementBuffer(
    usage: Usage,
    data: StructArray,
    private val mode: DrawMode,
    manage: Boolean = true,
) : Buffer(Type.ELEMENT, usage, data.buffer, manage) {
    /**
     * Как имеено будут отрисовываться данные.
     * Пока что поддерживается только [DrawMode.Triangles].
     */
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

    /**
     * Отрисовывает все элементы буфера.
     *
     * Предполагается, что в этот момент выбрана нужная [ShaderProgram],
     * установлены нужные юниформы и буферы вершин. Обычно это делается в [Vao.drawAll].
     */
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
