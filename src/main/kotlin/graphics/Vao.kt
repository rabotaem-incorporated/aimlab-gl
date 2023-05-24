package graphics

import org.lwjgl.opengl.GL30

/**
 * Vertex Array Object.
 *
 * VAO это контейнер, который показывает железу как именно организованы данные в буферах.
 * В теории, вершинные и элементные буферы можно было бы привязывать к нескольким VAO одновременно,
 * но в нашем случае это скорее всего не нужно.
 *
 * В связи с этим VAO обобщены: они представляют собой уже готовые к использованию меши, которые
 * уже отправлены на GPU.
 *
 * @param manage Если true, то VAO будет освобожден при выходе из контекста [NativeAllocatorContext]
 */
class Vao(val manage: Boolean = true) {
    /** OpenGL handle. */
    val handle: Int = GL30.glGenVertexArrays()
    private val vertexBuffers: MutableList<VertexBuffer> = mutableListOf()
    private val elementsBuffer: MutableList<ElementBuffer> = mutableListOf()

    init {
        AllocationStats.gpuAllocated++

        if (manage) {
            NativeAllocatorContext.scope {
                defer { free() }
            }
        }
    }

    /** Освобождает VAO. Если VAO не помечено как `manage`, эту функцию обязательно нужно вызвать */
    fun free() {
        GL30.glDeleteVertexArrays(handle)
        AllocationStats.gpuFreed++
    }

    /**
     * Выполняет блок кода, привязывая VAO к контексту OpenGL,
     * то есть готовит модель к рендерингу.
     *
     * @param block Блок кода, который будет выполнен
     */
    inline fun <T> withBind(crossinline block: BoundVaoContext.() -> T): T {
        GL30.glBindVertexArray(handle)
        return BoundVaoContext(this).with(block)
    }

    /**
     * Рисует все элементные буферы, привязанные к VAO.
     *
     * Вызывается внутри [withBind], поэтому привязывать VAO еще раз не нужно.
     */
    fun drawAll() {
        withBind {
            for (buffer in elementsBuffer) {
                buffer.draw()
            }
        }
    }

    /**
     * Контекст, в котором VAO привязан к OpenGL. Только если VAO привязан,
     * к нему можно добавлять буферы.
     */
    class BoundVaoContext(private val vao: Vao) {
        inline fun <T> with(crossinline block: BoundVaoContext.() -> T): T = block(this)

        /**
         * Добавляет вершинный буфер в [StructArray] к VAO.
         */
        fun vertexBuffer(
            usage: Buffer.Usage,
            data: StructArray,
        ): VertexBuffer {
            val buffer = VertexBuffer(usage, data, vao.manage)
            vao.vertexBuffers.add(buffer)
            return buffer
        }

        /**
         * Добавляет элементный буфер в [StructArray] к VAO.
         */
        fun elementBuffer(
            usage: Buffer.Usage,
            data: StructArray,
        ): ElementBuffer {
            val buffer = ElementBuffer(usage, data, ElementBuffer.DrawMode.Triangles, vao.manage)
            vao.elementsBuffer.add(buffer)
            return buffer
        }
    }
}
