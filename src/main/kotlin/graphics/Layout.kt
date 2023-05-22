package graphics

import glm_.vec2.Vec2
import glm_.vec3.Vec3
import java.nio.ByteBuffer

/**
 * Описывает как структура выглядит в памяти.
 *
 * Структура состоит из фрагментов ([Fragment]) разных типов, которые описываются в [fragments].
 * Фрагменты идут в порядке, в котором они расположены в памяти, непрерывно.
 *
 * В основном [Layout] нужен, чтобы описывать, как выглядит структура в (видео)памяти
 * и соответствует расстановке атрибутов в шейдере. Например, вот такой шейдер:
 * ```glsl
 * layout(location = 0) in vec3 position;
 * layout(location = 1) in vec2 texCoord;
 * ```
 * соответствует такому [Layout]:
 * ```kt
 * val layout = Layout.of(Layout.Fragment.Float3, Layout.Fragment.Float2)
 * ```
 *
 * Тут [Layout] используется чуть более широко. Например, буфер элементов из треугольников
 * обязан иметь вид `Layout.of(Layout.Fragment.Int3)`. Это проверяется в Run-time в конструкторе
 * [ElementBuffer].
 *
 * @param fragments фрагменты, из которых состоит структура
 */
data class Layout(val fragments: List<Fragment>) {
    /**
     * Фрагмент структуры, имеет какой-то тип данных.
     */
    enum class Fragment {
        Float3,
        Float2,
        Int1,
        Int3,
        ;

        /**
         * Возвращает размер фрагмента в байтах.
         */
        fun width(): Int = when (this) {
            Float3 -> 3 * 4
            Float2 -> 2 * 4
            Int1 -> 1 * 4
            Int3 -> 3 * 4
        }
    }

    /**
     * Размер всей структуры в байтах.
     */
    val width: Int = fragments.sumOf { it.width() }

    /**
     * Смещения полей относительно начала структуры.
     */
    val offsets: List<Int> = fragments.runningFold(0) { offset, fragment ->
        offset + fragment.width()
    }

    companion object {
        /**
         * Создаёт [Layout] из фрагментов.
         */
        fun of(vararg fragments: Fragment): Layout = Layout(fragments.toList())
    }
}
