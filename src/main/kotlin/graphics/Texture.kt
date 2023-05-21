package graphics

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13
import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryStack

class Texture(private val path: String) {
    private val texture: Int

    init {
        var width = 0
        var height = 0

        val rawImage = MemoryStack.stackPush().use { stack ->
            val texture = NativeAllocatorContext.scope {
                return@scope allocAndCopy(
                    javaClass.getResourceAsStream(path)?.readBytes()!!
                )
            }

            val widthBuf = stack.callocInt(1)
            val heightBuf = stack.callocInt(1)
            val channelsBuf = stack.callocInt(1)

            STBImage.stbi_set_flip_vertically_on_load(true);
            val rawImage = STBImage.stbi_load_from_memory(
                texture, widthBuf, heightBuf, channelsBuf, 4
            )!!

            width = widthBuf.get()
            height = heightBuf.get()

            return@use rawImage
        }

        texture = GL11.glGenTextures()

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR)

        NativeAllocatorContext.scope {
            defer { GL11.glDeleteTextures(texture) }
        }

        GL11.glTexImage2D(
            GL11.GL_TEXTURE_2D,
            0,
            GL11.GL_RGBA,
            width,
            height,
            0,
            GL11.GL_RGBA,
            GL11.GL_UNSIGNED_BYTE,
            rawImage
        )

        STBImage.stbi_image_free(rawImage)
    }

    fun bind() {
        GL13.glActiveTexture(GL13.GL_TEXTURE0)
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture)
    }
}