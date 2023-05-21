package graphics

import glm_.vec2.Vec2
import glm_.vec3.Vec3
import org.lwjgl.system.MemoryUtil
import java.nio.Buffer
import java.nio.ByteBuffer

class NativeAllocatorContext {
    companion object {
        val instanceStack = mutableListOf<NativeAllocatorContext>()

        inline fun new(block: NativeAllocator.() -> Unit) {
            val allocator = NativeAllocatorContext()
            instanceStack.add(allocator)
            NativeAllocator(allocator).block()
            instanceStack.removeLast().dropAll()
        }

        inline fun <T> scope(crossinline block: NativeAllocator.() -> T): T {
            return NativeAllocator(instanceStack.last()).block()
        }
    }

    private val deferred = mutableListOf<() -> Unit>()

    fun manage(buffer: Buffer) {
        deferred.add { MemoryUtil.memFree(buffer) }
    }

    fun defer(action: () -> Unit) {
        deferred.add(action)
    }

    fun dropAll() {
        deferred.asReversed().forEach { it() }
    }
}

data class StructArray(val layout: Layout, val count: Int, val buffer: ByteBuffer) {
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

    fun flip() {
        buffer.flip()
    }

    inline fun fill(crossinline block: StructArray.() -> Unit): StructArray {
        block()
        flip()
        return this
    }
}

class NativeAllocator(private val allocator: NativeAllocatorContext) {
    private fun alloc(size: Int): ByteBuffer = MemoryUtil.memAlloc(size).also { allocator.manage(it) }

    fun allocAndCopy(data: ByteArray): ByteBuffer = alloc(data.size).put(data).flip()

    fun defer(action: () -> Unit) {
        allocator.defer(action)
    }

    fun allocStructs(layout: Layout, count: Int): StructArray {
        val buf = alloc(layout.width * count)
        return StructArray(layout, count, buf)
    }
}
