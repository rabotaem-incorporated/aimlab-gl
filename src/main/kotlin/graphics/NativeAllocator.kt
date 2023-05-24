package graphics

import org.lwjgl.system.MemoryUtil
import java.nio.Buffer
import java.nio.ByteBuffer

object AllocationStats {
    var peakAllocated = 0
    var peakGpuAllocated = 0

    var allocated = 0
        set(value) {
            field = value
            if (value - freed > peakAllocated) {
                peakAllocated = value - gpuFreed
            }
        }

    var freed = 0

    var gpuAllocated = 0
        set(value) {
            field = value
            if (value - gpuFreed > peakGpuAllocated) {
                peakGpuAllocated = value - gpuFreed
            }
        }

    var gpuFreed = 0
}

/**
 * Контекст системного аллокатора.
 *
 * lwjgl не может отправлять на видеокарту данные, которые были выделены в java-куче, поэтому
 * эта проблема решается выделением памяти через [MemoryUtil.memAlloc] и последующим ручным освобождением
 * через [MemoryUtil.memFree].
 *
 * Для удобства, чтобы не забывать освобождать память, используется этот класс, который хранит
 * все выделенные буферы и освобождает их при выходе из блока. В нашем случае блоком является
 * вся программа, но предположим что блоков на самом деле несколько.
 *
 * Тогда [NativeAllocatorContext.new] создает новый блок, а [NativeAllocatorContext.scope] позволяет
 * получить доступ к текущему блоку.
 *
 * Интерфейс самого аллокатора находится в [NativeAllocator]. Он позволяет выделить память.
 *
 * Пример:
 * ```kt
 * NativeAllocatorContext.new {
 *     val buf = allocAndCopy(byteArrayOf(1, 2, 3))
 *     NativeAllocatorContext.scope {
 *          val buf2 = allocAndCopy(byteArrayOf(4, 5, 6))
 *     } // buf2 освобождается
 * } // buf освобождается
 * ```
 */
class NativeAllocatorContext {
    companion object {
        /** Все экземпляры аллокаторов в порядке от старых к новым */
        val instanceStack = mutableListOf<NativeAllocatorContext>()

        /**
         * Создает новую область вызова аллокатора.
         * При выходе из блока все выделенные буферы будут освобождены.
         */
        inline fun new(block: NativeAllocator.() -> Unit) {
            val allocator = NativeAllocatorContext()
            instanceStack.add(allocator)
            NativeAllocator(allocator).block()
            instanceStack.removeLast().dropAll()

            AllocationStats.run {
                println("RAM: Allocated: $allocated, freed: $freed, peak: $peakAllocated")
                println("GPU: Allocated: $gpuAllocated, freed: $gpuFreed, peak: $peakGpuAllocated")
            }
        }

        /**
         * Получает доступ к самому новому текущему аллокатору.
         */
        inline fun <T> scope(crossinline block: NativeAllocator.() -> T): T {
            return NativeAllocator(instanceStack.last()).block()
        }
    }

    /** Список того, что нужно сделать при выходе из блока */
    private val deferred = mutableListOf<() -> Unit>()

    /**
     *  Помечает [buffer] как "нужно освободить при выходе из блока".
     */
    fun manage(buffer: Buffer) {
        deferred.add {
            MemoryUtil.memFree(buffer)
            AllocationStats.freed++
        }
    }

    /**
     * Откладывает выполнение [action] до выхода из блока.
     */
    fun defer(action: () -> Unit) {
        deferred.add(action)
    }

    /**
     * Выполняет все отложенные действия.
     * Предполагается, что это будет вызвано при выходе из блока.
     */
    fun dropAll() {
        deferred.asReversed().forEach { it() }
    }
}

/**
 * Сам интерфейс аллокатора.
 *
 * @see NativeAllocatorContext
 *
 * @constructor используется только внутри [NativeAllocatorContext]
 */
class NativeAllocator(private val allocator: NativeAllocatorContext) {
    /**
     * Выделяет буфер [ByteBuffer] размера [size].
     */
    private fun alloc(size: Int): ByteBuffer {
        AllocationStats.allocated++
        return MemoryUtil.memAlloc(size).also { allocator.manage(it) }
    }

    /**
     * Выделяет буфер [ByteBuffer] размера равного [data] и копирует в него данные из [data].
     */
    fun allocAndCopy(data: ByteArray): ByteBuffer {
        return alloc(data.size).put(data).flip()
    }

    /**
     * Откладывает выполнение [action] до выхода из блока.
     */
    fun defer(action: () -> Unit) {
        allocator.defer(action)
    }

    /**
     * Выделяет буфер [ByteBuffer] размера [Layout.width] * [count] и создает на нем [StructArray].
     */
    fun allocStructs(layout: Layout, count: Int): StructArray {
        val buf = alloc(layout.width * count)
        return StructArray(layout, count, buf)
    }
}
