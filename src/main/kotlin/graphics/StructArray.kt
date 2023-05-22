package graphics

import glm_.vec2.Vec2
import glm_.vec3.Vec3
import java.nio.ByteBuffer

/**
 * Структура, которая хранит данные в виде массива структур.
 *
 * Каждая структура состоит из фрагметов разных типов, которые описываются в [layout].
 *
 * @param layout описание структуры
 * @param count количество структур
 * @param buffer [ByteBuffer] буфер, в котором хранятся данные
 *
 * @see Layout
 */
data class StructArray(val layout: Layout, val count: Int, val buffer: ByteBuffer) {
    /**
     * Помещает новую структуру в буфер.
     *
     * @param data данные для каждого фрагмента структуры, в порядке, описанном в [layout]
     */
    fun put(vararg data: Any) {
        assert(data.size == layout.fragments.size) { "Data size must match fragment count" }

        for ((i, x) in data.withIndex()) {
            when (layout.fragments[i]) {
                Layout.Fragment.Float3 -> buffer.putFloat3(x as Vec3)
                Layout.Fragment.Float2 -> buffer.putFloat2(x as Vec2)
                Layout.Fragment.Int1 -> buffer.putInt(x as Int)
                Layout.Fragment.Int3 -> buffer.putInt3(x as Int3)
            }
        }
    }

    /**
     * Возвращает указатель на начало буфера.
     */
    fun flip() {
        buffer.flip()
    }

    /**
     * Заполняет буфер данными, которые передаются в [block]. Предполагается, что
     * внутри [block] будет вызван [put] для каждой структуры, и ничего больше.
     *
     * После вызова [block] буфер автоматически переворачивается.
     *
     * ```kt
     * NativeAllocatorContext.new {
     *     val buf = allocStructs(Layout(Layout.Fragment.Float3, Layout.Fragment.Float2), 2).fill {
     *          put(Vec3(1f, 2f, 3f), Vec2(0.5f, 0.3f))
     *          put(Vec3(4f, 5f, 6f), Vec2(0.1f, 0.2f))
     *          // flip() вызывать не нужно
     *     }
     * }
     * ```
     */
    inline fun fill(crossinline block: StructArray.() -> Unit): StructArray {
        block()
        flip()
        return this
    }
}

fun ByteBuffer.putFloat3(float3: Vec3) {
    putFloat(float3.x)
    putFloat(float3.y)
    putFloat(float3.z)
}

fun ByteBuffer.putFloat2(float2: Vec2) {
    putFloat(float2.x)
    putFloat(float2.y)
}

fun ByteBuffer.putInt3(int: Int3) {
    putInt(int.x)
    putInt(int.y)
    putInt(int.z)
}
