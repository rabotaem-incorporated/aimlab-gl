package graphics

import glm_.mat4x4.Mat4
import glm_.vec3.Vec3
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20

/**
 * Тип шейдера. Используется только для создания шейдерной программы.
 *
 * Использование вне [ShaderProgram] не предполагается.
 */
enum class ShaderType {
    VERTEX,
    FRAGMENT,
}

/**
 * Шейдер. Содержит хэндл полученный из OpenGL. Используется только для создания шейдерной программы.
 *
 * Использование вне [ShaderProgram] не предполагается.
 */
class Shader(type: ShaderType, path: String) {
    val handle: Int

    init {
        val glType = when (type) {
            ShaderType.VERTEX -> GL20.GL_VERTEX_SHADER
            ShaderType.FRAGMENT -> GL20.GL_FRAGMENT_SHADER
        }

        handle = GL20.glCreateShader(glType)

        val shaderSource = object {}.javaClass.getResource(path)!!.readText()

        GL20.glShaderSource(handle, shaderSource)
        GL20.glCompileShader(handle)

        val shaderStatus = intArrayOf(0)
        GL20.glGetShaderiv(handle, GL20.GL_COMPILE_STATUS, shaderStatus)
        if (shaderStatus[0] == GL11.GL_FALSE) {
            println("Failed to compile shader: $path :(")
            println(GL20.glGetShaderInfoLog(handle))
            throw Error("Failed to compile shader")
        }
    }
}

/**
 * Шейдерная программа. Содержит хэндл OpenGL и список uniform-переменных, которые можно устанавливать.
 *
 * На данный момент любой контекст содержит не более одной шейдерной программы,
 * поэтому переключение шейдерных программ не используется.
 */
class ShaderProgram(private val handle: Int, private val uniforms: Map<String, Uniform>) {
     /** Заменяет шейдерную программу на данную */
    fun use() = GL20.glUseProgram(handle)

    init {
        NativeAllocatorContext.scope {
            defer { GL20.glDeleteProgram(handle) }
        }
    }

    /** Внутри этого блока гарантируется, что шейдерная программа помечена как используемая. */
    inline fun using(block: ShaderProgram.() -> Unit) {
        use()
        block()
    }

    private fun uniformLocation(name: String): Int {
        return uniforms[name]?.location ?: throw Error("Uniform $name not found")
    }

    fun enableDepthTest() {
        GL20.glEnable(GL20.GL_DEPTH_TEST)
    }

    fun disableDepthTest() {
        GL20.glDisable(GL20.GL_DEPTH_TEST)
    }

    fun setUniform(name: String, value: Int) {
        GL20.glUniform1i(uniformLocation(name), value)
    }

    fun setUniform(name: String, value: Float) {
        GL20.glUniform1f(uniformLocation(name), value)
    }

    fun setUniform(name: String, value: Vec3) {
        GL20.glUniform3f(uniformLocation(name), value.x, value.y, value.z)
    }

    fun setUniform(name: String, value: Mat4) {
        GL20.glUniformMatrix4fv(uniformLocation(name), false, value.toFloatArray())
    }
}

/**
 * Обертка над OpenGL-ссылкой на uniform-переменную в шейдере.
 *
 * Чтобы поставить ее значение, нужно вызвать [ShaderProgram.setUniform].
 *
 * Использование вне [ShaderProgram] не предполагается.
 */
data class Uniform(val location: Int)

/**
 * Создает шейдерные программы. Предполагается что вызывается через [GlfwContext.compileShaderProgram]
 *
 * Пример:
 * ```kt
 *     val shaderProgram = GlfwContext.compileShaderProgram {
 *         vertex("/sample.vert") // Путь относительно resources
 *         fragment("/sample.frag")
 *         uniform("projection") // Имена uniform-переменных из шейдеров
 *         uniform("model")
 *         uniform("view")
 *     }
 * ```
 *
 * Чтобы использовать uniform'ы их нужно явно объявить через [uniform].
 */
class ShaderProgramBuilder {
    private val shaders = mutableListOf<Shader>()
    private val uniformNames = mutableListOf<String>()

    /** Добавляет вершинный шейдер к программе */
    fun vertex(path: String) {
        shaders.add(Shader(ShaderType.VERTEX, path))
    }

    /** Добавляет фрагментный шейдер к программе */
    fun fragment(path: String): Boolean {
        return shaders.add(Shader(ShaderType.FRAGMENT, path))
    }

    /** Объявляет Uniform-переменную из шейдера, делая ее доступной к использованию */
    fun uniform(name: String): Boolean {
        return uniformNames.add(name)
    }

    /**
     * Компилирует и слинковывает получившуюся шейдерную программу.
     * Это происходит автоматически при вызове [GlfwContext.compileShaderProgram]
     */
    fun build(): ShaderProgram {
        val program = GL20.glCreateProgram()

        for (shader in shaders) GL20.glAttachShader(program, shader.handle)

        GL20.glLinkProgram(program)

        val programStatus = intArrayOf(0)
        GL20.glGetProgramiv(program, GL20.GL_LINK_STATUS, programStatus)
        if (programStatus[0] == GL11.GL_FALSE) {
            println("Failed to link shader program :(")
            println(GL20.glGetProgramInfoLog(program))
            throw Error("Failed to link shader program")
        }

        val uniforms: Map<String, Uniform> = uniformNames.associateWith { name ->
            val location = GL20.glGetUniformLocation(program, name)
            Uniform(location)
        }

        for (shader in shaders) GL20.glDeleteShader(shader.handle)

        return ShaderProgram(program, uniforms)
    }
}
